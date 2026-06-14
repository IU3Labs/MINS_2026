package ru.iu3;

import java.util.Scanner;

import ru.iu3.entity.Booking;
import ru.iu3.grpc.PassServiceClient;
import ru.iu3.grpc.RoomServiceClient;
import ru.iu3.repository.LBookingRepositorylmpl;
import ru.iu3.repository.interfaces.Repository;
import ru.iu3.service.BookingServiceImpl;
import ru.iu3.service.DefaultRoomFactory;
import ru.iu3.service.pricing.GreedyStrategy;
import ru.iu3.service.interfaces.BookingService;
import ru.iu3.service.interfaces.PassService;
import ru.iu3.service.interfaces.RoomFactory;
import ru.iu3.service.interfaces.RoomService;
import ru.iu3.ui.UI;
import ru.iu3.util.AppLogger;
import ru.iu3.util.FileAppLogger;
import ru.iu3.validation.BookingValidator;

public class CoreApp {
    private static final int PORT = 50052;
    private static final String LOG_FILE = "logs/app.log";

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        Repository<Booking, Integer> bookingRepository = new LBookingRepositorylmpl();

        RoomFactory roomFactory = new DefaultRoomFactory();
        AppLogger logger = new FileAppLogger(LOG_FILE);

        RoomService roomService = new RoomServiceClient("localhost", PORT, roomFactory, logger);
        PassService passService = new PassServiceClient("localhost", PORT, logger);

        BookingValidator bookingValidator = new BookingValidator();
        BookingService bookingService = new BookingServiceImpl(roomService, passService, bookingRepository,
                bookingValidator, new GreedyStrategy());

        UI consoleUI = new UI(scanner, roomService, passService, bookingService);
        consoleUI.start();
    }
}
