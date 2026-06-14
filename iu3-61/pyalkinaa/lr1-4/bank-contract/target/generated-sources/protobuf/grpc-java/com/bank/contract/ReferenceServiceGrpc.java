package com.bank.contract;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.64.0)",
    comments = "Source: reference_service.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class ReferenceServiceGrpc {

  private ReferenceServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "bank.contract.ReferenceService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.bank.contract.ValidateAccountTypeRequest,
      com.bank.contract.ValidateAccountTypeResponse> getValidateAccountTypeMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ValidateAccountType",
      requestType = com.bank.contract.ValidateAccountTypeRequest.class,
      responseType = com.bank.contract.ValidateAccountTypeResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.bank.contract.ValidateAccountTypeRequest,
      com.bank.contract.ValidateAccountTypeResponse> getValidateAccountTypeMethod() {
    io.grpc.MethodDescriptor<com.bank.contract.ValidateAccountTypeRequest, com.bank.contract.ValidateAccountTypeResponse> getValidateAccountTypeMethod;
    if ((getValidateAccountTypeMethod = ReferenceServiceGrpc.getValidateAccountTypeMethod) == null) {
      synchronized (ReferenceServiceGrpc.class) {
        if ((getValidateAccountTypeMethod = ReferenceServiceGrpc.getValidateAccountTypeMethod) == null) {
          ReferenceServiceGrpc.getValidateAccountTypeMethod = getValidateAccountTypeMethod =
              io.grpc.MethodDescriptor.<com.bank.contract.ValidateAccountTypeRequest, com.bank.contract.ValidateAccountTypeResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ValidateAccountType"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bank.contract.ValidateAccountTypeRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bank.contract.ValidateAccountTypeResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ReferenceServiceMethodDescriptorSupplier("ValidateAccountType"))
              .build();
        }
      }
    }
    return getValidateAccountTypeMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.bank.contract.ValidateAmountRequest,
      com.bank.contract.ValidateAmountResponse> getValidateAmountMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ValidateAmount",
      requestType = com.bank.contract.ValidateAmountRequest.class,
      responseType = com.bank.contract.ValidateAmountResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.bank.contract.ValidateAmountRequest,
      com.bank.contract.ValidateAmountResponse> getValidateAmountMethod() {
    io.grpc.MethodDescriptor<com.bank.contract.ValidateAmountRequest, com.bank.contract.ValidateAmountResponse> getValidateAmountMethod;
    if ((getValidateAmountMethod = ReferenceServiceGrpc.getValidateAmountMethod) == null) {
      synchronized (ReferenceServiceGrpc.class) {
        if ((getValidateAmountMethod = ReferenceServiceGrpc.getValidateAmountMethod) == null) {
          ReferenceServiceGrpc.getValidateAmountMethod = getValidateAmountMethod =
              io.grpc.MethodDescriptor.<com.bank.contract.ValidateAmountRequest, com.bank.contract.ValidateAmountResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ValidateAmount"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bank.contract.ValidateAmountRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bank.contract.ValidateAmountResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ReferenceServiceMethodDescriptorSupplier("ValidateAmount"))
              .build();
        }
      }
    }
    return getValidateAmountMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.bank.contract.GetInterestRuleRequest,
      com.bank.contract.GetInterestRuleResponse> getGetInterestRuleMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetInterestRule",
      requestType = com.bank.contract.GetInterestRuleRequest.class,
      responseType = com.bank.contract.GetInterestRuleResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.bank.contract.GetInterestRuleRequest,
      com.bank.contract.GetInterestRuleResponse> getGetInterestRuleMethod() {
    io.grpc.MethodDescriptor<com.bank.contract.GetInterestRuleRequest, com.bank.contract.GetInterestRuleResponse> getGetInterestRuleMethod;
    if ((getGetInterestRuleMethod = ReferenceServiceGrpc.getGetInterestRuleMethod) == null) {
      synchronized (ReferenceServiceGrpc.class) {
        if ((getGetInterestRuleMethod = ReferenceServiceGrpc.getGetInterestRuleMethod) == null) {
          ReferenceServiceGrpc.getGetInterestRuleMethod = getGetInterestRuleMethod =
              io.grpc.MethodDescriptor.<com.bank.contract.GetInterestRuleRequest, com.bank.contract.GetInterestRuleResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetInterestRule"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bank.contract.GetInterestRuleRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bank.contract.GetInterestRuleResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ReferenceServiceMethodDescriptorSupplier("GetInterestRule"))
              .build();
        }
      }
    }
    return getGetInterestRuleMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static ReferenceServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ReferenceServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ReferenceServiceStub>() {
        @java.lang.Override
        public ReferenceServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ReferenceServiceStub(channel, callOptions);
        }
      };
    return ReferenceServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static ReferenceServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ReferenceServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ReferenceServiceBlockingStub>() {
        @java.lang.Override
        public ReferenceServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ReferenceServiceBlockingStub(channel, callOptions);
        }
      };
    return ReferenceServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static ReferenceServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ReferenceServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ReferenceServiceFutureStub>() {
        @java.lang.Override
        public ReferenceServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ReferenceServiceFutureStub(channel, callOptions);
        }
      };
    return ReferenceServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void validateAccountType(com.bank.contract.ValidateAccountTypeRequest request,
        io.grpc.stub.StreamObserver<com.bank.contract.ValidateAccountTypeResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getValidateAccountTypeMethod(), responseObserver);
    }

    /**
     */
    default void validateAmount(com.bank.contract.ValidateAmountRequest request,
        io.grpc.stub.StreamObserver<com.bank.contract.ValidateAmountResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getValidateAmountMethod(), responseObserver);
    }

    /**
     */
    default void getInterestRule(com.bank.contract.GetInterestRuleRequest request,
        io.grpc.stub.StreamObserver<com.bank.contract.GetInterestRuleResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetInterestRuleMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service ReferenceService.
   */
  public static abstract class ReferenceServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return ReferenceServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service ReferenceService.
   */
  public static final class ReferenceServiceStub
      extends io.grpc.stub.AbstractAsyncStub<ReferenceServiceStub> {
    private ReferenceServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ReferenceServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ReferenceServiceStub(channel, callOptions);
    }

    /**
     */
    public void validateAccountType(com.bank.contract.ValidateAccountTypeRequest request,
        io.grpc.stub.StreamObserver<com.bank.contract.ValidateAccountTypeResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getValidateAccountTypeMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void validateAmount(com.bank.contract.ValidateAmountRequest request,
        io.grpc.stub.StreamObserver<com.bank.contract.ValidateAmountResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getValidateAmountMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getInterestRule(com.bank.contract.GetInterestRuleRequest request,
        io.grpc.stub.StreamObserver<com.bank.contract.GetInterestRuleResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetInterestRuleMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service ReferenceService.
   */
  public static final class ReferenceServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<ReferenceServiceBlockingStub> {
    private ReferenceServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ReferenceServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ReferenceServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.bank.contract.ValidateAccountTypeResponse validateAccountType(com.bank.contract.ValidateAccountTypeRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getValidateAccountTypeMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.bank.contract.ValidateAmountResponse validateAmount(com.bank.contract.ValidateAmountRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getValidateAmountMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.bank.contract.GetInterestRuleResponse getInterestRule(com.bank.contract.GetInterestRuleRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetInterestRuleMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service ReferenceService.
   */
  public static final class ReferenceServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<ReferenceServiceFutureStub> {
    private ReferenceServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ReferenceServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ReferenceServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.bank.contract.ValidateAccountTypeResponse> validateAccountType(
        com.bank.contract.ValidateAccountTypeRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getValidateAccountTypeMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.bank.contract.ValidateAmountResponse> validateAmount(
        com.bank.contract.ValidateAmountRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getValidateAmountMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.bank.contract.GetInterestRuleResponse> getInterestRule(
        com.bank.contract.GetInterestRuleRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetInterestRuleMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_VALIDATE_ACCOUNT_TYPE = 0;
  private static final int METHODID_VALIDATE_AMOUNT = 1;
  private static final int METHODID_GET_INTEREST_RULE = 2;

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
        case METHODID_VALIDATE_ACCOUNT_TYPE:
          serviceImpl.validateAccountType((com.bank.contract.ValidateAccountTypeRequest) request,
              (io.grpc.stub.StreamObserver<com.bank.contract.ValidateAccountTypeResponse>) responseObserver);
          break;
        case METHODID_VALIDATE_AMOUNT:
          serviceImpl.validateAmount((com.bank.contract.ValidateAmountRequest) request,
              (io.grpc.stub.StreamObserver<com.bank.contract.ValidateAmountResponse>) responseObserver);
          break;
        case METHODID_GET_INTEREST_RULE:
          serviceImpl.getInterestRule((com.bank.contract.GetInterestRuleRequest) request,
              (io.grpc.stub.StreamObserver<com.bank.contract.GetInterestRuleResponse>) responseObserver);
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
          getValidateAccountTypeMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.bank.contract.ValidateAccountTypeRequest,
              com.bank.contract.ValidateAccountTypeResponse>(
                service, METHODID_VALIDATE_ACCOUNT_TYPE)))
        .addMethod(
          getValidateAmountMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.bank.contract.ValidateAmountRequest,
              com.bank.contract.ValidateAmountResponse>(
                service, METHODID_VALIDATE_AMOUNT)))
        .addMethod(
          getGetInterestRuleMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.bank.contract.GetInterestRuleRequest,
              com.bank.contract.GetInterestRuleResponse>(
                service, METHODID_GET_INTEREST_RULE)))
        .build();
  }

  private static abstract class ReferenceServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    ReferenceServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.bank.contract.ReferenceServiceOuterClass.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("ReferenceService");
    }
  }

  private static final class ReferenceServiceFileDescriptorSupplier
      extends ReferenceServiceBaseDescriptorSupplier {
    ReferenceServiceFileDescriptorSupplier() {}
  }

  private static final class ReferenceServiceMethodDescriptorSupplier
      extends ReferenceServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    ReferenceServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (ReferenceServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new ReferenceServiceFileDescriptorSupplier())
              .addMethod(getValidateAccountTypeMethod())
              .addMethod(getValidateAmountMethod())
              .addMethod(getGetInterestRuleMethod())
              .build();
        }
      }
    }
    return result;
  }
}
