package gov.cabinetoffice.gapfindapiadmin.controllers;

import gov.cabinetoffice.gapfindapiadmin.services.ApiKeyService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.view.RedirectView;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RedirectControllerTest {
    @Mock
    private ApiKeyController apiKeyController;

    @Mock
    HttpServletRequest httpServletRequest;

    @InjectMocks
    private RedirectController controllerUnderTest;
    @Test
    void redirectUser() {
        when(apiKeyController.generateRedirectValue()).thenReturn("someValue");
        when(httpServletRequest.getContextPath()).thenReturn("someContextPath");
        final RedirectView expectedRedirectView = new RedirectView("someContextPath" + "someValue");

        final RedirectView actualRedirectView = controllerUnderTest.redirectUser(httpServletRequest);

        assertThat(actualRedirectView.getUrl()).isEqualTo(expectedRedirectView.getUrl());
    }
}