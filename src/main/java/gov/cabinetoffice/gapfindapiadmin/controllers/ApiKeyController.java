package gov.cabinetoffice.gapfindapiadmin.controllers;

import gov.cabinetoffice.gapfindapiadmin.models.ApiKey;
import gov.cabinetoffice.gapfindapiadmin.repositories.ApiKeyRepository;
import gov.cabinetoffice.gapfindapiadmin.services.ApiKeyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/api-keys")
@RequiredArgsConstructor
public class ApiKeyController {

    public static final String REVOKE_API_KEY_CONFIRMATION_PAGE = "revoke-api-key-confirmation";

    public static final String ORGANISATION_API_KEYS_PAGE = "organisation-api-keys";

    private ApiKeyService apiKeyService;

    @GetMapping
    public ModelAndView showKeys() {
        return new ModelAndView("organisation-api-keys");
    }

    @GetMapping("/revoke-api-key-confirmation")
    public ModelAndView showRevokeApiKeyConfirmation(@RequestParam Integer apiKeyId) {
        ModelAndView modelAndView = new ModelAndView(REVOKE_API_KEY_CONFIRMATION_PAGE);
        modelAndView.addObject("apiKeyId", apiKeyId);
        return modelAndView;
    }

    @PostMapping("/remove-api-key")
    public ModelAndView removeApiKey(@ModelAttribute Integer apiKeyId) {
        //TODO: set revoked to true based on how it is saved in database
        apiKeyService.revokeApiKey(apiKeyId);
        return new ModelAndView(ORGANISATION_API_KEYS_PAGE);
    }
}
