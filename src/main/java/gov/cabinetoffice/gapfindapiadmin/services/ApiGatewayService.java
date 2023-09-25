package gov.cabinetoffice.gapfindapiadmin.services;

import gov.cabinetoffice.gapfindapiadmin.config.ApiGatewayConfigProperties;
import gov.cabinetoffice.gapfindapiadmin.exceptions.ApiKeyException;
import gov.cabinetoffice.gapfindapiadmin.exceptions.UnauthorizedException;
import gov.cabinetoffice.gapfindapiadmin.models.GapApiKey;
import gov.cabinetoffice.gapfindapiadmin.models.GrantAdmin;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.apigateway.ApiGatewayClient;
import software.amazon.awssdk.services.apigateway.model.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.ZonedDateTime;

import static java.util.Optional.ofNullable;

@Service
@RequiredArgsConstructor
public class ApiGatewayService {

    private final ApiGatewayConfigProperties apiGatewayConfigProperties;

    private final ApiGatewayClient apiGatewayClient;

    private final ApiKeyService apiKeyService;

    public String createApiKeysInAwsAndDb(String keyName) {
        final GrantAdmin grantAdmin = (GrantAdmin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //create the api key in aws api gateway
        final CreateApiKeyResponse apiKey = createApiKeyInAwsApiGateway(keyName);
        // set the usage plan for the created api key.
        addApiKeyToApiGatewayUsagePlan(apiKey);
        // save the api key in the database
        saveKeyInDatabase(keyName, apiKey, grantAdmin);
        return apiKey.value();
    }

    protected CreateApiKeyResponse createApiKeyInAwsApiGateway(String keyName) {
        final CreateApiKeyRequest apiKeyRequest = CreateApiKeyRequest.builder()
                .name(keyName)
                .enabled(true)
                .generateDistinctId(true)
                .build();

        return apiGatewayClient.createApiKey(apiKeyRequest);
    }

    protected void addApiKeyToApiGatewayUsagePlan(CreateApiKeyResponse apiKey) {
        final CreateUsagePlanKeyRequest planRequest = CreateUsagePlanKeyRequest.builder()
                .usagePlanId(apiGatewayConfigProperties.getApiGatewayUsagePlanId())
                .keyId(apiKey.id())
                .keyType("API_KEY")
                .build();

        apiGatewayClient.createUsagePlanKey(planRequest);
    }

    protected void saveKeyInDatabase(String keyName, CreateApiKeyResponse response, GrantAdmin grantAdmin) {
        //TODO add in some error handling here in case it doesn't save to the db for some reason but we manage to save in aws? Could catch any db exceptions and then delete from aws if it exists.

        try {
            final GapApiKey apiKey = GapApiKey.builder()
                    .apiGatewayId(response.id())
                    .fundingOrganisation(grantAdmin.getFunder())
                    .apiKey(hashString(response.value()))
                    .name(keyName)
                    .createdDate(ZonedDateTime.now())
                    .isRevoked(false)
                    .build();

            apiKeyService.saveApiKey(apiKey);
        } catch (NoSuchAlgorithmException e) {
            throw new ApiKeyException(e);
        }

    }

    private String hashString(String hashingValue) throws NoSuchAlgorithmException {
            final MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(hashingValue.getBytes(StandardCharsets.UTF_8));
            final byte[] hashed = md.digest();

            final StringBuilder hashedStringBuilder = new StringBuilder();
            for (byte b : hashed) {
                hashedStringBuilder.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
            }

            return hashedStringBuilder.toString();


    }

    public void deleteApiKey(GapApiKey apiKey) {
        final GrantAdmin grantAdmin = (GrantAdmin) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); ofNullable(grantAdmin)
                .filter(admin -> admin.getFunder().getName().equals(apiKey.getFundingOrganisation().getName()))
                .ifPresentOrElse(
                        admin -> apiGatewayClient.deleteApiKey(DeleteApiKeyRequest.builder()
                                .apiKey(apiKey.getApiGatewayId())
                                .build()),
                        () -> {
                            throw new UnauthorizedException("User is unauthorized to revoke this API key");
                        }
                );
    }


}
