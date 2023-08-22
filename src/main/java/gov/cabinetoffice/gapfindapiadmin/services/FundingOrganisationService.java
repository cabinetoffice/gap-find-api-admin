package gov.cabinetoffice.gapfindapiadmin.services;

import gov.cabinetoffice.gapfindapiadmin.dtos.FilterDTO;
import gov.cabinetoffice.gapfindapiadmin.repositories.FundingOrganisationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FundingOrganisationService {

    private final FundingOrganisationRepository fundingOrganisationRepository;

    public List<FilterDTO> getAllFundingOrganisationNames(List<String> fundingOrgName) {
        final List<FilterDTO> fundingOrganisationNames = new ArrayList<>();
        fundingOrganisationRepository.findAll().forEach(fundingOrganisation -> {
            final String name = fundingOrganisation.getName();
            fundingOrganisationNames.add(new FilterDTO(name, fundingOrgName != null && fundingOrgName.contains(name)));
        });

        return fundingOrganisationNames;
    }
}
