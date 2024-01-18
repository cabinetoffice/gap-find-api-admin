package gov.cabinetoffice.gapfindapiadmin.controllers;

import gov.cabinetoffice.gapfindapiadmin.config.NavBarConfigProperties;
import gov.cabinetoffice.gapfindapiadmin.config.SwaggerConfigProperties;
import gov.cabinetoffice.gapfindapiadmin.config.UserServiceConfig;
import gov.cabinetoffice.gapfindapiadmin.dtos.CreateApiKeyDTO;
import gov.cabinetoffice.gapfindapiadmin.dtos.NavBarDto;
import gov.cabinetoffice.gapfindapiadmin.helpers.PaginationHelper;
import gov.cabinetoffice.gapfindapiadmin.models.GapApiKey;
import gov.cabinetoffice.gapfindapiadmin.models.GrantAdmin;
import gov.cabinetoffice.gapfindapiadmin.services.ApiGatewayService;
import gov.cabinetoffice.gapfindapiadmin.services.ApiKeyService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

import static gov.cabinetoffice.gapfindapiadmin.security.JwtAuthorisationFilter.ADMIN_ROLE;
import static gov.cabinetoffice.gapfindapiadmin.security.JwtAuthorisationFilter.SUPER_ADMIN_ROLE;

@Controller
@RequestMapping("/api-keys")
@RequiredArgsConstructor
@Slf4j
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
    private final UserServiceConfig userServiceConfig;
    private final NavBarConfigProperties navBarConfigProperties;
    private final SwaggerConfigProperties swaggerConfigProperties;

    @GetMapping
    @PreAuthorize("hasAuthority('TECHNICAL_SUPPORT')")
    public ModelAndView showKeys() {
        log.info("Showing API keys page");

        final GrantAdmin grantAdmin = (GrantAdmin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        final String departmentName = grantAdmin.getFunder().getName();

        final ModelAndView model = new ModelAndView(ORGANISATION_API_KEYS_PAGE)
                .addObject("apiKeys", apiKeyService.getApiKeysForFundingOrganisation(grantAdmin.getFunder().getId()))
                .addObject("departmentName", departmentName)
                .addObject("signOutUrl", userServiceConfig.getLogoutUrl())
                .addObject("apiDocumentationLink", swaggerConfigProperties.getDocumentationLink());

        if (isAdmin()) {
            model.addObject("navBar", generateNavBarDto());
        }

        log.info("Finished showing API keys page");

        return model;
    }

    @GetMapping("/create")
    @PreAuthorize("hasAuthority('TECHNICAL_SUPPORT')")
    public ModelAndView showCreateKeyForm() {
        log.info("Showing create page");

        final ModelAndView createApiKey = new ModelAndView(CREATE_API_KEY_FORM_PAGE)
                .addObject("createApiKeyDTO", new CreateApiKeyDTO())
                .addObject("signOutUrl", userServiceConfig.getLogoutUrl())
                .addObject("apiDocumentationLink", swaggerConfigProperties.getDocumentationLink());

        if (isAdmin()) {
            createApiKey.addObject("navBar", generateNavBarDto());
        }

        log.info("Finished showing create page");

        return createApiKey;
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('TECHNICAL_SUPPORT')")
    public ModelAndView createKey(final @Valid @ModelAttribute CreateApiKeyDTO createApiKeyDTO, final BindingResult bindingResult) {
        log.info("Creating API key POST request");

        if (apiKeyService.doesApiKeyExist(createApiKeyDTO.getKeyName())) {
            log.info("API key with name {} already exists, showing error", createApiKeyDTO.getKeyName());

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
            final ModelAndView model = new ModelAndView(CREATE_API_KEY_FORM_PAGE)
                    .addObject("createApiKeyDTO", createApiKeyDTO)
                    .addObject("signOutUrl", userServiceConfig.getLogoutUrl())
                    .addObject("apiDocumentationLink", swaggerConfigProperties.getDocumentationLink());
            if (isAdmin()) {
                model.addObject("navBar", generateNavBarDto());
            }
            return model;
        }

        log.info("sending user to new API key page");

        final ModelAndView model = new ModelAndView(NEW_API_KEY_PAGE)
                .addObject("keyValue", apiGatewayService.createApiKeysInAwsAndDb(createApiKeyDTO.getKeyName()))
                .addObject("signOutUrl", userServiceConfig.getLogoutUrl())
                .addObject("apiDocumentationLink", swaggerConfigProperties.getDocumentationLink());

        if (isAdmin()) {
            model.addObject("navBar", generateNavBarDto());
        }

        return model;
    }

    @GetMapping("/revoke/{apiKeyId}")
    @PreAuthorize("hasAuthority('TECHNICAL_SUPPORT') || hasAuthority('SUPER_ADMIN')")
    public ModelAndView showRevokeApiKeyConfirmation(@PathVariable int apiKeyId) {
        log.info("Showing revoke API key confirmation page");

        final GapApiKey apiKey = apiKeyService.getApiKeyById(apiKeyId);
        final ModelAndView model = new ModelAndView(REVOKE_API_KEY_CONFIRMATION_PAGE)
                .addObject("apiKey", apiKey)
                .addObject("backButtonUrl", generateRedirectValue())
                .addObject("signOutUrl", userServiceConfig.getLogoutUrl());

        if (!isSuperAdmin()) {
            model.addObject("apiDocumentationLink", swaggerConfigProperties.getDocumentationLink());
        }

        if (isAdmin() || isSuperAdmin()) {
            model.addObject("navBar", generateNavBarDto());
        }

        return model;
    }

    @PostMapping("/revoke")
    @PreAuthorize("hasAuthority('TECHNICAL_SUPPORT') || hasAuthority('SUPER_ADMIN')")
    @Transactional
    public String revokeApiKey(@ModelAttribute GapApiKey apiKey) {
        log.info("Revoking API key POST request");

        try {
            apiKeyService.revokeApiKey(apiKey.getId());
            apiGatewayService.deleteApiKey(apiKeyService.getApiKeyById(apiKey.getId()), isSuperAdmin());
        } catch (Exception e) {
            log.error("An error occurred when revoking the apiKey with id: {} with error : {}", apiKey.getId(), e.getMessage());

            e.printStackTrace();
            throw e;
        }

        return "redirect:" + generateRedirectValue();
    }

    @GetMapping("/error")
    public ModelAndView displayError() {
        log.info("Showing error page");

        return new ModelAndView(ERROR_PAGE)
                .addObject("backButtonUrl", generateRedirectValue());
    }

    @GetMapping("/manage")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ModelAndView displaySuperAdminPage(@RequestParam(value = "selectedDepartments", required = false) List<String> selectedDepartment,
                                              @RequestParam(value = "page", required = false) Optional<Integer> page) {
        log.info("Showing super admin page");

        final List<GapApiKey> allApiKeys = apiKeyService.getApiKeysForSelectedFundingOrganisations(selectedDepartment);
        final int currentPage = page.orElse(1);
        final Page<GapApiKey> apiKeysPage = paginationHelper.getGapApiKeysPage(allApiKeys, currentPage);

        return new ModelAndView(SUPER_ADMIN_PAGE)
                .addObject("departments", apiKeyService.getFundingOrgForAllApiKeys())
                .addObject("activeKeyCount", apiKeyService.getActiveKeyCount(allApiKeys))
                .addObject("apiKeysPage", apiKeysPage)
                .addObject("pageNumbers", paginationHelper.getNumberOfPages(apiKeysPage.getTotalPages()))
                .addObject("selectedDepartments", selectedDepartment == null ? List.of() : selectedDepartment)
                .addObject("navBar", generateNavBarDto())
                .addObject("signOutUrl", userServiceConfig.getLogoutUrl());
    }

    protected NavBarDto generateNavBarDto() {
        log.info("Generating nav bar items");

        return NavBarDto.builder()
                .name(isSuperAdmin() ? "Super admin dashboard" : "Admin dashboard")
                .link(isSuperAdmin() ? navBarConfigProperties.getSuperAdminDashboardLink() : navBarConfigProperties.getAdminDashboardLink())
                .build();
    }

    protected boolean isSuperAdmin() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(SUPER_ADMIN_ROLE));
    }

    protected boolean isAdmin() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(ADMIN_ROLE));
    }

    protected String generateRedirectValue() {
        if (isSuperAdmin()) {
            return "/api-keys/manage";
        }
        return "/api-keys";
    }
}
