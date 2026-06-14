package com.autoservice.model;

public class Service {
    private String name;
    private double price;
    private int durationMinutes;

    public Service(String name, double price, int durationMinutes) {
        this.name = name;
        this.price = price;
        this.durationMinutes = durationMinutes;
    }

    public String getName() { return name; }
    public double getPrice() { return price; }

    public int getDurationMinutes() {
        return durationMinutes;
    }
    @Override
    public String toString() {
        return String.format("%s - %.2f руб. (%d мин.)", name, price, durationMinutes);
    }
}