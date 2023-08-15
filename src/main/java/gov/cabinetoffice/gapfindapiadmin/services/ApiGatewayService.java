package gov.cabinetoffice.gapfindapiadmin.services;


import gov.cabinetoffice.gapfindapiadmin.config.ApiGatewayConfigProperties;
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
           final CreateApiKeyRequest apiKeyRequest = CreateApiKeyRequest.builder()
                .name(keyName)
                .enabled(true)
                .generateDistinctId(true)
                .build();

        // Creating a api key
        final CreateApiKeyResponse response = apiGatewayClient.createApiKey(apiKeyRequest);

        // set the usage plan for the created api key.
        final CreateUsagePlanKeyRequest planRequest = CreateUsagePlanKeyRequest.builder()
                .usagePlanId(apiGatewayConfigProperties.getApiGatewayUsagePlanId())
                .keyId(response.id())
                .keyType("API_KEY")
                .build();

        apiGatewayClient.createUsagePlanKey(planRequest);
        //TODO create apiKey into db table
        return response.value();
    }

    public boolean doesKeyExist(String keyName) {
        return apiGatewayClient.getApiKeys().items()
                .stream()
                .anyMatch(key -> key.name()!= null && !key.name().isEmpty() && key.name().equals(keyName));

    }

}
