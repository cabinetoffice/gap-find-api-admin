package gov.cabinetoffice.gapfindapiadmin.controllers;

import gov.cabinetoffice.gapfindapiadmin.config.NavBarConfigProperties;
import gov.cabinetoffice.gapfindapiadmin.config.UserServiceConfig;
import gov.cabinetoffice.gapfindapiadmin.dtos.CreateApiKeyDTO;
import gov.cabinetoffice.gapfindapiadmin.dtos.NavBarDto;
import gov.cabinetoffice.gapfindapiadmin.helpers.PaginationHelper;
import gov.cabinetoffice.gapfindapiadmin.models.FundingOrganisation;
import gov.cabinetoffice.gapfindapiadmin.models.GapApiKey;
import gov.cabinetoffice.gapfindapiadmin.models.GapUser;
import gov.cabinetoffice.gapfindapiadmin.models.GrantAdmin;
import gov.cabinetoffice.gapfindapiadmin.services.ApiGatewayService;
import gov.cabinetoffice.gapfindapiadmin.services.ApiKeyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static gov.cabinetoffice.gapfindapiadmin.security.JwtAuthorisationFilter.ADMIN_ROLE;
import static gov.cabinetoffice.gapfindapiadmin.security.JwtAuthorisationFilter.SUPER_ADMIN_ROLE;
import static gov.cabinetoffice.gapfindapiadmin.security.JwtAuthorisationFilter.TECHNICAL_SUPPORT_ROLE;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApiKeyControllerTest {

    private static final Integer API_KEY_ID = 1;
    private final FundingOrganisation fundingOrganisation = FundingOrganisation.builder().id(1).name("testDepartmentName").build();
    private final GapUser gapUser = GapUser.builder().id(1).userSub("sub").build();
    private final GrantAdmin grantAdmin = GrantAdmin.builder().gapUser(gapUser).funder(fundingOrganisation).build();
    private final GapApiKey apiKey = GapApiKey.builder()
            .id(API_KEY_ID)
            .apiGatewayId("apiGatewayId")
            .name("Test API Key name")
            .apiKey("Test API Key")
            .isRevoked(false)
            .build();
    private final Page<GapApiKey> apiKeyPage = new PageImpl<>(Collections.singletonList(apiKey), PageRequest.of(0, 1), 1);
    private final List<String> departments = List.of("testDepartmentName", "anotherDepartment");
    private final List<GapApiKey> apiKeyList = List.of(apiKey);
    private final List<Integer> pageNumbers = List.of(1);
    private final NavBarDto navBarDto = NavBarDto.builder().link("link").name("name").build();
    @Mock
    private ApiKeyService apiKeyService;
    @Mock
    private BindingResult bindingResult;
    @Mock
    private ApiGatewayService apiGatewayService;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private Authentication authentication;
    @Mock
    private PaginationHelper paginationHelper;
    @Mock
    private UserServiceConfig userServiceConfig;

    @Mock
    private NavBarConfigProperties navBarConfigProperties;
    @InjectMocks
    private ApiKeyController controllerUnderTest;


    @Test
    void showKeys_expectedResponse_hasNotAdminRole() {

        final String apiKey = "Key";
        final List<GapApiKey> expectedApiKeys = List.of(GapApiKey.builder().apiKey(apiKey).build());

        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(createAuthenticationWithRoles(TECHNICAL_SUPPORT_ROLE));
        when(apiKeyService.getApiKeysForFundingOrganisation(grantAdmin.getFunder().getId())).thenReturn(expectedApiKeys);
        when(userServiceConfig.getLogoutUrl()).thenReturn("logoutUrl");

        ModelAndView actualResponse = controllerUnderTest.showKeys();
        List<GapApiKey> actualApiKeys = (List<GapApiKey>) actualResponse.getModel().get("apiKeys");

        assertThat(actualResponse.getViewName()).isEqualTo(ApiKeyController.ORGANISATION_API_KEYS_PAGE);
        assertThat(actualResponse.getModel()).hasSize(3);
        assertThat(actualResponse.getModel()).containsEntry("apiKeys", expectedApiKeys);
        assertThat(actualResponse.getModel()).containsEntry("departmentName", fundingOrganisation.getName());
        assertThat(actualResponse.getModel()).containsEntry("signOutUrl", "logoutUrl");
        assertThat(actualApiKeys.get(0).getApiKey()).isEqualTo(apiKey);
    }

    @Test
    void showKeys_expectedResponse_hasAdminRole() {
        final String apiKey = "Key";
        final List<GapApiKey> expectedApiKeys = List.of(GapApiKey.builder().apiKey(apiKey).build());
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(createAuthenticationWithRoles(ADMIN_ROLE));
        when(navBarConfigProperties.getAdminDashboardLink()).thenReturn("adminLink");

        when(apiKeyService.getApiKeysForFundingOrganisation(grantAdmin.getFunder().getId())).thenReturn(expectedApiKeys);
        when(userServiceConfig.getLogoutUrl()).thenReturn("logoutUrl");

        ModelAndView actualResponse = controllerUnderTest.showKeys();
        List<GapApiKey> actualApiKeys = (List<GapApiKey>) actualResponse.getModel().get("apiKeys");
        final NavBarDto actualNavBarDto = (NavBarDto) actualResponse.getModel().get("navBar");

        assertThat(actualResponse.getViewName()).isEqualTo(ApiKeyController.ORGANISATION_API_KEYS_PAGE);
        assertThat(actualResponse.getModel()).hasSize(4);
        assertThat(actualResponse.getModel()).containsEntry("apiKeys", expectedApiKeys);
        assertThat(actualResponse.getModel()).containsEntry("departmentName", fundingOrganisation.getName());
        assertThat(actualResponse.getModel()).containsEntry("signOutUrl", "logoutUrl");
        assertThat(actualApiKeys.get(0).getApiKey()).isEqualTo(apiKey);
        assertThat(actualNavBarDto.getName()).isEqualTo("Admin Dashboard");
        assertThat(actualNavBarDto.getLink()).isEqualTo("adminLink");
    }

    @Test
    void showKeys_expectedResponse_emptyList() {
        List<GapApiKey> expectedApiKeys = new ArrayList<>();
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(createAuthenticationWithRoles(TECHNICAL_SUPPORT_ROLE));
        when(apiKeyService.getApiKeysForFundingOrganisation(grantAdmin.getFunder().getId())).thenReturn(expectedApiKeys);
        when(userServiceConfig.getLogoutUrl()).thenReturn("logoutUrl");

        ModelAndView actualResponse = controllerUnderTest.showKeys();

        assertThat(actualResponse.getViewName()).isEqualTo(ApiKeyController.ORGANISATION_API_KEYS_PAGE);
        assertThat(actualResponse.getModel()).hasSize(3);
        assertThat(actualResponse.getModel()).containsEntry("apiKeys", expectedApiKeys);
        assertThat(actualResponse.getModel()).containsEntry("departmentName", fundingOrganisation.getName());
        assertThat(actualResponse.getModel()).containsEntry("signOutUrl", "logoutUrl");

    }

    @Test
    void showCreateApiKeyFormPage_ShouldShowTheCorrectViewAndAttachedObject_hasNotAdminRole() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(createAuthenticationWithRoles(TECHNICAL_SUPPORT_ROLE));
        when(userServiceConfig.getLogoutUrl()).thenReturn("logoutUrl");

        final ModelAndView methodResponse = controllerUnderTest.showCreateKeyForm();

        assertThat(methodResponse.getViewName()).isEqualTo(ApiKeyController.CREATE_API_KEY_FORM_PAGE);
        assertThat(methodResponse.getModel()).hasSize(2);
        assertThat(methodResponse.getModel().get("createApiKeyDTO")).isInstanceOf(CreateApiKeyDTO.class);
        assertThat(methodResponse.getModel()).containsEntry("signOutUrl", "logoutUrl");
    }

    @Test
    void showCreateApiKeyFormPage_ShouldShowTheCorrectViewAndAttachedObject_hasAdminRole() {
        when(userServiceConfig.getLogoutUrl()).thenReturn("logoutUrl");
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(createAuthenticationWithRoles(ADMIN_ROLE));
        when(navBarConfigProperties.getAdminDashboardLink()).thenReturn("adminLink");

        final ModelAndView methodResponse = controllerUnderTest.showCreateKeyForm();
        final NavBarDto actualNavBarDto = (NavBarDto) methodResponse.getModel().get("navBar");

        assertThat(methodResponse.getViewName()).isEqualTo(ApiKeyController.CREATE_API_KEY_FORM_PAGE);
        assertThat(methodResponse.getModel()).hasSize(3);
        assertThat(methodResponse.getModel().get("createApiKeyDTO")).isInstanceOf(CreateApiKeyDTO.class);
        assertThat(methodResponse.getModel()).containsEntry("signOutUrl", "logoutUrl");
        assertThat(actualNavBarDto.getName()).isEqualTo("Admin Dashboard");
        assertThat(actualNavBarDto.getLink()).isEqualTo("adminLink");
    }

    @Test
    void createKey_ShouldShowTheCorrectViewAndAttachedObject_hasNotAdminRole() {
        final CreateApiKeyDTO createApiKeyDTO = CreateApiKeyDTO.builder().keyName("keyName").build();

        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(createAuthenticationWithRoles(TECHNICAL_SUPPORT_ROLE));
        when(apiGatewayService.createApiKeysInAwsAndDb(createApiKeyDTO.getKeyName())).thenReturn("keyValue");
        when(userServiceConfig.getLogoutUrl()).thenReturn("logoutUrl");

        final ModelAndView methodResponse = controllerUnderTest.createKey(createApiKeyDTO, bindingResult);

        assertThat(methodResponse.getViewName()).isEqualTo(ApiKeyController.NEW_API_KEY_PAGE);
        assertThat(methodResponse.getModel()).hasSize(2);
        assertThat(methodResponse.getModel()).containsEntry("keyValue", "keyValue");
        assertThat(methodResponse.getModel()).containsEntry("signOutUrl", "logoutUrl");
    }

    @Test
    void createKey_ShouldShowTheCorrectViewAndAttachedObject_hasAdminRole() {
        final CreateApiKeyDTO createApiKeyDTO = CreateApiKeyDTO.builder().keyName("keyName").build();
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(createAuthenticationWithRoles(ADMIN_ROLE));
        when(navBarConfigProperties.getAdminDashboardLink()).thenReturn("adminLink");
        when(apiGatewayService.createApiKeysInAwsAndDb(createApiKeyDTO.getKeyName())).thenReturn("keyValue");
        when(userServiceConfig.getLogoutUrl()).thenReturn("logoutUrl");

        final ModelAndView methodResponse = controllerUnderTest.createKey(createApiKeyDTO, bindingResult);
        final NavBarDto actualNavBarDto = (NavBarDto) methodResponse.getModel().get("navBar");

        assertThat(methodResponse.getViewName()).isEqualTo(ApiKeyController.NEW_API_KEY_PAGE);
        assertThat(methodResponse.getModel()).hasSize(3);
        assertThat(methodResponse.getModel()).containsEntry("keyValue", "keyValue");
        assertThat(methodResponse.getModel()).containsEntry("signOutUrl", "logoutUrl");
        assertThat(actualNavBarDto.getName()).isEqualTo("Admin Dashboard");
        assertThat(actualNavBarDto.getLink()).isEqualTo("adminLink");
    }

    @Test
    void createKey_ShouldShowTheCorrectViewAndAttachedObject_WhenApiKeyDtoIsEmpty_hasNotAdminRole() {
        final CreateApiKeyDTO createApiKeyDTO = CreateApiKeyDTO.builder().build();
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(createAuthenticationWithRoles(TECHNICAL_SUPPORT_ROLE));
        when(bindingResult.hasErrors()).thenReturn(true);
        when(userServiceConfig.getLogoutUrl()).thenReturn("logoutUrl");

        final ModelAndView methodResponse = controllerUnderTest.createKey(createApiKeyDTO, bindingResult);

        assertThat(methodResponse.getViewName()).isEqualTo(ApiKeyController.CREATE_API_KEY_FORM_PAGE);
        assertThat(methodResponse.getModel()).hasSize(2);
        assertThat(methodResponse.getModel()).containsEntry("createApiKeyDTO", createApiKeyDTO);
        assertThat(methodResponse.getModel()).containsEntry("signOutUrl", "logoutUrl");
    }

    @Test
    void createKey_ShouldShowTheCorrectViewAndAttachedObject_WhenApiKeyDtoIsEmpty_hasAdminRole() {
        final CreateApiKeyDTO createApiKeyDTO = CreateApiKeyDTO.builder().build();
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(createAuthenticationWithRoles(ADMIN_ROLE));
        when(navBarConfigProperties.getAdminDashboardLink()).thenReturn("adminLink");
        when(bindingResult.hasErrors()).thenReturn(true);
        when(userServiceConfig.getLogoutUrl()).thenReturn("logoutUrl");

        final ModelAndView methodResponse = controllerUnderTest.createKey(createApiKeyDTO, bindingResult);
        final NavBarDto actualNavBarDto = (NavBarDto) methodResponse.getModel().get("navBar");

        assertThat(methodResponse.getViewName()).isEqualTo(ApiKeyController.CREATE_API_KEY_FORM_PAGE);
        assertThat(methodResponse.getModel()).hasSize(3);
        assertThat(methodResponse.getModel()).containsEntry("createApiKeyDTO", createApiKeyDTO);
        assertThat(methodResponse.getModel()).containsEntry("signOutUrl", "logoutUrl");
        assertThat(actualNavBarDto.getName()).isEqualTo("Admin Dashboard");
        assertThat(actualNavBarDto.getLink()).isEqualTo("adminLink");
    }

    @Test
    void createKey_ShouldShowTheCorrectViewAndAttachedObject_WhenApiKeyAlreadyExists_hasNotAdminRole() {
        final CreateApiKeyDTO createApiKeyDTO = CreateApiKeyDTO.builder().keyName("keyName").build();
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(createAuthenticationWithRoles(TECHNICAL_SUPPORT_ROLE));
        when(apiKeyService.doesApiKeyExist(createApiKeyDTO.getKeyName())).thenReturn(true);
        when(bindingResult.hasErrors()).thenReturn(true);
        when(userServiceConfig.getLogoutUrl()).thenReturn("logoutUrl");

        final ModelAndView methodResponse = controllerUnderTest.createKey(createApiKeyDTO, bindingResult);

        assertThat(methodResponse.getViewName()).isEqualTo(ApiKeyController.CREATE_API_KEY_FORM_PAGE);
        assertThat(methodResponse.getModel()).hasSize(2);
        assertThat(methodResponse.getModel()).containsEntry("createApiKeyDTO", createApiKeyDTO);
        assertThat(methodResponse.getModel()).containsEntry("signOutUrl", "logoutUrl");
    }

    @Test
    void createKey_ShouldShowTheCorrectViewAndAttachedObject_WhenApiKeyAlreadyExists_hasAdminRole() {
        final CreateApiKeyDTO createApiKeyDTO = CreateApiKeyDTO.builder().keyName("keyName").build();
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(createAuthenticationWithRoles(ADMIN_ROLE));
        when(navBarConfigProperties.getAdminDashboardLink()).thenReturn("adminLink");
        when(apiKeyService.doesApiKeyExist(createApiKeyDTO.getKeyName())).thenReturn(true);
        when(bindingResult.hasErrors()).thenReturn(true);
        when(userServiceConfig.getLogoutUrl()).thenReturn("logoutUrl");

        final ModelAndView methodResponse = controllerUnderTest.createKey(createApiKeyDTO, bindingResult);
        final NavBarDto actualNavBarDto = (NavBarDto) methodResponse.getModel().get("navBar");

        assertThat(methodResponse.getViewName()).isEqualTo(ApiKeyController.CREATE_API_KEY_FORM_PAGE);
        assertThat(methodResponse.getModel()).hasSize(3);
        assertThat(methodResponse.getModel()).containsEntry("createApiKeyDTO", createApiKeyDTO);
        assertThat(methodResponse.getModel()).containsEntry("signOutUrl", "logoutUrl");
        assertThat(actualNavBarDto.getName()).isEqualTo("Admin Dashboard");
        assertThat(actualNavBarDto.getLink()).isEqualTo("adminLink");
    }

    @Test
    void showRevokeApiKeyPage_showsCorrectView_hasNotAdminOrSuperAdminRole() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(createAuthenticationWithRoles(TECHNICAL_SUPPORT_ROLE));
        when(apiKeyService.getApiKeyById(API_KEY_ID)).thenReturn(apiKey);
        when(userServiceConfig.getLogoutUrl()).thenReturn("logoutUrl");

        final ModelAndView response = controllerUnderTest.showRevokeApiKeyConfirmation(API_KEY_ID);

        assertThat(response.getViewName()).isEqualTo(ApiKeyController.REVOKE_API_KEY_CONFIRMATION_PAGE);
        assertThat(response.getModel()).hasSize(3);
        assertThat(response.getModel()).containsEntry("apiKey", apiKey);
        assertThat(response.getModel()).containsEntry("backButtonUrl", "/api-keys");
        assertThat(response.getModel()).containsEntry("signOutUrl", "logoutUrl");
    }

    @Test
    void showRevokeApiKeyPage_showsCorrectView_hasAdminRole() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(createAuthenticationWithRoles(ADMIN_ROLE));
        when(navBarConfigProperties.getAdminDashboardLink()).thenReturn("adminLink");
        when(apiKeyService.getApiKeyById(API_KEY_ID)).thenReturn(apiKey);
        when(userServiceConfig.getLogoutUrl()).thenReturn("logoutUrl");

        final ModelAndView response = controllerUnderTest.showRevokeApiKeyConfirmation(API_KEY_ID);
        final NavBarDto actualNavBarDto = (NavBarDto) response.getModel().get("navBar");

        assertThat(response.getViewName()).isEqualTo(ApiKeyController.REVOKE_API_KEY_CONFIRMATION_PAGE);
        assertThat(response.getModel()).hasSize(4);
        assertThat(response.getModel()).containsEntry("apiKey", apiKey);
        assertThat(response.getModel()).containsEntry("backButtonUrl", "/api-keys");
        assertThat(response.getModel()).containsEntry("signOutUrl", "logoutUrl");
        assertThat(actualNavBarDto.getName()).isEqualTo("Admin Dashboard");
        assertThat(actualNavBarDto.getLink()).isEqualTo("adminLink");
    }

    @Test
    void showRevokeApiKeyPage_showsCorrectView_hasSuperAdminRole() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(createAuthenticationWithRoles(SUPER_ADMIN_ROLE));
        when(navBarConfigProperties.getSuperAdminDashboardLink()).thenReturn("superAdminLink");
        when(apiKeyService.getApiKeyById(API_KEY_ID)).thenReturn(apiKey);
        when(userServiceConfig.getLogoutUrl()).thenReturn("logoutUrl");

        final ModelAndView response = controllerUnderTest.showRevokeApiKeyConfirmation(API_KEY_ID);
        final NavBarDto actualNavBarDto = (NavBarDto) response.getModel().get("navBar");

        assertThat(response.getViewName()).isEqualTo(ApiKeyController.REVOKE_API_KEY_CONFIRMATION_PAGE);
        assertThat(response.getModel()).hasSize(4);
        assertThat(response.getModel()).containsEntry("apiKey", apiKey);
        assertThat(response.getModel()).containsEntry("backButtonUrl", "/api-keys/manage");
        assertThat(response.getModel()).containsEntry("signOutUrl", "logoutUrl");
        assertThat(actualNavBarDto.getName()).isEqualTo("Super Admin Dashboard");
        assertThat(actualNavBarDto.getLink()).isEqualTo("superAdminLink");
    }

    @Test
    void revokeApiKeyPost_returnsExpectedResponse_TechnicalSupport() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(createAuthenticationWithRoles(TECHNICAL_SUPPORT_ROLE));
        when(apiKeyService.getApiKeyById(API_KEY_ID)).thenReturn(apiKey);

        final String response = controllerUnderTest.revokeApiKey(apiKey);

        verify(apiKeyService).revokeApiKey(apiKey.getId());
        verify(apiGatewayService).deleteApiKey(apiKey);
        assertThat(response).isEqualTo("redirect:/api-keys");
    }

    @Test
    void revokeApiKeyPost_returnsExpectedResponse_SuperAdmin() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(createAuthenticationWithRoles(SUPER_ADMIN_ROLE));
        when(apiKeyService.getApiKeyById(API_KEY_ID)).thenReturn(apiKey);

        final String response = controllerUnderTest.revokeApiKey(apiKey);

        verify(apiKeyService).revokeApiKey(apiKey.getId());
        verify(apiGatewayService).deleteApiKey(apiKey);
        assertThat(response).isEqualTo("redirect:/api-keys/manage");
    }

    @Test
    void revokeApiKeyPost_rollsBackDatabaseRevoke() {
        when(apiKeyService.getApiKeyById(API_KEY_ID)).thenReturn(apiKey);
        doThrow(new RuntimeException("Test exception")).when(apiGatewayService).deleteApiKey(apiKey);

        assertThrows(RuntimeException.class, () -> controllerUnderTest.revokeApiKey(apiKey));

        verify(apiKeyService).revokeApiKey(apiKey.getId());
        assertThat(apiKey.isRevoked()).isFalse();
    }

    @Test
    void displayError_showsCorrectView() {
        final ModelAndView response = controllerUnderTest.displayError();
        assertThat(response.getViewName()).isEqualTo(ApiKeyController.ERROR_PAGE);
    }

    @Test
    void displaySuperAdminPage_showsCorrectViewWithRequestParams() {
        final List<String> selectedDepartments = List.of("testDepartmentName");
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(createAuthenticationWithRoles(SUPER_ADMIN_ROLE));
        when(navBarConfigProperties.getSuperAdminDashboardLink()).thenReturn("superAdminLink");
        when(apiKeyService.getApiKeysForSelectedFundingOrganisations(selectedDepartments))
                .thenReturn(apiKeyList);
        when(apiKeyService.getFundingOrgForAllApiKeys()).thenReturn(departments);
        when(apiKeyService.getActiveKeyCount(apiKeyList)).thenReturn(Long.valueOf(1));
        when(paginationHelper.getGapApiKeysPage(apiKeyList, 1)).thenReturn(apiKeyPage);
        when(paginationHelper.getNumberOfPages(apiKeyPage.getTotalPages())).thenReturn(List.of(1));
        when(userServiceConfig.getLogoutUrl()).thenReturn("logoutUrl");

        final ModelAndView response = controllerUnderTest.displaySuperAdminPage(selectedDepartments, of(1));
        final NavBarDto actualNavBarDto = (NavBarDto) response.getModel().get("navBar");
        assertThat(response.getViewName()).isEqualTo(ApiKeyController.SUPER_ADMIN_PAGE);
        assertThat(response.getModel()).hasSize(7);
        assertThat(response.getModel()).containsEntry("departments", departments);
        assertThat(response.getModel()).containsEntry("activeKeyCount", 1L);
        assertThat(response.getModel()).containsEntry("apiKeysPage", apiKeyPage);
        assertThat(response.getModel()).containsEntry("pageNumbers", pageNumbers);
        assertThat(response.getModel()).containsEntry("selectedDepartments", selectedDepartments);
        assertThat(response.getModel()).containsEntry("signOutUrl", "logoutUrl");
        assertThat(actualNavBarDto.getName()).isEqualTo("Super Admin Dashboard");
        assertThat(actualNavBarDto.getLink()).isEqualTo("superAdminLink");
    }

    @Test
    void displaySuperAdminPage_showsCorrectViewNoRequestParams() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(createAuthenticationWithRoles(SUPER_ADMIN_ROLE));
        when(navBarConfigProperties.getSuperAdminDashboardLink()).thenReturn("superAdminLink");
        when(apiKeyService.getApiKeysForSelectedFundingOrganisations(null)).thenReturn(apiKeyList);
        when(apiKeyService.getFundingOrgForAllApiKeys()).thenReturn(departments);
        when(apiKeyService.getActiveKeyCount(apiKeyList)).thenReturn(Long.valueOf(1));
        when(paginationHelper.getGapApiKeysPage(apiKeyList, 1)).thenReturn(apiKeyPage);
        when(paginationHelper.getNumberOfPages(apiKeyPage.getTotalPages())).thenReturn(List.of(1));
        when(userServiceConfig.getLogoutUrl()).thenReturn("logoutUrl");

        final ModelAndView response = controllerUnderTest.displaySuperAdminPage(null, empty());
        final NavBarDto actualNavBarDto = (NavBarDto) response.getModel().get("navBar");

        assertThat(response.getViewName()).isEqualTo(ApiKeyController.SUPER_ADMIN_PAGE);
        assertThat(response.getModel()).hasSize(7);
        assertThat(response.getModel()).containsEntry("departments", departments);
        assertThat(response.getModel()).containsEntry("activeKeyCount", 1L);
        assertThat(response.getModel()).containsEntry("apiKeysPage", apiKeyPage);
        assertThat(response.getModel()).containsEntry("pageNumbers", pageNumbers);
        assertThat(response.getModel()).containsEntry("selectedDepartments", List.of());
        assertThat(response.getModel()).containsEntry("signOutUrl", "logoutUrl");
        assertThat(actualNavBarDto.getName()).isEqualTo("Super Admin Dashboard");
        assertThat(actualNavBarDto.getLink()).isEqualTo("superAdminLink");
    }

    @Test
    void generateBackButtonValue_returnExpectedWhenUserIsASuperAdmin() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(createAuthenticationWithRoles(SUPER_ADMIN_ROLE));
        assertThat(controllerUnderTest.generateRedirectValue()).isEqualTo("/api-keys/manage");
    }

    @Test
    void generateBackButtonValue_returnExpectedWhenUserIsATechnicalSupport() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(createAuthenticationWithRoles(TECHNICAL_SUPPORT_ROLE));
        assertThat(controllerUnderTest.generateRedirectValue()).isEqualTo("/api-keys");
    }

    @Test
    void isSuperAdmin_returnTrueWhenUserIsASuperAdmin() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(createAuthenticationWithRoles(SUPER_ADMIN_ROLE));
        final boolean actual = controllerUnderTest.isSuperAdmin();
        assertThat(actual).isTrue();
    }

    @Test
    void isSuperAdmin_returnFalseWhenUserIsASuperAdmin() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(createAuthenticationWithRoles(TECHNICAL_SUPPORT_ROLE));
        final boolean actual = controllerUnderTest.isSuperAdmin();
        assertThat(actual).isFalse();
    }


    @Test
    void generateNavBarDto_admin() {
        final NavBarDto expectedNavBarDto = NavBarDto.builder()
                .name("Admin Dashboard")
                .link("link")
                .build();
        SecurityContextHolder.setContext(securityContext);

        when(securityContext.getAuthentication()).thenReturn(createAuthenticationWithRoles(TECHNICAL_SUPPORT_ROLE));
        when(navBarConfigProperties.getAdminDashboardLink()).thenReturn("link");

        final NavBarDto response = controllerUnderTest.generateNavBarDto();

        assertThat(response).isEqualTo(expectedNavBarDto);
    }

    @Test
    void generateNavBarDto_superAdmin() {
        final NavBarDto expectedNavBarDto = NavBarDto.builder()
                .name("Super Admin Dashboard")
                .link("link")
                .build();
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(createAuthenticationWithRoles(SUPER_ADMIN_ROLE));
        when(navBarConfigProperties.getSuperAdminDashboardLink()).thenReturn("link");

        final NavBarDto response = controllerUnderTest.generateNavBarDto();

        assertThat(response).isEqualTo(expectedNavBarDto);
    }

    @Test
    void isAdmin_returnTrueWhenUserIsAdmin() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(createAuthenticationWithRoles(ADMIN_ROLE));
        final boolean actual = controllerUnderTest.isAdmin();
        assertThat(actual).isTrue();
    }

    @Test
    void isAdmin_returnFalseWhenUserIsNotAdmin() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(createAuthenticationWithRoles(SUPER_ADMIN_ROLE));
        final boolean actual = controllerUnderTest.isAdmin();
        assertThat(actual).isFalse();
    }

    private Authentication createAuthenticationWithRoles(String role) {
        Collection<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(role));
        return new UsernamePasswordAuthenticationToken(grantAdmin, null, authorities);
    }

}
