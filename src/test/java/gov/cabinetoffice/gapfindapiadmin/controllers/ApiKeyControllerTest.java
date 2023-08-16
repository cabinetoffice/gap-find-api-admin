package gov.cabinetoffice.gapfindapiadmin.controllers;

import gov.cabinetoffice.gapfindapiadmin.dtos.CreateApiKeyDTO;
import gov.cabinetoffice.gapfindapiadmin.models.ApiKey;
import gov.cabinetoffice.gapfindapiadmin.services.ApiGatewayService;
import gov.cabinetoffice.gapfindapiadmin.services.ApiKeyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApiKeyControllerTest {

    @Mock
    private ApiKeyService apiKeyService;
    @Mock
    private BindingResult bindingResult;

    @Mock
    private ApiGatewayService apiGatewayService;

    @InjectMocks
    private ApiKeyController controllerUnderTest;

    @Test
     void showKeys_expectedResponse() {

        final String apiKey = "Key";
        final List<ApiKey> expectedApiKeys = List.of(ApiKey.builder().apiKey(apiKey).build());

        when(apiKeyService.getApiKeysForFundingOrganisation(any(Integer.class))).thenReturn(expectedApiKeys);

        ModelAndView actualResponse = controllerUnderTest.showKeys();


        List<ApiKey> actualApiKeys = (List<ApiKey>) actualResponse.getModel().get("apiKeys");

        assertThat(actualResponse.getModel().get("apiKeys")).isEqualTo(expectedApiKeys);
        assertThat(actualApiKeys.get(0).getApiKey()).isEqualTo(apiKey);
    }

    @Test
    void showKeys_expectedResponse_emptyList() {

        List<ApiKey> expectedApiKeys = new ArrayList<>();

        when(apiKeyService.getApiKeysForFundingOrganisation(any(Integer.class))).thenReturn(expectedApiKeys);

        ModelAndView actualResponse = controllerUnderTest.showKeys();
        assertThat(actualResponse.getModel().get("apiKeys")).isEqualTo(expectedApiKeys);

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
        when(apiGatewayService.createApiKeys(createApiKeyDTO.getKeyName())).thenReturn("keyValue");
        final ModelAndView methodResponse = controllerUnderTest.createKey(createApiKeyDTO, bindingResult);
        assertThat(methodResponse.getViewName()).isEqualTo(ApiKeyController.NEW_API_KEY_PAGE);
        assertThat(methodResponse.getModel()).containsEntry("keyValue", "keyValue");
    }

    @Test
    void createKey_ShouldShowTheCorrectViewAndAttachedObject_WhenApiKeyDtoIsEmpty() {
        final CreateApiKeyDTO createApiKeyDTO = CreateApiKeyDTO.builder().build();
        final FieldError fieldError = new FieldError("createApiKeyDTO", "keyName", "keyName is required");
        when(bindingResult.getFieldError()).thenReturn(fieldError);
        final ModelAndView methodResponse = controllerUnderTest.createKey(createApiKeyDTO, bindingResult);
        assertThat(methodResponse.getViewName()).isEqualTo(ApiKeyController.CREATE_API_KEY_FORM_PAGE);
        assertThat(methodResponse.getModel().get("createApiKeyDTO")).isInstanceOf(CreateApiKeyDTO.class);
        assertThat(methodResponse.getModel()).containsEntry("error", fieldError);

    }
}
