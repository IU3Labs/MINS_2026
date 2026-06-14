package ru.bmstu;

import ru.bmstu.repository.*;
import ru.bmstu.service.*;
import ru.bmstu.service.discount.*;
import ru.bmstu.service.interfaces.*;
import ru.bmstu.ui.ConsoleUI;
import ru.bmstu.util.TestDataInitializer;

public class Main {
    public static void main(String[] args) {

        TourRepository tourRepository = new TourRepository();
        ClientRepository clientRepository = new ClientRepository();
        BookingRepository bookingRepository = new BookingRepository();

/*        DiscountStrategy saleStrategy = new SaleDiscountStrategy();
        DiscountStrategy personalStrategy = new PersonalDiscountStrategy();
        DiscountStrategy maxDiscountStrategy = new MaxDiscountStrategy(
                saleStrategy,
                personalStrategy
        );*/

        IAdminService adminService = new AdminService(
                tourRepository,
                clientRepository,
                bookingRepository
        );

        IBookingService bookingService = new BookingService(
                bookingRepository,
                tourRepository,
                clientRepository
                //maxDiscountStrategy
        );

        IClientAuthService clientAuthService = new ClientAuthService(clientRepository); // через интерфейс

        IPaymentService paymentService = new PaymentService(bookingRepository); // через интерфейс

        TestDataInitializer testDataInitializer = new TestDataInitializer(adminService, clientAuthService);
        testDataInitializer.initialize();

        ConsoleUI ui = new ConsoleUI(adminService, bookingService, clientAuthService, paymentService);
        ui.start();
    }
}