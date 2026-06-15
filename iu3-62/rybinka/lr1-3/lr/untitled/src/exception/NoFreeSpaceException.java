package exception;

public class NoFreeSpaceException extends ParkingException {
    public NoFreeSpaceException(String message) {
        super(message);
    }
}