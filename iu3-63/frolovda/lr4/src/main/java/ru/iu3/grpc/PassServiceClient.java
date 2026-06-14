package ru.iu3.grpc;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import ru.iu3.entity.Pass;
import ru.iu3.exceptions.ConflictException;
import ru.iu3.exceptions.NotFoundExeption;
import ru.iu3.exceptions.ValidationException;
import ru.iu3.service.interfaces.PassService;
import ru.iu3.util.AppLogger;

public class PassServiceClient implements PassService {
    private final ManagedChannel channel;
    private final ReferenceServiceGrpc.ReferenceServiceBlockingStub stub;
    private final AppLogger logger;

    public PassServiceClient(String host, int port, AppLogger logger) {
        this.logger = logger;
        this.channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        this.stub = ReferenceServiceGrpc.newBlockingStub(channel);
    }

    @Override
    public void issuePass(int id, String holderName) {
        String traceId = newTraceId();
        try {
            logger.log("CORE", traceId, "IssuePass id=" + id);
            stub.withDeadlineAfter(2, TimeUnit.SECONDS)
                    .issuePass(ReferenceContract.IssuePassRequest.newBuilder()
                            .setId(id)
                            .setHolderName(holderName)
                            .setTraceId(traceId)
                            .build());
        } catch (StatusRuntimeException e) {
            logger.log("CORE", traceId, "IssuePass failed: " + safeDescription(e));
            throw mapGrpcException(e);
        }
    }

    @Override
    public void deactivatePass(int id) {
        String traceId = newTraceId();
        try {
            logger.log("CORE", traceId, "DeactivatePass id=" + id);
            stub.withDeadlineAfter(2, TimeUnit.SECONDS)
                    .deactivatePass(ReferenceContract.PassIdRequest.newBuilder()
                            .setId(id)
                            .setTraceId(traceId)
                            .build());
        } catch (StatusRuntimeException e) {
            logger.log("CORE", traceId, "DeactivatePass failed: " + safeDescription(e));
            throw mapGrpcException(e);
        }
    }

    @Override
    public List<Pass> getAllPasses() {
        String traceId = newTraceId();
        try {
            logger.log("CORE", traceId, "ListPasses");
            ReferenceContract.PassListResponse response = stub.withDeadlineAfter(2, TimeUnit.SECONDS)
                    .listPasses(ReferenceContract.TraceRequest.newBuilder()
                            .setTraceId(traceId)
                            .build());

            List<Pass> passes = new ArrayList<>();
            for (ReferenceContract.PassResponse passResponse : response.getPassesList()) {
                passes.add(mapPass(passResponse));
            }
            return passes;
        } catch (StatusRuntimeException e) {
            logger.log("CORE", traceId, "ListPasses failed: " + safeDescription(e));
            throw mapGrpcException(e);
        }
    }

    @Override
    public Pass getPassById(int id) {
        String traceId = newTraceId();
        try {
            logger.log("CORE", traceId, "GetPass id=" + id);
            ReferenceContract.PassResponse response = stub.withDeadlineAfter(2, TimeUnit.SECONDS)
                    .getPass(ReferenceContract.PassIdRequest.newBuilder()
                            .setId(id)
                            .setTraceId(traceId)
                            .build());
            return mapPass(response);
        } catch (StatusRuntimeException e) {
            logger.log("CORE", traceId, "GetPass failed: " + safeDescription(e));
            throw mapGrpcException(e);
        }
    }

    private Pass mapPass(ReferenceContract.PassResponse response) {
        Pass pass = new Pass(response.getId(), response.getHolderName());
        if (!response.getActive()) {
            pass.deactivate();
        }
        return pass;
    }

    private RuntimeException mapGrpcException(StatusRuntimeException e) {
        Status status = e.getStatus();
        String message = status.getDescription() == null ? "Ошибка gRPC при работе с пропусками." : status.getDescription();

        return switch (status.getCode()) {
            case NOT_FOUND -> new NotFoundExeption(message);
            case INVALID_ARGUMENT -> new ValidationException(message);
            case ALREADY_EXISTS -> new ConflictException(message);
            case UNAVAILABLE, DEADLINE_EXCEEDED ->
                    new ValidationException("Reference Service недоступен. Попробуйте позже.");
            default -> new RuntimeException(message, e);
        };
    }

    private String newTraceId() {
        return UUID.randomUUID().toString();
    }

    private String safeDescription(StatusRuntimeException e) {
        return e.getStatus().getDescription() == null ? e.getStatus().getCode().name() : e.getStatus().getDescription();
    }
}
