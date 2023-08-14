package gov.cabinetoffice.gapfindapiadmin.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.util.StringUtils;

@Controller
@RequestMapping("/api-keys")
@RequiredArgsConstructor
public class ApiKeyController {

    @GetMapping("/organisation-api-keys")
    public static final String CREATE_API_KEY_FORM_PAGE = "create-api-key-form";
    public static final String NEW_API_KEY_PAGE = "new-api-key";
    public static final String ORGANISATION_API_KEYS_PAGE = "organisation-api-keys";
    private final ApiGatewayService apiGatewayService;

    @GetMapping
    public ModelAndView showKeys() {
        return new ModelAndView(ORGANISATION_API_KEYS_PAGE);
    }

    @GetMapping("/create-api-key-form")
    public ModelAndView showCreateKeyForm() {
        ModelAndView createApiKey = new ModelAndView(CREATE_API_KEY_FORM_PAGE);
        createApiKey.addObject("createApiKeyDTO", new CreateApiKeyDTO()).addObject("backButtonValue", ORGANISATION_API_KEYS_PAGE);
        return createApiKey;
    }

    @PostMapping("/create-api-key")
    public ModelAndView createKey(final @Valid @ModelAttribute CreateApiKeyDTO createApiKeyDTO, final BindingResult bindingResult) {
        if (StringUtils.isEmptyOrWhitespace(createApiKeyDTO.getKeyName())) {
            return new ModelAndView(CREATE_API_KEY_FORM_PAGE).addObject("createApiKeyDTO", createApiKeyDTO).addObject("error", bindingResult.getFieldError());
        }
        ModelAndView newApiKey = new ModelAndView(NEW_API_KEY_PAGE);
        String apiKeyValue = apiGatewayService.createApiKeys(createApiKeyDTO.getKeyName());
        newApiKey.addObject("keyValue", apiKeyValue).addObject("backButtonValue", ORGANISATION_API_KEYS_PAGE);
        return newApiKey;
    }
}
