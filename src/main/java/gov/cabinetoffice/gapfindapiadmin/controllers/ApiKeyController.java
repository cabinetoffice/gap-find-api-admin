package gov.cabinetoffice.gapfindapiadmin.controllers;

import gov.cabinetoffice.gapfindapiadmin.services.ApiKeyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/api-keys")
@RequiredArgsConstructor
public class ApiKeyController {

    private final ApiKeyService apiKeyService;

    //TODO Make sure this takes the id from the request or the logged in user, rather than the hardcoded value
    @GetMapping
    public ModelAndView showKeys() {
        ModelAndView mav = new ModelAndView("organisation-api-keys");
        mav.addObject("apiKeys", apiKeyService.getApiKeysForFundingOrganisation(2));
        return mav;
    }
}
