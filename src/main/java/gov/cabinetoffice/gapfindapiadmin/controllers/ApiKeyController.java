package gov.cabinetoffice.gapfindapiadmin.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/api-keys")
@RequiredArgsConstructor
public class ApiKeyController {

    @GetMapping
    public ModelAndView showKeys() {
        return new ModelAndView("organisation-api-keys");
    }
}
