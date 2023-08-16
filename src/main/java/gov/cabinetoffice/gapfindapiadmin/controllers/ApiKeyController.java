package gov.cabinetoffice.gapfindapiadmin.controllers;

import gov.cabinetoffice.gapfindapiadmin.service.ApiKeyService;
import gov.cabinetoffice.gapfindapiadmin.services.ApiGatewayService;
import gov.cabinetoffice.gapfindapiadmin.services.ApiKeyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/api-keys")
@RequiredArgsConstructor
public class ApiKeyController {

    //TODO Make sure this takes the id from the request or the logged in user, rather than the hardcoded value
    public static final String REVOKE_API_KEY_CONFIRMATION_PAGE = "revoke-api-key-confirmation";

    public static final String ORGANISATION_API_KEYS_PAGE = "organisation-api-keys";

    private final ApiKeyService apiKeyService;

    private final ApiGatewayService apiGatewayService;

    @GetMapping
    public ModelAndView showKeys() {
        ModelAndView mav = new ModelAndView("organisation-api-keys");
        mav.addObject("apiKeys", apiKeyService.getApiKeysForFundingOrganisation(2));
        return mav;
    }

    @GetMapping("/revoke-api-key-confirmation")
    public ModelAndView showRevokeApiKeyConfirmation(@RequestParam Integer apiKeyId) {
        ModelAndView modelAndView = new ModelAndView(REVOKE_API_KEY_CONFIRMATION_PAGE);
        modelAndView.addObject("apiKeyId", apiKeyId);
        return modelAndView;
    }

    @PostMapping("/remove-api-key")
    public ModelAndView removeApiKey(@ModelAttribute Integer apiKeyId) {
        apiKeyService.revokeApiKey(apiKeyId);
        apiGatewayService.deleteApiKey(apiKeyService.getApiKeyName(apiKeyId));
        return new ModelAndView(ORGANISATION_API_KEYS_PAGE);
    }
}
