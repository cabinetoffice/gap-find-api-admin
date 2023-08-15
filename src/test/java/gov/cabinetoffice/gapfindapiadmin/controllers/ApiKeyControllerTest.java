package gov.cabinetoffice.gapfindapiadmin.controllers;

import gov.cabinetoffice.gapfindapiadmin.dtos.CreateApiKeyDTO;
import gov.cabinetoffice.gapfindapiadmin.models.ValidationFieldError;
import gov.cabinetoffice.gapfindapiadmin.services.ApiGatewayService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.ModelAndView;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApiKeyControllerTest {
    @Mock
    private BindingResult bindingResult;

    @Mock
    private ApiGatewayService apiGatewayService;

    @InjectMocks
    private ApiKeyController controllerUnderTest;

    @Test
    void showKeysPage_ShouldShowTheCorrectView() {
        final ModelAndView methodResponse = controllerUnderTest.showKeys();
        assertThat(methodResponse.getViewName()).isEqualTo(ApiKeyController.ORGANISATION_API_KEYS_PAGE);
    }

    @Test
    void showCreateApiKeyFormPage_ShouldShowTheCorrectViewAndAttachedObject() {
        final ModelAndView methodResponse = controllerUnderTest.showCreateKeyForm();
        assertThat(methodResponse.getViewName()).isEqualTo(ApiKeyController.CREATE_API_KEY_FORM_PAGE);
        assertThat(methodResponse.getModel().get("createApiKeyDTO")).isInstanceOf(CreateApiKeyDTO.class);
    }

    @Test
    void createKey_ShouldShowTheCorrectViewAndAttachedObject(){
        final CreateApiKeyDTO createApiKeyDTO = CreateApiKeyDTO.builder().keyName("keyName").build();
        when(apiGatewayService.createApiKeys(createApiKeyDTO.getKeyName())).thenReturn("keyValue");

        final ModelAndView methodResponse = controllerUnderTest.createKey(createApiKeyDTO, bindingResult);

        assertThat(methodResponse.getViewName()).isEqualTo(ApiKeyController.NEW_API_KEY_PAGE);
        assertThat(methodResponse.getModel()).containsEntry("keyValue", "keyValue");
    }

    @Test
    void createKey_ShouldShowTheCorrectViewAndAttachedObject_WhenApiKeyDtoIsEmpty(){
        final CreateApiKeyDTO createApiKeyDTO = CreateApiKeyDTO.builder().build();
        final FieldError fieldError = new FieldError("createApiKeyDTO", "keyName", "keyName is required");
        final ValidationFieldError expectedFieldError = ValidationFieldError.builder().field("#keyName").message("keyName is required").build();

        when(bindingResult.getErrorCount()).thenReturn(1);
        when(bindingResult.getFieldError()).thenReturn(fieldError);

        final ModelAndView methodResponse = controllerUnderTest.createKey(createApiKeyDTO, bindingResult);

        assertThat(methodResponse.getViewName()).isEqualTo(ApiKeyController.CREATE_API_KEY_FORM_PAGE);
        assertThat(methodResponse.getModel().get("createApiKeyDTO")).isInstanceOf(CreateApiKeyDTO.class);
        assertThat(methodResponse.getModel()).containsEntry("error", expectedFieldError);

    }
}