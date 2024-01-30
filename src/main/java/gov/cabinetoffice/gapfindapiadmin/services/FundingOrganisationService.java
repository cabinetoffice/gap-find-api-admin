package gov.cabinetoffice.gapfindapiadmin.services;

import gov.cabinetoffice.gapfindapiadmin.models.FundingOrganisation;
import gov.cabinetoffice.gapfindapiadmin.repositories.FundingOrganisationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FundingOrganisationService {

    private final FundingOrganisationRepository fundingOrganisationRepository;

    public FundingOrganisation getFundingOrganisationByName(String name) {
        return fundingOrganisationRepository
                .findByName(name).orElseGet(() ->
                        fundingOrganisationRepository.save(FundingOrganisation.builder().name(name)
                                .build()));}
}
