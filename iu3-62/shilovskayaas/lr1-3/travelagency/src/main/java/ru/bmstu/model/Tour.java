package ru.bmstu.model;

import java.math.BigDecimal;

public abstract class Tour {
    private int id;
    private String name;
    private String description;
    private BigDecimal basePrice;
    private boolean isOnSale;
    private BigDecimal salePercentage;

    public Tour(int id, String name, String description, BigDecimal basePrice) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.basePrice = basePrice;
        this.isOnSale = false;
        this.salePercentage = BigDecimal.ZERO;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public BigDecimal getBasePrice() { return basePrice; }
    public boolean isOnSale() { return isOnSale; }
    public BigDecimal getSalePercentage() { return salePercentage; }

    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setBasePrice(BigDecimal basePrice) { this.basePrice = basePrice; }
    public void setOnSale(boolean onSale) { isOnSale = onSale; }
    public void setSalePercentage(BigDecimal salePercentage) { this.salePercentage = salePercentage; }

    public abstract String getTourType();
    public abstract String getLocationInfo();

    @Override
    public String toString() {
        return String.format("ID: %d | [%s] %s | %.2f руб. | %s | %s",
                id, getTourType(), name, basePrice,
                isOnSale ? "Акция " + salePercentage + "%" : "Нет акции",
                getLocationInfo());
    }
}