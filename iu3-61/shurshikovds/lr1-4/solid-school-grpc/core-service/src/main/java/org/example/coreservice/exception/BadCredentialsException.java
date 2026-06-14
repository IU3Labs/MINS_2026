package org.example.coreservice.exception;

public class BadCredentialsException extends AuthException {
    public BadCredentialsException() {
        super("Неверный пароль");
    }
}
