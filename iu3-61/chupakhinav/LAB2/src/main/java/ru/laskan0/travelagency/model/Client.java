package ru.laskan0.travelagency.model;

import ru.laskan0.travelagency.exception.InvalidDataException;

public class Client {
    private final String id;
    private final String fullName;
    private final String phone;
    private final Discount discount;

    public Client(String id, String fullName, String phone, Discount discount) {
        this.id = requireText(id, "ID клиента");
        this.fullName = requireText(fullName, "ФИО клиента");
        this.phone = requireText(phone, "Телефон клиента");
        this.discount = discount;
    }

    private String requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new InvalidDataException(fieldName + " не может быть пустым");
        }
        return value.trim();
    }

    public String getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhone() {
        return phone;
    }

    public Discount getDiscount() {
        return discount;
    }

    @Override
    public String toString() {
        return "Client{id='" + id + "', name='" + fullName + "', phone='" + phone + "', discount="
                + (discount == null ? "нет" : discount) + "}";
    }
}
