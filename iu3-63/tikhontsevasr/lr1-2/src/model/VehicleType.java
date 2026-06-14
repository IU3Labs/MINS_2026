package model;

public enum VehicleType {
    CAR("Легковой автомобиль"),
    MOTORCYCLE("Мотоцикл"),
    ELECTRIC_CAR("Электромобиль"),
    ACCESSIBLE_VEHICLE("Авто для перевозки инвалидов");

    private final String displayName;

    VehicleType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
