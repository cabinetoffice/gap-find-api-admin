package gov.cabinetoffice.gapfindapiadmin.services;

import gov.cabinetoffice.gapfindapiadmin.exceptions.InvalidApiKeyIdException;
import gov.cabinetoffice.gapfindapiadmin.exceptions.UnauthorizedException;
import gov.cabinetoffice.gapfindapiadmin.models.*;
import gov.cabinetoffice.gapfindapiadmin.repositories.ApiKeyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApiKeyServiceTest {

    private final FundingOrganisation fundingOrganisation = FundingOrganisation.builder()
            .id(1)
            .name("test org")
            .build();
    private final FundingOrganisation fundingOrganisation2 = FundingOrganisation.builder()
            .id(2)
            .name("Funding org")
            .build();

    private final JwtPayload jwtPayload = JwtPayload.builder().sub("1234").departmentName("test org")
            .roles("[]").build();
    private final JwtPayload jwtPayload2 = JwtPayload.builder().sub("1234").departmentName("Funding org")
            .roles("[]").build();

    private final TechSupportUser techSupportUser =
            TechSupportUser.builder().userSub("1234").funder(fundingOrganisation).build();

    private final Integer API_KEY_ID = 1;

    private final GapApiKey apiKey = GapApiKey.builder()
            .id(API_KEY_ID)
            .name("Test API Key name")
            .apiKey("Test API Key")
            .fundingOrganisation(fundingOrganisation)
            .isRevoked(false)
            .build();

    private final GapApiKey apiKey2 = GapApiKey.builder()
            .id(API_KEY_ID)
            .name("Test API Key name 2")
            .apiKey("Test API Key 2")
            .fundingOrganisation(fundingOrganisation2)
            .isRevoked(false)
            .build();
    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Mock
    private ApiKeyRepository apiKeyRepository;

    @Mock
    private TechSupportUserService techSupportUserService;

    @Mock
    private Pageable pageable;


    @InjectMocks
    private ApiKeyService serviceUnderTest;

    @Test
    void getApiKeysForFundingOrganisation_resultsReturned() {
        final int fundingOrgId = 1;
        final String apiKeyName = "Key Name";
        final List<GapApiKey> expectedApiKeys = List.of(GapApiKey.builder().name(apiKeyName).build());

        when(apiKeyRepository.findByFundingOrganisation_IdOrderByIsRevokedAscCreatedDateAsc(any(Integer.class))).thenReturn(expectedApiKeys);

        List<GapApiKey> actualApiKeys = serviceUnderTest.getApiKeysForFundingOrganisation(fundingOrgId);
        assertThat(actualApiKeys).isEqualTo(expectedApiKeys);

    }

    @Test
    void saveApiKey_apiKeySaved() {
        final GapApiKey apiKey = GapApiKey.builder().name("Key Name").build();

        serviceUnderTest.saveApiKey(apiKey);

        verify(apiKeyRepository).save(apiKey);
    }

    @Test
    void getApiKeyName_returnsExpectedResponse() {
        when(apiKeyRepository.findById(API_KEY_ID)).thenReturn(java.util.Optional.ofNullable(apiKey));

        final String response = serviceUnderTest.getApiKeyName(API_KEY_ID);

        verify(apiKeyRepository).findById(API_KEY_ID);
        assertThat(response).isEqualTo(apiKey.getName());
    }

    @Test
    void revokeApiKey_returnsExpectedResponse() {
        setSecurityContext(jwtPayload);
        when(apiKeyRepository.findById(API_KEY_ID)).thenReturn(Optional.ofNullable(apiKey));
        when(techSupportUserService.getTechSupportUserBySub(jwtPayload.getSub())).thenReturn(techSupportUser);

        serviceUnderTest.revokeApiKey(API_KEY_ID);

        verify(apiKeyRepository).findById(API_KEY_ID);
        verify(apiKeyRepository).save(apiKey);
        assertThat(apiKey.isRevoked()).isTrue();
    }

    @Test
    void revokeApiKey_throwsInvalidApiKeyIdException() {
        setSecurityContext(jwtPayload);
        when(apiKeyRepository.findById(API_KEY_ID)).thenThrow(new InvalidApiKeyIdException());

        serviceUnderTest.revokeApiKey(API_KEY_ID);

        verify(apiKeyRepository).findById(API_KEY_ID);
        assertThat(apiKey.isRevoked()).isFalse();
    }

    @Test
    void revokeApiKey_throwsUnauthorizedException() {
        setSecurityContext(jwtPayload2);
        when(apiKeyRepository.findById(API_KEY_ID)).thenReturn(Optional.ofNullable(apiKey));
        when(techSupportUserService.getTechSupportUserBySub(jwtPayload.getSub())).thenReturn(techSupportUser);

        assertThatThrownBy(() -> serviceUnderTest.revokeApiKey(API_KEY_ID))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage("User is unauthorised to revoke this API key");

        verify(apiKeyRepository).findById(API_KEY_ID);
        assertThat(apiKey.isRevoked()).isFalse();
    }

    @Test
    void doesApiKeyExist_apiKeyExists() {
        final String apiKeyName = "Key Name";

        when(apiKeyRepository.findByName(apiKeyName)).thenReturn(GapApiKey.builder().name(apiKeyName).build());

        final boolean actual = serviceUnderTest.doesApiKeyExist(apiKeyName);
        assertThat(actual).isTrue();

    }

    @Test
    void doesApiKeyExist_apiKeyDoesNotExist() {
        final String apiKeyName = "nonExistingKey";

        when(apiKeyRepository.findByName(apiKeyName)).thenReturn(null);

        boolean actual = serviceUnderTest.doesApiKeyExist(apiKeyName);
        assertThat(actual).isFalse();
    }

    @Test
    void getApiKeyById_returnsExpectedResponse() {
        when(apiKeyRepository.findById(API_KEY_ID)).thenReturn(java.util.Optional.ofNullable(apiKey));

        final GapApiKey response = serviceUnderTest.getApiKeyById(API_KEY_ID);

        verify(apiKeyRepository).findById(API_KEY_ID);
        assertThat(response).isEqualTo(apiKey);
    }

    @Test
    void getApiKeyById_throwsException() {
        when(apiKeyRepository.findById(API_KEY_ID)).thenReturn(java.util.Optional.empty());

        assertThatThrownBy(() -> serviceUnderTest.getApiKeyById(API_KEY_ID))
                .isInstanceOf(InvalidApiKeyIdException.class)
                .hasMessage("Invalid API Key Id: " + API_KEY_ID);
        verify(apiKeyRepository).findById(API_KEY_ID);
    }


    @Test
    void getApiKeysForSelectedFundingOrganisations_noneSelected() {
        when(apiKeyRepository.findByOrderByIsRevokedAscCreatedDateAsc()).thenReturn(Collections.singletonList(apiKey));

        final List<GapApiKey> response = serviceUnderTest.getApiKeysForSelectedFundingOrganisations(null);

        verify(apiKeyRepository).findByOrderByIsRevokedAscCreatedDateAsc();
        assertThat(response).isEqualTo(Collections.singletonList(apiKey));
    }

    @Test
    void getApiKeysForSelectedFundingOrganisations_oneSelected() {
        final List<String> fundingOrgName = Collections.singletonList("test org");
        when(apiKeyRepository.findByFundingOrganisation_NameOrderByIsRevokedAscCreatedDateAsc("test org")).thenReturn(List.of(apiKey));

        final List<GapApiKey> response = serviceUnderTest.getApiKeysForSelectedFundingOrganisations(fundingOrgName);

        verify(apiKeyRepository).findByFundingOrganisation_NameOrderByIsRevokedAscCreatedDateAsc("test org");
        assertThat(response).isEqualTo(Collections.singletonList(apiKey));
    }

    @Test
    void getActiveKeyCount_forAllKeys() {
        when(apiKeyRepository.countByIsRevokedFalse()).thenReturn(Long.valueOf(3));

        final Long response = serviceUnderTest.getActiveKeyCount(null);

        verify(apiKeyRepository).countByIsRevokedFalse();
        assertThat(response).isEqualTo(Long.valueOf(3));
    }

    @Test
    void getActiveKeyCount_forAllFilteredKeys() {
        when(apiKeyRepository.countByIsRevokedFalse()).thenReturn(Long.valueOf(2));

        final Long response = serviceUnderTest.getActiveKeyCount(List.of(apiKey, apiKey2));

        assertThat(response).isEqualTo(Long.valueOf(2));
    }

    @Test
    void findPaginated_returnApiKeyPage() {
        final Page<GapApiKey> apiKeyPage = new PageImpl<>(Collections.singletonList(apiKey), PageRequest.of(0, 1), 1);
        when(pageable.getPageSize()).thenReturn(1);
        when(pageable.getPageNumber()).thenReturn(0);

        final Page<GapApiKey> response = serviceUnderTest.findPaginated(pageable, Collections.singletonList(apiKey));

        assertThat(response).isEqualTo(apiKeyPage);
    }

    @Test
    void findPaginated_returnEmptyList() {
        final Page<GapApiKey> apiKeyPage = new PageImpl<>(List.of(), PageRequest.of(1, 1), 0);
        when(pageable.getPageSize()).thenReturn(1);
        when(pageable.getPageNumber()).thenReturn(1);

        final Page<GapApiKey> response = serviceUnderTest.findPaginated(pageable, List.of());

        assertThat(response).isEqualTo(apiKeyPage);
    }

    @Test
    void getFundingOrgForAllApiKeys_returnsUniqueKeys() {
        final List<String> orgNames = List.of("test org");
        when(apiKeyRepository.findByUniqueFundingOrganisationNames()).thenReturn(orgNames);

        final List<String> response = serviceUnderTest.getFundingOrgForAllApiKeys();

        verify(apiKeyRepository).findByUniqueFundingOrganisationNames();
        assertThat(response).isEqualTo(orgNames);
    }


    private void setSecurityContext(JwtPayload jwtPayload) {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(jwtPayload);
    }
}
