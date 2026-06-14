package service.pricing;

import entity.Ticket;

public interface PricingStrategy {
    String getStrategyId();
    double calculatePrice(Ticket ticket);
    String getCategoryName();
}