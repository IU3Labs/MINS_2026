package org.example.coreservice.exception;

public class UserNotFoundException extends AuthException {
    public UserNotFoundException(String username) {
      super("Пользователь '" + username + "' не найден");
    }
}
