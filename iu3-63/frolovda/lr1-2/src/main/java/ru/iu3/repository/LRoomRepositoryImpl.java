package ru.iu3.repository;

import java.util.ArrayList;
import java.util.List;

import ru.iu3.entity.interfaces.Room;
import ru.iu3.repository.interfaces.Repository;

public class LRoomRepositoryImpl implements Repository<Room, Integer> {
    private List<Room> rooms = new ArrayList<Room>();

    @Override
    public void add(Room room) {
        rooms.add(room);
    }

    @Override
    public Room getById(int id) {
        for (Room room : rooms) {
            if (room.getId() == id) {
                return room;
            }
        }
        return null;
    }

    @Override
    public List<Room> getAll() {
        return rooms;
    }

    @Override
    public void update(Room room) {
        for (int i = 0; i < rooms.size(); i++) {
            if (rooms.get(i).getId() == room.getId()) {
                rooms.set(i, room);
                return;
            }
        }
    }
}
