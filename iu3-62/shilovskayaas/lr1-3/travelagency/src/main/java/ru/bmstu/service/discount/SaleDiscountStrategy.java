package ru.bmstu.service.discount;

import ru.bmstu.model.Tour;
import ru.bmstu.model.Client;
import java.math.BigDecimal;

public class SaleDiscountStrategy implements DiscountStrategy {
    @Override
    public BigDecimal getDiscountPercentage(Tour tour, Client client) {
        if (tour != null && tour.isOnSale()) {
            return tour.getSalePercentage();
        }
        return BigDecimal.ZERO;
    }
}
