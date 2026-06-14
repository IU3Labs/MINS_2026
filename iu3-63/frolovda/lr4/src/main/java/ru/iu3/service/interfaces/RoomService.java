package ru.iu3.service.interfaces;

import java.util.List;

import ru.iu3.entity.interfaces.Room;
import ru.iu3.enums.RoomEnum;

public interface RoomService {

    void addRoom(RoomEnum type, int id, String name, int hourlyRate);

    List<Room> getAllRooms();

    void lockRoom(int id);

    Room getRoomById(int id);
}
