package ru.bmstu.service.discount;

import ru.bmstu.model.Tour;
import ru.bmstu.model.Client;
import java.math.BigDecimal;

public interface DiscountStrategy {
    BigDecimal getDiscountPercentage(Tour tour, Client client);
}
