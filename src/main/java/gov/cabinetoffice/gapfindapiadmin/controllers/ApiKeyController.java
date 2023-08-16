package gov.cabinetoffice.gapfindapiadmin.controllers;

import gov.cabinetoffice.gapfindapiadmin.services.ApiKeyService;
import gov.cabinetoffice.gapfindapiadmin.dtos.CreateApiKeyDTO;
import gov.cabinetoffice.gapfindapiadmin.services.ApiGatewayService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.util.StringUtils;

@Controller
@RequestMapping("/api-keys")
@RequiredArgsConstructor
public class ApiKeyController {

    private final ApiKeyService apiKeyService;
    private final ApiGatewayService apiGatewayService;

    //TODO Make sure this takes the id from the request or the logged in user, rather than the hardcoded value
    public static final String CREATE_API_KEY_FORM_PAGE = "create-api-key-form";
    public static final String NEW_API_KEY_PAGE = "new-api-key";
    public static final String ORGANISATION_API_KEYS_PAGE = "organisation-api-keys";


    //TODO Make sure this takes the id from the request or the logged in user, rather than the hardcoded value
    @GetMapping
    public ModelAndView showKeys() {
        ModelAndView mav = new ModelAndView("organisation-api-keys");
        mav.addObject("apiKeys", apiKeyService.getApiKeysForFundingOrganisation(2));
        return mav;
    }

    @GetMapping("/create-api-key-form")
    public ModelAndView showCreateKeyForm() {
        ModelAndView createApiKey = new ModelAndView(CREATE_API_KEY_FORM_PAGE);
        createApiKey.addObject("createApiKeyDTO", new CreateApiKeyDTO());
        return createApiKey;
    }

    @PostMapping("/create-api-key")
    public ModelAndView createKey(final @Valid @ModelAttribute CreateApiKeyDTO createApiKeyDTO, final BindingResult bindingResult) {
        if (StringUtils.isEmptyOrWhitespace(createApiKeyDTO.getKeyName())) {
            return new ModelAndView(CREATE_API_KEY_FORM_PAGE).addObject("createApiKeyDTO", createApiKeyDTO).addObject("error", bindingResult.getFieldError());
        }
        ModelAndView newApiKey = new ModelAndView(NEW_API_KEY_PAGE);
        String apiKeyValue = apiGatewayService.createApiKeys(createApiKeyDTO.getKeyName());
        newApiKey.addObject("keyValue", apiKeyValue);
        return newApiKey;
    }
}
