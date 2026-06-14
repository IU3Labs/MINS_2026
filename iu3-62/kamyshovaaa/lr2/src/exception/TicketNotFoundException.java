package exception;

public class TicketNotFoundException extends CinemaException {
    public TicketNotFoundException(String id) {
        super("Билет не найден: " + id);
    }
}
