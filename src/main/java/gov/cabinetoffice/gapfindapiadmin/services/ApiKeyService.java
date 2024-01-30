package gov.cabinetoffice.gapfindapiadmin.services;

import gov.cabinetoffice.gapfindapiadmin.exceptions.InvalidApiKeyIdException;
import gov.cabinetoffice.gapfindapiadmin.exceptions.UnauthorizedException;
import gov.cabinetoffice.gapfindapiadmin.models.GapApiKey;
import gov.cabinetoffice.gapfindapiadmin.models.JwtPayload;
import gov.cabinetoffice.gapfindapiadmin.models.TechSupportUser;
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

    private final TechSupportUserService techSupportUserService;

    public List<GapApiKey> getApiKeysForFundingOrganisation(int fundingOrgId) {
        log.info("Getting API keys for funding org id: {}", fundingOrgId);

        return apiKeyRepository.findByFundingOrganisation_IdOrderByIsRevokedAscCreatedDateAsc(fundingOrgId);
    }

    public void saveApiKey(GapApiKey apiKey) {
        log.info("Saving API key: {}", apiKey.getName());

        apiKeyRepository.save(apiKey);

        log.info("API key saved");
    }

    public boolean doesApiKeyExist(String name) {
        log.info("Checking if API key with name: {} exists", name);

        return apiKeyRepository.findByName(name) != null;
    }

    public String getApiKeyName(int apiKeyId) {
        log.info("Getting API key name for id: {}", apiKeyId);

        return getApiKeyById(apiKeyId).getName();
    }

    public void revokeApiKey(int apiKeyId) {
        log.info("Revoking API key with id: {} in db", apiKeyId);

        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final List<SimpleGrantedAuthority> authorities = (List<SimpleGrantedAuthority>) auth.getAuthorities();

        final JwtPayload jwtPayload = (JwtPayload) auth.getPrincipal();
        final boolean isSuperAdmin = authorities.contains(new SimpleGrantedAuthority(SUPER_ADMIN_ROLE));

        log.info("User is a super admin: {}", isSuperAdmin);

        GapApiKey apiKey;
        try {
            apiKey = getApiKeyById(apiKeyId);
        } catch (InvalidApiKeyIdException e) {
            log.info(e.getMessage());
            return;
        }

        log.info("API key found: {}", apiKey.getName());

        TechSupportUser techSupportUser = techSupportUserService.getTechSupportUserBySub(jwtPayload.getSub());

        if (isSuperAdmin || jwtPayload.getDepartmentName().equals(apiKey.getFundingOrganisation().getName())) {
            apiKey.setRevokedBy(techSupportUser.getId());
            apiKey.setRevocationDate(ZonedDateTime.now());
            apiKey.setRevoked(true);
            apiKeyRepository.save(apiKey);

            log.info("API key revoked");

        } else {
            throw new UnauthorizedException("User is unauthorised to revoke this API key");
        }
    }

    public GapApiKey getApiKeyById(int apiKeyId) {
        log.info("Getting API key for id: {}", apiKeyId);

        return apiKeyRepository.findById(apiKeyId)
                .orElseThrow(() -> new InvalidApiKeyIdException("Invalid API Key Id: " + apiKeyId));
    }


    public List<GapApiKey> getApiKeysForSelectedFundingOrganisations(List<String> selectedFundingOrgName) {

        if (selectedFundingOrgName == null) {
            log.info("No filter applied, Getting all API keys");
        } else {
            log.info("Getting API keys for funding orgs: {}", selectedFundingOrgName);
        }

        List<GapApiKey> gapApiKeys = ofNullable(selectedFundingOrgName)
                .map(names -> names.stream()
                        .flatMap(name -> apiKeyRepository.findByFundingOrganisation_NameOrderByIsRevokedAscCreatedDateAsc(name).stream())
                        .toList())
                .orElseGet(() -> apiKeyRepository.findByOrderByIsRevokedAscCreatedDateAsc());

        return gapApiKeys.stream()
                .sorted(comparing(GapApiKey::isRevoked))
                .toList();
    }

    public Long getActiveKeyCount(List<GapApiKey> gapApiKeys) {
        log.info("Getting active key count");

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
        log.info("Getting all the funding org names for all API keys to populate filter");

        return apiKeyRepository.findByUniqueFundingOrganisationNames();
    }


}
