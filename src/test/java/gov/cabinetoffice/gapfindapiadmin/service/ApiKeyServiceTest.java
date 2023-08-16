package gov.cabinetoffice.gapfindapiadmin.service;

import gov.cabinetoffice.gapfindapiadmin.models.ApiKey;
import gov.cabinetoffice.gapfindapiadmin.repositories.ApiKeyRepository;
import gov.cabinetoffice.gapfindapiadmin.services.ApiKeyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ApiKeyServiceTest {

    @Mock
    private ApiKeyRepository apiKeyRepository;

    @InjectMocks
    private ApiKeyService serviceUnderTest;

    @Test
    public void getApiKeysForFundingOrganisation_resultsReturned(){

        final int fundingOrgId = 1;

        final String apiKeyName = "Key Name";

        final List<ApiKey> expectedApiKeys = List.of(ApiKey.builder().name(apiKeyName).build());

        when(apiKeyRepository.findByFundingOrganisationId(any(Integer.class))).thenReturn(expectedApiKeys);

        List<ApiKey> actualApiKeys = serviceUnderTest.getApiKeysForFundingOrganisation(fundingOrgId);

        assertThat(actualApiKeys).isEqualTo(expectedApiKeys);

    }



}
