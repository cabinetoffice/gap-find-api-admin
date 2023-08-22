package gov.cabinetoffice.gapfindapiadmin.services;

import gov.cabinetoffice.gapfindapiadmin.dtos.FilterDTO;
import gov.cabinetoffice.gapfindapiadmin.exceptions.InvalidApiKeyIdException;
import gov.cabinetoffice.gapfindapiadmin.models.FundingOrganisation;
import gov.cabinetoffice.gapfindapiadmin.models.GapApiKey;
import gov.cabinetoffice.gapfindapiadmin.models.GapUser;
import gov.cabinetoffice.gapfindapiadmin.models.GrantAdmin;
import gov.cabinetoffice.gapfindapiadmin.repositories.ApiKeyRepository;
import gov.cabinetoffice.gapfindapiadmin.repositories.FundingOrganisationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
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

    private final FilterDTO filterDTO = FilterDTO.builder()
            .name("test org")
            .isSelected(false)
            .build();

    private final List<String> filterDepartmentNames = Collections.singletonList("test org");

    @Test
    void getAllFundingOrganisationNames_noFilters() {
        when(fundingOrganisationRepository.findAll()).thenReturn(Collections.singleton(fundingOrganisation));

        final List<FilterDTO> filterDTOList = fundingOrganisationService.getAllFundingOrganisationNames(Collections.EMPTY_LIST);

        verify(fundingOrganisationRepository).findAll();
        assertThat(filterDTOList).isEqualTo(Collections.singletonList(filterDTO));
    }

    @Test
    void getAllFundingOrganisationNames_withFilters() {
        filterDTO.setSelected(true);
        when(fundingOrganisationRepository.findAll()).thenReturn(Collections.singleton(fundingOrganisation));

        final List<FilterDTO> filterDTOList = fundingOrganisationService.getAllFundingOrganisationNames(filterDepartmentNames);

        verify(fundingOrganisationRepository).findAll();
        assertThat(filterDTOList).isEqualTo(Collections.singletonList(filterDTO));
    }

}
