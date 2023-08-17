package gov.cabinetoffice.gapfindapiadmin.services;

import gov.cabinetoffice.gapfindapiadmin.models.ApiKey;
import gov.cabinetoffice.gapfindapiadmin.repositories.ApiKeyRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApiKeyServiceTest {

    @Mock
    private ApiKeyRepository apiKeyRepository;

    @InjectMocks
    private ApiKeyService serviceUnderTest;

    private final Integer API_KEY_ID = 1;

    private final ApiKey apiKey = ApiKey.builder()
            .id(API_KEY_ID)
            .name("Test API Key name")
            .apiKey("Test API Key")
            .isRevoked(false)
            .build();

    @Test
    void getApiKeysForFundingOrganisation_resultsReturned() {
        final int fundingOrgId = 1;
        final String apiKeyName = "Key Name";
        final List<ApiKey> expectedApiKeys = List.of(ApiKey.builder().name(apiKeyName).build());

        when(apiKeyRepository.findByFundingOrganisationId(any(Integer.class))).thenReturn(expectedApiKeys);

        List<ApiKey> actualApiKeys = serviceUnderTest.getApiKeysForFundingOrganisation(fundingOrgId);
        assertThat(actualApiKeys).isEqualTo(expectedApiKeys);

    }

    @Test
    void saveApiKey_apiKeySaved() {
        final ApiKey apiKey = ApiKey.builder().name("Key Name").build();

        serviceUnderTest.saveApiKey(apiKey);

        verify(apiKeyRepository).save(apiKey);
    }

    @Test
    void getApiKeyName_returnsExpectedResponse() {
        when(apiKeyRepository.findById(API_KEY_ID)).thenReturn(java.util.Optional.ofNullable(apiKey));

        final String response = serviceUnderTest.getApiKeyName(API_KEY_ID);

        verify(apiKeyRepository).findById(API_KEY_ID);
        assertThat(response).isEqualTo(apiKey.getName());
    }

    @Test
    void getApiKey_doesNotExist() {
        when(apiKeyRepository.findById(API_KEY_ID)).thenReturn(java.util.Optional.empty());

        final String response = serviceUnderTest.getApiKeyName(API_KEY_ID);

        verify(apiKeyRepository).findById(API_KEY_ID);
        assertThat(response).isNull();
    }

    @Test
    void revokeApiKey_returnsExpectedResponse() {
        when(apiKeyRepository.findById(API_KEY_ID)).thenReturn(java.util.Optional.ofNullable(apiKey));

        serviceUnderTest.revokeApiKey(API_KEY_ID);

        verify(apiKeyRepository).findById(API_KEY_ID);
        assertThat(apiKey.isRevoked()).isEqualTo(true);
    }

    @Test
    void doesApiKeyExist_apiKeyExists() {
        final String apiKeyName = "Key Name";

        when(apiKeyRepository.findByName(apiKeyName)).thenReturn(ApiKey.builder().name(apiKeyName).build());

        final boolean actual = serviceUnderTest.doesApiKeyExist(apiKeyName);
        assertThat(actual).isTrue();

    }

    @Test
    void doesApiKeyExist_apiKeyDoesNotExist() {
        final String apiKeyName = "nonExistingKey";

        when(apiKeyRepository.findByName(apiKeyName)).thenReturn(null);

        boolean actual = serviceUnderTest.doesApiKeyExist(apiKeyName);
        assertThat(actual).isFalse();
    }

    @Test
    void getApiKeyById_returnsExpectedResponse() {
        when(apiKeyRepository.findById(API_KEY_ID)).thenReturn(java.util.Optional.ofNullable(apiKey));

        final Optional<ApiKey> response = serviceUnderTest.getApiKeyById(API_KEY_ID);

        verify(apiKeyRepository).findById(API_KEY_ID);
        assertThat(response).isEqualTo(java.util.Optional.of(apiKey));
    }

    @Test
    void getApiKeyById_doesNotExist() {
        when(apiKeyRepository.findById(API_KEY_ID)).thenReturn(java.util.Optional.empty());

        final Optional<ApiKey> response = serviceUnderTest.getApiKeyById(API_KEY_ID);

        verify(apiKeyRepository).findById(API_KEY_ID);
        assertThat(response).isEqualTo(java.util.Optional.empty());
    }

}
