package gov.cabinetoffice.gapfindapiadmin.controllers.controller_advice;

import gov.cabinetoffice.gapfindapiadmin.exceptions.ApiKeyAlreadyExistException;
import gov.cabinetoffice.gapfindapiadmin.exceptions.ApiKeyDoesNotExistException;
import gov.cabinetoffice.gapfindapiadmin.models.ErrorMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import software.amazon.awssdk.services.apigateway.model.ApiGatewayException;

import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ControllerExceptionsHandlerTest {


    @Mock
    private MethodArgumentTypeMismatchException methodArgumentTypeMismatchException;

    @Mock
    private ApiKeyAlreadyExistException apiKeyAlreadyExistException;

    @Mock
    private ApiKeyDoesNotExistException apiKeyDoesNotExistException;

    @Mock
    private ApiGatewayException apiGatewayException;

    @Mock
    private WebRequest webRequest;

    @InjectMocks
    private ControllerExceptionsHandler controllerExceptionsHandler;

    @Test
    void testHandleException_ApiKeyAlreadyExists() {
        final String errorMessage = "Api key already exist";

        when(apiKeyAlreadyExistException.getMessage()).thenReturn(errorMessage);

        final ResponseEntity<Object> responseEntity = controllerExceptionsHandler
                .handleException(apiKeyAlreadyExistException, webRequest);

        final String errorMessageFromResponse = ((ErrorMessage) Objects.requireNonNull(responseEntity.getBody()))
                .getMessage();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorMessageFromResponse).isEqualTo(errorMessage);
    }

    @Test
    void testHandleException_ApiKeyDoesNotExist() {
        final String errorMessage = "Api does not exist";

        when(apiKeyDoesNotExistException.getMessage()).thenReturn(errorMessage);

        final ResponseEntity<Object> responseEntity = controllerExceptionsHandler
                .handleException(apiKeyDoesNotExistException, webRequest);

        final String errorMessageFromResponse = ((ErrorMessage) Objects.requireNonNull(responseEntity.getBody()))
                .getMessage();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorMessageFromResponse).isEqualTo(errorMessage);
    }

    @Test
    void testHandleException_ApiGatewayException() {
        final String errorMessage = "Api gateway exception";

        when(apiGatewayException.getMessage()).thenReturn(errorMessage);

        final ResponseEntity<Object> responseEntity = controllerExceptionsHandler.handleException(apiGatewayException,
                webRequest);

        final String errorMessageFromResponse = ((ErrorMessage) Objects.requireNonNull(responseEntity.getBody()))
                .getMessage();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorMessageFromResponse).isEqualTo(errorMessage);
    }

}
