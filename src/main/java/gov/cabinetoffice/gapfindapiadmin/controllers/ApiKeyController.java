package gov.cabinetoffice.gapfindapiadmin.controllers;

import gov.cabinetoffice.gapfindapiadmin.dtos.CreateApiKeyDTO;
import gov.cabinetoffice.gapfindapiadmin.models.GapApiKey;
import gov.cabinetoffice.gapfindapiadmin.models.GrantAdmin;
import gov.cabinetoffice.gapfindapiadmin.services.ApiGatewayService;
import gov.cabinetoffice.gapfindapiadmin.services.ApiKeyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/api-keys")
@RequiredArgsConstructor
public class ApiKeyController {
    public static final String CREATE_API_KEY_FORM_PAGE = "create-api-key-form";
    public static final String NEW_API_KEY_PAGE = "new-api-key";
    public static final String ORGANISATION_API_KEYS_PAGE = "organisation-api-keys";
    public static final String REVOKE_API_KEY_CONFIRMATION_PAGE = "revoke-api-key-confirmation";
    public static final String ERROR_PAGE = "error-page";
    public static final String SUPER_ADMIN_PAGE = "super-admin-api-keys";
    public static final String SUPER_ADMIN_ROLE = "SUPER_ADMIN";
    public static final String TECHNICAL_SUPPORT_ROLE = "TECHNICAL_SUPPORT";

    private final ApiKeyService apiKeyService;
    private final ApiGatewayService apiGatewayService;

    @GetMapping
    @PreAuthorize("hasAuthority('TECHNICAL_SUPPORT')")
    public ModelAndView showKeys() {
        final GrantAdmin grantAdmin = (GrantAdmin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        final String departmentName = grantAdmin.getFunder().getName();

        return new ModelAndView(ORGANISATION_API_KEYS_PAGE)
                .addObject("apiKeys", apiKeyService.getApiKeysForFundingOrganisation(grantAdmin.getFunder().getId()))
                .addObject("departmentName", departmentName);
    }

    @GetMapping("/create")
    @PreAuthorize("hasAuthority('TECHNICAL_SUPPORT')")
    public ModelAndView showCreateKeyForm() {
        final ModelAndView createApiKey = new ModelAndView(CREATE_API_KEY_FORM_PAGE);
        return createApiKey.addObject("createApiKeyDTO", new CreateApiKeyDTO());
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('TECHNICAL_SUPPORT')")
    public ModelAndView createKey(final @Valid @ModelAttribute CreateApiKeyDTO createApiKeyDTO, final BindingResult bindingResult) {
        if (apiKeyService.doesApiKeyExist(createApiKeyDTO.getKeyName())) {
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

        return new ModelAndView(NEW_API_KEY_PAGE)
                .addObject("keyValue", apiGatewayService.createApiKeysInAwsAndDb(createApiKeyDTO.getKeyName()));
    }

    @GetMapping("/revoke/{apiKeyId}")
    @PreAuthorize("hasAuthority('TECHNICAL_SUPPORT') || hasAuthority('SUPER_ADMIN')")
    public ModelAndView showRevokeApiKeyConfirmation(@PathVariable int apiKeyId) {
        final GapApiKey apiKey = apiKeyService.getApiKeyById(apiKeyId);
        return new ModelAndView(REVOKE_API_KEY_CONFIRMATION_PAGE)
                .addObject("apiKey", apiKey)
                .addObject("backButtonUrl", apiKeyService.generateBackButtonValue());
    }

    @PostMapping("/revoke")
    @PreAuthorize("hasAuthority('TECHNICAL_SUPPORT') || hasAuthority('SUPER_ADMIN')")
    public String revokeApiKey(@ModelAttribute GapApiKey apiKey) {
        // TODO: see if we can do this in one transaction
        apiGatewayService.deleteApiKey(apiKeyService.getApiKeyById(apiKey.getId()));
        apiKeyService.revokeApiKey(apiKey.getId());
        return "redirect:" + apiKeyService.generateBackButtonValue();
    }

    @GetMapping("/error")
    public ModelAndView displayError() {
        return new ModelAndView(ERROR_PAGE).addObject("backButtonUrl", apiKeyService.generateBackButtonValue());
    }

    @GetMapping("/manage")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ModelAndView displaySuperAdminPage(@RequestParam(value = "selectedDepartments", required = false) List<String> selectedDepartment,
                                 @RequestParam(value = "page", required = false) Optional<Integer> page) {
        final List<GapApiKey> allApiKeys = apiKeyService.getApiKeysForSelectedFundingOrganisations(selectedDepartment);
        final List<String> allFundingOrganisations = apiKeyService.getFundingOrgForAllApiKeys();
        final Long activeKeyCount = apiKeyService.getActiveKeyCount(allApiKeys);

        final int currentPage = page.orElse(1);
        final int pageSize = 10;
        final Page<GapApiKey> apiKeysPage =  apiKeyService.findPaginated(PageRequest.of(currentPage - 1, pageSize), allApiKeys);
        final int totalPages = apiKeysPage.getTotalPages();
        final List<Integer> pageNumbers = totalPages > 0 ?
                IntStream.rangeClosed(1, totalPages)
                        .boxed()
                        .collect(Collectors.toList()) : new ArrayList<>();

        return new ModelAndView(SUPER_ADMIN_PAGE)
                .addObject("departments", allFundingOrganisations)
                .addObject("activeKeyCount", activeKeyCount)
                .addObject("apiKeysPage", apiKeysPage)
                .addObject("pageNumbers",pageNumbers)
                .addObject("selectedDepartments", selectedDepartment==null? Collections.EMPTY_LIST : selectedDepartment);
    }
}
