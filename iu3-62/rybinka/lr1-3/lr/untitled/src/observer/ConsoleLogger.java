package observer;

public class ConsoleLogger implements ParkingObserver {
    @Override
    public void update(ParkingEventType type, String message) {
        System.out.println(" [LOG] " + type + " | " + message);
    }
}