package repository;

import model.Car;
import model.WorkHistory;

import java.util.List;

public interface HistoryRepository {
    void addToHistory(WorkHistory history);
    List<WorkHistory> getHistoryByCar(Car car);
    List<WorkHistory> getAllHistory();
}