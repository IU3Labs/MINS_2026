package observer;

public class AdminNotificationObserver implements ParkingObserver {
    @Override
    public void onParkingEvent(ParkingEvent event) {
        if (event.getType() == ParkingEventType.HIGH_OCCUPANCY) {
            System.out.println("[ADMIN] Внимание: " + event.getMessage());
            return;
        }
        System.out.println("[ADMIN] Получено событие: " + event.getMessage());
    }
}
