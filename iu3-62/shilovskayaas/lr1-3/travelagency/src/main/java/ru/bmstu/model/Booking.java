package ru.bmstu.model;

import ru.bmstu.exception.TourAgencyException;
import ru.bmstu.model.state.*;
import ru.bmstu.observer.*;
import ru.bmstu.observer.Observer;
import ru.bmstu.observer.Subject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Booking implements Subject{
    private int id;
    private int clientId;
    private int tourId;
    private String clientEmail;
    private BigDecimal finalPrice;
    private boolean isPaid;
    private BookingState state;
    private static final List<Observer> GLOBAL_OBSERVERS = new ArrayList<>();
    static {
        GLOBAL_OBSERVERS.add(ConsoleNotifier.getInstance());
        GLOBAL_OBSERVERS.add(LoggerNotifier.getInstance());
    }

    public Booking (int id, int clientId, int tourId,  String clientEmail, BigDecimal finalPrice) {
        this.id = id;
        this.clientId = clientId;
        this.tourId = tourId;
        this.clientEmail = clientEmail;
        this.finalPrice = finalPrice;
        this.isPaid = false;
        this.state = new ActiveState();
    }

    @Override
    public void attach(Observer observer) {
//        observers.add(observer);
//        System.out.println("Добавлен наблюдатель: " + observer.getObserverName() + " для брони #" + id);
    }

    @Override
    public void detach(Observer observer) {
//        observers.remove(observer);
//        System.out.println("Удален наблюдатель: " + observer.getObserverName() + " для брони #" + id);
    }

    @Override
    public void notifyObservers(String event) {
        for (Observer observer : GLOBAL_OBSERVERS) {
            observer.update(this, event);
        }
        EmailNotifier emailNotifier = new EmailNotifier(clientEmail);
        emailNotifier.update(this, event);
    }

    public int getId() { return id; }
    public int getClientId() { return clientId; }
    public int getTourId() { return tourId; }
    public BigDecimal getFinalPrice() { return finalPrice; }
    public boolean isPaid() { return isPaid; }
    public BookingState getState() { return state; }
    public String getClientEmail() { return clientEmail; }

    public void setId(int id) { this.id = id; }
    public void setPaid(boolean paid) {
        this.isPaid = paid;
        if (paid && state instanceof ActiveState) {
            this.state = new PaidState();
            notifyObservers("Бронирование оплачено на сумму " + finalPrice + " руб.");
        }
    }
    public void setState(BookingState state) { this.state = state; }

    public void cancel() throws TourAgencyException {
        state.cancel(this);
        notifyObservers("Бронирование отменено");
    }

    public void pay() throws TourAgencyException {
        state.pay(this);
    }

    public String getStatusName() {
        return state.getStatusName();
    }

    public boolean canBeCancelled() {
        return state.canBeCancelled();
    }

    public boolean canBePaid() {
        return state.canBePaid();
    }

    @Override
    public String toString() {
        return String.format("Бронь ID: %d (Клиент: %d, Тур: %d, Цена: %.2f руб., Статус: %s, Оплата: %s)",
                id, clientId, tourId, finalPrice, getStatusName(), isPaid ? "Оплачено" : "Не оплачено");
    }
}