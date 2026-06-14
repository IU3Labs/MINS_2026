package main.java.service;

import java.time.LocalDate;

/**
 * ⚠️  НАМЕРЕННОЕ НАРУШЕНИЕ: Magic Numbers + Hardcoded Dependencies + Copy-Paste
 * Изолированный сервис для расчёта сроков выдачи по категориям пользователей.
 * Антипаттерны локализованы здесь — не влияют на основной код.
 */
public class LoanPeriodService {
    /**
     * Рассчитать срок выдачи в днях.
     * ❌ Антипаттерн: магические числа, жёсткие строки
     */
    public static int calculatePeriod(String userCategory, String publicationType) {
        if (userCategory.equals("VIP")) {
            return 30; // ❌ MAGIC NUMBER
        } else if (userCategory.equals("Преподаватель")) {
            return 30;
        } else if (userCategory.equals("Студент")) {
            LocalDate today = LocalDate.now();
            LocalDate sessionStart = LocalDate.parse("2026-06-01");
            LocalDate sessionEnd = LocalDate.parse("2026-06-30");
            if (!today.isBefore(sessionStart) && !today.isAfter(sessionEnd)) {
                return 21;
            }
            return 14;
        }
        return 14; // ❌ Default magic number
    }

    /**
     * ❌ Антипаттерн: магические числа, сравнение строк
     */
    public static boolean canExtend(String userCategory, int currentLoanDays) {
        if (userCategory.equals("VIP")) {
            return currentLoanDays < 90;
        } else if (userCategory.equals("Преподаватель")) {
            return currentLoanDays < 60;
        } else if (userCategory.equals("Студент")) {
            return currentLoanDays < 30;
        }
        return currentLoanDays < 14;
    }

    /**
     * Получить категорию пользователя по email.
     * ❌ Антипаттерн: хардкод доменов
     */
    public static String getUserCategory(String email) {
        if (email == null) return "Обычный";
        if (email.endsWith("@msu.ru") || email.endsWith("@mgu.ru")) {
            return "Преподаватель";
        } else if (email.endsWith("@student.msu.ru") || email.contains("student")) {
            return "Студент";
        } else if (email.endsWith("@vip.library.ru")) {
            return "VIP";
        }
        return "Обычный";
    }

    /**
     * Получить информацию о правилах.
     * ❌ Антипаттерн: хардкод текста
     */
    public static String getRulesInfo() {
        return "📋 ПРАВИЛА СРОКА ВЫДАЧИ:\n" +
                "• Обычные читатели: 14 дней\n" +
                "• Студенты: 14 дней \n" +
                "• Преподаватели: 30 дней\n" +
                "• VIP (исследователи): 30 дней \n" +
                "️  Правила могут меняться без уведомления";
    }
}