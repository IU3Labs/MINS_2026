package model;

public enum SpotType {
    REGULAR("Обычное"),
    DISABLED("Для инвалидов"),
    ELECTRIC("Для электромобилей");

    private final String displayName;

    SpotType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
