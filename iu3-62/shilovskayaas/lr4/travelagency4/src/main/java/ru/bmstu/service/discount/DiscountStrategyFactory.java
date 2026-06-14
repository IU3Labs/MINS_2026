package ru.bmstu.service.discount;

import java.util.HashMap;
import java.util.Map;

public class DiscountStrategyFactory {
    private static final Map<String, DiscountStrategy> strategies = new HashMap<>();

    static {
        strategies.put("акция", new SaleDiscountStrategy());
        strategies.put("персональная", new PersonalDiscountStrategy());
        strategies.put("нет", new NoDiscountStrategy());
    }

    public static DiscountStrategy getStrategy(String name) {
        DiscountStrategy strategy = strategies.get(name.toLowerCase());
        if (strategy == null) {
            return new NoDiscountStrategy();
        }
        return strategy;
    }

    public static String getAvailableStrategies() {
        return String.join(", ", strategies.keySet());
    }
}