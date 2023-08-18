package gov.cabinetoffice.gapfindapiadmin.exceptions;

public class InvalidApiKeyIdException extends RuntimeException {

    public InvalidApiKeyIdException() {
    }

    public InvalidApiKeyIdException(String message) {
        super(message);
    }
}
