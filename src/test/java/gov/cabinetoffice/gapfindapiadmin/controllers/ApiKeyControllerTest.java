package gov.cabinetoffice.gapfindapiadmin.controllers;

import gov.cabinetoffice.gapfindapiadmin.dtos.CreateApiKeyDTO;
import gov.cabinetoffice.gapfindapiadmin.services.ApiGatewayService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
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
    void showCreateApiKeyFormPage_ShouldShowTheCorrectViewandAttachedObject() {
        final ModelAndView methodResponse = controllerUnderTest.showCreateKeyForm();
        final  CreateApiKeyDTO createApiKeyDTO = CreateApiKeyDTO.builder().build();
        assertThat(methodResponse.getViewName()).isEqualTo(ApiKeyController.CREATE_API_KEY_FORM_PAGE);
        assertThat(methodResponse.getModel().get(createApiKeyDTO)).isInstanceOf(CreateApiKeyDTO.class);
        assertThat(methodResponse.getModel()).containsEntry("backButtonValue", ApiKeyController.ORGANISATION_API_KEYS_PAGE);
    }
}