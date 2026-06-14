package ru.bmstu.service.discount;

import ru.bmstu.model.Tour;
import ru.bmstu.model.Client;

import java.math.BigDecimal;

public class NoDiscountStrategy implements DiscountStrategy {
    @Override
    public BigDecimal getDiscountPercentage(Tour tour, Client client) {
        return BigDecimal.ZERO;
    }
}