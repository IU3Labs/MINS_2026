package ru.bmstu.travel.core.gateway;

import io.grpc.Status;

public class ReferenceServiceException extends RuntimeException {
    private final Status.Code statusCode;

    public ReferenceServiceException(String message, Status.Code statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public Status.Code statusCode() {
        return statusCode;
    }
}
