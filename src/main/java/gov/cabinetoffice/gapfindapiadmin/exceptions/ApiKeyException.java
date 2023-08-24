package gov.cabinetoffice.gapfindapiadmin.exceptions;

public class ApiKeyException extends RuntimeException {

    public ApiKeyException(Exception e) {
            super(e.getMessage());
        }
}
