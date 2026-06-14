package com.university.service.observer;

import com.university.model.NotificationEntry;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InMemoryNotificationRepository implements NotificationRepository {
    private final List<NotificationEntry> storage = new ArrayList<>();

    @Override
    public void save(NotificationEntry entry) {
        storage.add(entry);
    }

    @Override
    public List<NotificationEntry> findAll() {
        return new ArrayList<>(storage);
    }

    @Override
    public List<NotificationEntry> findByObserverName(String observerName) {
        return storage.stream()
                .filter(e -> e.observerName().contains(observerName))
                .collect(Collectors.toList());
    }

    @Override
    public int getCount() {
        return storage.size();
    }

    @Override
    public void clear() {
        storage.clear();
    }

}
