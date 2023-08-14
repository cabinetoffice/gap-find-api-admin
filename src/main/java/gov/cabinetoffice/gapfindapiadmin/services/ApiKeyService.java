package gov.cabinetoffice.gapfindapiadmin.services;

import gov.cabinetoffice.gapfindapiadmin.models.ApiKey;
import gov.cabinetoffice.gapfindapiadmin.repositories.ApiKeyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApiKeyService {

    private final ApiKeyRepository apiKeyRepository;

    public void revokeApiKey(Integer apiKeyId) {
        ApiKey apiKey = apiKeyRepository.findByApiKeyId(apiKeyId);
        apiKey.setRevoked(true);
        apiKeyRepository.save(apiKey);
    }
}
