package promo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

//Антипаттерн: Hardcoded Dependencies + Magic Numbers + Swallowing Exceptions

public class QuickPromoCalculator {

    // Жестко заданные зависимости и магические числа
    private static final double BASE_RATE = 150.0;
    private static final double PROMO_MULTIPLIER = 0.75;
    private static final int NIGHT_START_HOUR = 22;
    private static final int NIGHT_END_HOUR = 6;
    private static final double NIGHT_MULTIPLIER = 1.3;

    public void calculateAndShow() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n=== БЫСТРЫЙ КАЛЬКУЛЯТОР АКЦИИ ===");

        try {
            System.out.print("Въезд (ГГГГ-ММ-ДД ЧЧ:ММ): ");
            String entryInput = scanner.nextLine();
            System.out.print("Выезд (ГГГГ-ММ-ДД ЧЧ:ММ): ");
            String exitInput = scanner.nextLine();

            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime entry = LocalDateTime.parse(entryInput, fmt);
            LocalDateTime exit = LocalDateTime.parse(exitInput, fmt);

            long minutes = java.time.Duration.between(entry, exit).toMinutes();
            if (minutes < 0) {
                System.out.println("Ошибка: время выезда раньше въезда.");
                return;
            }

            // Расчет по жестко зашитой логике
            double cost = (minutes / 60.0) * BASE_RATE;

            if (exit.getHour() >= NIGHT_START_HOUR || exit.getHour() < NIGHT_END_HOUR) {
                cost *= NIGHT_MULTIPLIER;
            }

            cost *= PROMO_MULTIPLIER;

            System.out.printf("Примерная стоимость: %.2f руб.\n", cost);
        } catch (Exception e) {
            // Swallowing Exceptions: исключение просто игнорируется
            // Пользователь не видит, что пошло не так
        }
    }
}