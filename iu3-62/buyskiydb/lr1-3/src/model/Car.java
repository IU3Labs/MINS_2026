package model;

public class Car {
    private String licensePlate;
    private String brand;
    private String model;
    private int year;
    private Client owner;

    public Car(String licensePlate, String brand, String model, int year, Client owner) {
        this.licensePlate = licensePlate;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.owner = owner;
    }

    public String getLicensePlate() { return licensePlate; }
    public String getModel() { return model; }
    public Client getOwner() { return owner; }

    @Override
    public String toString() {
        return String.format("%s %s (%d, госномер %s)", brand, model, year, licensePlate);
    }
}
