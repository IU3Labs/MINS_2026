package ru.bmstu.server;

import io.grpc.stub.StreamObserver;
import ru.bmstu.exception.TourNotFoundException;
import ru.bmstu.grpc.*;
import ru.bmstu.model.Client;
import ru.bmstu.model.Tour;
import ru.bmstu.repository.ClientRepository;
import ru.bmstu.repository.TourRepository;
import ru.bmstu.util.Logger;

public class ReferenceServiceImpl extends ReferenceServiceGrpc.ReferenceServiceImplBase {

    private final TourRepository tourRepository;
    private final ClientRepository clientRepository;

    public ReferenceServiceImpl(TourRepository tourRepository, ClientRepository clientRepository) {
        this.tourRepository = tourRepository;
        this.clientRepository = clientRepository;
    }

    @Override
    public void getTour(GetTourRequest request, StreamObserver<TourResponse> responseObserver) {
        String traceId = "service-b-" + System.currentTimeMillis();
        Logger.log(traceId, "Service B: getTour вызван, tourId=" + request.getTourId());

        try {
            Tour tour = tourRepository.findById(request.getTourId())
                    .orElseThrow(() -> new TourNotFoundException("Тур не найден: " + request.getTourId()));

            TourResponse response = mapToTourResponse(tour);
            Logger.log(traceId, "Service B: тур найден, name=" + tour.getName());
            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (TourNotFoundException e) {
            Logger.log(traceId, "Service B: ошибка - " + e.getMessage());
            responseObserver.onError(io.grpc.Status.NOT_FOUND
                    .withDescription(e.getMessage())
                    .asRuntimeException());
        } catch (Exception e) {
            Logger.log(traceId, "Service B: внутренняя ошибка - " + e.getMessage());
            responseObserver.onError(io.grpc.Status.INTERNAL
                    .withDescription("Внутренняя ошибка сервера")
                    .asRuntimeException());
        }
    }

    @Override
    public void getClient(GetClientRequest request, StreamObserver<ClientResponse> responseObserver) {
        String traceId = "service-b-" + System.currentTimeMillis();
        Logger.log(traceId, "Service B: getClient вызван, clientId=" + request.getClientId());

        try {
            Client client = clientRepository.findById(request.getClientId())
                    .orElseThrow(() -> new RuntimeException("Клиент не найден: " + request.getClientId()));

            ClientResponse response = mapToClientResponse(client);
            Logger.log(traceId, "Service B: клиент найден, email=" + client.getEmail());
            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception e) {
            Logger.log(traceId, "Service B: ошибка - " + e.getMessage());
            responseObserver.onError(io.grpc.Status.NOT_FOUND
                    .withDescription(e.getMessage())
                    .asRuntimeException());
        }
    }

    @Override
    public void getClientByEmail(GetClientByEmailRequest request, StreamObserver<ClientResponse> responseObserver) {
        String traceId = "service-b-" + System.currentTimeMillis();
        Logger.log(traceId, "Service B: getClientByEmail вызван, email=" + request.getEmail());

        try {
            Client client = clientRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("Клиент не найден: " + request.getEmail()));

            ClientResponse response = mapToClientResponse(client);
            Logger.log(traceId, "Service B: клиент найден, id=" + client.getId());
            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception e) {
            Logger.log(traceId, "Service B: ошибка - " + e.getMessage());
            responseObserver.onError(io.grpc.Status.NOT_FOUND
                    .withDescription(e.getMessage())
                    .asRuntimeException());
        }
    }

    @Override
    public void checkClientExists(CheckClientExistsRequest request, StreamObserver<ExistsResponse> responseObserver) {
        String traceId = "service-b-" + System.currentTimeMillis();
        Logger.log(traceId, "Service B: checkClientExists, clientId=" + request.getClientId());

        boolean exists = clientRepository.findById(request.getClientId()).isPresent();
        ExistsResponse response = ExistsResponse.newBuilder().setExists(exists).build();

        Logger.log(traceId, "Service B: клиент существует = " + exists);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void checkTourExists(CheckTourExistsRequest request, StreamObserver<ExistsResponse> responseObserver) {
        String traceId = "service-b-" + System.currentTimeMillis();
        Logger.log(traceId, "Service B: checkTourExists, tourId=" + request.getTourId());

        boolean exists = tourRepository.findById(request.getTourId()).isPresent();
        ExistsResponse response = ExistsResponse.newBuilder().setExists(exists).build();

        Logger.log(traceId, "Service B: тур существует = " + exists);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getAllTours(ru.bmstu.grpc.Empty request, StreamObserver<TourListResponse> responseObserver) {
        String traceId = "service-b-" + System.currentTimeMillis();
        Logger.log(traceId, "Service B: getAllTours вызван");

        TourListResponse.Builder responseBuilder = TourListResponse.newBuilder();
        for (Tour tour : tourRepository.findAll()) {
            responseBuilder.addTours(mapToTourResponse(tour));
        }

        TourListResponse response = responseBuilder.build();
        Logger.log(traceId, "Service B: возвращено туров = " + response.getToursCount());
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    // Вспомогательные методы для маппинга

    private TourResponse mapToTourResponse(Tour tour) {
        TourResponse.Builder builder = TourResponse.newBuilder()
                .setId(tour.getId())
                .setName(tour.getName())
                .setDescription(tour.getDescription() != null ? tour.getDescription() : "")
                .setBasePrice(tour.getBasePrice().toString())
                .setOnSale(tour.isOnSale())
                .setSalePercentage(tour.getSalePercentage().toString())
                .setTourType(tour.getTourType())
                .setLocationInfo(tour.getLocationInfo() != null ? tour.getLocationInfo() : "");

        return builder.build();
    }

    private ClientResponse mapToClientResponse(Client client) {
        return ClientResponse.newBuilder()
                .setId(client.getId())
                .setName(client.getName())
                .setEmail(client.getEmail())
                .setPersonalDiscount(client.getPersonalDiscount().toString())
                .build();
    }
}