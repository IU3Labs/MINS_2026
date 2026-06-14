package ru.bmstu.travel.core.service;

import java.util.List;

public record EstimateResult(
        String headline,
        double total,
        double prepayment,
        double monthlyPayment,
        List<String> explanation
) {
}
