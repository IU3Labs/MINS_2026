package ru.bmstu.service.discount;

import ru.bmstu.grpc.ReferenceServiceClient;
import java.math.BigDecimal;

public class PersonalDiscountStrategy implements DiscountStrategy {
    @Override
    public BigDecimal getDiscountPercentage(ReferenceServiceClient.TourInfo tour, ReferenceServiceClient.ClientInfo client) {
        if (client != null && client.getPersonalDiscount() != null) {
            return client.getPersonalDiscount();
        }
        return BigDecimal.ZERO;
    }
}