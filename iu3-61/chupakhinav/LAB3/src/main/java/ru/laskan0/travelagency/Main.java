package ru.laskan0.travelagency;

import java.util.List;
import java.util.Scanner;

import ru.laskan0.travelagency.factory.DefaultBookingFactory;
import ru.laskan0.travelagency.factory.DefaultClientFactory;
import ru.laskan0.travelagency.factory.DefaultTourFactory;
import ru.laskan0.travelagency.antipattern.ExpressTravelCalculatorGodClass;
import ru.laskan0.travelagency.repository.BookingRepository;
import ru.laskan0.travelagency.repository.ClientRepository;
import ru.laskan0.travelagency.repository.TourRepository;
import ru.laskan0.travelagency.repository.inmemory.InMemoryBookingRepository;
import ru.laskan0.travelagency.repository.inmemory.InMemoryClientRepository;
import ru.laskan0.travelagency.repository.inmemory.InMemoryTourRepository;
import ru.laskan0.travelagency.service.BookingService;
import ru.laskan0.travelagency.service.ClientService;
import ru.laskan0.travelagency.service.PricingService;
import ru.laskan0.travelagency.service.TourService;
import ru.laskan0.travelagency.service.observer.TravelAgencyHistoryObserver;
import ru.laskan0.travelagency.service.pricing.PercentageDiscountPolicy;
import ru.laskan0.travelagency.service.pricing.SeasonalDiscountPolicy;
import ru.laskan0.travelagency.service.pricing.VipDiscountPolicy;
import ru.laskan0.travelagency.ui.ConsoleUi;

public class Main {
    public static void main(String[] args) {
        ClientRepository clientRepository = new InMemoryClientRepository();
        TourRepository tourRepository = new InMemoryTourRepository();
        BookingRepository bookingRepository = new InMemoryBookingRepository();

        TravelAgencyHistoryObserver historyObserver = new TravelAgencyHistoryObserver();

        ClientService clientService = new ClientService(
                clientRepository,
                new DefaultClientFactory(),
                List.of(historyObserver));
        TourService tourService = new TourService(
                tourRepository,
                new DefaultTourFactory(),
                List.of(historyObserver));
        PricingService pricingService = new PricingService(List.of(
                new PercentageDiscountPolicy(),
                new VipDiscountPolicy(),
                new SeasonalDiscountPolicy()
        ));

        BookingService bookingService = new BookingService(
                bookingRepository,
                clientService,
                tourService,
                pricingService,
                new DefaultBookingFactory(),
                List.of(historyObserver)
        );

        Scanner scanner = new Scanner(System.in);

        ConsoleUi consoleUi = new ConsoleUi(
                clientService,
                tourService,
                bookingService,
                historyObserver,
                new ExpressTravelCalculatorGodClass(clientService, tourService, scanner),
                scanner);
        consoleUi.start();
    }
}
