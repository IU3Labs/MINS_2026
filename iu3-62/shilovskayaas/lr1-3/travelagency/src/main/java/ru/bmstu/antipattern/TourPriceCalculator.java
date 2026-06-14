package ru.bmstu.antipattern;

import ru.bmstu.model.Tour;
import ru.bmstu.model.DomesticTour;
import ru.bmstu.model.ForeignTour;

import java.math.BigDecimal;

public class TourPriceCalculator {

    public BigDecimal calculateApproximatePrice(Tour tour) {
        if (tour == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal price = tour.getBasePrice();

        // если название короче 7 скидка 7%
        if (tour.getName().length() < 7) {
            price = price.multiply(BigDecimal.valueOf(0.93));
        }

        if (tour instanceof ForeignTour) {
            // наценка 20%
            price = price.multiply(BigDecimal.valueOf(1.2));
        } else if (tour instanceof DomesticTour) {
            // скидка 10%
            price = price.multiply(BigDecimal.valueOf(0.9));
        }

        return price.setScale(2, BigDecimal.ROUND_HALF_UP);
    }


    public BigDecimal calculateApproximatePrice(Tour tour, int month) {
        BigDecimal price = calculateApproximatePrice(tour);

        if (month == 6 || month == 7 || month == 8) { //лето
            price = price.multiply(BigDecimal.valueOf(1.5));
        } else if (month == 12 || month == 1 || month == 2) { //зима
            price = price.multiply(BigDecimal.valueOf(0.8));
        } else if (month == 3 || month == 4 || month == 5) { //весна
            price = price.multiply(BigDecimal.valueOf(1.1));
        } else if (month == 9 || month == 10 || month == 11) { //осень
            price = price.multiply(BigDecimal.valueOf(0.8));
        }

        return price.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal calculateApproximatePrice(Tour tour, int persons, int month) {
        BigDecimal price = calculateApproximatePrice(tour, month);

        price = price.multiply(BigDecimal.valueOf(persons));

        if (persons % 2 == 0) {
            price = price.multiply(BigDecimal.valueOf(0.95));
        }

        if (persons == month) {
            price = price.multiply(BigDecimal.valueOf(1.5));
        }

        return price.setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}
