package ru.bmstu.model;

import java.math.BigDecimal;

public class ForeignTour extends Tour {
    private String country;
    private boolean needVisa;
    private String currency;

    public ForeignTour(int id, String name, String description, BigDecimal basePrice,
                       String country, boolean needVisa, String currency) {
        super(id, name, description, basePrice);
        this.country = country;
        this.needVisa = needVisa;
        this.currency = currency;
    }

    public String getCountry() { return country; }
    public boolean isNeedVisa() { return needVisa; }
    public String getCurrency() { return currency; }

    public void setCountry(String country) { this.country = country; }
    public void setNeedVisa(boolean needVisa) { this.needVisa = needVisa; }
    public void setCurrency(String currency) { this.currency = currency; }

    @Override
    public String getTourType() {
        return "Заграница";
    }

    @Override
    public String getLocationInfo() {
        return String.format("Страна: %s, Валюта: %s, Виза: %s",
                country, currency, needVisa ? "требуется" : "не требуется");
    }
}