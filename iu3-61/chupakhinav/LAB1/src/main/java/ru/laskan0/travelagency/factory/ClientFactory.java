package ru.laskan0.travelagency.factory;

import ru.laskan0.travelagency.exception.InvalidDataException;
import ru.laskan0.travelagency.model.Client;
import ru.laskan0.travelagency.model.Discount;

public abstract class ClientFactory {
    public final Client create(String id, String fullName, String phone, Discount discount) {
        if (id == null || id.isBlank()) {
            throw new InvalidDataException("ID клиента не может быть пустым");
        }
        if (fullName == null || fullName.isBlank()) {
            throw new InvalidDataException("Имя клиента не может быть пустым");
        }
        if (phone == null || phone.isBlank()) {
            throw new InvalidDataException("Телефон клиента не может быть пустым");
        }
        return createClient(id.trim(), fullName.trim(), phone.trim(), discount);
    }

    protected abstract Client createClient(String id, String fullName, String phone, Discount discount);
}
