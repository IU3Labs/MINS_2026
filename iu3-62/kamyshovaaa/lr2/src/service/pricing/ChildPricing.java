package service.pricing;

import entity.Ticket;
// Enum-ом переделать возврат имён
public class ChildPricing implements PricingStrategy {
    public static final String STRATEGY_ID = "child";
    private static final double MULTIPLIER = 2.0;

    @Override
    public String getStrategyId() {
        return STRATEGY_ID;
    }

    @Override
    public double calculatePrice(Ticket ticket) {
        return ticket.getPrice() * MULTIPLIER;
    }

    @Override
    public String getCategoryName() {
        return "Ребёнок";
    }
}