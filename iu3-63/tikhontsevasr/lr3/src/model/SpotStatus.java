package model;

public enum SpotStatus {
    FREE("Свободно"),
    OCCUPIED("Занято");

    private final String displayName;

    SpotStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
