package gov.cabinetoffice.gapfindapiadmin.controllers.controller_advice;

import gov.cabinetoffice.gapfindapiadmin.exceptions.ApiKeyException;
import gov.cabinetoffice.gapfindapiadmin.exceptions.InvalidApiKeyIdException;
import gov.cabinetoffice.gapfindapiadmin.exceptions.UnauthorizedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.context.request.WebRequest;
import software.amazon.awssdk.services.apigateway.model.ApiGatewayException;
import software.amazon.awssdk.services.apigateway.model.NotFoundException;

import java.sql.SQLException;

import static gov.cabinetoffice.gapfindapiadmin.controllers.controller_advice.ControllerExceptionsHandler.ERROR_PAGE_REDIRECT;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

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

    @InjectMocks
    private ControllerExceptionsHandler controllerExceptionsHandler;

    @Test
    void testHandleException_ApiGatewayException() {
        final String responseEntity = controllerExceptionsHandler.handleException(apiGatewayException,
                webRequest);

        assertThat(responseEntity).isEqualTo(ERROR_PAGE_REDIRECT);
    }

    @Test
    void testHandleException_SQLException() {
        final String responseEntity = controllerExceptionsHandler.handleException(sqlException,
                webRequest);

        assertThat(responseEntity).isEqualTo(ERROR_PAGE_REDIRECT);
    }

    @Test
    void testHandleException_ApiKeyException() {
        final String responseEntity = controllerExceptionsHandler.handleException(apiKeyException,
                webRequest);

        assertThat(responseEntity).isEqualTo(ERROR_PAGE_REDIRECT);
    }

    @Test
    void testHandleException_InvalidApiKeyIdException() {
        final String responseEntity = controllerExceptionsHandler.handleException(invalidApiKeyIdException,
                webRequest);

        assertThat(responseEntity).isEqualTo(ERROR_PAGE_REDIRECT);
    }

    @Test
    void testHandleException_NotFoundException() {
        final String responseEntity = controllerExceptionsHandler.handleException(notFoundException,
                webRequest);

        assertThat(responseEntity).isEqualTo(ERROR_PAGE_REDIRECT);
    }

    @Test
    void testHandleException_UnauthorizedException() {
        final String responseEntity = controllerExceptionsHandler.handleException(unauthorizedException,
                webRequest);

        assertThat(responseEntity).isEqualTo(ERROR_PAGE_REDIRECT);
    }

}
