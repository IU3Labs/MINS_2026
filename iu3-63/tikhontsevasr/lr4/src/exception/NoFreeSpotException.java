package exception;

public class NoFreeSpotException extends ParkingException {
    public NoFreeSpotException(String message) {
        super(message);
    }
}
