package main.java.ui;

import java.util.Arrays;
import java.util.Optional;
public enum MenuAction {
    SHOW_CATALOG("1", "Показать каталог"),
    SEARCH_BY_TITLE("2", "Поиск по названию"),
    SEARCH_BY_AUTHOR("3", "Поиск по автору"),
    BORROW_PUBLICATION("4", "Выдать издание"),
    RETURN_PUBLICATION("5", "Вернуть издание"),
    OVERDUE_REPORT("6", "Отчёт по задолженностям"),
    ADD_PUBLICATION("7", "Добавить издание"),
    ADD_USER("8", "Добавить пользователя"),
    SHOW_USERS("9", "Список пользователей"),
    USER_HISTORY("10", "История пользователя"),
    ACTIVE_LOANS("11", "Активные выдачи"),
    TEST_OVERDUE("12", "Выдать с просрочкой"),
    DELETE_PUBLICATION("13", "Удалить издание"),
    DELETE_USER("14", "Удалить пользователя"),
    SYSTEM_INFO("15", "Информация о системе"),
    CHANGE_FINE_STRATEGY("16", "Сменить стратегию штрафов"),
    SHOW_FINE_CALCULATION("17", "Рассчитать штраф (тест)"),
    ADD_EMAIL_OBSERVER("18", "Добавить email-подписчика"),
    SEND_OVERDUE_NOTIFY("19", "Отправить уведомление о просрочке"),
    CALCULATE_LOAN_PERIOD("20", "Рассчитать срок выдачи"),
    CHECK_LOAN_EXTENSION("21", "Проверить возможность продления"),
    SHOW_USER_CATEGORY("22", "Показать категорию пользователя"),
    SHOW_HISTORY("23", "История операций"),
    EXIT("0", "Выход");

    private final String code;
    private final String description;

    MenuAction(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() { return code; }
    public String getDescription() { return description; }

    public static Optional<MenuAction> fromCode(String code) {
        return Arrays.stream(values())
                .filter(action -> action.code.equals(code))
                .findFirst();
    }

    public static void printMenu() {
        System.out.println("\n========== МЕНЮ ==========");
        System.out.println("📚 КАТАЛОГ:");
        System.out.println("   " + SHOW_CATALOG.code + ".  " + SHOW_CATALOG.description);
        System.out.println("   " + SEARCH_BY_TITLE.code + ".  " + SEARCH_BY_TITLE.description);
        System.out.println("   " + SEARCH_BY_AUTHOR.code + ".  " + SEARCH_BY_AUTHOR.description);
        System.out.println("📖 ВЫДАЧА:");
        System.out.println("   " + BORROW_PUBLICATION.code + ".  " + BORROW_PUBLICATION.description);
        System.out.println("   " + RETURN_PUBLICATION.code + ".  " + RETURN_PUBLICATION.description);
        System.out.println("   " + OVERDUE_REPORT.code + ".  " + OVERDUE_REPORT.description);
        System.out.println("   " + ACTIVE_LOANS.code + ".  " + ACTIVE_LOANS.description);
        System.out.println("➕ ДОБАВИТЬ:");
        System.out.println("   " + ADD_PUBLICATION.code + ".  " + ADD_PUBLICATION.description);
        System.out.println("   " + ADD_USER.code + ".  " + ADD_USER.description);
        System.out.println("🗑️  УДАЛИТЬ:");
        System.out.println("   " + DELETE_PUBLICATION.code + ".  " + DELETE_PUBLICATION.description);
        System.out.println("   " + DELETE_USER.code + ".  " + DELETE_USER.description);
        System.out.println("👥 ИНФО:");
        System.out.println("   " + SHOW_USERS.code + ".  " + SHOW_USERS.description);
        System.out.println("   " + USER_HISTORY.code + ".  " + USER_HISTORY.description);
        System.out.println("   " + SYSTEM_INFO.code + ".  " + SYSTEM_INFO.description);
        System.out.println("   " + TEST_OVERDUE.code + ".  " + TEST_OVERDUE.description);
        System.out.println("💰 ШТРАФЫ (Strategy):");
        System.out.println("   " + CHANGE_FINE_STRATEGY.code + ".  " + CHANGE_FINE_STRATEGY.description);
        System.out.println("   " + SHOW_FINE_CALCULATION.code + ".  " + SHOW_FINE_CALCULATION.description);
        System.out.println("🔔 УВЕДОМЛЕНИЯ (Observer):");
        System.out.println("   " + ADD_EMAIL_OBSERVER.code + ".  " + ADD_EMAIL_OBSERVER.description);
        System.out.println("   " + SEND_OVERDUE_NOTIFY.code + ".  " + SEND_OVERDUE_NOTIFY.description);
        System.out.println("📋 СРОКИ ВЫДАЧИ:");
        System.out.println("   " + CALCULATE_LOAN_PERIOD.code + ".  " + CALCULATE_LOAN_PERIOD.description);
        System.out.println("   " + CHECK_LOAN_EXTENSION.code + ".  " + CHECK_LOAN_EXTENSION.description);
        System.out.println("   " + SHOW_USER_CATEGORY.code + ".  " + SHOW_USER_CATEGORY.description);
        System.out.println("📋 КОМАНДЫ (Command):");
        System.out.println("   " + SHOW_HISTORY.code + ".  " + SHOW_HISTORY.description);
        System.out.println(EXIT.code + ".  " + EXIT.description);
        System.out.println("==========================");
    }
}