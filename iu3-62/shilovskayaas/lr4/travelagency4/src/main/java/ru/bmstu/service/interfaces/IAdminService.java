package ru.bmstu.service.interfaces;

import ru.bmstu.grpc.ReferenceServiceClient;
import ru.bmstu.model.Booking;

import java.math.BigDecimal;
import java.util.List;

public interface IAdminService {
    List<ReferenceServiceClient.TourInfo> getAllTours();
    List<Booking> getAllBookings();
}
