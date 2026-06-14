package ru.bmstu.service.payment;

import java.math.BigDecimal;

public class OnlinePayment implements CardPayment {

    @Override
    public void payByCard(BigDecimal amount, String cardNumber, String expiryDate, String cvv) {
        System.out.println("\nОПЛАТА КАРТОЙ ОНЛАЙН");
        System.out.println("Сумма: " + amount + " руб.");
        System.out.println("Карта: ****" + cardNumber.substring(cardNumber.length() - 4));
        System.out.println("Срок действия: " + expiryDate);
        System.out.println("Оплата прошла успешно");
    }

    @Override
    public void refundToCard(BigDecimal amount, String cardNumber) {
        System.out.println("\nВОЗВРАТ НА КАРТУ");
        System.out.println("Сумма возврата: " + amount + " руб.");
        System.out.println("Карта: ****" + cardNumber.substring(cardNumber.length() - 4));
        System.out.println("Возврат выполнен успешно");
    }
}
