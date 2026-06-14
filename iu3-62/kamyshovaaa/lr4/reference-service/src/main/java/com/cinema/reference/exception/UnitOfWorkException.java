package com.cinema.reference.exception;

public class UnitOfWorkException extends RuntimeException {
    public UnitOfWorkException(String message) {
        super(message);
    }

    public UnitOfWorkException(String message, Throwable cause) {
        super(message, cause);
    }
}