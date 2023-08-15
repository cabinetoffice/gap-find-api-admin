package gov.cabinetoffice.gapfindapiadmin.controllers;

import gov.cabinetoffice.gapfindapiadmin.dtos.CreateApiKeyDTO;
import gov.cabinetoffice.gapfindapiadmin.services.ApiGatewayService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/api-keys")
@RequiredArgsConstructor
public class ApiKeyController {

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
        final ModelAndView createApiKey = new ModelAndView(CREATE_API_KEY_FORM_PAGE);
        return createApiKey.addObject("createApiKeyDTO", new CreateApiKeyDTO());
    }

    @PostMapping("/create-api-key-form")
    public ModelAndView createKey(final @Valid @ModelAttribute CreateApiKeyDTO createApiKeyDTO, final BindingResult bindingResult) {
        if (apiGatewayService.doesKeyExist(createApiKeyDTO.getKeyName())) {
            final FieldError duplicateKey = new FieldError("createApiKeyDTO",
                    "keyName",
                    createApiKeyDTO.getKeyName(),
                    true,
                    null,
                    null,
                    "An API key with this name already exists");
            bindingResult.addError(duplicateKey);
        }
        if (bindingResult.hasErrors()) {
            return new ModelAndView(CREATE_API_KEY_FORM_PAGE)
                    .addObject("createApiKeyDTO", createApiKeyDTO);
        }

        final ModelAndView newApiKey = new ModelAndView(NEW_API_KEY_PAGE);
        final String apiKeyValue = apiGatewayService.createApiKeys(createApiKeyDTO.getKeyName());
        return newApiKey.addObject("keyValue", apiKeyValue);
    }
}
