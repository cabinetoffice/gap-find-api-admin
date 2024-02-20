package gov.cabinetoffice.gapfindapiadmin.controllers.controller_advice;

import gov.cabinetoffice.gapfindapiadmin.config.BasePathConfigProperties;
import gov.cabinetoffice.gapfindapiadmin.exceptions.ApiKeyException;
import gov.cabinetoffice.gapfindapiadmin.exceptions.InvalidApiKeyIdException;
import gov.cabinetoffice.gapfindapiadmin.exceptions.UnauthorizedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.view.RedirectView;
import software.amazon.awssdk.services.apigateway.model.ApiGatewayException;
import software.amazon.awssdk.services.apigateway.model.NotFoundException;

import java.sql.SQLException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ControllerExceptionsHandlerTest {

    @Mock
    private ApiGatewayException apiGatewayException;

    @Mock
    private SQLException sqlException;

    @Mock
    private ApiKeyException apiKeyException;
    @Mock
    private InvalidApiKeyIdException invalidApiKeyIdException;

    @Mock
    private NotFoundException notFoundException;

    @Mock
    private UnauthorizedException unauthorizedException;

    @Mock
    private WebRequest webRequest;

    @Mock
    private BasePathConfigProperties basePathConfigProperties;

    @InjectMocks
    private ControllerExceptionsHandler controllerExceptionsHandler;

    @BeforeEach
    void setUp() {
        when(basePathConfigProperties.getPath()).thenReturn("basePath");
    }

    @Test
    void testHandleException_ApiGatewayException() {
        final RedirectView responseEntity = controllerExceptionsHandler.handleException(apiGatewayException,
                webRequest);

        assertThat(responseEntity.getUrl()).isEqualTo("basePath/api-keys/error");
    }

    @Test
    void testHandleException_SQLException() {
        final RedirectView responseEntity = controllerExceptionsHandler.handleException(sqlException,
                webRequest);

        assertThat(responseEntity.getUrl()).isEqualTo("basePath/api-keys/error");
    }

    @Test
    void testHandleException_ApiKeyException() {
        final RedirectView responseEntity = controllerExceptionsHandler.handleException(apiKeyException,
                webRequest);

        assertThat(responseEntity.getUrl()).isEqualTo("basePath/api-keys/error");
    }

    @Test
    void testHandleException_InvalidApiKeyIdException() {
        final RedirectView responseEntity = controllerExceptionsHandler.handleException(invalidApiKeyIdException,
                webRequest);

        assertThat(responseEntity.getUrl()).isEqualTo("basePath/api-keys/error");
    }

    @Test
    void testHandleException_NotFoundException() {
        final RedirectView responseEntity = controllerExceptionsHandler.handleException(notFoundException,
                webRequest);

        assertThat(responseEntity.getUrl()).isEqualTo("basePath/api-keys/error");
    }

    @Test
    void testHandleException_UnauthorizedException() {
        final RedirectView responseEntity = controllerExceptionsHandler.handleException(unauthorizedException,
                webRequest);

        assertThat(responseEntity.getUrl()).isEqualTo("basePath/api-keys/error");
    }

}
