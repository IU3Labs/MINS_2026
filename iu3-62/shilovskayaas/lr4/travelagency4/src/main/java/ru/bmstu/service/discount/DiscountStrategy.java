package ru.bmstu.service.discount;

import ru.bmstu.grpc.ReferenceServiceClient;
import java.math.BigDecimal;

public interface DiscountStrategy {
    BigDecimal getDiscountPercentage(ReferenceServiceClient.TourInfo tour, ReferenceServiceClient.ClientInfo client);
}