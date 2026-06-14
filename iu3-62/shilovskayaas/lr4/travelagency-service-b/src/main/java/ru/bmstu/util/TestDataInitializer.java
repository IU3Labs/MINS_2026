package ru.bmstu.util;

import ru.bmstu.model.*;
import ru.bmstu.repository.*;

import java.math.BigDecimal;

public class TestDataInitializer {

    private final TourRepository tourRepository;
    private final ClientRepository clientRepository;

    public TestDataInitializer(TourRepository tourRepository, ClientRepository clientRepository) {
        this.tourRepository = tourRepository;
        this.clientRepository = clientRepository;
    }

    public void initialize() {
        addTours();
        addTestClient();
        applyDiscounts();
        printInfo();
    }

    private void addTours() {
        DomesticTour tour1 = new DomesticTour(0, "Золотое кольцо", "поездка по русским городам",
                new BigDecimal("35000"), "Центральная Россия", "автобус");
        tourRepository.save(tour1);

        DomesticTour tour2 = new DomesticTour(0, "Сочи", "отдых на Черном море",
                new BigDecimal("45000"), "Краснодарский край", "самолет");
        tourRepository.save(tour2);

        ForeignTour tour3 = new ForeignTour(0, "Париж", "5 дней в Париже",
                new BigDecimal("75000"), "Франция", true, "евро");
        tourRepository.save(tour3);

        ForeignTour tour4 = new ForeignTour(0, "Турция", "Все включено",
                new BigDecimal("60000"), "Турция", false, "турецкая лира");
        tourRepository.save(tour4);
    }

    private void addTestClient() {
        Client client = new Client(0, "test", "test@test.com", "test123");
        client.setPersonalDiscount(new BigDecimal("10"));
        clientRepository.save(client);
    }

    private void applyDiscounts() {
        // Находим тур "Сочи" (ID будет присвоен при сохранении)
        for (Tour tour : tourRepository.findAll()) {
            if (tour.getName().equals("Сочи")) {
                tour.setOnSale(true);
                tour.setSalePercentage(new BigDecimal("15"));
                tourRepository.save(tour);
                break;
            }
        }
    }

    private void printInfo() {
        System.out.println("Service B: Тестовые данные загружены");
    }
}