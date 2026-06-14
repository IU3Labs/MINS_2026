package ru.bmstu.service.payment;

import java.math.BigDecimal;
import java.util.Scanner;

public class OfflinePayment implements CardPayment, CashPayment {
    private BigDecimal cashReceived = BigDecimal.ZERO;
    private final Scanner scanner;
    public OfflinePayment() {
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void payByCard(BigDecimal amount, String cardNumber, String expiryDate, String cvv) {
        System.out.println("\nОПЛАТА КАРТОЙ В ОФИСЕ");
        System.out.println("Сумма: " + amount + " руб.");
        System.out.println("Карта: ****" + cardNumber.substring(cardNumber.length() - 4));
        System.out.println("Терминал: оплата принята");
        System.out.println("Оплата прошла успешно!");
    }

    @Override
    public void refundToCard(BigDecimal amount, String cardNumber) {
        System.out.println("\nВОЗВРАТ НА КАРТУ В ОФИСЕ");
        System.out.println("Сумма возврата: " + amount + " руб.");
        System.out.println("Карта: ****" + cardNumber.substring(cardNumber.length() - 4));
        System.out.println("Возврат выполнен успешно!");
    }

    @Override
    public void payByCash(BigDecimal amount) {
        System.out.println("\nОПЛАТА НАЛИЧНЫМИ В ОФИСЕ");
        System.out.println("Сумма к оплате: " + amount + " руб.");
        System.out.print("Внесите сумму: ");

        String input = scanner.nextLine();

        try {
            cashReceived = new BigDecimal(input);

            if (cashReceived.compareTo(amount) < 0) {
                System.out.println("Ошибка: Внесенная сумма (" + cashReceived + " руб.) меньше суммы к оплате (" + amount + " руб.)");
                System.out.println("Пожалуйста, внесите недостающую сумму.");
                return;
            }

            System.out.println("Внесено: " + cashReceived + " руб.");

            giveChange(amount);

            System.out.println("Оплата наличными прошла успешно");

        } catch (NumberFormatException e) {
            System.out.println("Ошибка: Введите корректную сумму (число)");
        }
    }

    @Override
    public void giveChange(BigDecimal amount) {
        if (cashReceived.compareTo(amount) > 0) {
            BigDecimal change = cashReceived.subtract(amount);
            System.out.println("Ваша сдача: " + change + " руб.");
        }
        cashReceived = BigDecimal.ZERO;
    }
}
