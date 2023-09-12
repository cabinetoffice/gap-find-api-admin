package gov.cabinetoffice.gapfindapiadmin.controllers;

import gov.cabinetoffice.gapfindapiadmin.services.ApiKeyService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/redirect")
@RequiredArgsConstructor
@Slf4j
public class RedirectController {

    private final ApiKeyService apiKeyService;

    @GetMapping()
    public RedirectView redirectUser(HttpServletRequest httpServletRequest) {
        return new RedirectView(httpServletRequest.getContextPath() + apiKeyService.generateRedirectionValue());
    }
}
