package org.example.domain.exception;

public class EntityInUseException extends CinemaException {
    public EntityInUseException(String message) {
        super(message);
    }
}
