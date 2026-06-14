package exception;

public abstract class AutoServiceException extends Exception {
    public AutoServiceException(String message) {
        super(message);
    }
}