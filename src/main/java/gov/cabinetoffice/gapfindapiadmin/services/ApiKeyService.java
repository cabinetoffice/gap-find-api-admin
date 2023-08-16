package gov.cabinetoffice.gapfindapiadmin.services;

import gov.cabinetoffice.gapfindapiadmin.exceptions.ApiKeyDoesNotExistException;
import gov.cabinetoffice.gapfindapiadmin.models.ApiKey;
import gov.cabinetoffice.gapfindapiadmin.repositories.ApiKeyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApiKeyService {

    private final ApiKeyRepository apiKeyRepository;

    public List<ApiKey> getApiKeysForFundingOrganisation(int fundingOrgId){
        return apiKeyRepository.findByFundingOrganisationId(fundingOrgId);
    }

    public String getApiKeyName(int apiKeyId) {
        Optional<ApiKey> apiKey = apiKeyRepository.findById(apiKeyId);
        return apiKey.map(ApiKey::getName).orElse(null);
    }

    public void revokeApiKey(int apiKeyId) {
        Optional<ApiKey> apiKey = apiKeyRepository.findById(apiKeyId);

        if(apiKey.isPresent()) {
            final ZonedDateTime zonedDateTime = ZonedDateTime.now();

            UUID uuid = UUID.randomUUID();
            apiKey.get().setRevocationDate(zonedDateTime);
            apiKey.get().setRevokedBy(uuid); // TODO set to logged in user
            apiKey.get().setRevoked(true);

            apiKeyRepository.save(apiKey.get());
        }
    }

    public Optional<ApiKey> getApiKeyById(int apiKeyId) {
        return apiKeyRepository.findById(apiKeyId);
    }
}
