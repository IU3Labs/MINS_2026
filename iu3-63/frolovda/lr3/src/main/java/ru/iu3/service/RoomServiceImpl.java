package ru.iu3.service;

import java.util.List;

import ru.iu3.entity.interfaces.Room;
import ru.iu3.enums.RoomEnum;
import ru.iu3.repository.interfaces.Repository;
import ru.iu3.service.interfaces.RoomFactory;
import ru.iu3.service.interfaces.RoomService;
import ru.iu3.validation.RoomValidator;

public class RoomServiceImpl implements RoomService {
    private  Repository<Room, Integer> roomRepository;
    private  RoomFactory roomFactory;
    private  RoomValidator roomValidator;

    public RoomServiceImpl(Repository<Room, Integer> roomRepository, RoomFactory roomFactory, RoomValidator roomValidator) {
        this.roomRepository = roomRepository;
        this.roomFactory = roomFactory;
        this.roomValidator = roomValidator;
    }

    @Override
    public void addRoom(RoomEnum type, int id, String name, int hourlyRate) {
        roomValidator.validateForAdd(id, name, hourlyRate);
        Room room = roomFactory.create(type, id, name, hourlyRate);
        roomRepository.add(room);
    }

    @Override
    public List<Room> getAllRooms() {
        return roomRepository.getAll();
    }

    @Override
    public void lockRoom(int id) {
        Room room = roomRepository.getById(id);
        room.lock();
    }

    @Override
    public Room getRoomById(int id) {
        Room room = roomRepository.getById(id);
        return room;
    }
}
