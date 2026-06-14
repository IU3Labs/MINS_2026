package com.bank.contract;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.64.0)",
    comments = "Source: core_service.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class CoreBankServiceGrpc {

  private CoreBankServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "bank.contract.CoreBankService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.bank.contract.CreateAccountRequest,
      com.bank.contract.OperationResponse> getCreateAccountMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CreateAccount",
      requestType = com.bank.contract.CreateAccountRequest.class,
      responseType = com.bank.contract.OperationResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.bank.contract.CreateAccountRequest,
      com.bank.contract.OperationResponse> getCreateAccountMethod() {
    io.grpc.MethodDescriptor<com.bank.contract.CreateAccountRequest, com.bank.contract.OperationResponse> getCreateAccountMethod;
    if ((getCreateAccountMethod = CoreBankServiceGrpc.getCreateAccountMethod) == null) {
      synchronized (CoreBankServiceGrpc.class) {
        if ((getCreateAccountMethod = CoreBankServiceGrpc.getCreateAccountMethod) == null) {
          CoreBankServiceGrpc.getCreateAccountMethod = getCreateAccountMethod =
              io.grpc.MethodDescriptor.<com.bank.contract.CreateAccountRequest, com.bank.contract.OperationResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CreateAccount"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bank.contract.CreateAccountRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bank.contract.OperationResponse.getDefaultInstance()))
              .setSchemaDescriptor(new CoreBankServiceMethodDescriptorSupplier("CreateAccount"))
              .build();
        }
      }
    }
    return getCreateAccountMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.bank.contract.DepositRequest,
      com.bank.contract.OperationResponse> getDepositMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Deposit",
      requestType = com.bank.contract.DepositRequest.class,
      responseType = com.bank.contract.OperationResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.bank.contract.DepositRequest,
      com.bank.contract.OperationResponse> getDepositMethod() {
    io.grpc.MethodDescriptor<com.bank.contract.DepositRequest, com.bank.contract.OperationResponse> getDepositMethod;
    if ((getDepositMethod = CoreBankServiceGrpc.getDepositMethod) == null) {
      synchronized (CoreBankServiceGrpc.class) {
        if ((getDepositMethod = CoreBankServiceGrpc.getDepositMethod) == null) {
          CoreBankServiceGrpc.getDepositMethod = getDepositMethod =
              io.grpc.MethodDescriptor.<com.bank.contract.DepositRequest, com.bank.contract.OperationResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Deposit"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bank.contract.DepositRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bank.contract.OperationResponse.getDefaultInstance()))
              .setSchemaDescriptor(new CoreBankServiceMethodDescriptorSupplier("Deposit"))
              .build();
        }
      }
    }
    return getDepositMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.bank.contract.WithdrawRequest,
      com.bank.contract.OperationResponse> getWithdrawMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Withdraw",
      requestType = com.bank.contract.WithdrawRequest.class,
      responseType = com.bank.contract.OperationResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.bank.contract.WithdrawRequest,
      com.bank.contract.OperationResponse> getWithdrawMethod() {
    io.grpc.MethodDescriptor<com.bank.contract.WithdrawRequest, com.bank.contract.OperationResponse> getWithdrawMethod;
    if ((getWithdrawMethod = CoreBankServiceGrpc.getWithdrawMethod) == null) {
      synchronized (CoreBankServiceGrpc.class) {
        if ((getWithdrawMethod = CoreBankServiceGrpc.getWithdrawMethod) == null) {
          CoreBankServiceGrpc.getWithdrawMethod = getWithdrawMethod =
              io.grpc.MethodDescriptor.<com.bank.contract.WithdrawRequest, com.bank.contract.OperationResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Withdraw"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bank.contract.WithdrawRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bank.contract.OperationResponse.getDefaultInstance()))
              .setSchemaDescriptor(new CoreBankServiceMethodDescriptorSupplier("Withdraw"))
              .build();
        }
      }
    }
    return getWithdrawMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.bank.contract.AccrueInterestRequest,
      com.bank.contract.OperationResponse> getAccrueInterestMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "AccrueInterest",
      requestType = com.bank.contract.AccrueInterestRequest.class,
      responseType = com.bank.contract.OperationResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.bank.contract.AccrueInterestRequest,
      com.bank.contract.OperationResponse> getAccrueInterestMethod() {
    io.grpc.MethodDescriptor<com.bank.contract.AccrueInterestRequest, com.bank.contract.OperationResponse> getAccrueInterestMethod;
    if ((getAccrueInterestMethod = CoreBankServiceGrpc.getAccrueInterestMethod) == null) {
      synchronized (CoreBankServiceGrpc.class) {
        if ((getAccrueInterestMethod = CoreBankServiceGrpc.getAccrueInterestMethod) == null) {
          CoreBankServiceGrpc.getAccrueInterestMethod = getAccrueInterestMethod =
              io.grpc.MethodDescriptor.<com.bank.contract.AccrueInterestRequest, com.bank.contract.OperationResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "AccrueInterest"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bank.contract.AccrueInterestRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bank.contract.OperationResponse.getDefaultInstance()))
              .setSchemaDescriptor(new CoreBankServiceMethodDescriptorSupplier("AccrueInterest"))
              .build();
        }
      }
    }
    return getAccrueInterestMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.bank.contract.AccountByIdRequest,
      com.bank.contract.GetAccountStatementResponse> getGetAccountStatementMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetAccountStatement",
      requestType = com.bank.contract.AccountByIdRequest.class,
      responseType = com.bank.contract.GetAccountStatementResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.bank.contract.AccountByIdRequest,
      com.bank.contract.GetAccountStatementResponse> getGetAccountStatementMethod() {
    io.grpc.MethodDescriptor<com.bank.contract.AccountByIdRequest, com.bank.contract.GetAccountStatementResponse> getGetAccountStatementMethod;
    if ((getGetAccountStatementMethod = CoreBankServiceGrpc.getGetAccountStatementMethod) == null) {
      synchronized (CoreBankServiceGrpc.class) {
        if ((getGetAccountStatementMethod = CoreBankServiceGrpc.getGetAccountStatementMethod) == null) {
          CoreBankServiceGrpc.getGetAccountStatementMethod = getGetAccountStatementMethod =
              io.grpc.MethodDescriptor.<com.bank.contract.AccountByIdRequest, com.bank.contract.GetAccountStatementResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetAccountStatement"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bank.contract.AccountByIdRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bank.contract.GetAccountStatementResponse.getDefaultInstance()))
              .setSchemaDescriptor(new CoreBankServiceMethodDescriptorSupplier("GetAccountStatement"))
              .build();
        }
      }
    }
    return getGetAccountStatementMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.bank.contract.EmptyRequest,
      com.bank.contract.GetAllAccountsResponse> getGetAllAccountsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetAllAccounts",
      requestType = com.bank.contract.EmptyRequest.class,
      responseType = com.bank.contract.GetAllAccountsResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.bank.contract.EmptyRequest,
      com.bank.contract.GetAllAccountsResponse> getGetAllAccountsMethod() {
    io.grpc.MethodDescriptor<com.bank.contract.EmptyRequest, com.bank.contract.GetAllAccountsResponse> getGetAllAccountsMethod;
    if ((getGetAllAccountsMethod = CoreBankServiceGrpc.getGetAllAccountsMethod) == null) {
      synchronized (CoreBankServiceGrpc.class) {
        if ((getGetAllAccountsMethod = CoreBankServiceGrpc.getGetAllAccountsMethod) == null) {
          CoreBankServiceGrpc.getGetAllAccountsMethod = getGetAllAccountsMethod =
              io.grpc.MethodDescriptor.<com.bank.contract.EmptyRequest, com.bank.contract.GetAllAccountsResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetAllAccounts"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bank.contract.EmptyRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bank.contract.GetAllAccountsResponse.getDefaultInstance()))
              .setSchemaDescriptor(new CoreBankServiceMethodDescriptorSupplier("GetAllAccounts"))
              .build();
        }
      }
    }
    return getGetAllAccountsMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static CoreBankServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<CoreBankServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<CoreBankServiceStub>() {
        @java.lang.Override
        public CoreBankServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new CoreBankServiceStub(channel, callOptions);
        }
      };
    return CoreBankServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static CoreBankServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<CoreBankServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<CoreBankServiceBlockingStub>() {
        @java.lang.Override
        public CoreBankServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new CoreBankServiceBlockingStub(channel, callOptions);
        }
      };
    return CoreBankServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static CoreBankServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<CoreBankServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<CoreBankServiceFutureStub>() {
        @java.lang.Override
        public CoreBankServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new CoreBankServiceFutureStub(channel, callOptions);
        }
      };
    return CoreBankServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void createAccount(com.bank.contract.CreateAccountRequest request,
        io.grpc.stub.StreamObserver<com.bank.contract.OperationResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCreateAccountMethod(), responseObserver);
    }

    /**
     */
    default void deposit(com.bank.contract.DepositRequest request,
        io.grpc.stub.StreamObserver<com.bank.contract.OperationResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getDepositMethod(), responseObserver);
    }

    /**
     */
    default void withdraw(com.bank.contract.WithdrawRequest request,
        io.grpc.stub.StreamObserver<com.bank.contract.OperationResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getWithdrawMethod(), responseObserver);
    }

    /**
     */
    default void accrueInterest(com.bank.contract.AccrueInterestRequest request,
        io.grpc.stub.StreamObserver<com.bank.contract.OperationResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getAccrueInterestMethod(), responseObserver);
    }

    /**
     */
    default void getAccountStatement(com.bank.contract.AccountByIdRequest request,
        io.grpc.stub.StreamObserver<com.bank.contract.GetAccountStatementResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetAccountStatementMethod(), responseObserver);
    }

    /**
     */
    default void getAllAccounts(com.bank.contract.EmptyRequest request,
        io.grpc.stub.StreamObserver<com.bank.contract.GetAllAccountsResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetAllAccountsMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service CoreBankService.
   */
  public static abstract class CoreBankServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return CoreBankServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service CoreBankService.
   */
  public static final class CoreBankServiceStub
      extends io.grpc.stub.AbstractAsyncStub<CoreBankServiceStub> {
    private CoreBankServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CoreBankServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new CoreBankServiceStub(channel, callOptions);
    }

    /**
     */
    public void createAccount(com.bank.contract.CreateAccountRequest request,
        io.grpc.stub.StreamObserver<com.bank.contract.OperationResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCreateAccountMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void deposit(com.bank.contract.DepositRequest request,
        io.grpc.stub.StreamObserver<com.bank.contract.OperationResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getDepositMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void withdraw(com.bank.contract.WithdrawRequest request,
        io.grpc.stub.StreamObserver<com.bank.contract.OperationResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getWithdrawMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void accrueInterest(com.bank.contract.AccrueInterestRequest request,
        io.grpc.stub.StreamObserver<com.bank.contract.OperationResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getAccrueInterestMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getAccountStatement(com.bank.contract.AccountByIdRequest request,
        io.grpc.stub.StreamObserver<com.bank.contract.GetAccountStatementResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetAccountStatementMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getAllAccounts(com.bank.contract.EmptyRequest request,
        io.grpc.stub.StreamObserver<com.bank.contract.GetAllAccountsResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetAllAccountsMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service CoreBankService.
   */
  public static final class CoreBankServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<CoreBankServiceBlockingStub> {
    private CoreBankServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CoreBankServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new CoreBankServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.bank.contract.OperationResponse createAccount(com.bank.contract.CreateAccountRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCreateAccountMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.bank.contract.OperationResponse deposit(com.bank.contract.DepositRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getDepositMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.bank.contract.OperationResponse withdraw(com.bank.contract.WithdrawRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getWithdrawMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.bank.contract.OperationResponse accrueInterest(com.bank.contract.AccrueInterestRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getAccrueInterestMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.bank.contract.GetAccountStatementResponse getAccountStatement(com.bank.contract.AccountByIdRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetAccountStatementMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.bank.contract.GetAllAccountsResponse getAllAccounts(com.bank.contract.EmptyRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetAllAccountsMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service CoreBankService.
   */
  public static final class CoreBankServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<CoreBankServiceFutureStub> {
    private CoreBankServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CoreBankServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new CoreBankServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.bank.contract.OperationResponse> createAccount(
        com.bank.contract.CreateAccountRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCreateAccountMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.bank.contract.OperationResponse> deposit(
        com.bank.contract.DepositRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getDepositMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.bank.contract.OperationResponse> withdraw(
        com.bank.contract.WithdrawRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getWithdrawMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.bank.contract.OperationResponse> accrueInterest(
        com.bank.contract.AccrueInterestRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getAccrueInterestMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.bank.contract.GetAccountStatementResponse> getAccountStatement(
        com.bank.contract.AccountByIdRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetAccountStatementMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.bank.contract.GetAllAccountsResponse> getAllAccounts(
        com.bank.contract.EmptyRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetAllAccountsMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_CREATE_ACCOUNT = 0;
  private static final int METHODID_DEPOSIT = 1;
  private static final int METHODID_WITHDRAW = 2;
  private static final int METHODID_ACCRUE_INTEREST = 3;
  private static final int METHODID_GET_ACCOUNT_STATEMENT = 4;
  private static final int METHODID_GET_ALL_ACCOUNTS = 5;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_CREATE_ACCOUNT:
          serviceImpl.createAccount((com.bank.contract.CreateAccountRequest) request,
              (io.grpc.stub.StreamObserver<com.bank.contract.OperationResponse>) responseObserver);
          break;
        case METHODID_DEPOSIT:
          serviceImpl.deposit((com.bank.contract.DepositRequest) request,
              (io.grpc.stub.StreamObserver<com.bank.contract.OperationResponse>) responseObserver);
          break;
        case METHODID_WITHDRAW:
          serviceImpl.withdraw((com.bank.contract.WithdrawRequest) request,
              (io.grpc.stub.StreamObserver<com.bank.contract.OperationResponse>) responseObserver);
          break;
        case METHODID_ACCRUE_INTEREST:
          serviceImpl.accrueInterest((com.bank.contract.AccrueInterestRequest) request,
              (io.grpc.stub.StreamObserver<com.bank.contract.OperationResponse>) responseObserver);
          break;
        case METHODID_GET_ACCOUNT_STATEMENT:
          serviceImpl.getAccountStatement((com.bank.contract.AccountByIdRequest) request,
              (io.grpc.stub.StreamObserver<com.bank.contract.GetAccountStatementResponse>) responseObserver);
          break;
        case METHODID_GET_ALL_ACCOUNTS:
          serviceImpl.getAllAccounts((com.bank.contract.EmptyRequest) request,
              (io.grpc.stub.StreamObserver<com.bank.contract.GetAllAccountsResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getCreateAccountMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.bank.contract.CreateAccountRequest,
              com.bank.contract.OperationResponse>(
                service, METHODID_CREATE_ACCOUNT)))
        .addMethod(
          getDepositMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.bank.contract.DepositRequest,
              com.bank.contract.OperationResponse>(
                service, METHODID_DEPOSIT)))
        .addMethod(
          getWithdrawMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.bank.contract.WithdrawRequest,
              com.bank.contract.OperationResponse>(
                service, METHODID_WITHDRAW)))
        .addMethod(
          getAccrueInterestMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.bank.contract.AccrueInterestRequest,
              com.bank.contract.OperationResponse>(
                service, METHODID_ACCRUE_INTEREST)))
        .addMethod(
          getGetAccountStatementMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.bank.contract.AccountByIdRequest,
              com.bank.contract.GetAccountStatementResponse>(
                service, METHODID_GET_ACCOUNT_STATEMENT)))
        .addMethod(
          getGetAllAccountsMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.bank.contract.EmptyRequest,
              com.bank.contract.GetAllAccountsResponse>(
                service, METHODID_GET_ALL_ACCOUNTS)))
        .build();
  }

  private static abstract class CoreBankServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    CoreBankServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.bank.contract.CoreService.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("CoreBankService");
    }
  }

  private static final class CoreBankServiceFileDescriptorSupplier
      extends CoreBankServiceBaseDescriptorSupplier {
    CoreBankServiceFileDescriptorSupplier() {}
  }

  private static final class CoreBankServiceMethodDescriptorSupplier
      extends CoreBankServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    CoreBankServiceMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (CoreBankServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new CoreBankServiceFileDescriptorSupplier())
              .addMethod(getCreateAccountMethod())
              .addMethod(getDepositMethod())
              .addMethod(getWithdrawMethod())
              .addMethod(getAccrueInterestMethod())
              .addMethod(getGetAccountStatementMethod())
              .addMethod(getGetAllAccountsMethod())
              .build();
        }
      }
    }
    return result;
  }
}
