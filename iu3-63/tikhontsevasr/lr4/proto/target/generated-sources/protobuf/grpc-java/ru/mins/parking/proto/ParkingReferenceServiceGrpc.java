package ru.mins.parking.proto;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.64.0)",
    comments = "Source: parking.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class ParkingReferenceServiceGrpc {

  private ParkingReferenceServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "parking.ParkingReferenceService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<ru.mins.parking.proto.TariffRequest,
      ru.mins.parking.proto.TariffResponse> getGetTariffByTypeMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetTariffByType",
      requestType = ru.mins.parking.proto.TariffRequest.class,
      responseType = ru.mins.parking.proto.TariffResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<ru.mins.parking.proto.TariffRequest,
      ru.mins.parking.proto.TariffResponse> getGetTariffByTypeMethod() {
    io.grpc.MethodDescriptor<ru.mins.parking.proto.TariffRequest, ru.mins.parking.proto.TariffResponse> getGetTariffByTypeMethod;
    if ((getGetTariffByTypeMethod = ParkingReferenceServiceGrpc.getGetTariffByTypeMethod) == null) {
      synchronized (ParkingReferenceServiceGrpc.class) {
        if ((getGetTariffByTypeMethod = ParkingReferenceServiceGrpc.getGetTariffByTypeMethod) == null) {
          ParkingReferenceServiceGrpc.getGetTariffByTypeMethod = getGetTariffByTypeMethod =
              io.grpc.MethodDescriptor.<ru.mins.parking.proto.TariffRequest, ru.mins.parking.proto.TariffResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetTariffByType"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ru.mins.parking.proto.TariffRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ru.mins.parking.proto.TariffResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ParkingReferenceServiceMethodDescriptorSupplier("GetTariffByType"))
              .build();
        }
      }
    }
    return getGetTariffByTypeMethod;
  }

  private static volatile io.grpc.MethodDescriptor<ru.mins.parking.proto.SpotTypeRequest,
      ru.mins.parking.proto.SpotTypeResponse> getGetSpotTypeInfoMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetSpotTypeInfo",
      requestType = ru.mins.parking.proto.SpotTypeRequest.class,
      responseType = ru.mins.parking.proto.SpotTypeResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<ru.mins.parking.proto.SpotTypeRequest,
      ru.mins.parking.proto.SpotTypeResponse> getGetSpotTypeInfoMethod() {
    io.grpc.MethodDescriptor<ru.mins.parking.proto.SpotTypeRequest, ru.mins.parking.proto.SpotTypeResponse> getGetSpotTypeInfoMethod;
    if ((getGetSpotTypeInfoMethod = ParkingReferenceServiceGrpc.getGetSpotTypeInfoMethod) == null) {
      synchronized (ParkingReferenceServiceGrpc.class) {
        if ((getGetSpotTypeInfoMethod = ParkingReferenceServiceGrpc.getGetSpotTypeInfoMethod) == null) {
          ParkingReferenceServiceGrpc.getGetSpotTypeInfoMethod = getGetSpotTypeInfoMethod =
              io.grpc.MethodDescriptor.<ru.mins.parking.proto.SpotTypeRequest, ru.mins.parking.proto.SpotTypeResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetSpotTypeInfo"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ru.mins.parking.proto.SpotTypeRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ru.mins.parking.proto.SpotTypeResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ParkingReferenceServiceMethodDescriptorSupplier("GetSpotTypeInfo"))
              .build();
        }
      }
    }
    return getGetSpotTypeInfoMethod;
  }

  private static volatile io.grpc.MethodDescriptor<ru.mins.parking.proto.VehicleValidationRequest,
      ru.mins.parking.proto.VehicleValidationResponse> getValidateVehicleMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ValidateVehicle",
      requestType = ru.mins.parking.proto.VehicleValidationRequest.class,
      responseType = ru.mins.parking.proto.VehicleValidationResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<ru.mins.parking.proto.VehicleValidationRequest,
      ru.mins.parking.proto.VehicleValidationResponse> getValidateVehicleMethod() {
    io.grpc.MethodDescriptor<ru.mins.parking.proto.VehicleValidationRequest, ru.mins.parking.proto.VehicleValidationResponse> getValidateVehicleMethod;
    if ((getValidateVehicleMethod = ParkingReferenceServiceGrpc.getValidateVehicleMethod) == null) {
      synchronized (ParkingReferenceServiceGrpc.class) {
        if ((getValidateVehicleMethod = ParkingReferenceServiceGrpc.getValidateVehicleMethod) == null) {
          ParkingReferenceServiceGrpc.getValidateVehicleMethod = getValidateVehicleMethod =
              io.grpc.MethodDescriptor.<ru.mins.parking.proto.VehicleValidationRequest, ru.mins.parking.proto.VehicleValidationResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ValidateVehicle"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ru.mins.parking.proto.VehicleValidationRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ru.mins.parking.proto.VehicleValidationResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ParkingReferenceServiceMethodDescriptorSupplier("ValidateVehicle"))
              .build();
        }
      }
    }
    return getValidateVehicleMethod;
  }

  private static volatile io.grpc.MethodDescriptor<ru.mins.parking.proto.VipDiscountRequest,
      ru.mins.parking.proto.VipDiscountResponse> getGetVipDiscountMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetVipDiscount",
      requestType = ru.mins.parking.proto.VipDiscountRequest.class,
      responseType = ru.mins.parking.proto.VipDiscountResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<ru.mins.parking.proto.VipDiscountRequest,
      ru.mins.parking.proto.VipDiscountResponse> getGetVipDiscountMethod() {
    io.grpc.MethodDescriptor<ru.mins.parking.proto.VipDiscountRequest, ru.mins.parking.proto.VipDiscountResponse> getGetVipDiscountMethod;
    if ((getGetVipDiscountMethod = ParkingReferenceServiceGrpc.getGetVipDiscountMethod) == null) {
      synchronized (ParkingReferenceServiceGrpc.class) {
        if ((getGetVipDiscountMethod = ParkingReferenceServiceGrpc.getGetVipDiscountMethod) == null) {
          ParkingReferenceServiceGrpc.getGetVipDiscountMethod = getGetVipDiscountMethod =
              io.grpc.MethodDescriptor.<ru.mins.parking.proto.VipDiscountRequest, ru.mins.parking.proto.VipDiscountResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetVipDiscount"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ru.mins.parking.proto.VipDiscountRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ru.mins.parking.proto.VipDiscountResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ParkingReferenceServiceMethodDescriptorSupplier("GetVipDiscount"))
              .build();
        }
      }
    }
    return getGetVipDiscountMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static ParkingReferenceServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ParkingReferenceServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ParkingReferenceServiceStub>() {
        @java.lang.Override
        public ParkingReferenceServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ParkingReferenceServiceStub(channel, callOptions);
        }
      };
    return ParkingReferenceServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static ParkingReferenceServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ParkingReferenceServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ParkingReferenceServiceBlockingStub>() {
        @java.lang.Override
        public ParkingReferenceServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ParkingReferenceServiceBlockingStub(channel, callOptions);
        }
      };
    return ParkingReferenceServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static ParkingReferenceServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ParkingReferenceServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ParkingReferenceServiceFutureStub>() {
        @java.lang.Override
        public ParkingReferenceServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ParkingReferenceServiceFutureStub(channel, callOptions);
        }
      };
    return ParkingReferenceServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void getTariffByType(ru.mins.parking.proto.TariffRequest request,
        io.grpc.stub.StreamObserver<ru.mins.parking.proto.TariffResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetTariffByTypeMethod(), responseObserver);
    }

    /**
     */
    default void getSpotTypeInfo(ru.mins.parking.proto.SpotTypeRequest request,
        io.grpc.stub.StreamObserver<ru.mins.parking.proto.SpotTypeResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetSpotTypeInfoMethod(), responseObserver);
    }

    /**
     */
    default void validateVehicle(ru.mins.parking.proto.VehicleValidationRequest request,
        io.grpc.stub.StreamObserver<ru.mins.parking.proto.VehicleValidationResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getValidateVehicleMethod(), responseObserver);
    }

    /**
     */
    default void getVipDiscount(ru.mins.parking.proto.VipDiscountRequest request,
        io.grpc.stub.StreamObserver<ru.mins.parking.proto.VipDiscountResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetVipDiscountMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service ParkingReferenceService.
   */
  public static abstract class ParkingReferenceServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return ParkingReferenceServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service ParkingReferenceService.
   */
  public static final class ParkingReferenceServiceStub
      extends io.grpc.stub.AbstractAsyncStub<ParkingReferenceServiceStub> {
    private ParkingReferenceServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ParkingReferenceServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ParkingReferenceServiceStub(channel, callOptions);
    }

    /**
     */
    public void getTariffByType(ru.mins.parking.proto.TariffRequest request,
        io.grpc.stub.StreamObserver<ru.mins.parking.proto.TariffResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetTariffByTypeMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getSpotTypeInfo(ru.mins.parking.proto.SpotTypeRequest request,
        io.grpc.stub.StreamObserver<ru.mins.parking.proto.SpotTypeResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetSpotTypeInfoMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void validateVehicle(ru.mins.parking.proto.VehicleValidationRequest request,
        io.grpc.stub.StreamObserver<ru.mins.parking.proto.VehicleValidationResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getValidateVehicleMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getVipDiscount(ru.mins.parking.proto.VipDiscountRequest request,
        io.grpc.stub.StreamObserver<ru.mins.parking.proto.VipDiscountResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetVipDiscountMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service ParkingReferenceService.
   */
  public static final class ParkingReferenceServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<ParkingReferenceServiceBlockingStub> {
    private ParkingReferenceServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ParkingReferenceServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ParkingReferenceServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public ru.mins.parking.proto.TariffResponse getTariffByType(ru.mins.parking.proto.TariffRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetTariffByTypeMethod(), getCallOptions(), request);
    }

    /**
     */
    public ru.mins.parking.proto.SpotTypeResponse getSpotTypeInfo(ru.mins.parking.proto.SpotTypeRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetSpotTypeInfoMethod(), getCallOptions(), request);
    }

    /**
     */
    public ru.mins.parking.proto.VehicleValidationResponse validateVehicle(ru.mins.parking.proto.VehicleValidationRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getValidateVehicleMethod(), getCallOptions(), request);
    }

    /**
     */
    public ru.mins.parking.proto.VipDiscountResponse getVipDiscount(ru.mins.parking.proto.VipDiscountRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetVipDiscountMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service ParkingReferenceService.
   */
  public static final class ParkingReferenceServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<ParkingReferenceServiceFutureStub> {
    private ParkingReferenceServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ParkingReferenceServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ParkingReferenceServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<ru.mins.parking.proto.TariffResponse> getTariffByType(
        ru.mins.parking.proto.TariffRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetTariffByTypeMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<ru.mins.parking.proto.SpotTypeResponse> getSpotTypeInfo(
        ru.mins.parking.proto.SpotTypeRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetSpotTypeInfoMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<ru.mins.parking.proto.VehicleValidationResponse> validateVehicle(
        ru.mins.parking.proto.VehicleValidationRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getValidateVehicleMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<ru.mins.parking.proto.VipDiscountResponse> getVipDiscount(
        ru.mins.parking.proto.VipDiscountRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetVipDiscountMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_TARIFF_BY_TYPE = 0;
  private static final int METHODID_GET_SPOT_TYPE_INFO = 1;
  private static final int METHODID_VALIDATE_VEHICLE = 2;
  private static final int METHODID_GET_VIP_DISCOUNT = 3;

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
        case METHODID_GET_TARIFF_BY_TYPE:
          serviceImpl.getTariffByType((ru.mins.parking.proto.TariffRequest) request,
              (io.grpc.stub.StreamObserver<ru.mins.parking.proto.TariffResponse>) responseObserver);
          break;
        case METHODID_GET_SPOT_TYPE_INFO:
          serviceImpl.getSpotTypeInfo((ru.mins.parking.proto.SpotTypeRequest) request,
              (io.grpc.stub.StreamObserver<ru.mins.parking.proto.SpotTypeResponse>) responseObserver);
          break;
        case METHODID_VALIDATE_VEHICLE:
          serviceImpl.validateVehicle((ru.mins.parking.proto.VehicleValidationRequest) request,
              (io.grpc.stub.StreamObserver<ru.mins.parking.proto.VehicleValidationResponse>) responseObserver);
          break;
        case METHODID_GET_VIP_DISCOUNT:
          serviceImpl.getVipDiscount((ru.mins.parking.proto.VipDiscountRequest) request,
              (io.grpc.stub.StreamObserver<ru.mins.parking.proto.VipDiscountResponse>) responseObserver);
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
          getGetTariffByTypeMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              ru.mins.parking.proto.TariffRequest,
              ru.mins.parking.proto.TariffResponse>(
                service, METHODID_GET_TARIFF_BY_TYPE)))
        .addMethod(
          getGetSpotTypeInfoMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              ru.mins.parking.proto.SpotTypeRequest,
              ru.mins.parking.proto.SpotTypeResponse>(
                service, METHODID_GET_SPOT_TYPE_INFO)))
        .addMethod(
          getValidateVehicleMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              ru.mins.parking.proto.VehicleValidationRequest,
              ru.mins.parking.proto.VehicleValidationResponse>(
                service, METHODID_VALIDATE_VEHICLE)))
        .addMethod(
          getGetVipDiscountMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              ru.mins.parking.proto.VipDiscountRequest,
              ru.mins.parking.proto.VipDiscountResponse>(
                service, METHODID_GET_VIP_DISCOUNT)))
        .build();
  }

  private static abstract class ParkingReferenceServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    ParkingReferenceServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return ru.mins.parking.proto.ParkingProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("ParkingReferenceService");
    }
  }

  private static final class ParkingReferenceServiceFileDescriptorSupplier
      extends ParkingReferenceServiceBaseDescriptorSupplier {
    ParkingReferenceServiceFileDescriptorSupplier() {}
  }

  private static final class ParkingReferenceServiceMethodDescriptorSupplier
      extends ParkingReferenceServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    ParkingReferenceServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (ParkingReferenceServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new ParkingReferenceServiceFileDescriptorSupplier())
              .addMethod(getGetTariffByTypeMethod())
              .addMethod(getGetSpotTypeInfoMethod())
              .addMethod(getValidateVehicleMethod())
              .addMethod(getGetVipDiscountMethod())
              .build();
        }
      }
    }
    return result;
  }
}
