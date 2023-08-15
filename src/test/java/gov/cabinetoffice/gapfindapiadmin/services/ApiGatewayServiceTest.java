package gov.cabinetoffice.gapfindapiadmin.services;

import gov.cabinetoffice.gapfindapiadmin.config.ApiGatewayConfigProperties;
import gov.cabinetoffice.gapfindapiadmin.exceptions.ApiKeyAlreadyExistException;
import gov.cabinetoffice.gapfindapiadmin.exceptions.ApiKeyDoesNotExistException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.services.apigateway.ApiGatewayClient;
import software.amazon.awssdk.services.apigateway.model.ApiKey;
import software.amazon.awssdk.services.apigateway.model.CreateApiKeyRequest;
import software.amazon.awssdk.services.apigateway.model.CreateApiKeyResponse;
import software.amazon.awssdk.services.apigateway.model.CreateUsagePlanKeyRequest;
import software.amazon.awssdk.services.apigateway.model.CreateUsagePlanKeyResponse;
import software.amazon.awssdk.services.apigateway.model.GetApiKeysResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApiGatewayServiceTest {

    private final String API_KEY_NAME = "apikeyName";
    private final ApiKey apiKey = ApiKey.builder().name(API_KEY_NAME).build();
    private final GetApiKeysResponse getApiKeysResponse = GetApiKeysResponse.builder().items(List.of(apiKey)).build();
    @Mock
    ApiGatewayConfigProperties apiGatewayConfigProperties;
    @Mock
    ApiGatewayClient apiGatewayClient;
    @InjectMocks
    ApiGatewayService apiGatewayService;

    @Test
    void createApiKeys() {
        when(apiGatewayClient.getApiKeys()).thenReturn(GetApiKeysResponse.builder().build());

        CreateApiKeyResponse apiKeyRequest = CreateApiKeyResponse.builder().name(API_KEY_NAME).value("apiKeyValue").build();
        when(apiGatewayClient.createApiKey(any(CreateApiKeyRequest.class))).thenReturn(apiKeyRequest);

        CreateUsagePlanKeyResponse usagePlanKeyResponse = CreateUsagePlanKeyResponse.builder().build();
        when(apiGatewayClient.createUsagePlanKey(any(CreateUsagePlanKeyRequest.class))).thenReturn(usagePlanKeyResponse);

        String response = apiGatewayService.createApiKeys(API_KEY_NAME);
        assertThat(response).isEqualTo("apiKeyValue");
        verify(apiGatewayClient).createApiKey(any(CreateApiKeyRequest.class));
        verify(apiGatewayClient).createUsagePlanKey(any(CreateUsagePlanKeyRequest.class));
    }

    @Test
    void checkIfKeyExistAlready_throwsApiKeyAlreadyExistException() {
        when(apiGatewayClient.getApiKeys()).thenReturn(getApiKeysResponse);
        assertThrows(ApiKeyAlreadyExistException.class, () -> apiGatewayService.checkIfKeyExistAlready(API_KEY_NAME));
    }

    @Test
    void checkIfKeyExistAlready_doesNotThrowException() {
        when(apiGatewayClient.getApiKeys()).thenReturn(getApiKeysResponse);
        assertDoesNotThrow(() -> apiGatewayService.checkIfKeyExistAlready("anotherKeyName"));
    }

}