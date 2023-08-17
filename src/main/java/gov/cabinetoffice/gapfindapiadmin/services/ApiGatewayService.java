package gov.cabinetoffice.gapfindapiadmin.services;


import gov.cabinetoffice.gapfindapiadmin.config.ApiGatewayConfigProperties;
import gov.cabinetoffice.gapfindapiadmin.exceptions.ApiKeyDoesNotExistException;
import gov.cabinetoffice.gapfindapiadmin.models.ApiKey;
import gov.cabinetoffice.gapfindapiadmin.models.GrantAdmin;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.apigateway.ApiGatewayClient;
import software.amazon.awssdk.services.apigateway.model.CreateApiKeyRequest;
import software.amazon.awssdk.services.apigateway.model.CreateApiKeyResponse;
import software.amazon.awssdk.services.apigateway.model.CreateUsagePlanKeyRequest;

import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
public class ApiGatewayService {

    private final ApiGatewayConfigProperties apiGatewayConfigProperties;

    private final ApiGatewayClient apiGatewayClient;

    private final ApiKeyService apiKeyService;

    private final GrantAdminService grantAdminService;

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
        final ApiKey apiKey = ApiKey.builder()
                .fundingOrganisation(grantAdmin.getFunder())
                .apiKey(response.value())
                .name(keyName)
                .createdDate(ZonedDateTime.now())
                .isRevoked(false)
                .build();

        apiKeyService.saveApiKey(apiKey);
    }

    public void deleteApiKey(String keyName) {
        apiGatewayClient.getApiKeys().items()
                .stream()
                .filter(k -> k.name() != null && k.name().equals(keyName))
                .findFirst()
                .ifPresentOrElse(k -> apiGatewayClient.deleteApiKey(builder -> builder.apiKey(k.id())), () -> {
                    throw new ApiKeyDoesNotExistException("API Key with name " + keyName + " does not exist");
                });
    }

    boolean doesKeyExist(String keyName) {
        return apiGatewayClient.getApiKeys().items()
                .stream()
                .anyMatch(key -> key.name() != null && key.name().equals(keyName));
    }

}
