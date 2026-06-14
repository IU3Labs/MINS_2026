package repository.impl;

import model.Car;
import model.WorkHistory;
import repository.HistoryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InMemoryHistoryRepository implements HistoryRepository {
    private final List<WorkHistory> history = new ArrayList<>();

    @Override
    public void addToHistory(WorkHistory workHistory) {
        history.add(workHistory);
    }

    @Override
    public List<WorkHistory> getHistoryByCar(Car car) {
        return history.stream()
                .filter(h -> h.toString().contains(car.getLicensePlate())) // упрощенно
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkHistory> getAllHistory() {
        return new ArrayList<>(history);
    }
}