package ru.iu3.lab4.referenceservice.server;

import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.iu3.lab4.proto.reference.*;
import ru.iu3.lab4.referenceservice.model.Vehicle;
import ru.iu3.lab4.referenceservice.service.VehicleService;

/**
 * Реализация gRPC-сервера для Reference Service.
 * Предоставляет справочную информацию (список транспортов, валидация).
 */
public class ReferenceServiceImpl extends ReferenceServiceGrpc.ReferenceServiceImplBase {
    private static final Logger log = LoggerFactory.getLogger(ReferenceServiceImpl.class);

    private final VehicleService vehicleService;

    public ReferenceServiceImpl(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @Override
    public void validateVehicle(ValidateVehicleRequest request, StreamObserver<ValidateVehicleResponse> responseObserver) {
        log.info("[trace:{}] ValidateVehicle: id={}", request.getTraceId(), request.getVehicleId());
        boolean exists = vehicleService.getAllVehicles().stream()
                .anyMatch(v -> v.getId().equals(request.getVehicleId()));

        ValidateVehicleResponse response = ValidateVehicleResponse.newBuilder()
                .setExists(exists)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getAllVehicles(GetAllVehiclesRequest request, StreamObserver<GetAllVehiclesResponse> responseObserver) {
        log.info("[trace:{}] GetAllVehicles", request.getTraceId());
        for (Vehicle v : vehicleService.getAllVehicles()) {
            responseObserver.onNext(
                    GetAllVehiclesResponse.newBuilder()
                            .setVehicleId(v.getId())
                            .setType(v.getType())
                            .setCapacity(v.getCapacity())
                            .build()
            );
        }
        responseObserver.onCompleted();
    }
}