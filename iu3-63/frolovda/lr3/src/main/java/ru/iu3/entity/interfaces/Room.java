package ru.iu3.entity.interfaces;

import ru.iu3.enums.RoomEnum;

public interface Room {
    int getId();
    String getName();
    void lock();
    RoomEnum getType();
    int getHourlyRate();
    boolean getIsLocked();
    
}
