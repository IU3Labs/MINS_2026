package org.example.domain.exception;

import java.util.UUID;

public class SessionNotFoundException extends NotFoundException {
    public SessionNotFoundException (UUID id) {
        super("Session not found: " + id);
    }
}
