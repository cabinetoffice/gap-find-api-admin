package gov.cabinetoffice.gapfindapiadmin.controllers;

import gov.cabinetoffice.gapfindapiadmin.services.ApiKeyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/redirect")
@RequiredArgsConstructor
@Slf4j
public class RedirectController {

    ApiKeyService apiKeyService;

    @GetMapping()
    public String redirectUser() {
        return "redirect:" + apiKeyService.generateRedirectionValue();
    }
}
