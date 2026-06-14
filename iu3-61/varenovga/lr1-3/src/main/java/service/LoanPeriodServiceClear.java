package main.java.service;

import java.time.LocalDate;
import java.util.Map;

/**
 * ✅ РЕФАКТОРИНГ: Устранены Magic Numbers, Hardcoded Strings, Copy-Paste.
 * Использованы Enums, Constants, Maps.
 */
public class LoanPeriodServiceClear {
    // ✅ Именованные константы вместо магических чисел
    private static final int DEFAULT_LOAN_DAYS = 14;
    private static final int STUDENT_LOAN_DAYS = 14;
    private static final int STUDENT_SESSION_LOAN_DAYS = 21;
    private static final int TEACHER_LOAN_DAYS = 30;
    private static final int VIP_LOAN_DAYS = 30;
    private static final int VIP_RARE_BOOK_DAYS = 90;

    // ✅ Массивы доменов (убраны лишние пробелы!)
    private static final String[] TEACHER_DOMAINS = {"@msu.ru", "@mgu.ru"};
    private static final String[] STUDENT_DOMAINS = {"@student.msu.ru"};
    private static final String STUDENT_KEYWORD = "student";
    private static final String VIP_DOMAIN = "@vip.library.ru";

    // ✅ Период сессии
    private static final LocalDate SESSION_START = LocalDate.of(2026, 6, 1);
    private static final LocalDate SESSION_END = LocalDate.of(2026, 6, 30);

    // ✅ Карта лимитов продления по категориям
    private static final Map<UserCategory, Integer> EXTENSION_LIMITS = Map.of(
            UserCategory.VIP, 90,
            UserCategory.TEACHER, 60,
            UserCategory.STUDENT, 30,
            UserCategory.REGULAR, 14,
            UserCategory.GUEST, 14
    );

    /**
     * Enum для категорий пользователей.
     * ✅ Замена магическим строкам.
     */
    public enum UserCategory {
        GUEST("Обычный"),
        REGULAR("Обычный"),
        STUDENT("Студент"),
        TEACHER("Преподаватель"),
        VIP("VIP");

        private final String displayName;

        UserCategory(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    /**
     * Enum для типов изданий.
     * ✅ Замена магическим строкам.
     */
    public enum PublicationType {
        REGULAR("Книга"),
        RARE("Редкая книга");

        private final String displayName;

        PublicationType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    /**
     * Рассчитать срок выдачи в днях.
     * ✅ Без магических чисел и строк.
     */
    public static int calculatePeriod(UserCategory category, PublicationType type) {
        return calculatePeriod(category, type, LocalDate.now());
    }

    /**
     * Рассчитать срок выдачи в днях с возможностью указания текущей даты.
     * ✅ Для тестируемости.
     */
    public static int calculatePeriod(UserCategory category, PublicationType type, LocalDate currentDate) {
        return switch (category) {
            case VIP -> calculateVipPeriod(type);
            case TEACHER -> TEACHER_LOAN_DAYS;
            case STUDENT -> calculateStudentPeriod(currentDate);
            case REGULAR, GUEST -> DEFAULT_LOAN_DAYS;
        };
    }

    private static int calculateVipPeriod(PublicationType type) {
        return (type == PublicationType.RARE) ? VIP_RARE_BOOK_DAYS : VIP_LOAN_DAYS;
    }

    private static int calculateStudentPeriod(LocalDate currentDate) {
        if (isSessionPeriod(currentDate)) {
            return STUDENT_SESSION_LOAN_DAYS;
        }
        return STUDENT_LOAN_DAYS;
    }

    /**
     * Проверить, попадает ли дата в период сессии.
     */
    private static boolean isSessionPeriod(LocalDate date) {
        return !date.isBefore(SESSION_START) && !date.isAfter(SESSION_END);
    }

    /**
     * Проверить, можно ли продлить выдачу.
     * ✅ Использует enum и карту лимитов.
     */
    public static boolean canExtend(UserCategory category, int currentLoanDays) {
        int limit = EXTENSION_LIMITS.getOrDefault(category, DEFAULT_LOAN_DAYS);
        return currentLoanDays < limit;
    }

    /**
     * Получить категорию пользователя по email.
     * ✅ Использует массивы доменов вместо хардкод условий.
     */
    public static UserCategory getUserCategory(String email) {
        if (email == null || email.isEmpty()) {
            return UserCategory.GUEST;
        }

        String lowerEmail = email.toLowerCase();

        if (matchesDomain(lowerEmail, VIP_DOMAIN)) {
            return UserCategory.VIP;
        }
        if (matchesAnyDomain(lowerEmail, TEACHER_DOMAINS)) {
            return UserCategory.TEACHER;
        }
        if (matchesAnyDomain(lowerEmail, STUDENT_DOMAINS) ||
                lowerEmail.contains(STUDENT_KEYWORD)) {
            return UserCategory.STUDENT;
        }

        return UserCategory.REGULAR;
    }

    private static boolean matchesDomain(String email, String domain) {
        return email.endsWith(domain);
    }

    private static boolean matchesAnyDomain(String email, String[] domains) {
        for (String domain : domains) {
            if (matchesDomain(email, domain)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Получить информацию о правилах.
     * ✅ Текст вынесен в отдельный метод для лёгкости изменения.
     */
    public static String getRulesInfo() {
        return String.format(
                "📋 ПРАВИЛА СРОКА ВЫДАЧИ:\n" +
                        "• Обычные читатели: %d дней\n" +
                        "• Студенты: %d дней (%d в период сессии)\n" +
                        "• Преподаватели: %d дней\n" +
                        "• VIP (исследователи): %d дней (%d для редких книг)\n" +
                        "⚠️  Правила могут меняться без уведомления",
                DEFAULT_LOAN_DAYS,
                STUDENT_LOAN_DAYS,
                STUDENT_SESSION_LOAN_DAYS,
                TEACHER_LOAN_DAYS,
                VIP_LOAN_DAYS,
                VIP_RARE_BOOK_DAYS
        );
    }
}