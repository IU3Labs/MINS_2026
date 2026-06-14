package ru.bmstu.travel.core.pricing;

import ru.bmstu.travel.core.catalog.CatalogDiscount;

public class SeasonalDiscountPolicy implements DiscountPolicy {
    @Override
    public boolean supports(CatalogDiscount discount) {
        return "SEASONAL".equals(discount.discountType());
    }

    @Override
    public double apply(double basePrice, CatalogDiscount discount) {
        return Math.max(basePrice - 7500, basePrice * (1 - discount.percent() / 100.0));
    }
}
