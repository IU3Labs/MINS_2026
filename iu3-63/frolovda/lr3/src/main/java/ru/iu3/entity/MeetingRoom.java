package ru.iu3.entity;

import ru.iu3.entity.interfaces.Room;
import ru.iu3.enums.RoomEnum;

public class MeetingRoom implements Room {
    private int id;
    private String name;
    private int hourlyRate;
    private boolean isLocked;

    public MeetingRoom(int id, String name, int hourlyRate) {
        this.id = id;
        this.name = name;
        this.hourlyRate = hourlyRate;
    }

    @Override
    public int getId() {
        return id;
    }       

    @Override
    public String getName() {
        return name;
    }

    @Override
    public RoomEnum getType() {
        return RoomEnum.MEETING_ROOM;
    }

    @Override
    public int getHourlyRate() {
        return hourlyRate;
    }

    @Override
    public boolean getIsLocked() {
        return isLocked;
    }

    @Override
    public void lock() {
        isLocked = true;
    }
}
