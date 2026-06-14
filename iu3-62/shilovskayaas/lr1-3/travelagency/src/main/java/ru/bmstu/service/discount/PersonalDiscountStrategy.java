package ru.bmstu.service.discount;

import ru.bmstu.model.Tour;
import ru.bmstu.model.Client;
import java.math.BigDecimal;

public class PersonalDiscountStrategy implements DiscountStrategy {
    @Override
    public BigDecimal getDiscountPercentage(Tour tour, Client client) {
        if (client != null && client.getPersonalDiscount() != null) {
            return client.getPersonalDiscount();
        }
        return BigDecimal.ZERO;
    }
}
