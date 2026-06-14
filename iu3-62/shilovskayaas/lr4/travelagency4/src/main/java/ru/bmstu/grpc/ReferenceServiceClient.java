package ru.bmstu.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import ru.bmstu.util.Logger;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import ru.bmstu.grpc.*;

public class ReferenceServiceClient {
    private final ReferenceServiceGrpc.ReferenceServiceBlockingStub stub;
    private final ManagedChannel channel;

    public ReferenceServiceClient(String host, int port) {
        this.channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        this.stub = ReferenceServiceGrpc.newBlockingStub(channel);
    }

    public void shutdown() {
        channel.shutdown();
    }

    // Получить тур по ID
    public Optional<TourInfo> getTour(int tourId, String traceId) {
        try {
            Logger.log(traceId, "Service A: запрос тура " + tourId + " к Service B");

            GetTourRequest request = GetTourRequest.newBuilder()
                    .setTourId(tourId)
                    .build();

            TourResponse response = stub.getTour(request);

            TourInfo tourInfo = new TourInfo(
                    response.getId(),
                    response.getName(),
                    response.getDescription(),
                    new BigDecimal(response.getBasePrice()),
                    response.getOnSale(),
                    new BigDecimal(response.getSalePercentage()),
                    response.getTourType(),
                    response.getLocationInfo()
            );

            Logger.log(traceId, "Service A: тур получен: " + tourInfo.getName());
            return Optional.of(tourInfo);

        } catch (StatusRuntimeException e) {
            Logger.log(traceId, "Service A: ошибка при получении тура - " + e.getMessage());
            return Optional.empty();
        }
    }

    // Получить клиента по ID
    public Optional<ClientInfo> getClient(int clientId, String traceId) {
        try {
            Logger.log(traceId, "Service A: запрос клиента " + clientId + " к Service B");

            GetClientRequest request = GetClientRequest.newBuilder()
                    .setClientId(clientId)
                    .build();

            ClientResponse response = stub.getClient(request);

            ClientInfo clientInfo = new ClientInfo(
                    response.getId(),
                    response.getName(),
                    response.getEmail(),
                    new BigDecimal(response.getPersonalDiscount())
            );

            Logger.log(traceId, "Service A: клиент получен: " + clientInfo.getName());
            return Optional.of(clientInfo);

        } catch (StatusRuntimeException e) {
            Logger.log(traceId, "Service A: ошибка при получении клиента - " + e.getMessage());
            return Optional.empty();
        }
    }

    // Получить клиента по email
    public Optional<ClientInfo> getClientByEmail(String email, String traceId) {
        try {
            Logger.log(traceId, "Service A: запрос клиента по email " + email + " к Service B");

            GetClientByEmailRequest request = GetClientByEmailRequest.newBuilder()
                    .setEmail(email)
                    .build();

            ClientResponse response = stub.getClientByEmail(request);

            ClientInfo clientInfo = new ClientInfo(
                    response.getId(),
                    response.getName(),
                    response.getEmail(),
                    new BigDecimal(response.getPersonalDiscount())
            );

            Logger.log(traceId, "Service A: клиент найден: " + clientInfo.getName());
            return Optional.of(clientInfo);

        } catch (StatusRuntimeException e) {
            Logger.log(traceId, "Service A: клиент не найден - " + e.getMessage());
            return Optional.empty();
        }
    }

    // Проверить существование тура
    public boolean checkTourExists(int tourId, String traceId) {
        try {
            CheckTourExistsRequest request = CheckTourExistsRequest.newBuilder()
                    .setTourId(tourId)
                    .build();

            ExistsResponse response = stub.checkTourExists(request);
            return response.getExists();

        } catch (StatusRuntimeException e) {
            Logger.log(traceId, "Service A: ошибка при проверке тура - " + e.getMessage());
            return false;
        }
    }

    // Получить все туры
    public List<TourInfo> getAllTours(String traceId) {
        try {
            Logger.log(traceId, "Service A: запрос всех туров к Service B");

            TourListResponse response = stub.getAllTours(Empty.newBuilder().build());
            List<TourInfo> tours = new ArrayList<>();

            for (TourResponse tourResponse : response.getToursList()) {
                tours.add(new TourInfo(
                        tourResponse.getId(),
                        tourResponse.getName(),
                        tourResponse.getDescription(),
                        new BigDecimal(tourResponse.getBasePrice()),
                        tourResponse.getOnSale(),
                        new BigDecimal(tourResponse.getSalePercentage()),
                        tourResponse.getTourType(),
                        tourResponse.getLocationInfo()
                ));
            }

            Logger.log(traceId, "Service A: получено туров: " + tours.size());
            return tours;

        } catch (StatusRuntimeException e) {
            Logger.log(traceId, "Service A: ошибка при получении туров - " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Проверить доступность Service B
    public boolean isAvailable() {
        try {
            getAllTours("health-check");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // ========== Вспомогательные классы для хранения данных ==========

    public static class TourInfo {
        private final int id;
        private final String name;
        private final String description;
        private final BigDecimal basePrice;
        private final boolean onSale;
        private final BigDecimal salePercentage;
        private final String tourType;
        private final String locationInfo;

        public TourInfo(int id, String name, String description, BigDecimal basePrice,
                        boolean onSale, BigDecimal salePercentage, String tourType, String locationInfo) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.basePrice = basePrice;
            this.onSale = onSale;
            this.salePercentage = salePercentage;
            this.tourType = tourType;
            this.locationInfo = locationInfo;
        }

        public int getId() { return id; }
        public String getName() { return name; }
        public String getDescription() { return description; }
        public BigDecimal getBasePrice() { return basePrice; }
        public boolean isOnSale() { return onSale; }
        public BigDecimal getSalePercentage() { return salePercentage; }
        public String getTourType() { return tourType; }
        public String getLocationInfo() { return locationInfo; }
    }

    public static class ClientInfo {
        private final int id;
        private final String name;
        private final String email;
        private final BigDecimal personalDiscount;

        public ClientInfo(int id, String name, String email, BigDecimal personalDiscount) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.personalDiscount = personalDiscount;
        }

        public int getId() { return id; }
        public String getName() { return name; }
        public String getEmail() { return email; }
        public BigDecimal getPersonalDiscount() { return personalDiscount; }
    }
}