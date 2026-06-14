package com.autoservice.repository;


import com.autoservice.model.Car;
import com.autoservice.model.WorkHistory;

import java.util.List;

public interface HistoryRepository {
    void addToHistory(WorkHistory history);
    List<WorkHistory> getHistoryByCar(Car car);
    List<WorkHistory> getAllHistory();
}