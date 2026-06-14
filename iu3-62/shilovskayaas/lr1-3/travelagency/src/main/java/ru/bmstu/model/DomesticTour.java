package ru.bmstu.model;

import java.math.BigDecimal;

public class DomesticTour extends Tour {
    private String region;
    private boolean needVisa;
    private String transport;

    public DomesticTour(int id, String name, String description, BigDecimal basePrice,
                        String region, String transport) {
        super(id, name, description, basePrice);
        this.region = region;
        this.needVisa = false;
        this.transport = transport;
    }

    public String getRegion() { return region; }
    public boolean isNeedVisa() { return needVisa; }
    public String getTransport() { return transport; }

    public void setRegion(String region) { this.region = region; }
    public void setTransport(String transport) { this.transport = transport; }

    @Override
    public String getTourType() {
        return "Россия";
    }

    @Override
    public String getLocationInfo() {
        return String.format("Регион: %s, Транспорт: %s, Виза: не требуется",
                region, transport);
    }
}