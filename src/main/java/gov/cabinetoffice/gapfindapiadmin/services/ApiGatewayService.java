package gov.cabinetoffice.gapfindapiadmin.services;

import gov.cabinetoffice.gapfindapiadmin.config.ApiGatewayConfigProperties;
import gov.cabinetoffice.gapfindapiadmin.exceptions.ApiKeyException;
import gov.cabinetoffice.gapfindapiadmin.exceptions.UnauthorizedException;
import gov.cabinetoffice.gapfindapiadmin.models.FundingOrganisation;
import gov.cabinetoffice.gapfindapiadmin.models.GapApiKey;
import gov.cabinetoffice.gapfindapiadmin.models.JwtPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.apigateway.ApiGatewayClient;
import software.amazon.awssdk.services.apigateway.model.CreateApiKeyRequest;
import software.amazon.awssdk.services.apigateway.model.CreateApiKeyResponse;
import software.amazon.awssdk.services.apigateway.model.CreateUsagePlanKeyRequest;
import software.amazon.awssdk.services.apigateway.model.DeleteApiKeyRequest;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.ZonedDateTime;

import static java.util.Optional.ofNullable;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApiGatewayService {

    private final ApiGatewayConfigProperties apiGatewayConfigProperties;

    private final ApiGatewayClient apiGatewayClient;

    private final ApiKeyService apiKeyService;

    private final FundingOrganisationService fundingOrganisationService;

    public String createApiKeysInAwsAndDb(String keyName) {
        final JwtPayload jwtPayload = (JwtPayload) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        //create the api key in aws api gateway
        final CreateApiKeyResponse apiKey = createApiKeyInAwsApiGateway(keyName);
        log.info("Api key created in apiGateway");

        // set the usage plan for the created api key.
        addApiKeyToApiGatewayUsagePlan(apiKey);

        // save the api key in the database
        saveKeyInDatabase(keyName, apiKey, jwtPayload);

        return apiKey.value();
    }

    protected CreateApiKeyResponse createApiKeyInAwsApiGateway(String keyName) {
        log.info("Creating api key in api gateway with name: {}", keyName);

        final CreateApiKeyRequest apiKeyRequest = CreateApiKeyRequest.builder()
                .name(keyName)
                .enabled(true)
                .generateDistinctId(true)
                .build();

        return apiGatewayClient.createApiKey(apiKeyRequest);
    }

    protected void addApiKeyToApiGatewayUsagePlan(CreateApiKeyResponse apiKey) {
        log.info("Adding api key to usage plan");

        final CreateUsagePlanKeyRequest planRequest = CreateUsagePlanKeyRequest.builder()
                .usagePlanId(apiGatewayConfigProperties.getApiGatewayUsagePlanId())
                .keyId(apiKey.id())
                .keyType("API_KEY")
                .build();

        apiGatewayClient.createUsagePlanKey(planRequest);

        log.info("Api key added to usage plan");
    }

    protected void saveKeyInDatabase(String keyName, CreateApiKeyResponse response, JwtPayload jwtPayload) {
        //TODO add in some error handling here in case it doesn't save to the db for some reason but we manage to save in aws? Could catch any db exceptions and then delete from aws if it exists.
        log.info("Saving api key in database");

        FundingOrganisation fundingOrganisation =
                fundingOrganisationService.getFundingOrganisationByName(jwtPayload.getDepartmentName());

        try {
            final GapApiKey apiKey = GapApiKey.builder()
                    .apiGatewayId(response.id())
                    .fundingOrganisation(fundingOrganisation)
                    .apiKey(hashString(response.value()))
                    .name(keyName)
                    .createdDate(ZonedDateTime.now())
                    .isRevoked(false)
                    .build();

            apiKeyService.saveApiKey(apiKey);

            log.info("Api key saved in database");

        } catch (NoSuchAlgorithmException e) {
            throw new ApiKeyException(e);
        }

    }

    private String hashString(String hashingValue) throws NoSuchAlgorithmException {
        log.info("Hashing api key");

        final MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(hashingValue.getBytes(StandardCharsets.UTF_8));
        final byte[] hashed = md.digest();

        final StringBuilder hashedStringBuilder = new StringBuilder();
        for (byte b : hashed) {
            hashedStringBuilder.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }

        return hashedStringBuilder.toString();


    }

    public void deleteApiKey(GapApiKey apiKey, boolean isSuperAdmin) {
        log.info("deleting Api key from api gateway with id: {}", apiKey.getApiGatewayId());

        final JwtPayload jwtPayload = (JwtPayload) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ofNullable(jwtPayload).filter(admin -> jwtPayload.getDepartmentName()
                        .equals(apiKey.getFundingOrganisation().getName()) || isSuperAdmin)
                .ifPresentOrElse(
                        admin -> {
                            apiGatewayClient.deleteApiKey(DeleteApiKeyRequest.builder()
                                    .apiKey(apiKey.getApiGatewayId())
                                    .build());

                            log.info("Api key deleted from api gateway");
                        },
                        () -> {
                            throw new UnauthorizedException("User is unauthorized to revoke this API key");
                        }
                );
    }


}
