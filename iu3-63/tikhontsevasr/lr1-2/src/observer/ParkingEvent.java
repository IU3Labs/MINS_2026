package observer;

import java.time.LocalDateTime;

public class ParkingEvent {
    private final ParkingEventType type;
    private final String message;
    private final LocalDateTime timestamp;

    public ParkingEvent(ParkingEventType type, String message, LocalDateTime timestamp) {
        this.type = type;
        this.message = message;
        this.timestamp = timestamp;
    }

    public ParkingEventType getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
