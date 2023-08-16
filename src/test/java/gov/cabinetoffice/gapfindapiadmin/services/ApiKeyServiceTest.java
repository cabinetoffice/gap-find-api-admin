package gov.cabinetoffice.gapfindapiadmin.services;

import gov.cabinetoffice.gapfindapiadmin.models.ApiKey;
import gov.cabinetoffice.gapfindapiadmin.repositories.ApiKeyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApiKeyServiceTest {

    @Mock
    private ApiKeyRepository apiKeyRepository;

    @InjectMocks
    private ApiKeyService serviceUnderTest;

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
    void doesApiKeyExist_apiKeyExists() {
        final String apiKeyName = "Key Name";

        when(apiKeyRepository.findByName(apiKeyName)).thenReturn(of(ApiKey.builder().name(apiKeyName).build()));

        final boolean actual = serviceUnderTest.doesApiKeyExist(apiKeyName);
        assertThat(actual).isTrue();

    }

    @Test
    void doesApiKeyExist_apiKeyDoesNotExist() {
        final String apiKeyName = "nonExistingKey";

        when(apiKeyRepository.findByName(apiKeyName)).thenReturn(Optional.ofNullable(null));

        boolean actual = serviceUnderTest.doesApiKeyExist(apiKeyName);
        assertThat(actual).isFalse();

    }

}
