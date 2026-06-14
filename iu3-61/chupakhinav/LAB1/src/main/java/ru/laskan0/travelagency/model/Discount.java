package ru.laskan0.travelagency.model;

import ru.laskan0.travelagency.exception.InvalidDataException;

public class Discount {
    private final String name;
    private final int percent;
    private final DiscountType type;

    public Discount(String name, int percent, DiscountType type) {
        if (name == null || name.isBlank()) {
            throw new InvalidDataException("Название скидки не может быть пустым");
        }
        if (percent < 0 || percent > 100) {
            throw new InvalidDataException("Процент скидки должен быть в диапазоне от 0 до 100");
        }
        if (type == null) {
            throw new InvalidDataException("Тип скидки не может быть null");
        }

        this.name = name.trim();
        this.percent = percent;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public int getPercent() {
        return percent;
    }

    public DiscountType getType() {
        return type;
    }

    @Override
    public String toString() {
        return name + " [" + type + "] (" + percent + "%)";
    }
}
