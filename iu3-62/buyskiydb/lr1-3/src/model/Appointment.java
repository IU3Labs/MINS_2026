package model;

import model.status.AppointmentState;
import model.status.NewState;
import observer.AppointmentObserver;
import service.discount.DiscountStrategy;
import service.discount.NoDiscountStrategy;

import java.util.ArrayList;
import java.util.List;

public class Appointment {
    private Car car;
    private Service service;
    private String dateTime;
    private AppointmentState state;
    private DiscountStrategy discountStrategy;
    private int clientVisitsCount;
    private List<AppointmentObserver> observers = new ArrayList<>();


    public Appointment(Car car, Service service, String dateTime) {
        this.car = car;
        this.service = service;
        this.dateTime = dateTime;
        this.state = new NewState();
        this.discountStrategy = new NoDiscountStrategy();
        this.clientVisitsCount = 1;
    }

    public Car getCar() { return car; }
    public Service getService() { return service; }
    public String getDateTime() { return dateTime; }

    public AppointmentState getState() { return state; }
    public void setClientVisitsCount(int clientVisitsCount) {
        this.clientVisitsCount = clientVisitsCount;
    }
    public void setDiscountStrategy(DiscountStrategy discountStrategy) {
        this.discountStrategy = discountStrategy;
    }


    public void cancel() {
        state.cancel(this);
    }


    public boolean isCompleted() {
        return "ВЫПОЛНЕНА".equals(state.getStatusName());
    }

    public boolean isCancelled() {
        return "ОТМЕНЕНА".equals(state.getStatusName());
    }
    public void addObserver(AppointmentObserver observer) {
        observers.add(observer);
    }


    private void notifyObservers(String oldStatus, String newStatus) {
        for (AppointmentObserver observer : observers) {
            observer.update(this, oldStatus, newStatus);
        }
    }

    public double calculateCost() {
        if (discountStrategy != null) {
            return discountStrategy.calculatePrice(service, clientVisitsCount);
        }
        return service.getPrice();
    }

    public void setState(AppointmentState state) {
        String oldStatus = (this.state != null) ? this.state.getStatusName() : "НЕТ";
        this.state = state;
        String newStatus = this.state.getStatusName();
        notifyObservers(oldStatus, newStatus);
    }

    public void nextStatus() {
        state.next(this);
    }

    @Override
    public String toString() {
        return String.format("[%s] %s: %s -> %s (%s, статус: %s)",
                dateTime, car, service.getName(),
                (isCompleted() ? "ВЫПОЛНЕНО" : "ЗАПЛАНИРОВАНО"),
                calculateCost() + " руб.",
                state.getStatusName());
    }
}