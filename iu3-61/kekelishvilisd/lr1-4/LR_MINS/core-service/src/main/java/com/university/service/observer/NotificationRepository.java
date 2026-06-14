package com.university.service.observer;

import com.university.model.NotificationEntry;

import java.util.List;

public interface NotificationRepository {
    void save(NotificationEntry entry);
    List<NotificationEntry> findAll();
    List<NotificationEntry> findByObserverName(String observerName);
    int getCount();
    void clear();
}
