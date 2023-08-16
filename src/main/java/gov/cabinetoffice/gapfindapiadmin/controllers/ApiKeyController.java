package gov.cabinetoffice.gapfindapiadmin.controllers;

import gov.cabinetoffice.gapfindapiadmin.models.ApiKey;
import gov.cabinetoffice.gapfindapiadmin.services.ApiGatewayService;
import gov.cabinetoffice.gapfindapiadmin.services.ApiKeyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
@RequestMapping("/api-keys")
@RequiredArgsConstructor
public class ApiKeyController {

    private final ApiKeyService apiKeyService;

    private final ApiGatewayService apiGatewayService;

    public static final String REVOKE_API_KEY_CONFIRMATION_PAGE = "revoke-api-key-confirmation";

    public static final String ORGANISATION_API_KEYS_PAGE = "organisation-api-keys";

    //TODO Make sure this takes the id from the request or the logged in user, rather than the hardcoded value
    @GetMapping
    public ModelAndView showKeys() {
        ModelAndView mav = new ModelAndView(ORGANISATION_API_KEYS_PAGE);
        mav.addObject("apiKeys", apiKeyService.getApiKeysForFundingOrganisation(1));
        return mav;
    }

    @GetMapping("/revoke-api-key-confirmation/{apiKeyId}")
    public ModelAndView showRevokeApiKeyConfirmation(@PathVariable int apiKeyId) {
        Optional<ApiKey> apiKey = apiKeyService.getApiKeyById(apiKeyId);

        ModelAndView modelAndView = new ModelAndView(REVOKE_API_KEY_CONFIRMATION_PAGE);
        modelAndView.addObject("apiKey", apiKey);

        return modelAndView;
    }

    // TODO change mapping to match getMapping
    @PostMapping("/revoke-api-key")
    public ModelAndView revokeApiKey(@ModelAttribute ApiKey apiKey) {
        apiKeyService.revokeApiKey(apiKey.getId());
        apiGatewayService.deleteApiKey(apiKey.getName());
        return new ModelAndView(ORGANISATION_API_KEYS_PAGE);
    }
}
