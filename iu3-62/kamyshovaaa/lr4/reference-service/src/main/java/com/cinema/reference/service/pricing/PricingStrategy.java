package com.cinema.reference.service.pricing;

public interface PricingStrategy {
    PricingCategory getCategory();
    double calculatePrice(double basePrice);
}