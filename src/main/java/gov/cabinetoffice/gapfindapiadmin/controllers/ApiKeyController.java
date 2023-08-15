package gov.cabinetoffice.gapfindapiadmin.controllers;

import gov.cabinetoffice.gapfindapiadmin.dtos.CreateApiKeyDTO;
import gov.cabinetoffice.gapfindapiadmin.models.FieldError;
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
        ModelAndView createApiKey = new ModelAndView(CREATE_API_KEY_FORM_PAGE);
        createApiKey.addObject("createApiKeyDTO", new CreateApiKeyDTO());
        return createApiKey;
    }

    @PostMapping("/create-api-key")
    public ModelAndView createKey(final @Valid @ModelAttribute CreateApiKeyDTO createApiKeyDTO, final BindingResult bindingResult) {
        if (bindingResult.getErrorCount() > 0 && bindingResult.getFieldError() != null) {
            FieldError fieldError = FieldError.builder().field("#" + bindingResult.getFieldError().getField()).message(bindingResult.getFieldError().getDefaultMessage()).build();
            return new ModelAndView(CREATE_API_KEY_FORM_PAGE).addObject("createApiKeyDTO", createApiKeyDTO).addObject("error", fieldError);
        }
        ModelAndView newApiKey = new ModelAndView(NEW_API_KEY_PAGE);
        String apiKeyValue = apiGatewayService.createApiKeys(createApiKeyDTO.getKeyName());
        newApiKey.addObject("keyValue", apiKeyValue);
        return newApiKey;
    }
}
