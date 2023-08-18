package gov.cabinetoffice.gapfindapiadmin.controllers.controller_advice;

import gov.cabinetoffice.gapfindapiadmin.exceptions.InvalidApiKeyIdException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.context.request.WebRequest;
import software.amazon.awssdk.services.apigateway.model.ApiGatewayException;

import java.sql.SQLException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class ControllerExceptionsHandlerTest {

    @Mock
    private ApiGatewayException apiGatewayException;

    @Mock
    private SQLException sqlException;

    @Mock
    private InvalidApiKeyIdException invalidApiKeyIdException;

    @Mock
    private WebRequest webRequest;

    @InjectMocks
    private ControllerExceptionsHandler controllerExceptionsHandler;

    @Test
    void testHandleException_ApiGatewayException() {
        final String responseEntity = controllerExceptionsHandler.handleException(apiGatewayException,
                webRequest);

        assertThat(responseEntity).isEqualTo("redirect:/api-keys/error");
    }

    @Test
    void testHandleException_SQLException() {
        final String responseEntity = controllerExceptionsHandler.handleException(sqlException,
                webRequest);

        assertThat(responseEntity).isEqualTo("redirect:/api-keys/error");
    }

    @Test
    void testHandleException_InvalidApiKeyIdException() {
        final String responseEntity = controllerExceptionsHandler.handleException(invalidApiKeyIdException,
                webRequest);

        assertThat(responseEntity).isEqualTo("redirect:/api-keys/error");
    }

}
