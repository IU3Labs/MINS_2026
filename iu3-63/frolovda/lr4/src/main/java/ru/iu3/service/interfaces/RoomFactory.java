package ru.iu3.service.interfaces;

import ru.iu3.entity.interfaces.Room;
import ru.iu3.enums.RoomEnum;

public interface RoomFactory {
    Room create(RoomEnum type, int id, String name, int hourlyRate);
}
