package service.pricing;

import entity.Ticket;

public class DiscountedPricing implements PricingStrategy {
    public static final String STRATEGY_ID = "discounted";
    private static final double DISCOUNT = 0.5;

    @Override
    public String getStrategyId() {
        return STRATEGY_ID;
    }

    @Override
    public double calculatePrice(Ticket ticket) {
        return ticket.getPrice() * (1 - DISCOUNT);
    }

    @Override
    public String getCategoryName() {
        return "Студент/Пенсионер";
    }
}