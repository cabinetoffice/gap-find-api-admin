package gov.cabinetoffice.gapfindapiadmin.service;

import gov.cabinetoffice.gapfindapiadmin.model.ApiKey;
import gov.cabinetoffice.gapfindapiadmin.repository.ApiKeyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApiKeyService {

    private final ApiKeyRepository apiKeyRepository;

    public List<ApiKey> getApiKeysForFundingOrganisation(int fundingOrgId){
        return apiKeyRepository.findByFundingOrganisationId(fundingOrgId);
    }


}
