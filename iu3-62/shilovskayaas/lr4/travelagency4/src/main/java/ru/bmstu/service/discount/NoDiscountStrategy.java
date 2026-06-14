package ru.bmstu.service.discount;

import ru.bmstu.grpc.ReferenceServiceClient;
import java.math.BigDecimal;

public class NoDiscountStrategy implements DiscountStrategy {
    @Override
    public BigDecimal getDiscountPercentage(ReferenceServiceClient.TourInfo tour, ReferenceServiceClient.ClientInfo client) {
        return BigDecimal.ZERO;
    }
}