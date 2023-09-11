package gov.cabinetoffice.gapfindapiadmin.controllers;

import gov.cabinetoffice.gapfindapiadmin.models.GapApiKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping()
@RequiredArgsConstructor
@Slf4j
public class MainController {

    @GetMapping("/")
    public String revokeApiKey(@ModelAttribute GapApiKey apiKey) {
       // TODO
        // if the user is superAdmin redirect to /api-keys/manage otherwise redirect to /api-keys
    }
}
