package gov.cabinetoffice.gapfindapiadmin.services;

import gov.cabinetoffice.gapfindapiadmin.exceptions.InvalidApiKeyIdException;
import gov.cabinetoffice.gapfindapiadmin.models.GapApiKey;
import gov.cabinetoffice.gapfindapiadmin.models.GrantAdmin;
import gov.cabinetoffice.gapfindapiadmin.repositories.ApiKeyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public List<GapApiKey> getApiKeysForSelectedFundingOrganisations(List<String> selectedFundingOrgName) {
        final List<GapApiKey> gapApiKeys = (List<GapApiKey>) apiKeyRepository.findAll();
        return Optional.ofNullable(selectedFundingOrgName)
                .map(names -> gapApiKeys.stream()
                        .filter(key -> names.contains(key.getFundingOrganisation().getName()))
                        .collect(Collectors.toList()))
                .orElse(gapApiKeys);
    }

    public Long getActiveKeyCount(List<GapApiKey> gapApiKeys) {
        return Optional.ofNullable(gapApiKeys)
                .map(keys -> keys.stream()
                        .filter(key -> !key.isRevoked())
                        .count())
                .orElse(apiKeyRepository.countByIsRevokedFalse());
    }

    public Page<GapApiKey> findPaginated(Pageable pageable, List<GapApiKey> apiKeys) {
        final int pageSize = pageable.getPageSize();
        final int currentPage = pageable.getPageNumber();
        final int startItem = currentPage * pageSize;
        List<GapApiKey> paginatedList;

        if (apiKeys.size() < startItem) {
            paginatedList = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, apiKeys.size());
            paginatedList = apiKeys.subList(startItem, toIndex);
        }

        return new PageImpl<>(paginatedList, PageRequest.of(currentPage, pageSize), apiKeys.size());

    }
}
