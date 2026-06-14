package exception;

public class ActiveSessionNotFoundException extends ParkingException {
    public ActiveSessionNotFoundException(String message) {
        super(message);
    }
}
