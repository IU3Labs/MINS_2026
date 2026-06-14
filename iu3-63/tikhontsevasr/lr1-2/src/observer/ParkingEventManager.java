package observer;

import java.util.ArrayList;
import java.util.List;

public class ParkingEventManager {
    private final List<ParkingObserver> observers = new ArrayList<>();

    public void subscribe(ParkingObserver observer) {
        observers.add(observer);
    }

    public void notifyObservers(ParkingEvent event) {
        for (ParkingObserver observer : observers) {
            observer.onParkingEvent(event);
        }
    }
}
