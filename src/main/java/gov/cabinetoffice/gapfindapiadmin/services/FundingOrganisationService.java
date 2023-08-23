package gov.cabinetoffice.gapfindapiadmin.services;

import gov.cabinetoffice.gapfindapiadmin.models.FundingOrganisation;
import gov.cabinetoffice.gapfindapiadmin.repositories.FundingOrganisationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FundingOrganisationService {

    private final FundingOrganisationRepository fundingOrganisationRepository;

    public List<String> getAllFundingOrganisationNames() {
        return fundingOrganisationRepository.findAllBy().stream().map(FundingOrganisation::getName).toList();
    }
}
