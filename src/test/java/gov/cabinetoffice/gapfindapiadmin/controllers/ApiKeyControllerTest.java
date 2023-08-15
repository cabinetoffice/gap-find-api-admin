package gov.cabinetoffice.gapfindapiadmin.controllers;

import gov.cabinetoffice.gapfindapiadmin.services.ApiGatewayService;
import gov.cabinetoffice.gapfindapiadmin.services.ApiKeyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.ModelAndView;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(MockitoExtension.class)
public class ApiKeyControllerTest {

    @Mock
    private ApiKeyService apiKeyService;

    @Mock
    private ApiGatewayService apiGatewayService;

    @InjectMocks
    private ApiKeyController apiKeyController;

    private static final Integer API_KEY_ID = 1;

    public static final String ORGANISATION_API_KEYS_PAGE = "organisation-api-keys";

    @Test
    void showRevokeApiKeyConfirmationPage_showsCorrectView() {
        final ModelAndView response = apiKeyController.showRevokeApiKeyConfirmation(API_KEY_ID);
        assertThat(response.getViewName()).isEqualTo("revoke-api-key-confirmation");
    }

    @Test
    void removeApiKey_returnsExpectedResponse() {
        final ModelAndView response = apiKeyController.removeApiKey(API_KEY_ID);
        assertThat(response.getViewName()).isEqualTo(ORGANISATION_API_KEYS_PAGE);
    }
}
