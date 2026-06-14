package com.university.service;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class DiscountConfig {

    private final Properties properties;

    public DiscountConfig() {
        this.properties = new Properties();
        loadProperties();
    }

    private void loadProperties() {
        try (InputStream input = getClass().getClassLoader()
                .getResourceAsStream("config/discount-rules.properties")) {

            if (input == null) {
                System.out.println("Файл discount-rules.properties не найден, используются значения по умолчанию");
                setDefaultProperties();
                return;
            }

            properties.load(input);
            System.out.println("Правила скидок загружены из конфигурации");

        } catch (IOException e) {
            System.out.println("Ошибка чтения конфигурации: " + e.getMessage());
            setDefaultProperties();
        }
    }

    private void setDefaultProperties() {
        properties.setProperty("discount.excellent.threshold", "4.5");
        properties.setProperty("discount.good.threshold", "4.0");
        properties.setProperty("discount.excellent.percent", "15");
        properties.setProperty("discount.good.percent", "10");
        properties.setProperty("discount.min.grades.count", "3");
        properties.setProperty("discount.excellent.promo", "EXCELLENT");
        properties.setProperty("discount.good.promo", "GOOD");
        properties.setProperty("discount.no.discount.promo", "NO_DISCOUNT");
    }

    public double getExcellentThreshold() {
        return Double.parseDouble(properties.getProperty("discount.excellent.threshold"));
    }

    public double getGoodThreshold() {
        return Double.parseDouble(properties.getProperty("discount.good.threshold"));
    }

    public int getExcellentDiscountPercent() {
        return Integer.parseInt(properties.getProperty("discount.excellent.percent"));
    }

    public int getGoodDiscountPercent() {
        return Integer.parseInt(properties.getProperty("discount.good.percent"));
    }

    public int getMinGradesCount() {
        return Integer.parseInt(properties.getProperty("discount.min.grades.count"));
    }

    public String getExcellentPromo() {
        return properties.getProperty("discount.excellent.promo");
    }

    public String getGoodPromo() {
        return properties.getProperty("discount.good.promo");
    }

    public String getNoDiscountPromo() {
        return properties.getProperty("discount.no.discount.promo");
    }
}
