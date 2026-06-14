package ru.mins.parking.reference.grpc;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import ru.mins.parking.proto.ParkingReferenceServiceGrpc;
import ru.mins.parking.proto.SpotTypeRequest;
import ru.mins.parking.proto.SpotTypeResponse;
import ru.mins.parking.proto.TariffRequest;
import ru.mins.parking.proto.TariffResponse;
import ru.mins.parking.proto.VehicleValidationRequest;
import ru.mins.parking.proto.VehicleValidationResponse;
import ru.mins.parking.proto.VipDiscountRequest;
import ru.mins.parking.proto.VipDiscountResponse;
import ru.mins.parking.reference.exception.ReferenceDataNotFoundException;
import ru.mins.parking.reference.model.SpotTypeInfo;
import ru.mins.parking.reference.model.TariffInfo;
import ru.mins.parking.reference.model.VehicleValidationResult;
import ru.mins.parking.reference.service.ReferenceCatalogService;
import ru.mins.parking.reference.util.EnumMapper;
import ru.mins.parking.reference.util.LogUtil;

import java.util.logging.Logger;

public class ParkingReferenceGrpcService extends ParkingReferenceServiceGrpc.ParkingReferenceServiceImplBase {
    private static final Logger LOGGER = Logger.getLogger(ParkingReferenceGrpcService.class.getName());

    private final ReferenceCatalogService catalogService;

    public ParkingReferenceGrpcService(ReferenceCatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @Override
    public void getTariffByType(TariffRequest request, StreamObserver<TariffResponse> responseObserver) {
        try {
            TariffInfo tariffInfo = catalogService.getTariff(EnumMapper.fromProto(request.getTariffType()));
            LogUtil.info(LOGGER, "Запрошен тариф: " + tariffInfo.tariffType());
            responseObserver.onNext(TariffResponse.newBuilder()
                    .setTariffType(request.getTariffType())
                    .setBaseRate(tariffInfo.baseRate())
                    .setExtraHourRate(tariffInfo.extraHourRate())
                    .setIncludedHours(tariffInfo.includedHours())
                    .build());
            responseObserver.onCompleted();
        } catch (ReferenceDataNotFoundException exception) {
            responseObserver.onError(Status.NOT_FOUND.withDescription(exception.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void getSpotTypeInfo(SpotTypeRequest request, StreamObserver<SpotTypeResponse> responseObserver) {
        try {
            SpotTypeInfo info = catalogService.getSpotTypeInfo(EnumMapper.fromProto(request.getSpotType()));
            LogUtil.info(LOGGER, "Запрошена информация о типе места: " + info.spotType());
            SpotTypeResponse.Builder builder = SpotTypeResponse.newBuilder()
                    .setSpotType(request.getSpotType())
                    .setDisplayName(info.displayName())
                    .setPriceCoefficient(info.priceCoefficient());
            info.allowedVehicleTypes().stream()
                    .map(EnumMapper::toProto)
                    .forEach(builder::addAllowedVehicleTypes);
            responseObserver.onNext(builder.build());
            responseObserver.onCompleted();
        } catch (ReferenceDataNotFoundException exception) {
            responseObserver.onError(Status.NOT_FOUND.withDescription(exception.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void validateVehicle(VehicleValidationRequest request, StreamObserver<VehicleValidationResponse> responseObserver) {
        VehicleValidationResult result = catalogService.validateVehicle(
                request.getLicensePlate(),
                EnumMapper.fromProto(request.getVehicleType()),
                EnumMapper.fromProto(request.getRequestedSpotType())
        );
        LogUtil.info(LOGGER, "Запрошена валидация автомобиля " + request.getLicensePlate());
        responseObserver.onNext(VehicleValidationResponse.newBuilder()
                .setValid(result.valid())
                .setMessage(result.message())
                .build());
        responseObserver.onCompleted();
    }

    @Override
    public void getVipDiscount(VipDiscountRequest request, StreamObserver<VipDiscountResponse> responseObserver) {
        double discount = catalogService.getVipDiscount(request.getLicensePlate());
        LogUtil.info(LOGGER, "Запрошена VIP-скидка для автомобиля " + request.getLicensePlate());
        responseObserver.onNext(VipDiscountResponse.newBuilder()
                .setVip(discount > 0)
                .setDiscountPercent(discount)
                .build());
        responseObserver.onCompleted();
    }
}
