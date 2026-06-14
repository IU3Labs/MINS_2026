package ru.bmstu.service.discount;

import ru.bmstu.grpc.ReferenceServiceClient;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class MaxDiscountStrategy implements DiscountStrategy {
    private final List<DiscountStrategy> strategies;

    public MaxDiscountStrategy(DiscountStrategy... strategies) {
        this.strategies = Arrays.asList(strategies);
    }

    @Override
    public BigDecimal getDiscountPercentage(ReferenceServiceClient.TourInfo tour, ReferenceServiceClient.ClientInfo client) {
        return strategies.stream()
                .map(strategy -> strategy.getDiscountPercentage(tour, client))
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
    }
}