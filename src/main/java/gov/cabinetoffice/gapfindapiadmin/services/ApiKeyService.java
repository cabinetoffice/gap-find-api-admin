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
    // TODO: should we return null instead of throwing an exception?
    public String getApiKeyName(int apiKeyId) {
        ApiKey apiKey = apiKeyRepository.findById(apiKeyId).orElseThrow(() -> new ApiKeyDoesNotExistException("API Key with id " + apiKeyId + " does not exist"));
        return apiKey.getName();
    }

    public void revokeApiKey(int apiKeyId) {
        ApiKey apiKey = apiKeyRepository.findById(apiKeyId).orElseThrow(() -> new ApiKeyDoesNotExistException("API Key with id " + apiKeyId + " does not exist"));

        if(apiKey!=null) {
            final ZonedDateTime zonedDateTime = ZonedDateTime.now();

            UUID uuid = UUID.randomUUID();
            apiKey.setRevocationDate(zonedDateTime);
            apiKey.setRevokedBy(uuid); // TODO set to logged in user
            apiKey.setRevoked(true);

            apiKeyRepository.save(apiKey);
        }
    }

    public Optional<ApiKey> getApiKeyById(int apiKeyId) {
        return apiKeyRepository.findById(apiKeyId);
    }
}
