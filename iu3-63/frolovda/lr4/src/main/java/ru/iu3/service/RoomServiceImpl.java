package ru.iu3.service;

import java.util.List;

import ru.iu3.entity.interfaces.Room;
import ru.iu3.enums.RoomEnum;
import ru.iu3.exceptions.NotFoundExeption;
import ru.iu3.exceptions.ValidationException;
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
        if (type == null) {
            throw new ValidationException("Тип комнаты не может быть пустым.");
        }
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
        Room room = getRoomById(id);
        room.lock();
    }

    @Override
    public Room getRoomById(int id) {
        if (id <= 0) {
            throw new ValidationException("ID комнаты должен быть положительным числом.");
        }
        Room room = roomRepository.getById(id);
        if (room == null) {
            throw new NotFoundExeption("Комната с таким ID не найдена.");
        }
        return room;
    }
}
