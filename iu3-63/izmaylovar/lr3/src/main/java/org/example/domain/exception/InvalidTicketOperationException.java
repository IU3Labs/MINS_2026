package org.example.domain.exception;

public class InvalidTicketOperationException extends CinemaException {
    public InvalidTicketOperationException(String message) {
        super(message);
    }
}
