package gov.cabinetoffice.gapfindapiadmin.services;

import gov.cabinetoffice.gapfindapiadmin.exceptions.InvalidApiKeyIdException;
import gov.cabinetoffice.gapfindapiadmin.exceptions.UnauthorizedException;
import gov.cabinetoffice.gapfindapiadmin.models.GapApiKey;
import gov.cabinetoffice.gapfindapiadmin.models.GrantAdmin;
import gov.cabinetoffice.gapfindapiadmin.repositories.ApiKeyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

import static gov.cabinetoffice.gapfindapiadmin.security.JwtAuthorisationFilter.SUPER_ADMIN_ROLE;
import static java.util.Comparator.comparing;
import static java.util.Optional.ofNullable;

@Service
@RequiredArgsConstructor
@Slf4j
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
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final List<SimpleGrantedAuthority> authorities = (List<SimpleGrantedAuthority>) auth.getAuthorities();

        final GrantAdmin grantAdmin = (GrantAdmin) auth.getPrincipal();
        final boolean isSuperAdmin = authorities.contains(new SimpleGrantedAuthority(SUPER_ADMIN_ROLE));

        GapApiKey apiKey;
        try {
            apiKey = getApiKeyById(apiKeyId);
        } catch (InvalidApiKeyIdException e) {
            log.info(e.getMessage());
            return;
        }

        if(isSuperAdmin || grantAdmin.getFunder().getName().equals(apiKey.getFundingOrganisation().getName())) {
            apiKey.setRevokedBy(grantAdmin.getGapUser().getId());
            apiKey.setRevocationDate(ZonedDateTime.now());
            apiKey.setRevoked(true);
            apiKeyRepository.save(apiKey);
        } else {
            throw new UnauthorizedException("User is unauthorised to revoke this API key");
        }
    }

    public GapApiKey getApiKeyById(int apiKeyId) {
        return apiKeyRepository.findById(apiKeyId)
                .orElseThrow(() -> new InvalidApiKeyIdException("Invalid API Key Id: " + apiKeyId));
    }


    public List<GapApiKey> getApiKeysForSelectedFundingOrganisations(List<String> selectedFundingOrgName) {
        List<GapApiKey> gapApiKeys = ofNullable(selectedFundingOrgName)
                .map(names -> names.stream()
                        .flatMap(name -> apiKeyRepository.findByFundingOrganisationName(name).stream())
                        .toList())
                .orElseGet(() -> (List<GapApiKey>) apiKeyRepository.findAll());

        return gapApiKeys.stream()
                .sorted(comparing(GapApiKey::isRevoked))
                .toList();
    }

    public Long getActiveKeyCount(List<GapApiKey> gapApiKeys) {
        return ofNullable(gapApiKeys)
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
            paginatedList = List.of();
        } else {
            int toIndex = Math.min(startItem + pageSize, apiKeys.size());
            paginatedList = apiKeys.subList(startItem, toIndex);
        }

        return new PageImpl<>(paginatedList, PageRequest.of(currentPage, pageSize), apiKeys.size());

    }

    public List<String> getFundingOrgForAllApiKeys() {
        return apiKeyRepository.findByUniqueFundingOrganisationNames();
    }


}
