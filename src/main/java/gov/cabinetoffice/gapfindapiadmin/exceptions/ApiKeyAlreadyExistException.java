package gov.cabinetoffice.gapfindapiadmin.exceptions;

public class ApiKeyAlreadyExistException extends RuntimeException {

    public ApiKeyAlreadyExistException(String message) {
        super(message);
    }

}
