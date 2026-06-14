package ru.iu3.enums;

public enum RoomEnum {
    MEETING_ROOM(1,"Переговорная"),
    WORKPLACE(2, "Рабочее место");

    private int id;
    private String displayName;

    RoomEnum(int id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public int getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static RoomEnum findByKey(int key) {
        for (RoomEnum option : RoomEnum.values()) {
            if (option.getId() == key) {
                return option;
            }
        }
        return null;
    }
}
