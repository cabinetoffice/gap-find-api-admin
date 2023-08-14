package gov.cabinetoffice.gapfindapiadmin.services;


import gov.cabinetoffice.gapfindapiadmin.config.ApiGatewayConfigProperties;
import gov.cabinetoffice.gapfindapiadmin.exceptions.ApiKeyAlreadyExistException;
import gov.cabinetoffice.gapfindapiadmin.exceptions.ApiKeyDoesNotExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.apigateway.ApiGatewayClient;
import software.amazon.awssdk.services.apigateway.model.CreateApiKeyRequest;
import software.amazon.awssdk.services.apigateway.model.CreateApiKeyResponse;
import software.amazon.awssdk.services.apigateway.model.CreateUsagePlanKeyRequest;

@Service
@RequiredArgsConstructor
public class ApiGatewayService {

    private final ApiGatewayConfigProperties apiGatewayConfigProperties;

    private final ApiGatewayClient apiGatewayClient;

    public String createApiKeys(String keyName) {
        checkIfKeyExistAlready(keyName);

        CreateApiKeyRequest apiKeyRequest = CreateApiKeyRequest.builder()
                .name(keyName)
                .enabled(true)
                .generateDistinctId(true)
                .build();

        // Creating a api key
        CreateApiKeyResponse response = apiGatewayClient.createApiKey(apiKeyRequest);

        // set the usage plan for the created api key.
        CreateUsagePlanKeyRequest planRequest = CreateUsagePlanKeyRequest.builder()
                .usagePlanId(apiGatewayConfigProperties.getApiGatewayUsagePlanId())
                .keyId(response.id())
                .keyType("API_KEY")
                .build();

        apiGatewayClient.createUsagePlanKey(planRequest);

        return response.value();
    }

    public void deleteApiKeys(String keyName) {
        apiGatewayClient.getApiKeys().items()
                .stream()
                .filter(k -> k.name() != null && k.name().equals(keyName))
                .findFirst()
                .ifPresentOrElse(k -> apiGatewayClient.deleteApiKey(builder -> builder.apiKey(k.id())), () -> {
                    throw new ApiKeyDoesNotExistException("API Key with name " + keyName + " does not exist");
                });
    }


    void checkIfKeyExistAlready(String keyName) {
        apiGatewayClient.getApiKeys().items()
                .stream()
                .filter(key -> key.name().equals(keyName))
                .findFirst()
                .ifPresent(key -> {
                    throw new ApiKeyAlreadyExistException("API Key with name " + keyName + " already exists");
                });
    }

}
