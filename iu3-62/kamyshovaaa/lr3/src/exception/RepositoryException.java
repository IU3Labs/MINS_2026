package exception;

public class RepositoryException extends CinemaException {
    public RepositoryException(String message, Throwable cause) {
        super(message + cause.toString());
    }
}
