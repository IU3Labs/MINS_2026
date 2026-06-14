package ru.bmstu.travel.core.pricing;

import ru.bmstu.travel.core.catalog.CatalogDiscount;

public class PercentageDiscountPolicy implements DiscountPolicy {
    @Override
    public boolean supports(CatalogDiscount discount) {
        return "PERCENTAGE".equals(discount.discountType());
    }

    @Override
    public double apply(double basePrice, CatalogDiscount discount) {
        return basePrice * (1 - discount.percent() / 100.0);
    }
}
