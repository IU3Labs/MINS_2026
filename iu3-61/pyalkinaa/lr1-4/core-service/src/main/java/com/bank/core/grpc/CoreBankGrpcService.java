package com.bank.core.grpc;

import com.bank.contract.*;
import com.bank.core.exception.AccountNotFoundException;
import com.bank.core.exception.BankException;
import com.bank.core.exception.InsufficientFundsException;
import com.bank.core.exception.ReferenceServiceUnavailableException;
import com.bank.core.model.Account;
import com.bank.core.service.CoreBankDomainService;
import com.bank.core.trace.TraceKeys;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

import java.util.logging.Logger;

public class CoreBankGrpcService extends CoreBankServiceGrpc.CoreBankServiceImplBase {
    private static final Logger LOGGER = Logger.getLogger(CoreBankGrpcService.class.getName());
    private final CoreBankDomainService domainService;

    public CoreBankGrpcService(CoreBankDomainService domainService) {
        this.domainService = domainService;
    }

    @Override
    public void createAccount(CreateAccountRequest request, StreamObserver<OperationResponse> responseObserver) {
        String traceId = traceId();
        LOGGER.info(() -> "[core-service] traceId=" + traceId + " CreateAccount owner=" + request.getOwnerName() + " type=" + request.getAccountType());
        try {
            var account = domainService.createAccount(request.getOwnerName(), request.getAccountType(), traceId);
            responseObserver.onNext(OperationResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("Счет успешно открыт")
                    .setAccount(ProtoMapper.toProto(account))
                    .build());
            responseObserver.onCompleted();
        } catch (ReferenceServiceUnavailableException e) {
            fail(responseObserver, Status.UNAVAILABLE, "Справочный сервис временно недоступен. Операция не выполнена.");
        } catch (IllegalArgumentException e) {
            fail(responseObserver, Status.INVALID_ARGUMENT, e.getMessage());
        } catch (BankException e) {
            fail(responseObserver, Status.FAILED_PRECONDITION, e.getMessage());
        }
    }

    @Override
    public void deposit(DepositRequest request, StreamObserver<OperationResponse> responseObserver) {
        String traceId = traceId();
        LOGGER.info(() -> "[core-service] traceId=" + traceId + " Deposit accountId=" + request.getAccountId() + " amount=" + request.getAmount());
        try {
            var account = domainService.deposit(request.getAccountId(), request.getAmount(), traceId);
            responseObserver.onNext(OperationResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("Счет пополнен")
                    .setAccount(ProtoMapper.toProto(account))
                    .build());
            responseObserver.onCompleted();
        } catch (ReferenceServiceUnavailableException e) {
            fail(responseObserver, Status.UNAVAILABLE, "Справочный сервис временно недоступен. Операция не выполнена.");
        } catch (IllegalArgumentException e) {
            fail(responseObserver, Status.INVALID_ARGUMENT, e.getMessage());
        } catch (AccountNotFoundException e) {
            fail(responseObserver, Status.NOT_FOUND, e.getMessage());
        } catch (BankException e) {
            fail(responseObserver, Status.FAILED_PRECONDITION, e.getMessage());
        }
    }

    @Override
    public void withdraw(WithdrawRequest request, StreamObserver<OperationResponse> responseObserver) {
        String traceId = traceId();
        LOGGER.info(() -> "[core-service] traceId=" + traceId + " Withdraw accountId=" + request.getAccountId() + " amount=" + request.getAmount());
        try {
            var account = domainService.withdraw(request.getAccountId(), request.getAmount(), traceId);
            responseObserver.onNext(OperationResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("Средства успешно сняты")
                    .setAccount(ProtoMapper.toProto(account))
                    .build());
            responseObserver.onCompleted();
        } catch (ReferenceServiceUnavailableException e) {
            fail(responseObserver, Status.UNAVAILABLE, "Справочный сервис временно недоступен. Операция не выполнена.");
        } catch (IllegalArgumentException e) {
            fail(responseObserver, Status.INVALID_ARGUMENT, e.getMessage());
        } catch (AccountNotFoundException e) {
            fail(responseObserver, Status.NOT_FOUND, e.getMessage());
        } catch (InsufficientFundsException e) {
            fail(responseObserver, Status.FAILED_PRECONDITION, e.getMessage());
        } catch (BankException e) {
            fail(responseObserver, Status.FAILED_PRECONDITION, e.getMessage());
        }
    }

    @Override
    public void accrueInterest(AccrueInterestRequest request, StreamObserver<OperationResponse> responseObserver) {
        String traceId = traceId();
        LOGGER.info(() -> "[core-service] traceId=" + traceId + " AccrueInterest accountId=" + request.getAccountId() + " basePercent=" + request.getBasePercent());
        try {
            var account = domainService.accrueInterest(request.getAccountId(), request.getBasePercent(), traceId);
            responseObserver.onNext(OperationResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("Проценты начислены")
                    .setAccount(ProtoMapper.toProto(account))
                    .build());
            responseObserver.onCompleted();
        } catch (ReferenceServiceUnavailableException e) {
            fail(responseObserver, Status.UNAVAILABLE, "Справочный сервис временно недоступен. Операция не выполнена.");
        } catch (IllegalArgumentException e) {
            fail(responseObserver, Status.INVALID_ARGUMENT, e.getMessage());
        } catch (AccountNotFoundException e) {
            fail(responseObserver, Status.NOT_FOUND, e.getMessage());
        } catch (BankException e) {
            fail(responseObserver, Status.FAILED_PRECONDITION, e.getMessage());
        }
    }

    @Override
    public void getAccountStatement(AccountByIdRequest request, StreamObserver<GetAccountStatementResponse> responseObserver) {
        String traceId = traceId();
        LOGGER.info(() -> "[core-service] traceId=" + traceId + " GetAccountStatement accountId=" + request.getAccountId());
        try {
            var account = domainService.getAccountStatement(request.getAccountId());
            responseObserver.onNext(GetAccountStatementResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("Выписка получена")
                    .setAccount(ProtoMapper.toProto(account))
                    .build());
            responseObserver.onCompleted();
        } catch (AccountNotFoundException e) {
            fail(responseObserver, Status.NOT_FOUND, e.getMessage());
        } catch (BankException e) {
            fail(responseObserver, Status.FAILED_PRECONDITION, e.getMessage());
        }
    }

    @Override
    public void getAllAccounts(EmptyRequest request, StreamObserver<GetAllAccountsResponse> responseObserver) {
        String traceId = traceId();
        LOGGER.info(() -> "[core-service] traceId=" + traceId + " GetAllAccounts");
        GetAllAccountsResponse.Builder builder = GetAllAccountsResponse.newBuilder();
        for (var account : domainService.getAllAccounts()) {
            builder.addAccounts(ProtoMapper.toProto(account));
        }
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }

    private String traceId() {
        String traceId = TraceKeys.TRACE_ID_CONTEXT_KEY.get();
        return traceId == null ? "unknown-trace" : traceId;
    }

    private <T> void fail(StreamObserver<T> responseObserver, Status status, String message) {
        responseObserver.onError(status.withDescription(message).asRuntimeException());
    }
}
