package gov.cabinetoffice.gapfindapiadmin.services;

import gov.cabinetoffice.gapfindapiadmin.exceptions.InvalidApiKeyIdException;
import gov.cabinetoffice.gapfindapiadmin.models.GapApiKey;
import gov.cabinetoffice.gapfindapiadmin.models.GrantAdmin;
import gov.cabinetoffice.gapfindapiadmin.repositories.ApiKeyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

import static gov.cabinetoffice.gapfindapiadmin.controllers.ApiKeyController.SUPER_ADMIN_ROLE;

@Service
@RequiredArgsConstructor
public class ApiKeyService {

    private final ApiKeyRepository apiKeyRepository;

    public List<GapApiKey> getApiKeysForFundingOrganisation(int fundingOrgId) {
        return apiKeyRepository.findByFundingOrganisationId(fundingOrgId);
    }

    public void saveApiKey(GapApiKey apiKey) {
        apiKeyRepository.save(apiKey);
    }

    public boolean doesApiKeyExist(String name) {
        return apiKeyRepository.findByName(name) != null;
    }

    public String getApiKeyName(int apiKeyId) {
        return getApiKeyById(apiKeyId).getName();
    }

    public void revokeApiKey(int apiKeyId) {
        final GapApiKey apiKey = getApiKeyById(apiKeyId);
        final GrantAdmin grantAdmin = (GrantAdmin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        apiKey.setRevokedBy(grantAdmin.getGapUser().getId());
        apiKey.setRevocationDate(ZonedDateTime.now());
        apiKey.setRevoked(true);

        apiKeyRepository.save(apiKey);
    }

    public GapApiKey getApiKeyById(int apiKeyId) {
        return apiKeyRepository.findById(apiKeyId)
                .orElseThrow(() -> new InvalidApiKeyIdException("Invalid API Key Id: " + apiKeyId));
    }

    //TODO change those return values 
    public String generateBackButtonValue(){
        if(isSuperAdmin()){
            return "/api-keys/super-admin";
        }
        return "/api-keys";
    }

    protected boolean isSuperAdmin(){
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(SUPER_ADMIN_ROLE));
    }



}
