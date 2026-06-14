package badmodule;

import java.math.BigDecimal;

public class BadParkingSummary {
    private final BigDecimal baseAmount;
    private final BigDecimal penaltyAmount;
    private final BigDecimal specialTariffAmount;
    private final BigDecimal totalAmount;
    private final String category;
    private final String notes;

    public BadParkingSummary(BigDecimal baseAmount,
                             BigDecimal penaltyAmount,
                             BigDecimal specialTariffAmount,
                             BigDecimal totalAmount,
                             String category,
                             String notes) {
        this.baseAmount = baseAmount;
        this.penaltyAmount = penaltyAmount;
        this.specialTariffAmount = specialTariffAmount;
        this.totalAmount = totalAmount;
        this.category = category;
        this.notes = notes;
    }

    public BigDecimal getBaseAmount() {
        return baseAmount;
    }

    public BigDecimal getPenaltyAmount() {
        return penaltyAmount;
    }

    public BigDecimal getSpecialTariffAmount() {
        return specialTariffAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public String getCategory() {
        return category;
    }

    public String getNotes() {
        return notes;
    }
}
