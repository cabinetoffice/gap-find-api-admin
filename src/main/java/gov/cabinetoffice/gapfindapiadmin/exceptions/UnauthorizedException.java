package gov.cabinetoffice.gapfindapiadmin.exceptions;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@ResponseStatus(UNAUTHORIZED)
public class UnauthorizedException extends AccessDeniedException {

    public UnauthorizedException(String message) {
        super(message);
    }

}
