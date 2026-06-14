package com.bank.core.reference;

public record InterestRule(double effectivePercent,
                           double bonusAmount,
                           String strategyName) {
}
