package exception;

public class AppointmentConflictException extends AutoServiceException {
    public AppointmentConflictException(String dateTime) {
        super("На это время " + dateTime + " уже есть запись");
    }
}