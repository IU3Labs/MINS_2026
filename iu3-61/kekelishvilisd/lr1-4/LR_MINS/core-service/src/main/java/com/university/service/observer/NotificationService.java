package com.university.service.observer;

import com.university.model.NotificationEntry;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class NotificationService implements NotificationManager {
    private final List<Observer> observers = new ArrayList<>();
    private final Path logFile = Paths.get("notifications.log");
    private static final DateTimeFormatter DT_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    public NotificationService() {

    }

    @Override
    public void attach(Observer observer) {
        observers.add(observer);
        System.out.println(observer.getName() + " подписан на уведомления");
    }

    @Override
    public void detach(Observer observer) {
        observers.remove(observer);
        System.out.println(observer.getName() + " отписан от уведомлений");
    }

    @Override
    public void notifyObservers(String message) {
        for (Observer observer : observers) {
            notifyObserver(observer, message);
        }
    }

    @Override
    public void notifyObserver(Observer observer, String message) {
        NotificationEntry entry = new NotificationEntry(
                LocalDateTime.now(),
                observer.getName(),
                message
        );
        writeToFile(entry);

        observer.update(message);

    }

    private void writeToFile(NotificationEntry entry) {
        String logLine = String.format("[%s] %-15s | %s%n",
                entry.timestamp().format(DT_FORMAT),
                entry.observerName(),
                entry.message());

        try (BufferedWriter writer = Files.newBufferedWriter(logFile,
                StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            writer.write(logLine);
            writer.flush();
        } catch (IOException e) {

            System.err.println("Ошибка записи уведомления в файл: " + e.getMessage());
        }
    }

    @Override
    public int getObserverCount() {
        return observers.size();
    }
}
