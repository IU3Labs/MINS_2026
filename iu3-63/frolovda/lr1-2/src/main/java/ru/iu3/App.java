package ru.iu3;

import java.util.Scanner;

import ru.iu3.entity.Booking;
import ru.iu3.entity.Pass;
import ru.iu3.entity.interfaces.Room;
import ru.iu3.repository.LBookingRepositorylmpl;
import ru.iu3.repository.LPassRepositoryImpl;
import ru.iu3.repository.LRoomRepositoryImpl;
import ru.iu3.repository.interfaces.Repository;
import ru.iu3.service.BookingServiceImpl;
import ru.iu3.service.DefaultRoomFactory;
import ru.iu3.service.PassServiceImpl;
import ru.iu3.service.RoomServiceImpl;
import ru.iu3.service.pricing.GreedyStrategy;
import ru.iu3.service.interfaces.BookingService;
import ru.iu3.service.interfaces.PassService;
import ru.iu3.service.interfaces.RoomFactory;
import ru.iu3.service.interfaces.RoomService;
import ru.iu3.ui.ConsoleUI;
import ru.iu3.validation.BookingValidator;
import ru.iu3.validation.PassValidator;
import ru.iu3.validation.RoomValidator;

public class App {

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        Repository<Room, Integer> roomRepository = new LRoomRepositoryImpl();
        Repository<Pass, Integer> passRepository = new LPassRepositoryImpl();
        Repository<Booking, Integer> bookingRepository = new LBookingRepositorylmpl();

        RoomFactory roomFactory = new DefaultRoomFactory();
        RoomValidator roomValidator = new RoomValidator(roomRepository);
        RoomService roomService = new RoomServiceImpl(roomRepository, roomFactory, roomValidator);
        PassValidator passValidator = new PassValidator(passRepository);
        PassService passService = new PassServiceImpl(passRepository, passValidator);
        BookingValidator bookingValidator = new BookingValidator();
        BookingService bookingService = new BookingServiceImpl(roomService, passService, bookingRepository,
                bookingValidator, new GreedyStrategy());

        ConsoleUI consoleUI = new ConsoleUI(scanner, roomService, passService, bookingService);
        consoleUI.start();
    }
}
