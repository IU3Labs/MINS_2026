package com.university.service.observer;

import com.university.model.NotificationEntry;

import java.util.ArrayList;
import java.util.List;


public interface NotificationManager {
    void attach(Observer observer);
    void detach(Observer observer);
    void notifyObservers(String message);
    void notifyObserver(Observer observer, String message);
    int getObserverCount();
}
