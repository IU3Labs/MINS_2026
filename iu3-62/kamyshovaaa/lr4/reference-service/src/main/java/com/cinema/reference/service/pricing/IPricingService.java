package com.cinema.reference.service.pricing;

import java.util.List;

public interface IPricingService {
    void setStrategy(String strategyId);
    void setStrategyByIndex(int index);
    double calculatePrice(double basePrice);
    String getCategoryName();
    List<PricingStrategy> getStrategies();
    String getMenu();
}