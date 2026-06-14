package com.cinema.reference.service.pricing;

public enum PricingCategory {
    STANDARD("standard", "Взрослый"),
    CHILD("child", "Ребёнок"),
    DISCOUNTED("discounted", "Студент/Пенсионер");

    private final String strategyId;
    private final String displayName;

    PricingCategory(String strategyId, String displayName) {
        this.strategyId = strategyId;
        this.displayName = displayName;
    }

    public String getStrategyId() {
        return strategyId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static PricingCategory fromStrategyId(String strategyId) {
        for (PricingCategory category : values()) {
            if (category.strategyId.equals(strategyId)) {
                return category;
            }
        }
        throw new IllegalArgumentException("Unknown strategy: " + strategyId);
    }
}