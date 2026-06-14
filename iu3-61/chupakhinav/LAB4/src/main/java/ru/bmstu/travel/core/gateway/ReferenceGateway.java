package ru.bmstu.travel.core.gateway;

import ru.bmstu.travel.core.catalog.CatalogClient;
import ru.bmstu.travel.core.catalog.CatalogTour;
import ru.bmstu.travel.core.catalog.CatalogDiscount;
import ru.bmstu.travel.core.catalog.ClientOperationResult;
import ru.bmstu.travel.core.catalog.TourOperationResult;

import java.util.List;

public interface ReferenceGateway {
    List<CatalogClient> listClients(String traceId);
    List<CatalogTour> listTours(String traceId);
    CatalogClient getClient(String clientId, String traceId);
    CatalogTour getTour(String tourId, String traceId);
    ClientOperationResult addClient(String fullName, String phone, CatalogDiscount discount, String traceId);
    TourOperationResult addTour(String title, String destination, double basePrice, int capacity, String traceId);
    TourOperationResult reserveTourSeat(String tourId, String traceId);
    TourOperationResult releaseTourSeat(String tourId, String traceId);
}
