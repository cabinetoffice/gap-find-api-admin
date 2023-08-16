package gov.cabinetoffice.gapfindapiadmin.controllers;

import gov.cabinetoffice.gapfindapiadmin.models.ApiKey;
import gov.cabinetoffice.gapfindapiadmin.services.ApiKeyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ApiKeyControllerTest {

    @Mock
    private ApiKeyService apiKeyService;

    @Mock
    private ApiGatewayService apiGatewayService;

    @InjectMocks
    private ApiKeyController controllerUnderTest;

    private static final Integer API_KEY_ID = 1;

    public static final String ORGANISATION_API_KEYS_PAGE = "organisation-api-keys";

    @Test
    public void showKeys_expectedResponse(){

        final String apiKey = "Key";
        final List<ApiKey> expectedApiKeys = List.of(ApiKey.builder().apiKey(apiKey).build());

        when(apiKeyService.getApiKeysForFundingOrganisation(any(Integer.class))).thenReturn(expectedApiKeys);

        ModelAndView actualResponse = controllerUnderTest.showKeys();


        List<ApiKey> actualApiKeys = (List<ApiKey>) actualResponse.getModel().get("apiKeys");

        assertThat(actualResponse.getModel().get("apiKeys")).isEqualTo(expectedApiKeys);
        assertThat(actualApiKeys.get(0).getApiKey()).isEqualTo(apiKey);
    }

    @Test
    public void showKeys_expectedResponse_emptyList(){

        List<ApiKey> expectedApiKeys = new ArrayList<>();

        when(apiKeyService.getApiKeysForFundingOrganisation(any(Integer.class))).thenReturn(expectedApiKeys);

        ModelAndView actualResponse = controllerUnderTest.showKeys();
        assertThat(actualResponse.getModel().get("apiKeys")).isEqualTo(expectedApiKeys);

    }

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
