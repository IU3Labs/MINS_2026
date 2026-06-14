package com.autoservice.exception;

public class ClientNotFoundException extends AutoServiceException {
    public ClientNotFoundException(String phone) {
        super("Клиент с телефоном " + phone + " не найден");
    }
}