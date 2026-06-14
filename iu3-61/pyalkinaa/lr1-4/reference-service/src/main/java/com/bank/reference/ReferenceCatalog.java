package com.bank.reference;

import java.util.Locale;
import java.util.Map;

public class ReferenceCatalog {
    private static final Map<String, Rule> RULES = Map.of(
            "STANDARD", new Rule("STANDARD", 0.0, 0.0, 0.0, "StandardInterestRule"),
            "PREMIUM", new Rule("PREMIUM", 2.0, 0.0, 0.0, "PremiumInterestRule"),
            "SAVINGS", new Rule("SAVINGS", 1.0, 10_000.0, 150.0, "SavingsInterestRule")
    );

    public boolean isValidAccountType(String type) {
        return normalize(type) != null;
    }

    public String normalize(String type) {
        if (type == null) {
            return null;
        }
        String normalized = type.trim().toUpperCase(Locale.ROOT);
        return RULES.containsKey(normalized) ? normalized : null;
    }

    public Rule getRule(String type) {
        String normalized = normalize(type);
        return normalized == null ? null : RULES.get(normalized);
    }

    public record Rule(String accountType,
                       double extraPercent,
                       double bonusThreshold,
                       double bonusAmount,
                       String strategyName) {
    }
}
