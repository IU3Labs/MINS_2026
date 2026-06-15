package org.parking.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.59.0)",
    comments = "Source: parking_ref.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class ReferenceServiceGrpc {

  private ReferenceServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "parking.ref.ReferenceService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<org.parking.grpc.ValidationRequest,
      org.parking.grpc.ValidationResponse> getGetParkingRulesMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetParkingRules",
      requestType = org.parking.grpc.ValidationRequest.class,
      responseType = org.parking.grpc.ValidationResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.parking.grpc.ValidationRequest,
      org.parking.grpc.ValidationResponse> getGetParkingRulesMethod() {
    io.grpc.MethodDescriptor<org.parking.grpc.ValidationRequest, org.parking.grpc.ValidationResponse> getGetParkingRulesMethod;
    if ((getGetParkingRulesMethod = ReferenceServiceGrpc.getGetParkingRulesMethod) == null) {
      synchronized (ReferenceServiceGrpc.class) {
        if ((getGetParkingRulesMethod = ReferenceServiceGrpc.getGetParkingRulesMethod) == null) {
          ReferenceServiceGrpc.getGetParkingRulesMethod = getGetParkingRulesMethod =
              io.grpc.MethodDescriptor.<org.parking.grpc.ValidationRequest, org.parking.grpc.ValidationResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetParkingRules"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.parking.grpc.ValidationRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.parking.grpc.ValidationResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ReferenceServiceMethodDescriptorSupplier("GetParkingRules"))
              .build();
        }
      }
    }
    return getGetParkingRulesMethod;
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
    default void getParkingRules(org.parking.grpc.ValidationRequest request,
        io.grpc.stub.StreamObserver<org.parking.grpc.ValidationResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetParkingRulesMethod(), responseObserver);
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
    public void getParkingRules(org.parking.grpc.ValidationRequest request,
        io.grpc.stub.StreamObserver<org.parking.grpc.ValidationResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetParkingRulesMethod(), getCallOptions()), request, responseObserver);
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
    public org.parking.grpc.ValidationResponse getParkingRules(org.parking.grpc.ValidationRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetParkingRulesMethod(), getCallOptions(), request);
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
    public com.google.common.util.concurrent.ListenableFuture<org.parking.grpc.ValidationResponse> getParkingRules(
        org.parking.grpc.ValidationRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetParkingRulesMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_PARKING_RULES = 0;

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
        case METHODID_GET_PARKING_RULES:
          serviceImpl.getParkingRules((org.parking.grpc.ValidationRequest) request,
              (io.grpc.stub.StreamObserver<org.parking.grpc.ValidationResponse>) responseObserver);
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
          getGetParkingRulesMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              org.parking.grpc.ValidationRequest,
              org.parking.grpc.ValidationResponse>(
                service, METHODID_GET_PARKING_RULES)))
        .build();
  }

  private static abstract class ReferenceServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    ReferenceServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return org.parking.grpc.ParkingRef.getDescriptor();
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
              .addMethod(getGetParkingRulesMethod())
              .build();
        }
      }
    }
    return result;
  }
}
