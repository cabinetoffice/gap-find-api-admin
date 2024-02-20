package gov.cabinetoffice.gapfindapiadmin.controllers;

import gov.cabinetoffice.gapfindapiadmin.config.BasePathConfigProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/redirect")
@RequiredArgsConstructor
@Slf4j
public class RedirectController {

    private final ApiKeyController apiKeyController;
    private final BasePathConfigProperties basePathConfigProperties;

    @GetMapping()
    public RedirectView redirectUser() {
        String redirect = apiKeyController.generateRedirectValue();
        log.info("Redirecting to: " + redirect);
        return new RedirectView(basePathConfigProperties.getPath()  + redirect);
    }
}
