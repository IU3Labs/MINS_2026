package ru.bmstu.service.discount;

import ru.bmstu.grpc.ReferenceServiceClient;
import java.math.BigDecimal;

public class SaleDiscountStrategy implements DiscountStrategy {
    @Override
    public BigDecimal getDiscountPercentage(ReferenceServiceClient.TourInfo tour, ReferenceServiceClient.ClientInfo client) {
        if (tour != null && tour.isOnSale()) {
            return tour.getSalePercentage();
        }
        return BigDecimal.ZERO;
    }
}