package com.bank.reference;

import com.bank.contract.*;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

import java.util.logging.Logger;

public class ReferenceServiceImpl extends ReferenceServiceGrpc.ReferenceServiceImplBase {
    private static final Logger LOGGER = Logger.getLogger(ReferenceServiceImpl.class.getName());
    private final ReferenceCatalog catalog = new ReferenceCatalog();

    @Override
    public void validateAccountType(ValidateAccountTypeRequest request,
                                    StreamObserver<ValidateAccountTypeResponse> responseObserver) {
        String traceId = currentTraceId();
        String normalized = catalog.normalize(request.getAccountType());
        LOGGER.info(() -> "[reference-service] traceId=" + traceId + " ValidateAccountType accountType=" + request.getAccountType());

        if (normalized == null) {
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("Недопустимый тип счета: " + request.getAccountType())
                    .asRuntimeException());
            return;
        }

        ValidateAccountTypeResponse response = ValidateAccountTypeResponse.newBuilder()
                .setValid(true)
                .setMessage("Тип счета корректен")
                .setNormalizedAccountType(normalized)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void validateAmount(ValidateAmountRequest request,
                               StreamObserver<ValidateAmountResponse> responseObserver) {
        String traceId = currentTraceId();
        LOGGER.info(() -> "[reference-service] traceId=" + traceId + " ValidateAmount operation=" + request.getOperation() + " amount=" + request.getAmount());

        if (request.getAmount() <= 0) {
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("Сумма должна быть положительной")
                    .asRuntimeException());
            return;
        }

        responseObserver.onNext(ValidateAmountResponse.newBuilder()
                .setValid(true)
                .setMessage("Сумма допустима")
                .build());
        responseObserver.onCompleted();
    }

    @Override
    public void getInterestRule(GetInterestRuleRequest request,
                                StreamObserver<GetInterestRuleResponse> responseObserver) {
        String traceId = currentTraceId();
        LOGGER.info(() -> "[reference-service] traceId=" + traceId + " GetInterestRule accountType=" + request.getAccountType() + " basePercent=" + request.getBasePercent() + " balance=" + request.getCurrentBalance());

        ReferenceCatalog.Rule rule = catalog.getRule(request.getAccountType());
        if (rule == null) {
            responseObserver.onError(Status.NOT_FOUND
                    .withDescription("Правило для типа счета не найдено")
                    .asRuntimeException());
            return;
        }

        double bonus = request.getCurrentBalance() > rule.bonusThreshold() ? rule.bonusAmount() : 0.0;
        GetInterestRuleResponse response = GetInterestRuleResponse.newBuilder()
                .setFound(true)
                .setMessage("Правило найдено")
                .setEffectivePercent(request.getBasePercent() + rule.extraPercent())
                .setBonusAmount(bonus)
                .setStrategyName(rule.strategyName())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private String currentTraceId() {
        String traceId = TraceKeys.TRACE_ID_CONTEXT_KEY.get();
        return traceId == null ? "unknown-trace" : traceId;
    }
}
