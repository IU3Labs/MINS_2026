package exception;

public class HallNotFoundException extends CinemaException {
    public HallNotFoundException(String id) {
        super("Зал не найден: " + id);
    }
}
