package core;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import exception.InvalidSpaceException;
import exception.NoFreeSpaceException;
import exception.StorageException;
import model.ParkingTicket;
import observer.ParkingObserver;
import observer.ParkingEventType;
import report.ReportGenerator;
import storage.ParkingStorage;
import tariff.TariffCalculator;

public class ParkingSystem {
    private final int capacity;
    private final Map<Integer, ParkingTicket> activeSpaces = new HashMap<>();
    private final List<ParkingTicket> history = new ArrayList<>();
    private final List<ParkingObserver> observers = new ArrayList<>();

    private TariffCalculator globalTariff;
    private final Map<Integer, TariffCalculator> spaceTariffs = new HashMap<>(); // Тарифы для конкретных мест
    private final ReportGenerator reporter;
    private final ParkingStorage storage;

    public ParkingSystem(int capacity, TariffCalculator tariff, ReportGenerator reporter, ParkingStorage storage) {
        this.capacity = capacity;
        this.globalTariff = tariff;
        this.reporter = reporter;
        this.storage = storage;
        loadState();
    }

    public void subscribe(ParkingObserver observer) { observers.add(observer); }
    public void unsubscribe(ParkingObserver observer) { observers.remove(observer); }
    public void removeObserverByType(Class<?> type) { observers.removeIf(o -> o.getClass().equals(type)); }
    public int getObserverCount() { return observers.size(); }

    private void notifyObservers(ParkingEventType type, String message) {
        for (ParkingObserver obs : new ArrayList<>(observers)) {
            try {
                obs.update(type, message);
            } catch (Exception e) {
                System.err.println("Ошибка в наблюдателе " + obs.getClass().getSimpleName() + ": " + e.getMessage());
            }
        }
    }

    private void loadState() {
        try { storage.load(activeSpaces, history); }
        catch (StorageException e) { System.err.println("Ошибка загрузки: " + e.getMessage()); }
    }

    private void saveState() {
        try { storage.save(activeSpaces, history); }
        catch (StorageException e) { System.err.println("Ошибка сохранения: " + e.getMessage()); }
    }

    // Геттеры/сеттеры для тарифов
    public TariffCalculator getGlobalTariff() { return globalTariff; }
    public void setGlobalTariff(TariffCalculator tariff) { this.globalTariff = tariff; }
    public void setSpaceTariff(int spaceId, TariffCalculator calculator) {
        spaceTariffs.put(spaceId, calculator);
    }
    public void clearSpaceTariff(int spaceId) {
        spaceTariffs.remove(spaceId);
    }

    private TariffCalculator resolveTariff(int spaceId) {
        return spaceTariffs.getOrDefault(spaceId, globalTariff);
    }

    public ParkingTicket enter(String plate, LocalDateTime time) {
        if (activeSpaces.size() >= capacity) {
            throw new NoFreeSpaceException("На парковке нет свободных мест.");
        }
        int spaceId = 1;
        while (activeSpaces.containsKey(spaceId)) spaceId++;

        ParkingTicket ticket = new ParkingTicket(plate, spaceId, time);
        activeSpaces.put(spaceId, ticket);
        saveState();

        notifyObservers(ParkingEventType.ENTRY, "Автомобиль " + plate + " занял место #" + spaceId);
        if (capacity > 0 && activeSpaces.size() * 100 / capacity >= 80) {
            notifyObservers(ParkingEventType.CAPACITY_WARNING, "Загрузка достигла " + (activeSpaces.size() * 100 / capacity) + "%");
        }

        return ticket;
    }

    public double exit(int spaceId, LocalDateTime time) {
        ParkingTicket ticket = activeSpaces.remove(spaceId);
        if (ticket == null) {
            throw new InvalidSpaceException("Место #" + spaceId + " не занято или не существует.");
        }

        ticket.setExitTime(time);
        history.add(ticket);
        double cost = resolveTariff(spaceId).calculate(ticket.getEntryTime(), time);
        saveState();

        notifyObservers(ParkingEventType.EXIT, "Автомобиль покинул место #" + spaceId);
        return cost;
    }

    public String getReport() {
        Map<Integer, Double> estimatedCosts = new HashMap<>();
        LocalDateTime now = LocalDateTime.now();
        for (Map.Entry<Integer, ParkingTicket> entry : activeSpaces.entrySet()) {
            try {
                estimatedCosts.put(entry.getKey(), resolveTariff(entry.getKey()).calculate(entry.getValue().getEntryTime(), now));
            } catch (Exception e) {
                estimatedCosts.put(entry.getKey(), 0.0);
            }
        }
        return reporter.generate(capacity, activeSpaces, estimatedCosts);
    }

    public double getTotalRevenue() {
        double total = 0.0;
        for (ParkingTicket ticket : history) {
            if (ticket.getExitTime() != null) {
                total += resolveTariff(ticket.getSpaceId()).calculate(ticket.getEntryTime(), ticket.getExitTime());
            }
        }
        return total;
    }
}