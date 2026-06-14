package ru.iu3.lab4.coreservice.client;

import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;
import ru.iu3.lab4.proto.reference.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Клиент для обращения к Reference Service через gRPC.
 * Обрабатывает ситуации, когда Reference Service недоступен.
 */
public class ReferenceServiceClient {
    private final ManagedChannel channel;
    private final ReferenceServiceGrpc.ReferenceServiceBlockingStub stub;

    public ReferenceServiceClient(ManagedChannel channel) {
        this.channel = channel;
        this.stub = ReferenceServiceGrpc.newBlockingStub(channel)
                .withInterceptors(new TraceIdClientInterceptor());
    }

    public boolean validateVehicle(String vehicleId, String traceId) {
        try {
            var request = ValidateVehicleRequest.newBuilder()
                    .setVehicleId(vehicleId)
                    .setTraceId(traceId)
                    .build();
            var response = stub.validateVehicle(request);
            return response.getExists();
        } catch (StatusRuntimeException e) {
            System.err.println("[Trace:" + traceId + "] Reference Service недоступен: " + e.getStatus());
            return false;
        }
    }

    public List<VehicleInfo> getAllVehicles(String traceId) {
        try {
            var request = GetAllVehiclesRequest.newBuilder()
                    .setTraceId(traceId)
                    .build();
            Iterator<GetAllVehiclesResponse> responses = stub.getAllVehicles(request);
            List<VehicleInfo> result = new ArrayList<>();
            while (responses.hasNext()) {
                var r = responses.next();
                result.add(new VehicleInfo(r.getVehicleId(), r.getType(), r.getCapacity()));
            }
            return result;
        } catch (StatusRuntimeException e) {
            System.err.println("[Trace:" + traceId + "] Не удалось получить список транспорта");
            return new ArrayList<>();
        }
    }

    public void shutdown() {
        try {
            channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}