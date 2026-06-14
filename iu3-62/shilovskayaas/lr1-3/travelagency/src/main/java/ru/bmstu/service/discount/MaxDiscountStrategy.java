package ru.bmstu.service.discount;

import ru.bmstu.model.Tour;
import ru.bmstu.model.Client;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;


public class MaxDiscountStrategy implements DiscountStrategy {
    private final List<DiscountStrategy> strategies;

    public MaxDiscountStrategy(DiscountStrategy... strategies) {
        this.strategies = Arrays.asList(strategies);
    }

    @Override
    public BigDecimal getDiscountPercentage(Tour tour, Client client) {
        return strategies.stream()
                .map(strategy -> strategy.getDiscountPercentage(tour, client))
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
    }
}
