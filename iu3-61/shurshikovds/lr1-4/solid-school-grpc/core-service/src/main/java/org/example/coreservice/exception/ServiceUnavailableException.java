package org.example.coreservice.exception;

public class ServiceUnavailableException extends SystemException {

    public ServiceUnavailableException(String serviceName) {
        super("Сервис \"" + serviceName + "\" временно недоступен, попробуйте позже");
    }

    public ServiceUnavailableException(String serviceName, Throwable cause) {
        super("Сервис \"" + serviceName + "\" временно недоступен, попробуйте позже");
        initCause(cause);
    }
}
