package com.autoservice.grpc;


import com.autoservice.proto.ReferenceServiceGrpc;
import com.autoservice.proto.ReferenceServiceProto;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class ReferenceServiceGrpcClient {
    private static final Logger logger = Logger.getLogger(ReferenceServiceGrpcClient.class.getName());
    private final ManagedChannel channel;
    private final ReferenceServiceGrpc.ReferenceServiceBlockingStub blockingStub;

    public ReferenceServiceGrpcClient(String host, int port) {
        channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        blockingStub = ReferenceServiceGrpc.newBlockingStub(channel);
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public ReferenceServiceProto.ServiceResponse getServiceByName(String name, String traceId) {
        ReferenceServiceProto.ServiceRequest request = ReferenceServiceProto.ServiceRequest.newBuilder()
                .setName(name)
                .setTraceId(traceId)
                .build();
        try {
            return blockingStub.getServiceByName(request);
        } catch (StatusRuntimeException e) {
            logger.severe("[TraceID: " + traceId + "] Ошибка: " + e.getMessage());
            return ReferenceServiceProto.ServiceResponse.newBuilder()
                    .setFound(false)
                    .setErrorMessage("Сервис справочников недоступен. Попробуйте позже.")
                    .build();
        }
    }
}
