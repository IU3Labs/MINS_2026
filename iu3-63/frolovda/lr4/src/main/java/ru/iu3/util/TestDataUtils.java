package ru.iu3.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import ru.iu3.enums.RoomEnum;
import ru.iu3.ui.constants.UiConstants;
import ru.iu3.service.interfaces.BookingService;
import ru.iu3.service.interfaces.PassService;
import ru.iu3.service.interfaces.RoomService;

public class TestDataUtils {
    private RoomService roomService;
    private PassService passService;
    private BookingService bookingService;

    public static void generateDemoData(RoomService roomService, PassService passService,
            BookingService bookingService) {
        new TestDataUtils(roomService, passService, bookingService).generateDemoData();
    }

    public TestDataUtils(RoomService roomService, PassService passService, BookingService bookingService) {
        this.roomService = roomService;
        this.passService = passService;
        this.bookingService = bookingService;
    }

    public void generateDemoData() {
        roomService.addRoom(RoomEnum.MEETING_ROOM, 1, "Большая переговорная", 500);
        roomService.addRoom(RoomEnum.WORKPLACE, 2, "Рабочее место 1", 200);

        passService.issuePass(1, "Иван Иванов");
        passService.issuePass(2, "Пётр Петров");

        LocalDate today = LocalDate.now();
        LocalDateTime start1 = LocalDateTime.of(today, LocalTime.of(10, 0));
        LocalDateTime end1 = LocalDateTime.of(today, LocalTime.of(12, 0));
        bookingService.createBooking(1, 1, start1, end1);

        LocalDateTime start2 = LocalDateTime.of(today, LocalTime.of(13, 0));
        LocalDateTime end2 = LocalDateTime.of(today, LocalTime.of(15, 0));
        bookingService.createBooking(2, 2, start2, end2);

        System.out.println(UiConstants.TEST_DATA_SUCCESS);
    }
}
