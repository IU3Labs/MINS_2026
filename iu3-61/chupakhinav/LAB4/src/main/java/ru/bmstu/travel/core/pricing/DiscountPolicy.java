package ru.bmstu.travel.core.pricing;

import ru.bmstu.travel.core.catalog.CatalogDiscount;

public interface DiscountPolicy {
    boolean supports(CatalogDiscount discount);
    double apply(double basePrice, CatalogDiscount discount);
}
