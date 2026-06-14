package observer;

import java.time.format.DateTimeFormatter;

public class LoggerObserver implements ParkingObserver {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    @Override
    public void onParkingEvent(ParkingEvent event) {
        System.out.println("[LOG " + event.getTimestamp().format(FORMATTER) + "] " + event.getMessage());
    }
}
