package gov.cabinetoffice.gapfindapiadmin.services;

import gov.cabinetoffice.gapfindapiadmin.config.ApiGatewayConfigProperties;
import gov.cabinetoffice.gapfindapiadmin.exceptions.ApiKeyDoesNotExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.apigateway.ApiGatewayClient;

@Service
@RequiredArgsConstructor
public class ApiGatewayService {

    private final ApiGatewayConfigProperties apiGatewayConfigProperties;

    private final ApiGatewayClient apiGatewayClient;

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
