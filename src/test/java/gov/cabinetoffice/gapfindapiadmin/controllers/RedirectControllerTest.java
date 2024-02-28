package gov.cabinetoffice.gapfindapiadmin.controllers;

import gov.cabinetoffice.gapfindapiadmin.config.BasePathConfigProperties;
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
    private
    BasePathConfigProperties basePathConfigProperties;

    @InjectMocks
    private RedirectController controllerUnderTest;

    @Test
    void redirectUser() {
        when(apiKeyController.generateRedirectValue()).thenReturn("someValue");
        when(basePathConfigProperties.getPath()).thenReturn("somePath");

        final RedirectView expectedRedirectView = new RedirectView("somePath" + "someValue");

        final RedirectView actualRedirectView = controllerUnderTest.redirectUser();

        assertThat(actualRedirectView.getUrl()).isEqualTo(expectedRedirectView.getUrl());
    }
}