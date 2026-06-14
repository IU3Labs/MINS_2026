package service.pricing;

public class DiscountedPricing implements PricingStrategy {
    private static final double DISCOUNT = 0.5;

    @Override
    public PricingCategory getCategory() {
        return PricingCategory.DISCOUNTED;
    }

    @Override
    public double calculatePrice(double basePrice) {
        return basePrice * (1 - DISCOUNT);
    }
}