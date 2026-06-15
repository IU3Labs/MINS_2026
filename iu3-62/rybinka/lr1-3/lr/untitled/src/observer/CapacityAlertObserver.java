package observer;

public class CapacityAlertObserver implements ParkingObserver {
    private final int thresholdPercent;

    public CapacityAlertObserver(int thresholdPercent) {
        this.thresholdPercent = thresholdPercent;
    }

    @Override
    public void update(ParkingEventType type, String message) {
        if (type == ParkingEventType.CAPACITY_WARNING) {
            System.out.println(" [ALERT] " + message);
        }
    }
}