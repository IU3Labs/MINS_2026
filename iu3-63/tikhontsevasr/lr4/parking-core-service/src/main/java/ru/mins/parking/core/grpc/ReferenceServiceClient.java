package ru.mins.parking.core.grpc;

import io.grpc.ClientInterceptors;
import io.grpc.ManagedChannel;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import ru.mins.parking.core.exception.ReferenceDataNotFoundException;
import ru.mins.parking.core.exception.ReferenceServiceUnavailableException;
import ru.mins.parking.core.model.SpotType;
import ru.mins.parking.core.model.TariffType;
import ru.mins.parking.core.model.VehicleType;
import ru.mins.parking.core.util.EnumMapper;
import ru.mins.parking.core.util.LogUtil;
import ru.mins.parking.proto.ParkingReferenceServiceGrpc;
import ru.mins.parking.proto.SpotTypeRequest;
import ru.mins.parking.proto.SpotTypeResponse;
import ru.mins.parking.proto.TariffRequest;
import ru.mins.parking.proto.TariffResponse;
import ru.mins.parking.proto.VehicleValidationRequest;
import ru.mins.parking.proto.VehicleValidationResponse;
import ru.mins.parking.proto.VipDiscountRequest;
import ru.mins.parking.proto.VipDiscountResponse;

import java.util.logging.Logger;

public class ReferenceServiceClient {
    private static final Logger LOGGER = Logger.getLogger(ReferenceServiceClient.class.getName());

    private final ParkingReferenceServiceGrpc.ParkingReferenceServiceBlockingStub blockingStub;

    public ReferenceServiceClient(ManagedChannel channel) {
        this.blockingStub = ParkingReferenceServiceGrpc.newBlockingStub(
                ClientInterceptors.intercept(channel, new TraceClientInterceptor())
        );
    }

    public TariffResponse getTariffByType(TariffType tariffType) {
        return execute(() -> blockingStub.getTariffByType(TariffRequest.newBuilder()
                .setTariffType(EnumMapper.toProto(tariffType))
                .build()));
    }

    public SpotTypeResponse getSpotTypeInfo(SpotType spotType) {
        return execute(() -> blockingStub.getSpotTypeInfo(SpotTypeRequest.newBuilder()
                .setSpotType(EnumMapper.toProto(spotType))
                .build()));
    }

    public VehicleValidationResponse validateVehicle(String licensePlate, VehicleType vehicleType, SpotType spotType) {
        return execute(() -> blockingStub.validateVehicle(VehicleValidationRequest.newBuilder()
                .setLicensePlate(licensePlate)
                .setVehicleType(EnumMapper.toProto(vehicleType))
                .setRequestedSpotType(EnumMapper.toProto(spotType))
                .build()));
    }

    public VipDiscountResponse getVipDiscount(String licensePlate) {
        return execute(() -> blockingStub.getVipDiscount(VipDiscountRequest.newBuilder()
                .setLicensePlate(licensePlate)
                .build()));
    }

    private <T> T execute(GrpcCall<T> call) {
        try {
            return call.execute();
        } catch (StatusRuntimeException exception) {
            if (exception.getStatus().getCode() == Status.Code.UNAVAILABLE) {
                LogUtil.error(LOGGER, "Справочный сервис недоступен: " + exception.getStatus().getDescription());
                throw new ReferenceServiceUnavailableException();
            }
            if (exception.getStatus().getCode() == Status.Code.NOT_FOUND) {
                String message = exception.getStatus().getDescription();
                LogUtil.error(LOGGER, "Справочные данные не найдены: " + message);
                throw new ReferenceDataNotFoundException(
                        message == null || message.isBlank() ? "Reference data not found" : message
                );
            }
            LogUtil.error(LOGGER, "Ошибка gRPC-вызова: " + exception.getStatus());
            throw exception;
        }
    }

    @FunctionalInterface
    private interface GrpcCall<T> {
        T execute();
    }
}
