package gov.cabinetoffice.gapfindapiadmin.services;

import gov.cabinetoffice.gapfindapiadmin.exceptions.ApiKeyDoesNotExistException;
import gov.cabinetoffice.gapfindapiadmin.models.ApiKey;
import gov.cabinetoffice.gapfindapiadmin.repositories.ApiKeyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApiKeyService {

    private final ApiKeyRepository apiKeyRepository;

    public String getApiKeyName(Integer apiKeyId) {
        ApiKey apiKey = apiKeyRepository.findById(apiKeyId).orElseThrow(() -> new ApiKeyDoesNotExistException("API Key with id " + apiKeyId + " does not exist"));
        return apiKey.getApiKeyName();
    }

    public void revokeApiKey(Integer apiKeyId) {
        ApiKey apiKey = apiKeyRepository.findById(apiKeyId).orElseThrow(() -> new ApiKeyDoesNotExistException("API Key with id " + apiKeyId + " does not exist"));
        // TODO: set this based on how it's stored in the database
        apiKey.setRevoked(true);
        apiKeyRepository.save(apiKey);
    }
}
