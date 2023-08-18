package gov.cabinetoffice.gapfindapiadmin.controllers;

import gov.cabinetoffice.gapfindapiadmin.dtos.CreateApiKeyDTO;
import gov.cabinetoffice.gapfindapiadmin.models.ApiKey;
import gov.cabinetoffice.gapfindapiadmin.models.FundingOrganisation;
import gov.cabinetoffice.gapfindapiadmin.models.GapUser;
import gov.cabinetoffice.gapfindapiadmin.models.GrantAdmin;
import gov.cabinetoffice.gapfindapiadmin.services.ApiGatewayService;
import gov.cabinetoffice.gapfindapiadmin.services.ApiKeyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
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
    private Principal principal;
    
    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private ApiKeyController controllerUnderTest;

    private final FundingOrganisation fundingOrganisation = FundingOrganisation.builder().id(1).build();
    private final GapUser gapUser = GapUser.builder().id(1).userSub("sub").build();

    private final GrantAdmin grantAdmin = GrantAdmin.builder().gapUser(gapUser).funder(fundingOrganisation).build();

    private static final Integer API_KEY_ID = 1;

    private final ApiKey apiKey = ApiKey.builder()
            .id(API_KEY_ID)
            .name("Test API Key name")
            .apiKey("Test API Key")
            .isRevoked(false)
            .build();

    @Test
    @WithMockUser()
    void showKeys_expectedResponse() {

        final String apiKey = "Key";
        final List<ApiKey> expectedApiKeys = List.of(ApiKey.builder().apiKey(apiKey).build());
        prepareAuthentication();

        when(apiKeyService.getApiKeysForFundingOrganisation(grantAdmin.getFunder().getId())).thenReturn(expectedApiKeys);

        ModelAndView actualResponse = controllerUnderTest.showKeys();

        List<ApiKey> actualApiKeys = (List<ApiKey>) actualResponse.getModel().get("apiKeys");

        assertThat(actualResponse.getModel()).containsEntry("apiKeys",expectedApiKeys);
        assertThat(actualApiKeys.get(0).getApiKey()).isEqualTo(apiKey);
    }

    @Test
    void showKeys_expectedResponse_emptyList() {

        List<ApiKey> expectedApiKeys = new ArrayList<>();
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
        when(apiKeyService.getApiKeyById(API_KEY_ID)).thenReturn(java.util.Optional.ofNullable(apiKey));
        final ModelAndView response = controllerUnderTest.showRevokeApiKeyConfirmation(API_KEY_ID);
        assertThat(response.getViewName()).isEqualTo(ApiKeyController.REVOKE_API_KEY_CONFIRMATION_PAGE);
    }

    @Test
    void showRevokeApiKeyPage_apiKeyNotPresent() {
        when(apiKeyService.getApiKeyById(API_KEY_ID)).thenReturn(java.util.Optional.empty());
        final ModelAndView response = controllerUnderTest.showRevokeApiKeyConfirmation(API_KEY_ID);
        assertThat(response.getViewName()).isEqualTo("redirect:/api-keys");
    }

    @Test
    void revokeApiKeyPost_returnsExpectedResponse() {
        final String response = controllerUnderTest.revokeApiKey(apiKey);
        assertThat(response).isEqualTo("redirect:/api-keys");
    }

    @Test
    void displayError_showsCorrectView() {
        final ModelAndView response = controllerUnderTest.displayError();
        assertThat(response.getViewName()).isEqualTo(ApiKeyController.ERROR_PAGE);
    }

}
