package gov.cabinetoffice.gapfindapiadmin.services;

import gov.cabinetoffice.gapfindapiadmin.exceptions.InvalidApiKeyIdException;
import gov.cabinetoffice.gapfindapiadmin.models.FundingOrganisation;
import gov.cabinetoffice.gapfindapiadmin.models.GapApiKey;
import gov.cabinetoffice.gapfindapiadmin.models.GapUser;
import gov.cabinetoffice.gapfindapiadmin.models.GrantAdmin;
import gov.cabinetoffice.gapfindapiadmin.repositories.ApiKeyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApiKeyServiceTest {

    private final FundingOrganisation fundingOrganisation = FundingOrganisation.builder().id(1).build();
    private final GapUser gapUser = GapUser.builder().id(1).userSub("sub").build();
    private final GrantAdmin grantAdmin = GrantAdmin.builder().gapUser(gapUser).funder(fundingOrganisation).build();
    private final Integer API_KEY_ID = 1;
    private final GapApiKey apiKey = GapApiKey.builder()
            .id(API_KEY_ID)
            .name("Test API Key name")
            .apiKey("Test API Key")
            .isRevoked(false)
            .build();
    @Mock
    private SecurityContext securityContext;
    @Mock
    private Authentication authentication;
    @Mock
    private ApiKeyRepository apiKeyRepository;
    @InjectMocks
    private ApiKeyService serviceUnderTest;

    @Test
    void getApiKeysForFundingOrganisation_resultsReturned() {
        final int fundingOrgId = 1;
        final String apiKeyName = "Key Name";
        final List<GapApiKey> expectedApiKeys = List.of(GapApiKey.builder().name(apiKeyName).build());

        when(apiKeyRepository.findByFundingOrganisationId(any(Integer.class))).thenReturn(expectedApiKeys);

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
        setSecurityContext();
        when(apiKeyRepository.findById(API_KEY_ID)).thenReturn(Optional.ofNullable(apiKey));

        serviceUnderTest.revokeApiKey(API_KEY_ID);

        verify(apiKeyRepository).findById(API_KEY_ID);
        verify(apiKeyRepository).save(apiKey);
        assertThat(apiKey.isRevoked()).isTrue();
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
    void generateBackButtonValue_returnExpectedWhenUserIsASuperAdmin() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(createAuthenticationWithRoles("SUPER_ADMIN"));
        assertThat(serviceUnderTest.generateBackButtonValue()).isEqualTo("/api-keys/super-admin");
    }

    @Test
    void generateBackButtonValue_returnExpectedWhenUserIsATechnicalSupport() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(createAuthenticationWithRoles("TECHNICAL_SUPPORT"));
        assertThat(serviceUnderTest.generateBackButtonValue()).isEqualTo("/api-keys");
    }

    @Test
    void isSuperAdmin_returnTrueWhenUserIsASuperAdmin() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(createAuthenticationWithRoles("SUPER_ADMIN"));
        final boolean actual = serviceUnderTest.isSuperAdmin();
        assertThat(actual).isTrue();
    }

    @Test
    void isSuperAdmin_returnFalseWhenUserIsASuperAdmin() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(createAuthenticationWithRoles("TECHNICAL_SUPPORT"));
        final boolean actual = serviceUnderTest.isSuperAdmin();
        assertThat(actual).isFalse();
    }

    private Authentication createAuthenticationWithRoles(String role) {
        Collection<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(role));
        return new UsernamePasswordAuthenticationToken("username", "password", authorities);
    }

    private void setSecurityContext() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(grantAdmin);
    }
}
