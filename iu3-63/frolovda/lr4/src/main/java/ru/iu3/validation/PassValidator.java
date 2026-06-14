package ru.iu3.validation;

import ru.iu3.entity.Pass;
import ru.iu3.exceptions.ValidationException;
import ru.iu3.repository.interfaces.Repository;

public class PassValidator {

    private final Repository<Pass, Integer> passRepository;

    public PassValidator(Repository<Pass, Integer> passRepository) {
        this.passRepository = passRepository;
    }

    public void validateForIssue(int id, String holderName) {
        validateId(id);
        validateHolderName(holderName);
    }

    private void validateId(int id) {
        if (id <= 0) {
            throw new ValidationException("ID пропуска должен быть положительным числом.");
        }
        if (passRepository.getById(id) != null) {
            throw new ValidationException("Пропуск с таким ID уже существует.");
        }
    }

    private void validateHolderName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("Имя держателя пропуска не может быть пустым.");
        }
    }
}
