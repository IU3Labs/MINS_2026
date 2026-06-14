package main.java.exceptions;

public class UserNotFoundException extends LibrarySystemException {
    public UserNotFoundException(int id) {
        super("Пользователь с ID " + id + " не найден.");
    }
}