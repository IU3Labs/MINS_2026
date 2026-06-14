package ru.bmstu.util;

import ru.bmstu.model.Client;
import ru.bmstu.model.Tour;
import ru.bmstu.service.AdminService;
import ru.bmstu.service.ClientAuthService;
import ru.bmstu.service.interfaces.*;

import java.math.BigDecimal;

public class TestDataInitializer {

    private final IAdminService adminService;
    private final IClientAuthService  clientAuthService;

    public TestDataInitializer(IAdminService adminService, IClientAuthService clientAuthService) {
        this.adminService = adminService;
        this.clientAuthService = clientAuthService;
    }

    public void initialize() {
        try {
            addTours();
            addTestClient();
            applyDiscounts();
            printInfo();
        } catch (Exception e) {
            System.out.println("Ошибка при добавлении тестовых данных: " + e.getMessage());
        }
    }

    private void addTours() {
        Tour tour1 = adminService.addDomesticTour(
                "Золотое кольцо",
                "поездка по русским городам",
                new BigDecimal("35000"),
                "Центральная Россия",
                "автобус"
        );

        Tour tour2 = adminService.addDomesticTour(
                "Сочи",
                "отдых на Черном море",
                new BigDecimal("45000"),
                "Краснодарский край",
                "самолет"
        );

        Tour tour3 = adminService.addForeignTour(
                "Париж",
                "5 дней в Париже",
                new BigDecimal("75000"),
                "Франция",
                true,
                "евро"
        );

        Tour tour4 = adminService.addForeignTour(
                "Турция",
                "Все включено",
                new BigDecimal("60000"),
                "Турция",
                false,
                "турецкая лира"
        );
    }

    private void addTestClient() {
        try {
            Client testClient = clientAuthService.register(
                    "test",
                    "test@test.com",
                    "test123"
            );
            adminService.setPersonalDiscount(testClient.getId(), new BigDecimal("10"));
        } catch (Exception e) {
            // если существует игнор
        }
    }

    private void applyDiscounts() throws Exception {
        adminService.setTourSale(2, new BigDecimal("15"));
    }

    private void printInfo() {
        System.out.println("Добавлены тестовые туры");
        System.out.println("1 Золотое кольцо");
        System.out.println("2 Сочи (-15%)");
        System.out.println("3 Париж");
        System.out.println("4 Турция");
        System.out.println("Добавлен тестовый клиент: test@test.com / test123 (скидка 10%)");
    }
}