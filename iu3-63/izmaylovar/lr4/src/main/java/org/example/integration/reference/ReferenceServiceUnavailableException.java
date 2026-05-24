package org.example.integration.reference;

import org.example.domain.exception.CinemaException;

public class ReferenceServiceUnavailableException extends CinemaException {
    public ReferenceServiceUnavailableException(String message) {
        super(message);
    }
}
