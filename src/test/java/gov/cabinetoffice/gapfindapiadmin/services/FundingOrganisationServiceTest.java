package gov.cabinetoffice.gapfindapiadmin.services;

import gov.cabinetoffice.gapfindapiadmin.models.FundingOrganisation;
import gov.cabinetoffice.gapfindapiadmin.repositories.FundingOrganisationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FundingOrganisationServiceTest {

    @Mock
    private FundingOrganisationRepository fundingOrganisationRepository;

    @InjectMocks
    private FundingOrganisationService fundingOrganisationService;

    private final FundingOrganisation fundingOrganisation = FundingOrganisation.builder()
            .id(1)
            .name("test org")
            .build();

    private final List<String> fundingOrganisationNamesList= List.of("test org");

    @Test
    void getAllFundingOrganisationNames_noFilters() {
        when(fundingOrganisationRepository.findAllBy()).thenReturn(List.of(fundingOrganisation));

        final List<String> response = fundingOrganisationService.getAllFundingOrganisationNames();

        verify(fundingOrganisationRepository).findAllBy();
        assertThat(response).isEqualTo(fundingOrganisationNamesList);
    }

}
