package com.bank.core.reference;

import com.bank.contract.*;
import com.bank.core.exception.BankException;
import com.bank.core.exception.ReferenceServiceUnavailableException;
import com.bank.core.trace.TraceIdClientInterceptor;
import io.grpc.ManagedChannel;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;

import java.util.concurrent.TimeUnit;

public class ReferenceDataClient {
    private final String host;
    private final int port;

    public ReferenceDataClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String validateAccountType(String accountType, String traceId) {
        ManagedChannel channel = newChannel();
        try {
            ReferenceServiceGrpc.ReferenceServiceBlockingStub stub = newStub(channel, traceId);
            ValidateAccountTypeResponse response = stub.validateAccountType(
                    ValidateAccountTypeRequest.newBuilder().setAccountType(accountType).build()
            );

            if (!response.getValid()) {
                throw new IllegalArgumentException(response.getMessage());
            }
            return response.getNormalizedAccountType();
        } catch (StatusRuntimeException e) {
            throw mapStatusException(e);
        } finally {
            shutdownQuietly(channel);
        }
    }

    public void validateAmount(double amount, String operation, String traceId) {
        ManagedChannel channel = newChannel();
        try {
            ReferenceServiceGrpc.ReferenceServiceBlockingStub stub = newStub(channel, traceId);
            ValidateAmountResponse response = stub.validateAmount(
                    ValidateAmountRequest.newBuilder()
                            .setAmount(amount)
                            .setOperation(operation)
                            .build()
            );

            if (!response.getValid()) {
                throw new IllegalArgumentException(response.getMessage());
            }
        } catch (StatusRuntimeException e) {
            throw mapStatusException(e);
        } finally {
            shutdownQuietly(channel);
        }
    }

    public InterestRule getInterestRule(String accountType, double basePercent, double currentBalance, String traceId) {
        ManagedChannel channel = newChannel();
        try {
            ReferenceServiceGrpc.ReferenceServiceBlockingStub stub = newStub(channel, traceId);
            GetInterestRuleResponse response = stub.getInterestRule(
                    GetInterestRuleRequest.newBuilder()
                            .setAccountType(accountType)
                            .setBasePercent(basePercent)
                            .setCurrentBalance(currentBalance)
                            .build()
            );

            if (!response.getFound()) {
                throw new BankException(response.getMessage());
            }

            return new InterestRule(
                    response.getEffectivePercent(),
                    response.getBonusAmount(),
                    response.getStrategyName()
            );
        } catch (StatusRuntimeException e) {
            throw mapStatusException(e);
        } finally {
            shutdownQuietly(channel);
        }
    }

    public void shutdown() {
        // Каналы короткоживущие, поэтому завершать нечего.
    }

    private ManagedChannel newChannel() {
        return NettyChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
    }

    private ReferenceServiceGrpc.ReferenceServiceBlockingStub newStub(ManagedChannel channel, String traceId) {
        return ReferenceServiceGrpc.newBlockingStub(channel)
                .withInterceptors(new TraceIdClientInterceptor(traceId))
                .withDeadlineAfter(3, TimeUnit.SECONDS);
    }

    private RuntimeException mapStatusException(StatusRuntimeException e) {
        Status.Code code = e.getStatus().getCode();
        String description = e.getStatus().getDescription();
        String message = description == null || description.isBlank()
                ? "Ошибка при обращении к Reference Service"
                : description;

        return switch (code) {
            case INVALID_ARGUMENT -> new IllegalArgumentException(message);
            case NOT_FOUND, FAILED_PRECONDITION -> new BankException(message);
            case UNAVAILABLE, DEADLINE_EXCEEDED ->
                    new ReferenceServiceUnavailableException("Reference Service недоступен", e);
            default -> new ReferenceServiceUnavailableException(message, e);
        };
    }

    private void shutdownQuietly(ManagedChannel channel) {
        channel.shutdown();
        try {
            channel.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }
}
