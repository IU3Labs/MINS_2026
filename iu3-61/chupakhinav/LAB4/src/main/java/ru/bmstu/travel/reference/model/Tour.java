package ru.bmstu.travel.reference.model;

public class Tour {
    private final String id;
    private final String title;
    private final String destination;
    private final double basePrice;
    private final int capacity;
    private int bookedSeats;

    public Tour(String id, String title, String destination, double basePrice, int capacity, int bookedSeats) {
        this.id = id;
        this.title = title;
        this.destination = destination;
        this.basePrice = basePrice;
        this.capacity = capacity;
        this.bookedSeats = bookedSeats;
    }

    public String id() { return id; }
    public String title() { return title; }
    public String destination() { return destination; }
    public double basePrice() { return basePrice; }
    public int capacity() { return capacity; }
    public int bookedSeats() { return bookedSeats; }
    public int availableSeats() { return capacity - bookedSeats; }

    public void reserveSeat() {
        bookedSeats += 1;
    }

    public void releaseSeat() {
        if (bookedSeats > 0) {
            bookedSeats -= 1;
        }
    }
}
