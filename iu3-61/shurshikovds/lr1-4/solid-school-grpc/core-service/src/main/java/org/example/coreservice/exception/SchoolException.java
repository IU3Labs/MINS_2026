package org.example.coreservice.exception;

public class SchoolException extends RuntimeException {
    public SchoolException(String message) {
        super(message);
    }

    public SchoolException(String message, Throwable cause) {
        super(message, cause);
    }
}
