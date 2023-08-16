package gov.cabinetoffice.gapfindapiadmin.services;

import gov.cabinetoffice.gapfindapiadmin.exceptions.ApiKeyDoesNotExistException;
import gov.cabinetoffice.gapfindapiadmin.models.ApiKey;
import gov.cabinetoffice.gapfindapiadmin.repositories.ApiKeyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ApiKeyServiceTest {

    @Mock
    private ApiKeyRepository apiKeyRepository;

    @InjectMocks
    private ApiKeyService apiKeyService;

    private final Integer API_KEY_ID = 1;
    private final ApiKey apiKey = ApiKey.builder()
            .name("Test API Key name")
            .apiKey("Test API Key")
            .isRevoked(false)
            .build();

    @Test
    void getApiKeyName_returnsExpectedResponse() {
        when(apiKeyRepository.findById(API_KEY_ID)).thenReturn(java.util.Optional.ofNullable(apiKey));

        final String response = apiKeyService.getApiKeyName(API_KEY_ID);

        verify(apiKeyRepository).findById(API_KEY_ID);
        assertThat(response).isEqualTo(apiKey.getName());
    }

    @Test
    void getApiKey_doesNotExist() {
        when(apiKeyRepository.findById(API_KEY_ID)).thenReturn(java.util.Optional.empty());
        final Throwable exception = assertThrows(ApiKeyDoesNotExistException.class,
                () -> apiKeyService.getApiKeyName(API_KEY_ID));

        verify(apiKeyRepository).findById(API_KEY_ID);
        assertThat(exception.getMessage()).isEqualTo("API Key with id " + API_KEY_ID + " does not exist");

    }

    @Test
    void revokeApiKey_returnsExpectedResponse() {
        when(apiKeyRepository.findById(API_KEY_ID)).thenReturn(java.util.Optional.ofNullable(apiKey));

        apiKeyService.revokeApiKey(API_KEY_ID);

        verify(apiKeyRepository).findById(API_KEY_ID);
        assertThat(apiKey.isRevoked()).isEqualTo(true);
    }

    @Test
    void revokeApiKey_doesNotExist() {
        when(apiKeyRepository.findById(API_KEY_ID)).thenReturn(java.util.Optional.empty());
        final Throwable exception = assertThrows(ApiKeyDoesNotExistException.class,
                () -> apiKeyService.getApiKeyName(API_KEY_ID));

        verify(apiKeyRepository).findById(API_KEY_ID);
        assertThat(exception.getMessage()).isEqualTo("API Key with id " + API_KEY_ID + " does not exist");
    }

}
