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


    public static final String ORGANISATION_API_KEYS_PAGE = "organisation-api-keys";

    @GetMapping("/organisation-api-keys")
    public ModelAndView showKeys() {
        return new ModelAndView(ORGANISATION_API_KEYS_PAGE);
    }
}
