package gov.cabinetoffice.gapfindapiadmin.controllers;

import gov.cabinetoffice.gapfindapiadmin.dtos.CreateApiKeyDTO;
import gov.cabinetoffice.gapfindapiadmin.models.FundingOrganisation;
import gov.cabinetoffice.gapfindapiadmin.models.GapApiKey;
import gov.cabinetoffice.gapfindapiadmin.models.GapUser;
import gov.cabinetoffice.gapfindapiadmin.models.GrantAdmin;
import gov.cabinetoffice.gapfindapiadmin.services.ApiGatewayService;
import gov.cabinetoffice.gapfindapiadmin.services.ApiKeyService;
import gov.cabinetoffice.gapfindapiadmin.services.FundingOrganisationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApiKeyControllerTest {

    @Mock
    private ApiKeyService apiKeyService;
    
    @Mock
    private BindingResult bindingResult;

    @Mock
    private ApiGatewayService apiGatewayService;

    @Mock
    private FundingOrganisationService fundingOrganisationService;
    
    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private ApiKeyController controllerUnderTest;

    private final FundingOrganisation fundingOrganisation = FundingOrganisation.builder().id(1).name("testDepartmentName").build();
    private final GapUser gapUser = GapUser.builder().id(1).userSub("sub").build();

    private final GrantAdmin grantAdmin = GrantAdmin.builder().gapUser(gapUser).funder(fundingOrganisation).build();

    private static final Integer API_KEY_ID = 1;

    private final GapApiKey apiKey = GapApiKey.builder()
            .id(API_KEY_ID)
            .apiGatewayId("apiGatewayId")
            .name("Test API Key name")
            .apiKey("Test API Key")
            .isRevoked(false)
            .build();

    private final Page<GapApiKey> apiKeyPage = new PageImpl<>(Collections.singletonList(apiKey), PageRequest.of(0, 1), 1);
    private final List<String> departments = List.of("testDepartmentName","anotherDepartment");
    private final List<GapApiKey> apiKeyList = List.of(apiKey);
    private final List<Integer> pageNumbers = List.of(1);

    @Test
    @WithMockUser()
    void showKeys_expectedResponse() {

        final String apiKey = "Key";
        final List<GapApiKey> expectedApiKeys = List.of(GapApiKey.builder().apiKey(apiKey).build());
        prepareAuthentication();

        when(apiKeyService.getApiKeysForFundingOrganisation(grantAdmin.getFunder().getId())).thenReturn(expectedApiKeys);

        ModelAndView actualResponse = controllerUnderTest.showKeys();

        List<GapApiKey> actualApiKeys = (List<GapApiKey>) actualResponse.getModel().get("apiKeys");

        assertThat(actualResponse.getModel()).containsEntry("apiKeys",expectedApiKeys);
        assertThat(actualResponse.getModel()).containsEntry("departmentName",fundingOrganisation.getName());
        assertThat(actualApiKeys.get(0).getApiKey()).isEqualTo(apiKey);
    }

    @Test
    void showKeys_expectedResponse_emptyList() {

        List<GapApiKey> expectedApiKeys = new ArrayList<>();
        prepareAuthentication();
        when(apiKeyService.getApiKeysForFundingOrganisation(grantAdmin.getFunder().getId())).thenReturn(expectedApiKeys);

        ModelAndView actualResponse = controllerUnderTest.showKeys();
        assertThat(actualResponse.getModel()).containsEntry("apiKeys",expectedApiKeys);

    }

    @Test
    void showCreateApiKeyFormPage_ShouldShowTheCorrectViewAndAttachedObject() {
        final ModelAndView methodResponse = controllerUnderTest.showCreateKeyForm();
        assertThat(methodResponse.getViewName()).isEqualTo(ApiKeyController.CREATE_API_KEY_FORM_PAGE);
        assertThat(methodResponse.getModel().get("createApiKeyDTO")).isInstanceOf(CreateApiKeyDTO.class);
    }

    @Test
    void createKey_ShouldShowTheCorrectViewAndAttachedObject() {
        final CreateApiKeyDTO createApiKeyDTO = CreateApiKeyDTO.builder().keyName("keyName").build();
        when(apiGatewayService.createApiKeysInAwsAndDb(createApiKeyDTO.getKeyName())).thenReturn("keyValue");

        final ModelAndView methodResponse = controllerUnderTest.createKey(createApiKeyDTO, bindingResult);

        assertThat(methodResponse.getViewName()).isEqualTo(ApiKeyController.NEW_API_KEY_PAGE);
        assertThat(methodResponse.getModel()).containsEntry("keyValue", "keyValue");
    }

    @Test
    void createKey_ShouldShowTheCorrectViewAndAttachedObject_WhenApiKeyDtoIsEmpty() {
        final CreateApiKeyDTO createApiKeyDTO = CreateApiKeyDTO.builder().build();
        when(bindingResult.hasErrors()).thenReturn(true);

        final ModelAndView methodResponse = controllerUnderTest.createKey(createApiKeyDTO, bindingResult);

        assertThat(methodResponse.getViewName()).isEqualTo(ApiKeyController.CREATE_API_KEY_FORM_PAGE);
        assertThat(methodResponse.getModel().get("createApiKeyDTO")).isInstanceOf(CreateApiKeyDTO.class);
    }

    @Test
    void createKey_ShouldShowTheCorrectViewAndAttachedObject_WhenApiKeyAlreadyExists() {
        final CreateApiKeyDTO createApiKeyDTO = CreateApiKeyDTO.builder().keyName("keyName").build();
        when(apiKeyService.doesApiKeyExist(createApiKeyDTO.getKeyName())).thenReturn(true);
        when(bindingResult.hasErrors()).thenReturn(true);
        final ModelAndView methodResponse = controllerUnderTest.createKey(createApiKeyDTO, bindingResult);

        assertThat(methodResponse.getViewName()).isEqualTo(ApiKeyController.CREATE_API_KEY_FORM_PAGE);
        assertThat(methodResponse.getModel().get("createApiKeyDTO")).isInstanceOf(CreateApiKeyDTO.class);
    }

    private void prepareAuthentication() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(grantAdmin);
    }

    @Test
    void showRevokeApiKeyPage_showsCorrectView() {
        when(apiKeyService.getApiKeyById(API_KEY_ID)).thenReturn(apiKey);

        final ModelAndView response = controllerUnderTest.showRevokeApiKeyConfirmation(API_KEY_ID);

        assertThat(response.getViewName()).isEqualTo(ApiKeyController.REVOKE_API_KEY_CONFIRMATION_PAGE);
        assertThat(response.getModel()).containsEntry("apiKey", apiKey);
    }

    @Test
    void revokeApiKeyPost_returnsExpectedResponse() {
        when(apiKeyService.getApiKeyById(API_KEY_ID)).thenReturn(apiKey);

        final String response = controllerUnderTest.revokeApiKey(apiKey);

        verify(apiKeyService).revokeApiKey(apiKey.getId());
        verify(apiGatewayService).deleteApiKey(apiKey);
        assertThat(response).isEqualTo("redirect:/api-keys");
    }

    @Test
    void displayError_showsCorrectView() {
        final ModelAndView response = controllerUnderTest.displayError();
        assertThat(response.getViewName()).isEqualTo(ApiKeyController.ERROR_PAGE);
    }

    @Test
    void superAdminShowKeys_showsCorrectViewWithRequestParams() {
        final List<String> selectedDepartments = List.of("testDepartmentName");

        when(apiKeyService.getApiKeysForSelectedFundingOrganisations(selectedDepartments))
                .thenReturn(apiKeyList);
        when(fundingOrganisationService.getAllFundingOrganisationNames()).thenReturn(departments);
        when(apiKeyService.getActiveKeyCount(apiKeyList)).thenReturn(Long.valueOf(1));
        when(apiKeyService.findPaginated(PageRequest.of(0, 1),Collections.singletonList(apiKey))).thenReturn(apiKeyPage);

        final ModelAndView response = controllerUnderTest.showKeys(selectedDepartments, java.util.Optional.of(1));

        assertThat(response.getViewName()).isEqualTo(ApiKeyController.SUPER_ADMIN_API_KEYS_PAGE);
        assertThat(response.getModel()).containsEntry("departments", departments);
        assertThat(response.getModel()).containsEntry("activeKeyCount", 1L);
        assertThat(response.getModel()).containsEntry("apiKeysPage", apiKeyPage);
        assertThat(response.getModel()).containsEntry("pageNumbers", pageNumbers);
        assertThat(response.getModel()).containsEntry("selectedDepartments", selectedDepartments);
    }

    @Test
    void superAdminShowKeys_showsCorrectViewNoRequestParams() {
        when(apiKeyService.getApiKeysForSelectedFundingOrganisations(null))
                .thenReturn(apiKeyList);
        when(fundingOrganisationService.getAllFundingOrganisationNames()).thenReturn(departments);
        when(apiKeyService.getActiveKeyCount(apiKeyList)).thenReturn(Long.valueOf(1));
        when(apiKeyService.findPaginated(PageRequest.of(0, 1),Collections.singletonList(apiKey))).thenReturn(apiKeyPage);

        final ModelAndView response = controllerUnderTest.showKeys(null, java.util.Optional.empty());

        assertThat(response.getViewName()).isEqualTo(ApiKeyController.SUPER_ADMIN_API_KEYS_PAGE);
        assertThat(response.getModel()).containsEntry("departments", departments);
        assertThat(response.getModel()).containsEntry("activeKeyCount", 1L);
        assertThat(response.getModel()).containsEntry("apiKeysPage", apiKeyPage);
        assertThat(response.getModel()).containsEntry("pageNumbers", pageNumbers);
        assertThat(response.getModel()).containsEntry("selectedDepartments", Collections.EMPTY_LIST);
    }

}
