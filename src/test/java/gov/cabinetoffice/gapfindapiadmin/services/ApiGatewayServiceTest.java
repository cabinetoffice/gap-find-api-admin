package gov.cabinetoffice.gapfindapiadmin.services;

import gov.cabinetoffice.gapfindapiadmin.config.ApiGatewayConfigProperties;
import gov.cabinetoffice.gapfindapiadmin.exceptions.ApiKeyDoesNotExistException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.services.apigateway.ApiGatewayClient;
import software.amazon.awssdk.services.apigateway.model.ApiKey;
import software.amazon.awssdk.services.apigateway.model.GetApiKeysResponse;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApiGatewayServiceTest {

    private final String API_KEY_NAME = "apikeyName";
    private final String API_KEY_DESCRIPTION = "apikeyDescription";
    private final ApiKey apiKey = ApiKey.builder().name(API_KEY_NAME).build();
    private final GetApiKeysResponse getApiKeysResponse = GetApiKeysResponse.builder().items(List.of(apiKey)).build();
    @Mock
    ApiGatewayConfigProperties apiGatewayConfigProperties;
    @Mock
    ApiGatewayClient apiGatewayClient;
    @InjectMocks
    ApiGatewayService apiGatewayService;

    @Test
    void deleteApiKeys() {
        when(apiGatewayClient.getApiKeys()).thenReturn(getApiKeysResponse);
        assertDoesNotThrow(() -> apiGatewayService.deleteApiKey(API_KEY_NAME));
    }

    @Test
    void deleteApiKeys_throwsApiKeyDoesNotExistException() {
        when(apiGatewayClient.getApiKeys()).thenReturn(getApiKeysResponse);
        assertThrows(ApiKeyDoesNotExistException.class, () -> apiGatewayService.deleteApiKey("anotherKeyName"));
    }


    @Test
    void checkIfKeyExistAlready_returnsTrue() {
        when(apiGatewayClient.getApiKeys()).thenReturn(getApiKeysResponse);
        boolean result = apiGatewayService.doesKeyExist(API_KEY_NAME);
        assertThat(result).isTrue();
    }

    @Test
    void checkIfKeyExistAlready_returnsFalse() {
        when(apiGatewayClient.getApiKeys()).thenReturn(getApiKeysResponse);
        boolean result = apiGatewayService.doesKeyExist("anotherKeyName");
        assertThat(result).isFalse();
    }

}