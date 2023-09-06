package gov.cabinetoffice.gapfindapiadmin.controllers;

import gov.cabinetoffice.gapfindapiadmin.dtos.CreateApiKeyDTO;
import gov.cabinetoffice.gapfindapiadmin.helpers.PaginationHelper;
import gov.cabinetoffice.gapfindapiadmin.models.GapApiKey;
import gov.cabinetoffice.gapfindapiadmin.models.GrantAdmin;
import gov.cabinetoffice.gapfindapiadmin.services.ApiGatewayService;
import gov.cabinetoffice.gapfindapiadmin.services.ApiKeyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

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

    private final ApiKeyService apiKeyService;
    private final ApiGatewayService apiGatewayService;
    private final PaginationHelper paginationHelper;

    @GetMapping
    @PreAuthorize("hasAuthority('TECHNICAL_SUPPORT')")
    public ModelAndView showKeys() {
        final GrantAdmin grantAdmin = (GrantAdmin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        final String departmentName = grantAdmin.getFunder().getName();
        ModelAndView model = new ModelAndView(ORGANISATION_API_KEYS_PAGE)
                .addObject("apiKeys", apiKeyService.getApiKeysForFundingOrganisation(grantAdmin.getFunder().getId()))
                .addObject("departmentName", departmentName);

        return apiKeyService.isAdmin() ? model.addObject("navBar", apiKeyService.generateNavBarDto()) : model;
    }

    @GetMapping("/create")
    @PreAuthorize("hasAuthority('TECHNICAL_SUPPORT')")
    public ModelAndView showCreateKeyForm() {
        final ModelAndView createApiKey = new ModelAndView(CREATE_API_KEY_FORM_PAGE).addObject("createApiKeyDTO", new CreateApiKeyDTO());

        return apiKeyService.isAdmin() ? createApiKey.addObject("navBar", apiKeyService.generateNavBarDto()) : createApiKey;
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
            ModelAndView model = new ModelAndView(CREATE_API_KEY_FORM_PAGE)
                    .addObject("createApiKeyDTO", createApiKeyDTO);

            return apiKeyService.isAdmin() ? model.addObject("navBar", apiKeyService.generateNavBarDto()) : model;
        }
        ModelAndView model = new ModelAndView(NEW_API_KEY_PAGE)
                .addObject("keyValue", apiGatewayService.createApiKeysInAwsAndDb(createApiKeyDTO.getKeyName()));

        return apiKeyService.isAdmin() ? model.addObject("navBar", apiKeyService.generateNavBarDto()) : model;
    }

    @GetMapping("/revoke/{apiKeyId}")
    @PreAuthorize("hasAuthority('TECHNICAL_SUPPORT') || hasAuthority('SUPER_ADMIN')")
    public ModelAndView showRevokeApiKeyConfirmation(@PathVariable int apiKeyId) {
        final GapApiKey apiKey = apiKeyService.getApiKeyById(apiKeyId);
        ModelAndView model = new ModelAndView(REVOKE_API_KEY_CONFIRMATION_PAGE)
                .addObject("apiKey", apiKey)
                .addObject("backButtonUrl", apiKeyService.generateBackButtonValue());

        return apiKeyService.isAdmin() || apiKeyService.isSuperAdmin() ? model.addObject("navBar", apiKeyService.generateNavBarDto()) : model;
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
        ModelAndView model = new ModelAndView(ERROR_PAGE)
                .addObject("backButtonUrl", apiKeyService.generateBackButtonValue());

        return apiKeyService.isAdmin() ? model.addObject("navBar", apiKeyService.generateNavBarDto()) : model;
    }

    @GetMapping("/manage")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ModelAndView displaySuperAdminPage(@RequestParam(value = "selectedDepartments", required = false) List<String> selectedDepartment,
                                              @RequestParam(value = "page", required = false) Optional<Integer> page) {
        final List<GapApiKey> allApiKeys = apiKeyService.getApiKeysForSelectedFundingOrganisations(selectedDepartment);
        final int currentPage = page.orElse(1);
        final Page<GapApiKey> apiKeysPage = paginationHelper.getGapApiKeysPage(allApiKeys, currentPage);

        return new ModelAndView(SUPER_ADMIN_PAGE)
                .addObject("departments", apiKeyService.getFundingOrgForAllApiKeys())
                .addObject("activeKeyCount", apiKeyService.getActiveKeyCount(allApiKeys))
                .addObject("apiKeysPage", apiKeysPage)
                .addObject("pageNumbers", paginationHelper.getNumberOfPages(apiKeysPage.getTotalPages()))
                .addObject("selectedDepartments", selectedDepartment == null ? List.of() : selectedDepartment)
                .addObject("navBar", apiKeyService.generateNavBarDto());
    }
}
