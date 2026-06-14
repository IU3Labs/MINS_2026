package service.pricing;

public class ChildPricing implements PricingStrategy {
    private static final double MULTIPLIER = 2.0;

    @Override
    public PricingCategory getCategory() {
        return PricingCategory.CHILD;
    }

    @Override
    public double calculatePrice(double basePrice) {
        return basePrice * MULTIPLIER;
    }
}