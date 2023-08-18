package gov.cabinetoffice.gapfindapiadmin.controllers.controller_advice;


import gov.cabinetoffice.gapfindapiadmin.controllers.ApiKeyController;
import gov.cabinetoffice.gapfindapiadmin.exceptions.InvalidApiKeyIdException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import software.amazon.awssdk.services.apigateway.model.ApiGatewayException;
import software.amazon.awssdk.services.apigateway.model.NotFoundException;

import java.sql.SQLException;

@ControllerAdvice(
        assignableTypes = {ApiKeyController.class})
public class ControllerExceptionsHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {ApiGatewayException.class})
    public String handleException(ApiGatewayException ex, WebRequest request) {
        return "redirect:/api-keys/error";
    }

    @ExceptionHandler(value = {SQLException.class})
    public String handleException(SQLException ex, WebRequest request) {
        return "redirect:/api-keys/error";
    }

    @ExceptionHandler(value = {InvalidApiKeyIdException.class})
    public String handleException(InvalidApiKeyIdException ex, WebRequest request) {
        return "redirect:/api-keys/error";
    }

    @ExceptionHandler(value = {NotFoundException.class})
    public String handleException(NotFoundException ex, WebRequest request) {
        return "redirect:/api-keys/error";
    }
}
