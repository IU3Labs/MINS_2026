package ru.bmstu.util;

import ru.bmstu.exception.InvalidDiscountException;
import ru.bmstu.exception.InvalidPriceException;
import ru.bmstu.exception.TourAgencyException;
import ru.bmstu.model.Client;
import ru.bmstu.model.Tour;

import java.math.BigDecimal;

public class Validator {

    public static void validatePrice(BigDecimal price) throws InvalidPriceException {
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidPriceException("Цена тура должна быть положительной");
        }
    }

    public static void validateDiscountPercent(BigDecimal discountPercent) throws InvalidDiscountException {
        if (discountPercent == null) {
            throw new InvalidDiscountException("Скидка не может быть null");
        }
        if (discountPercent.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidDiscountException("Скидка не может быть отрицательной");
        }
        if (discountPercent.compareTo(new BigDecimal("100")) > 0) {
            throw new InvalidDiscountException("Скидка не может превышать 100%");
        }
    }

    public static void validatePassword(String password) throws TourAgencyException {
        if (password == null || password.trim().isEmpty()) {
            throw new TourAgencyException("Пароль не может быть пустым");
        }
    }

    public static void validateTourExists(Tour tour, int tourId) throws TourAgencyException {
        if (tour == null) {
            throw new TourAgencyException("Тур с ID " + tourId + " не найден");
        }
    }

    public static void validateClientExists(Client client, int clientId) throws TourAgencyException {
        if (client == null) {
            throw new TourAgencyException("Клиент с ID " + clientId + " не найден");
        }
    }

    public static void validateEmailNotExists(boolean exists, String email) throws TourAgencyException {
        if (exists) {
            throw new TourAgencyException("Клиент с email " + email + " уже существует");
        }
    }

    public static void validateEmailFormat(boolean isValid, String email) throws TourAgencyException {
        if (!isValid) {
            throw new TourAgencyException("Неверный формат email. " + EmailValidator.getEmailRequirements());
        }
    }
}