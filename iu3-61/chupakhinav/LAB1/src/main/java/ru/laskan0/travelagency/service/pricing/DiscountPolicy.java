package ru.laskan0.travelagency.service.pricing;

import ru.laskan0.travelagency.model.Discount;

public interface DiscountPolicy {
    boolean supports(Discount discount);
    double apply(double basePrice, Discount discount);
}
