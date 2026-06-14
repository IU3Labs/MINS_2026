package ru.iu3.validation;

import ru.iu3.entity.interfaces.Room;
import ru.iu3.exceptions.ConflictException;
import ru.iu3.exceptions.ValidationException;
import ru.iu3.repository.interfaces.Repository;

public class RoomValidator {

    private final Repository<Room, Integer> roomRepository;

    public RoomValidator(Repository<Room, Integer>  roomRepository) {
        this.roomRepository = roomRepository;
    }

    public void validateForAdd(int id, String name, int hourlyRate) {
        validateId(id);
        validateName(name);
        validateHourlyRate(hourlyRate);
    }

    private void validateId(int id) {
        if (id <= 0) {
            throw new ValidationException("ID комнаты должен быть положительным числом.");
        }
        if (roomRepository.getById(id) != null) {
            throw new ConflictException("Комната с таким ID уже существует.");
        }
    }

    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("Название комнаты не может быть пустым.");
        }
    }

    private void validateHourlyRate(int hourlyRate) {
        if (hourlyRate <= 0) {
            throw new ValidationException("Ставка аренды должна быть положительным числом.");
        }
    }
}
