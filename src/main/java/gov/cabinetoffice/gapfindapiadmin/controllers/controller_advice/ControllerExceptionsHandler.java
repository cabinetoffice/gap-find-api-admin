package gov.cabinetoffice.gapfindapiadmin.controllers.controller_advice;


import gov.cabinetoffice.gapfindapiadmin.controllers.ApiKeyController;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import software.amazon.awssdk.services.apigateway.model.ApiGatewayException;

@ControllerAdvice(
        assignableTypes = {ApiKeyController.class})
public class ControllerExceptionsHandler extends ResponseEntityExceptionHandler {

//TODO replace this with a proper error handler
    @ExceptionHandler(value = {ApiGatewayException.class})
    public String handleException(ApiGatewayException ex, WebRequest request) {
        return "redirect:/api-keys/error";
    }
}
