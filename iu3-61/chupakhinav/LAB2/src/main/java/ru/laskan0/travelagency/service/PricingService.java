package ru.laskan0.travelagency.service;

import java.util.List;

import ru.laskan0.travelagency.exception.InvalidDataException;
import ru.laskan0.travelagency.exception.PricingException;
import ru.laskan0.travelagency.model.Discount;
import ru.laskan0.travelagency.service.pricing.DiscountPolicy;

public class PricingService {
    private final List<DiscountPolicy> discountPolicies;

    public PricingService(List<DiscountPolicy> discountPolicies) {
        if (discountPolicies == null || discountPolicies.isEmpty()) {
            throw new InvalidDataException("Список правил расчета скидок не может быть пустым");
        }
        this.discountPolicies = List.copyOf(discountPolicies);
    }

    public double calculateFinalPrice(double basePrice, Discount discount) {
        if (basePrice <= 0) {
            throw new InvalidDataException("Базовая цена должна быть больше 0");
        }
        if (discount == null) {
            return basePrice;
        }
        return discountPolicies.stream()
                .filter(policy -> policy.supports(discount))
                .findFirst()
                .orElseThrow(() -> new PricingException(
                        "Не найдено правило расчета для скидки: " + discount.getName()))
                .apply(basePrice, discount);
    }
}
