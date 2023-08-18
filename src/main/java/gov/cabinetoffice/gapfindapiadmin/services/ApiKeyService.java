package gov.cabinetoffice.gapfindapiadmin.services;

import gov.cabinetoffice.gapfindapiadmin.models.ApiKey;
import gov.cabinetoffice.gapfindapiadmin.models.GrantAdmin;
import gov.cabinetoffice.gapfindapiadmin.repositories.ApiKeyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApiKeyService {

    private final ApiKeyRepository apiKeyRepository;

    public List<ApiKey> getApiKeysForFundingOrganisation(int fundingOrgId) {
        return apiKeyRepository.findByFundingOrganisationId(fundingOrgId);
    }

    public void saveApiKey(ApiKey apiKey) {
        apiKeyRepository.save(apiKey);
    }

    public boolean doesApiKeyExist(String name) {
        return apiKeyRepository.findByName(name) != null;

    }

    public String getApiKeyName(int apiKeyId) {
        Optional<ApiKey> apiKey = apiKeyRepository.findById(apiKeyId);
        return apiKey.map(ApiKey::getName).orElse(null);
    }

    public void revokeApiKey(int apiKeyId) {
        Optional<ApiKey> apiKey = apiKeyRepository.findById(apiKeyId);

        if(apiKey.isPresent()) {
            final ZonedDateTime zonedDateTime = ZonedDateTime.now();

            GrantAdmin grantAdmin = (GrantAdmin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            apiKey.get().setRevokedBy(grantAdmin.getGapUser().getId());
            apiKey.get().setRevocationDate(zonedDateTime);
            apiKey.get().setRevoked(true);
            apiKeyRepository.save(apiKey.get());
        }
    }

    public Optional<ApiKey> getApiKeyById(int apiKeyId) {
        return apiKeyRepository.findById(apiKeyId);
    }
}
