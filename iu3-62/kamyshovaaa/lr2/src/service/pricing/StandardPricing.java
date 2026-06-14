package service.pricing;

import entity.Ticket;

public class StandardPricing implements PricingStrategy {
    public static final String STRATEGY_ID = "standard";

    @Override
    public String getStrategyId() {
        return STRATEGY_ID;
    }

    @Override
    public double calculatePrice(Ticket ticket) {
        return ticket.getPrice();
    }

    @Override
    public String getCategoryName() {
        return "Взрослый";
    }
}