package service.pricing;

public class StandardPricing implements PricingStrategy {
    @Override
    public PricingCategory getCategory() {
        return PricingCategory.STANDARD;
    }

    @Override
    public double calculatePrice(double basePrice) {
        return basePrice;
    }
}