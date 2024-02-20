package gov.cabinetoffice.gapfindapiadmin.controllers.controller_advice;


import gov.cabinetoffice.gapfindapiadmin.config.BasePathConfigProperties;
import gov.cabinetoffice.gapfindapiadmin.controllers.ApiKeyController;
import gov.cabinetoffice.gapfindapiadmin.exceptions.ApiKeyException;
import gov.cabinetoffice.gapfindapiadmin.exceptions.InvalidApiKeyIdException;
import gov.cabinetoffice.gapfindapiadmin.exceptions.UnauthorizedException;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.view.RedirectView;
import software.amazon.awssdk.services.apigateway.model.ApiGatewayException;
import software.amazon.awssdk.services.apigateway.model.NotFoundException;

import java.sql.SQLException;

@ControllerAdvice(
        assignableTypes = {ApiKeyController.class})
@RequiredArgsConstructor
public class ControllerExceptionsHandler extends ResponseEntityExceptionHandler {

    private final  BasePathConfigProperties basePathConfigProperties;
    private final String ERROR_PAGE_REDIRECT ="/api-keys/error";

    @ExceptionHandler(value = {ApiGatewayException.class})
    public RedirectView handleException(ApiGatewayException ex, WebRequest request) {
        return new RedirectView(basePathConfigProperties.getPath() + ERROR_PAGE_REDIRECT);
    }

    @ExceptionHandler(value = {SQLException.class})
    public RedirectView handleException(SQLException ex, WebRequest request) {
        return new RedirectView(basePathConfigProperties.getPath() + ERROR_PAGE_REDIRECT);
    }

    @ExceptionHandler(value = {ApiKeyException.class})
    public RedirectView handleException(ApiKeyException ex, WebRequest request) {
        return new RedirectView(basePathConfigProperties.getPath() + ERROR_PAGE_REDIRECT);
    }

    @ExceptionHandler(value = {InvalidApiKeyIdException.class})
    public RedirectView handleException(InvalidApiKeyIdException ex, WebRequest request) {
        return new RedirectView(basePathConfigProperties.getPath() + ERROR_PAGE_REDIRECT);
    }

    @ExceptionHandler(value = {NotFoundException.class})
    public RedirectView handleException(NotFoundException ex, WebRequest request) {
        return new RedirectView(basePathConfigProperties.getPath() + ERROR_PAGE_REDIRECT);
    }

    @ExceptionHandler(value = {UnauthorizedException.class})
    public RedirectView handleException(UnauthorizedException ex, WebRequest request) {
        return new RedirectView(basePathConfigProperties.getPath() + ERROR_PAGE_REDIRECT);
    }
}
