package observer;

public interface ParkingObserver {
    void update(ParkingEventType type, String message);
}