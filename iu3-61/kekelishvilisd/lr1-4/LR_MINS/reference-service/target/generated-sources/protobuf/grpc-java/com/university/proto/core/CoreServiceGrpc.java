package com.university.proto.core;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 * <pre>
 * ===== Service Definition =====
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.59.0)",
    comments = "Source: core_service.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class CoreServiceGrpc {

  private CoreServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "core.CoreService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.university.proto.core.RegisterStudentRequest,
      com.university.proto.core.RegisterStudentResponse> getRegisterStudentMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "RegisterStudent",
      requestType = com.university.proto.core.RegisterStudentRequest.class,
      responseType = com.university.proto.core.RegisterStudentResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.university.proto.core.RegisterStudentRequest,
      com.university.proto.core.RegisterStudentResponse> getRegisterStudentMethod() {
    io.grpc.MethodDescriptor<com.university.proto.core.RegisterStudentRequest, com.university.proto.core.RegisterStudentResponse> getRegisterStudentMethod;
    if ((getRegisterStudentMethod = CoreServiceGrpc.getRegisterStudentMethod) == null) {
      synchronized (CoreServiceGrpc.class) {
        if ((getRegisterStudentMethod = CoreServiceGrpc.getRegisterStudentMethod) == null) {
          CoreServiceGrpc.getRegisterStudentMethod = getRegisterStudentMethod =
              io.grpc.MethodDescriptor.<com.university.proto.core.RegisterStudentRequest, com.university.proto.core.RegisterStudentResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "RegisterStudent"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.university.proto.core.RegisterStudentRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.university.proto.core.RegisterStudentResponse.getDefaultInstance()))
              .setSchemaDescriptor(new CoreServiceMethodDescriptorSupplier("RegisterStudent"))
              .build();
        }
      }
    }
    return getRegisterStudentMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.university.proto.core.AddGradeRequest,
      com.university.proto.core.AddGradeResponse> getAddGradeMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "AddGrade",
      requestType = com.university.proto.core.AddGradeRequest.class,
      responseType = com.university.proto.core.AddGradeResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.university.proto.core.AddGradeRequest,
      com.university.proto.core.AddGradeResponse> getAddGradeMethod() {
    io.grpc.MethodDescriptor<com.university.proto.core.AddGradeRequest, com.university.proto.core.AddGradeResponse> getAddGradeMethod;
    if ((getAddGradeMethod = CoreServiceGrpc.getAddGradeMethod) == null) {
      synchronized (CoreServiceGrpc.class) {
        if ((getAddGradeMethod = CoreServiceGrpc.getAddGradeMethod) == null) {
          CoreServiceGrpc.getAddGradeMethod = getAddGradeMethod =
              io.grpc.MethodDescriptor.<com.university.proto.core.AddGradeRequest, com.university.proto.core.AddGradeResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "AddGrade"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.university.proto.core.AddGradeRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.university.proto.core.AddGradeResponse.getDefaultInstance()))
              .setSchemaDescriptor(new CoreServiceMethodDescriptorSupplier("AddGrade"))
              .build();
        }
      }
    }
    return getAddGradeMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.university.proto.core.MarkAttendanceRequest,
      com.university.proto.core.MarkAttendanceResponse> getMarkAttendanceMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "MarkAttendance",
      requestType = com.university.proto.core.MarkAttendanceRequest.class,
      responseType = com.university.proto.core.MarkAttendanceResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.university.proto.core.MarkAttendanceRequest,
      com.university.proto.core.MarkAttendanceResponse> getMarkAttendanceMethod() {
    io.grpc.MethodDescriptor<com.university.proto.core.MarkAttendanceRequest, com.university.proto.core.MarkAttendanceResponse> getMarkAttendanceMethod;
    if ((getMarkAttendanceMethod = CoreServiceGrpc.getMarkAttendanceMethod) == null) {
      synchronized (CoreServiceGrpc.class) {
        if ((getMarkAttendanceMethod = CoreServiceGrpc.getMarkAttendanceMethod) == null) {
          CoreServiceGrpc.getMarkAttendanceMethod = getMarkAttendanceMethod =
              io.grpc.MethodDescriptor.<com.university.proto.core.MarkAttendanceRequest, com.university.proto.core.MarkAttendanceResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "MarkAttendance"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.university.proto.core.MarkAttendanceRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.university.proto.core.MarkAttendanceResponse.getDefaultInstance()))
              .setSchemaDescriptor(new CoreServiceMethodDescriptorSupplier("MarkAttendance"))
              .build();
        }
      }
    }
    return getMarkAttendanceMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.university.proto.core.GetGradeReportRequest,
      com.university.proto.core.GetGradeReportResponse> getGetGradeReportMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetGradeReport",
      requestType = com.university.proto.core.GetGradeReportRequest.class,
      responseType = com.university.proto.core.GetGradeReportResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.university.proto.core.GetGradeReportRequest,
      com.university.proto.core.GetGradeReportResponse> getGetGradeReportMethod() {
    io.grpc.MethodDescriptor<com.university.proto.core.GetGradeReportRequest, com.university.proto.core.GetGradeReportResponse> getGetGradeReportMethod;
    if ((getGetGradeReportMethod = CoreServiceGrpc.getGetGradeReportMethod) == null) {
      synchronized (CoreServiceGrpc.class) {
        if ((getGetGradeReportMethod = CoreServiceGrpc.getGetGradeReportMethod) == null) {
          CoreServiceGrpc.getGetGradeReportMethod = getGetGradeReportMethod =
              io.grpc.MethodDescriptor.<com.university.proto.core.GetGradeReportRequest, com.university.proto.core.GetGradeReportResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetGradeReport"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.university.proto.core.GetGradeReportRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.university.proto.core.GetGradeReportResponse.getDefaultInstance()))
              .setSchemaDescriptor(new CoreServiceMethodDescriptorSupplier("GetGradeReport"))
              .build();
        }
      }
    }
    return getGetGradeReportMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.university.proto.core.CalculateDiscountRequest,
      com.university.proto.core.CalculateDiscountResponse> getCalculateDiscountMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CalculateDiscount",
      requestType = com.university.proto.core.CalculateDiscountRequest.class,
      responseType = com.university.proto.core.CalculateDiscountResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.university.proto.core.CalculateDiscountRequest,
      com.university.proto.core.CalculateDiscountResponse> getCalculateDiscountMethod() {
    io.grpc.MethodDescriptor<com.university.proto.core.CalculateDiscountRequest, com.university.proto.core.CalculateDiscountResponse> getCalculateDiscountMethod;
    if ((getCalculateDiscountMethod = CoreServiceGrpc.getCalculateDiscountMethod) == null) {
      synchronized (CoreServiceGrpc.class) {
        if ((getCalculateDiscountMethod = CoreServiceGrpc.getCalculateDiscountMethod) == null) {
          CoreServiceGrpc.getCalculateDiscountMethod = getCalculateDiscountMethod =
              io.grpc.MethodDescriptor.<com.university.proto.core.CalculateDiscountRequest, com.university.proto.core.CalculateDiscountResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CalculateDiscount"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.university.proto.core.CalculateDiscountRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.university.proto.core.CalculateDiscountResponse.getDefaultInstance()))
              .setSchemaDescriptor(new CoreServiceMethodDescriptorSupplier("CalculateDiscount"))
              .build();
        }
      }
    }
    return getCalculateDiscountMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.university.proto.core.CoreScheduleRequest,
      com.university.proto.core.CoreScheduleResponse> getGetScheduleMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetSchedule",
      requestType = com.university.proto.core.CoreScheduleRequest.class,
      responseType = com.university.proto.core.CoreScheduleResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.university.proto.core.CoreScheduleRequest,
      com.university.proto.core.CoreScheduleResponse> getGetScheduleMethod() {
    io.grpc.MethodDescriptor<com.university.proto.core.CoreScheduleRequest, com.university.proto.core.CoreScheduleResponse> getGetScheduleMethod;
    if ((getGetScheduleMethod = CoreServiceGrpc.getGetScheduleMethod) == null) {
      synchronized (CoreServiceGrpc.class) {
        if ((getGetScheduleMethod = CoreServiceGrpc.getGetScheduleMethod) == null) {
          CoreServiceGrpc.getGetScheduleMethod = getGetScheduleMethod =
              io.grpc.MethodDescriptor.<com.university.proto.core.CoreScheduleRequest, com.university.proto.core.CoreScheduleResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetSchedule"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.university.proto.core.CoreScheduleRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.university.proto.core.CoreScheduleResponse.getDefaultInstance()))
              .setSchemaDescriptor(new CoreServiceMethodDescriptorSupplier("GetSchedule"))
              .build();
        }
      }
    }
    return getGetScheduleMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.university.proto.core.AddLessonRequest,
      com.university.proto.core.AddLessonResponse> getAddLessonMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "AddLesson",
      requestType = com.university.proto.core.AddLessonRequest.class,
      responseType = com.university.proto.core.AddLessonResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.university.proto.core.AddLessonRequest,
      com.university.proto.core.AddLessonResponse> getAddLessonMethod() {
    io.grpc.MethodDescriptor<com.university.proto.core.AddLessonRequest, com.university.proto.core.AddLessonResponse> getAddLessonMethod;
    if ((getAddLessonMethod = CoreServiceGrpc.getAddLessonMethod) == null) {
      synchronized (CoreServiceGrpc.class) {
        if ((getAddLessonMethod = CoreServiceGrpc.getAddLessonMethod) == null) {
          CoreServiceGrpc.getAddLessonMethod = getAddLessonMethod =
              io.grpc.MethodDescriptor.<com.university.proto.core.AddLessonRequest, com.university.proto.core.AddLessonResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "AddLesson"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.university.proto.core.AddLessonRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.university.proto.core.AddLessonResponse.getDefaultInstance()))
              .setSchemaDescriptor(new CoreServiceMethodDescriptorSupplier("AddLesson"))
              .build();
        }
      }
    }
    return getAddLessonMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static CoreServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<CoreServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<CoreServiceStub>() {
        @java.lang.Override
        public CoreServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new CoreServiceStub(channel, callOptions);
        }
      };
    return CoreServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static CoreServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<CoreServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<CoreServiceBlockingStub>() {
        @java.lang.Override
        public CoreServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new CoreServiceBlockingStub(channel, callOptions);
        }
      };
    return CoreServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static CoreServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<CoreServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<CoreServiceFutureStub>() {
        @java.lang.Override
        public CoreServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new CoreServiceFutureStub(channel, callOptions);
        }
      };
    return CoreServiceFutureStub.newStub(factory, channel);
  }

  /**
   * <pre>
   * ===== Service Definition =====
   * </pre>
   */
  public interface AsyncService {

    /**
     */
    default void registerStudent(com.university.proto.core.RegisterStudentRequest request,
        io.grpc.stub.StreamObserver<com.university.proto.core.RegisterStudentResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getRegisterStudentMethod(), responseObserver);
    }

    /**
     */
    default void addGrade(com.university.proto.core.AddGradeRequest request,
        io.grpc.stub.StreamObserver<com.university.proto.core.AddGradeResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getAddGradeMethod(), responseObserver);
    }

    /**
     */
    default void markAttendance(com.university.proto.core.MarkAttendanceRequest request,
        io.grpc.stub.StreamObserver<com.university.proto.core.MarkAttendanceResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getMarkAttendanceMethod(), responseObserver);
    }

    /**
     */
    default void getGradeReport(com.university.proto.core.GetGradeReportRequest request,
        io.grpc.stub.StreamObserver<com.university.proto.core.GetGradeReportResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetGradeReportMethod(), responseObserver);
    }

    /**
     */
    default void calculateDiscount(com.university.proto.core.CalculateDiscountRequest request,
        io.grpc.stub.StreamObserver<com.university.proto.core.CalculateDiscountResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCalculateDiscountMethod(), responseObserver);
    }

    /**
     */
    default void getSchedule(com.university.proto.core.CoreScheduleRequest request,
        io.grpc.stub.StreamObserver<com.university.proto.core.CoreScheduleResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetScheduleMethod(), responseObserver);
    }

    /**
     */
    default void addLesson(com.university.proto.core.AddLessonRequest request,
        io.grpc.stub.StreamObserver<com.university.proto.core.AddLessonResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getAddLessonMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service CoreService.
   * <pre>
   * ===== Service Definition =====
   * </pre>
   */
  public static abstract class CoreServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return CoreServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service CoreService.
   * <pre>
   * ===== Service Definition =====
   * </pre>
   */
  public static final class CoreServiceStub
      extends io.grpc.stub.AbstractAsyncStub<CoreServiceStub> {
    private CoreServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CoreServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new CoreServiceStub(channel, callOptions);
    }

    /**
     */
    public void registerStudent(com.university.proto.core.RegisterStudentRequest request,
        io.grpc.stub.StreamObserver<com.university.proto.core.RegisterStudentResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getRegisterStudentMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void addGrade(com.university.proto.core.AddGradeRequest request,
        io.grpc.stub.StreamObserver<com.university.proto.core.AddGradeResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getAddGradeMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void markAttendance(com.university.proto.core.MarkAttendanceRequest request,
        io.grpc.stub.StreamObserver<com.university.proto.core.MarkAttendanceResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getMarkAttendanceMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getGradeReport(com.university.proto.core.GetGradeReportRequest request,
        io.grpc.stub.StreamObserver<com.university.proto.core.GetGradeReportResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetGradeReportMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void calculateDiscount(com.university.proto.core.CalculateDiscountRequest request,
        io.grpc.stub.StreamObserver<com.university.proto.core.CalculateDiscountResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCalculateDiscountMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getSchedule(com.university.proto.core.CoreScheduleRequest request,
        io.grpc.stub.StreamObserver<com.university.proto.core.CoreScheduleResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetScheduleMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void addLesson(com.university.proto.core.AddLessonRequest request,
        io.grpc.stub.StreamObserver<com.university.proto.core.AddLessonResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getAddLessonMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service CoreService.
   * <pre>
   * ===== Service Definition =====
   * </pre>
   */
  public static final class CoreServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<CoreServiceBlockingStub> {
    private CoreServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CoreServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new CoreServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.university.proto.core.RegisterStudentResponse registerStudent(com.university.proto.core.RegisterStudentRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getRegisterStudentMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.university.proto.core.AddGradeResponse addGrade(com.university.proto.core.AddGradeRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getAddGradeMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.university.proto.core.MarkAttendanceResponse markAttendance(com.university.proto.core.MarkAttendanceRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getMarkAttendanceMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.university.proto.core.GetGradeReportResponse getGradeReport(com.university.proto.core.GetGradeReportRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetGradeReportMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.university.proto.core.CalculateDiscountResponse calculateDiscount(com.university.proto.core.CalculateDiscountRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCalculateDiscountMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.university.proto.core.CoreScheduleResponse getSchedule(com.university.proto.core.CoreScheduleRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetScheduleMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.university.proto.core.AddLessonResponse addLesson(com.university.proto.core.AddLessonRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getAddLessonMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service CoreService.
   * <pre>
   * ===== Service Definition =====
   * </pre>
   */
  public static final class CoreServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<CoreServiceFutureStub> {
    private CoreServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CoreServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new CoreServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.university.proto.core.RegisterStudentResponse> registerStudent(
        com.university.proto.core.RegisterStudentRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getRegisterStudentMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.university.proto.core.AddGradeResponse> addGrade(
        com.university.proto.core.AddGradeRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getAddGradeMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.university.proto.core.MarkAttendanceResponse> markAttendance(
        com.university.proto.core.MarkAttendanceRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getMarkAttendanceMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.university.proto.core.GetGradeReportResponse> getGradeReport(
        com.university.proto.core.GetGradeReportRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetGradeReportMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.university.proto.core.CalculateDiscountResponse> calculateDiscount(
        com.university.proto.core.CalculateDiscountRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCalculateDiscountMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.university.proto.core.CoreScheduleResponse> getSchedule(
        com.university.proto.core.CoreScheduleRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetScheduleMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.university.proto.core.AddLessonResponse> addLesson(
        com.university.proto.core.AddLessonRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getAddLessonMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_REGISTER_STUDENT = 0;
  private static final int METHODID_ADD_GRADE = 1;
  private static final int METHODID_MARK_ATTENDANCE = 2;
  private static final int METHODID_GET_GRADE_REPORT = 3;
  private static final int METHODID_CALCULATE_DISCOUNT = 4;
  private static final int METHODID_GET_SCHEDULE = 5;
  private static final int METHODID_ADD_LESSON = 6;

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
        case METHODID_REGISTER_STUDENT:
          serviceImpl.registerStudent((com.university.proto.core.RegisterStudentRequest) request,
              (io.grpc.stub.StreamObserver<com.university.proto.core.RegisterStudentResponse>) responseObserver);
          break;
        case METHODID_ADD_GRADE:
          serviceImpl.addGrade((com.university.proto.core.AddGradeRequest) request,
              (io.grpc.stub.StreamObserver<com.university.proto.core.AddGradeResponse>) responseObserver);
          break;
        case METHODID_MARK_ATTENDANCE:
          serviceImpl.markAttendance((com.university.proto.core.MarkAttendanceRequest) request,
              (io.grpc.stub.StreamObserver<com.university.proto.core.MarkAttendanceResponse>) responseObserver);
          break;
        case METHODID_GET_GRADE_REPORT:
          serviceImpl.getGradeReport((com.university.proto.core.GetGradeReportRequest) request,
              (io.grpc.stub.StreamObserver<com.university.proto.core.GetGradeReportResponse>) responseObserver);
          break;
        case METHODID_CALCULATE_DISCOUNT:
          serviceImpl.calculateDiscount((com.university.proto.core.CalculateDiscountRequest) request,
              (io.grpc.stub.StreamObserver<com.university.proto.core.CalculateDiscountResponse>) responseObserver);
          break;
        case METHODID_GET_SCHEDULE:
          serviceImpl.getSchedule((com.university.proto.core.CoreScheduleRequest) request,
              (io.grpc.stub.StreamObserver<com.university.proto.core.CoreScheduleResponse>) responseObserver);
          break;
        case METHODID_ADD_LESSON:
          serviceImpl.addLesson((com.university.proto.core.AddLessonRequest) request,
              (io.grpc.stub.StreamObserver<com.university.proto.core.AddLessonResponse>) responseObserver);
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
          getRegisterStudentMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.university.proto.core.RegisterStudentRequest,
              com.university.proto.core.RegisterStudentResponse>(
                service, METHODID_REGISTER_STUDENT)))
        .addMethod(
          getAddGradeMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.university.proto.core.AddGradeRequest,
              com.university.proto.core.AddGradeResponse>(
                service, METHODID_ADD_GRADE)))
        .addMethod(
          getMarkAttendanceMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.university.proto.core.MarkAttendanceRequest,
              com.university.proto.core.MarkAttendanceResponse>(
                service, METHODID_MARK_ATTENDANCE)))
        .addMethod(
          getGetGradeReportMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.university.proto.core.GetGradeReportRequest,
              com.university.proto.core.GetGradeReportResponse>(
                service, METHODID_GET_GRADE_REPORT)))
        .addMethod(
          getCalculateDiscountMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.university.proto.core.CalculateDiscountRequest,
              com.university.proto.core.CalculateDiscountResponse>(
                service, METHODID_CALCULATE_DISCOUNT)))
        .addMethod(
          getGetScheduleMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.university.proto.core.CoreScheduleRequest,
              com.university.proto.core.CoreScheduleResponse>(
                service, METHODID_GET_SCHEDULE)))
        .addMethod(
          getAddLessonMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.university.proto.core.AddLessonRequest,
              com.university.proto.core.AddLessonResponse>(
                service, METHODID_ADD_LESSON)))
        .build();
  }

  private static abstract class CoreServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    CoreServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.university.proto.core.CoreServiceOuterClass.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("CoreService");
    }
  }

  private static final class CoreServiceFileDescriptorSupplier
      extends CoreServiceBaseDescriptorSupplier {
    CoreServiceFileDescriptorSupplier() {}
  }

  private static final class CoreServiceMethodDescriptorSupplier
      extends CoreServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    CoreServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (CoreServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new CoreServiceFileDescriptorSupplier())
              .addMethod(getRegisterStudentMethod())
              .addMethod(getAddGradeMethod())
              .addMethod(getMarkAttendanceMethod())
              .addMethod(getGetGradeReportMethod())
              .addMethod(getCalculateDiscountMethod())
              .addMethod(getGetScheduleMethod())
              .addMethod(getAddLessonMethod())
              .build();
        }
      }
    }
    return result;
  }
}
