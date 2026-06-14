package ru.bmstu.service;

import ru.bmstu.exception.TourAgencyException;
import ru.bmstu.grpc.ReferenceServiceClient;
import ru.bmstu.model.Booking;
import ru.bmstu.repository.BookingRepository;
import ru.bmstu.service.interfaces.IAdminService;
import ru.bmstu.util.Logger;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class AdminService implements IAdminService {
    private final ReferenceServiceClient refClient;
    private final BookingRepository bookingRepository;

    public AdminService(ReferenceServiceClient refClient,
                        BookingRepository bookingRepository) {
        this.refClient = refClient;
        this.bookingRepository = bookingRepository;
    }

    private String getTraceId() {
        return UUID.randomUUID().toString();
    }

    @Override
    public List<ReferenceServiceClient.TourInfo> getAllTours() {
        String traceId = getTraceId();
        Logger.log(traceId, "AdminService: запрос всех туров через gRPC");

        try {
            List<ReferenceServiceClient.TourInfo> tourInfos = refClient.getAllTours(traceId);
            Logger.log(traceId, "AdminService: получено " + tourInfos.size() + " туров");
            return tourInfos;

        } catch (Exception e) {
            Logger.log(traceId, "AdminService: ОШИБКА - Service B недоступен: " + e.getMessage());
            throw new TourAgencyException(
                    "Сервис справочников недоступен. Пожалуйста, убедитесь, что Service B запущен на порту 50051.");
        }
    }

    @Override
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }
}