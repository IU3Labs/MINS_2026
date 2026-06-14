package com.university.proto.reference;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.59.0)",
    comments = "Source: reference_service.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class ReferenceServiceGrpc {

  private ReferenceServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "reference.ReferenceService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.university.proto.reference.ValidateStudentRequest,
      com.university.proto.reference.ValidateStudentResponse> getValidateStudentMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ValidateStudent",
      requestType = com.university.proto.reference.ValidateStudentRequest.class,
      responseType = com.university.proto.reference.ValidateStudentResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.university.proto.reference.ValidateStudentRequest,
      com.university.proto.reference.ValidateStudentResponse> getValidateStudentMethod() {
    io.grpc.MethodDescriptor<com.university.proto.reference.ValidateStudentRequest, com.university.proto.reference.ValidateStudentResponse> getValidateStudentMethod;
    if ((getValidateStudentMethod = ReferenceServiceGrpc.getValidateStudentMethod) == null) {
      synchronized (ReferenceServiceGrpc.class) {
        if ((getValidateStudentMethod = ReferenceServiceGrpc.getValidateStudentMethod) == null) {
          ReferenceServiceGrpc.getValidateStudentMethod = getValidateStudentMethod =
              io.grpc.MethodDescriptor.<com.university.proto.reference.ValidateStudentRequest, com.university.proto.reference.ValidateStudentResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ValidateStudent"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.university.proto.reference.ValidateStudentRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.university.proto.reference.ValidateStudentResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ReferenceServiceMethodDescriptorSupplier("ValidateStudent"))
              .build();
        }
      }
    }
    return getValidateStudentMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.university.proto.reference.ValidateLessonRequest,
      com.university.proto.reference.ValidateLessonResponse> getValidateLessonMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ValidateLesson",
      requestType = com.university.proto.reference.ValidateLessonRequest.class,
      responseType = com.university.proto.reference.ValidateLessonResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.university.proto.reference.ValidateLessonRequest,
      com.university.proto.reference.ValidateLessonResponse> getValidateLessonMethod() {
    io.grpc.MethodDescriptor<com.university.proto.reference.ValidateLessonRequest, com.university.proto.reference.ValidateLessonResponse> getValidateLessonMethod;
    if ((getValidateLessonMethod = ReferenceServiceGrpc.getValidateLessonMethod) == null) {
      synchronized (ReferenceServiceGrpc.class) {
        if ((getValidateLessonMethod = ReferenceServiceGrpc.getValidateLessonMethod) == null) {
          ReferenceServiceGrpc.getValidateLessonMethod = getValidateLessonMethod =
              io.grpc.MethodDescriptor.<com.university.proto.reference.ValidateLessonRequest, com.university.proto.reference.ValidateLessonResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ValidateLesson"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.university.proto.reference.ValidateLessonRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.university.proto.reference.ValidateLessonResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ReferenceServiceMethodDescriptorSupplier("ValidateLesson"))
              .build();
        }
      }
    }
    return getValidateLessonMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.university.proto.reference.ListCoursesRequest,
      com.university.proto.reference.ListCoursesResponse> getListCoursesMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ListCourses",
      requestType = com.university.proto.reference.ListCoursesRequest.class,
      responseType = com.university.proto.reference.ListCoursesResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.university.proto.reference.ListCoursesRequest,
      com.university.proto.reference.ListCoursesResponse> getListCoursesMethod() {
    io.grpc.MethodDescriptor<com.university.proto.reference.ListCoursesRequest, com.university.proto.reference.ListCoursesResponse> getListCoursesMethod;
    if ((getListCoursesMethod = ReferenceServiceGrpc.getListCoursesMethod) == null) {
      synchronized (ReferenceServiceGrpc.class) {
        if ((getListCoursesMethod = ReferenceServiceGrpc.getListCoursesMethod) == null) {
          ReferenceServiceGrpc.getListCoursesMethod = getListCoursesMethod =
              io.grpc.MethodDescriptor.<com.university.proto.reference.ListCoursesRequest, com.university.proto.reference.ListCoursesResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ListCourses"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.university.proto.reference.ListCoursesRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.university.proto.reference.ListCoursesResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ReferenceServiceMethodDescriptorSupplier("ListCourses"))
              .build();
        }
      }
    }
    return getListCoursesMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.university.proto.reference.SaveStudentRequest,
      com.university.proto.reference.SaveStudentResponse> getSaveStudentMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SaveStudent",
      requestType = com.university.proto.reference.SaveStudentRequest.class,
      responseType = com.university.proto.reference.SaveStudentResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.university.proto.reference.SaveStudentRequest,
      com.university.proto.reference.SaveStudentResponse> getSaveStudentMethod() {
    io.grpc.MethodDescriptor<com.university.proto.reference.SaveStudentRequest, com.university.proto.reference.SaveStudentResponse> getSaveStudentMethod;
    if ((getSaveStudentMethod = ReferenceServiceGrpc.getSaveStudentMethod) == null) {
      synchronized (ReferenceServiceGrpc.class) {
        if ((getSaveStudentMethod = ReferenceServiceGrpc.getSaveStudentMethod) == null) {
          ReferenceServiceGrpc.getSaveStudentMethod = getSaveStudentMethod =
              io.grpc.MethodDescriptor.<com.university.proto.reference.SaveStudentRequest, com.university.proto.reference.SaveStudentResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SaveStudent"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.university.proto.reference.SaveStudentRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.university.proto.reference.SaveStudentResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ReferenceServiceMethodDescriptorSupplier("SaveStudent"))
              .build();
        }
      }
    }
    return getSaveStudentMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.university.proto.reference.SaveGradeRequest,
      com.university.proto.reference.SaveGradeResponse> getSaveGradeMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SaveGrade",
      requestType = com.university.proto.reference.SaveGradeRequest.class,
      responseType = com.university.proto.reference.SaveGradeResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.university.proto.reference.SaveGradeRequest,
      com.university.proto.reference.SaveGradeResponse> getSaveGradeMethod() {
    io.grpc.MethodDescriptor<com.university.proto.reference.SaveGradeRequest, com.university.proto.reference.SaveGradeResponse> getSaveGradeMethod;
    if ((getSaveGradeMethod = ReferenceServiceGrpc.getSaveGradeMethod) == null) {
      synchronized (ReferenceServiceGrpc.class) {
        if ((getSaveGradeMethod = ReferenceServiceGrpc.getSaveGradeMethod) == null) {
          ReferenceServiceGrpc.getSaveGradeMethod = getSaveGradeMethod =
              io.grpc.MethodDescriptor.<com.university.proto.reference.SaveGradeRequest, com.university.proto.reference.SaveGradeResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SaveGrade"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.university.proto.reference.SaveGradeRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.university.proto.reference.SaveGradeResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ReferenceServiceMethodDescriptorSupplier("SaveGrade"))
              .build();
        }
      }
    }
    return getSaveGradeMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.university.proto.reference.SaveAttendanceRequest,
      com.university.proto.reference.SaveAttendanceResponse> getSaveAttendanceMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SaveAttendance",
      requestType = com.university.proto.reference.SaveAttendanceRequest.class,
      responseType = com.university.proto.reference.SaveAttendanceResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.university.proto.reference.SaveAttendanceRequest,
      com.university.proto.reference.SaveAttendanceResponse> getSaveAttendanceMethod() {
    io.grpc.MethodDescriptor<com.university.proto.reference.SaveAttendanceRequest, com.university.proto.reference.SaveAttendanceResponse> getSaveAttendanceMethod;
    if ((getSaveAttendanceMethod = ReferenceServiceGrpc.getSaveAttendanceMethod) == null) {
      synchronized (ReferenceServiceGrpc.class) {
        if ((getSaveAttendanceMethod = ReferenceServiceGrpc.getSaveAttendanceMethod) == null) {
          ReferenceServiceGrpc.getSaveAttendanceMethod = getSaveAttendanceMethod =
              io.grpc.MethodDescriptor.<com.university.proto.reference.SaveAttendanceRequest, com.university.proto.reference.SaveAttendanceResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SaveAttendance"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.university.proto.reference.SaveAttendanceRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.university.proto.reference.SaveAttendanceResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ReferenceServiceMethodDescriptorSupplier("SaveAttendance"))
              .build();
        }
      }
    }
    return getSaveAttendanceMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.university.proto.reference.GetStudentGradesRequest,
      com.university.proto.reference.GetStudentGradesResponse> getGetStudentGradesMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetStudentGrades",
      requestType = com.university.proto.reference.GetStudentGradesRequest.class,
      responseType = com.university.proto.reference.GetStudentGradesResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.university.proto.reference.GetStudentGradesRequest,
      com.university.proto.reference.GetStudentGradesResponse> getGetStudentGradesMethod() {
    io.grpc.MethodDescriptor<com.university.proto.reference.GetStudentGradesRequest, com.university.proto.reference.GetStudentGradesResponse> getGetStudentGradesMethod;
    if ((getGetStudentGradesMethod = ReferenceServiceGrpc.getGetStudentGradesMethod) == null) {
      synchronized (ReferenceServiceGrpc.class) {
        if ((getGetStudentGradesMethod = ReferenceServiceGrpc.getGetStudentGradesMethod) == null) {
          ReferenceServiceGrpc.getGetStudentGradesMethod = getGetStudentGradesMethod =
              io.grpc.MethodDescriptor.<com.university.proto.reference.GetStudentGradesRequest, com.university.proto.reference.GetStudentGradesResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetStudentGrades"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.university.proto.reference.GetStudentGradesRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.university.proto.reference.GetStudentGradesResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ReferenceServiceMethodDescriptorSupplier("GetStudentGrades"))
              .build();
        }
      }
    }
    return getGetStudentGradesMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.university.proto.reference.GetStudentAverageRequest,
      com.university.proto.reference.GetStudentAverageResponse> getGetStudentAverageMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetStudentAverage",
      requestType = com.university.proto.reference.GetStudentAverageRequest.class,
      responseType = com.university.proto.reference.GetStudentAverageResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.university.proto.reference.GetStudentAverageRequest,
      com.university.proto.reference.GetStudentAverageResponse> getGetStudentAverageMethod() {
    io.grpc.MethodDescriptor<com.university.proto.reference.GetStudentAverageRequest, com.university.proto.reference.GetStudentAverageResponse> getGetStudentAverageMethod;
    if ((getGetStudentAverageMethod = ReferenceServiceGrpc.getGetStudentAverageMethod) == null) {
      synchronized (ReferenceServiceGrpc.class) {
        if ((getGetStudentAverageMethod = ReferenceServiceGrpc.getGetStudentAverageMethod) == null) {
          ReferenceServiceGrpc.getGetStudentAverageMethod = getGetStudentAverageMethod =
              io.grpc.MethodDescriptor.<com.university.proto.reference.GetStudentAverageRequest, com.university.proto.reference.GetStudentAverageResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetStudentAverage"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.university.proto.reference.GetStudentAverageRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.university.proto.reference.GetStudentAverageResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ReferenceServiceMethodDescriptorSupplier("GetStudentAverage"))
              .build();
        }
      }
    }
    return getGetStudentAverageMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.university.proto.reference.SaveLessonRequest,
      com.university.proto.reference.SaveLessonResponse> getSaveLessonMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SaveLesson",
      requestType = com.university.proto.reference.SaveLessonRequest.class,
      responseType = com.university.proto.reference.SaveLessonResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.university.proto.reference.SaveLessonRequest,
      com.university.proto.reference.SaveLessonResponse> getSaveLessonMethod() {
    io.grpc.MethodDescriptor<com.university.proto.reference.SaveLessonRequest, com.university.proto.reference.SaveLessonResponse> getSaveLessonMethod;
    if ((getSaveLessonMethod = ReferenceServiceGrpc.getSaveLessonMethod) == null) {
      synchronized (ReferenceServiceGrpc.class) {
        if ((getSaveLessonMethod = ReferenceServiceGrpc.getSaveLessonMethod) == null) {
          ReferenceServiceGrpc.getSaveLessonMethod = getSaveLessonMethod =
              io.grpc.MethodDescriptor.<com.university.proto.reference.SaveLessonRequest, com.university.proto.reference.SaveLessonResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SaveLesson"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.university.proto.reference.SaveLessonRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.university.proto.reference.SaveLessonResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ReferenceServiceMethodDescriptorSupplier("SaveLesson"))
              .build();
        }
      }
    }
    return getSaveLessonMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.university.proto.reference.ReferenceScheduleRequest,
      com.university.proto.reference.ReferenceScheduleResponse> getGetScheduleMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetSchedule",
      requestType = com.university.proto.reference.ReferenceScheduleRequest.class,
      responseType = com.university.proto.reference.ReferenceScheduleResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.university.proto.reference.ReferenceScheduleRequest,
      com.university.proto.reference.ReferenceScheduleResponse> getGetScheduleMethod() {
    io.grpc.MethodDescriptor<com.university.proto.reference.ReferenceScheduleRequest, com.university.proto.reference.ReferenceScheduleResponse> getGetScheduleMethod;
    if ((getGetScheduleMethod = ReferenceServiceGrpc.getGetScheduleMethod) == null) {
      synchronized (ReferenceServiceGrpc.class) {
        if ((getGetScheduleMethod = ReferenceServiceGrpc.getGetScheduleMethod) == null) {
          ReferenceServiceGrpc.getGetScheduleMethod = getGetScheduleMethod =
              io.grpc.MethodDescriptor.<com.university.proto.reference.ReferenceScheduleRequest, com.university.proto.reference.ReferenceScheduleResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetSchedule"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.university.proto.reference.ReferenceScheduleRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.university.proto.reference.ReferenceScheduleResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ReferenceServiceMethodDescriptorSupplier("GetSchedule"))
              .build();
        }
      }
    }
    return getGetScheduleMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.university.proto.reference.GetNotificationHistoryRequest,
      com.university.proto.reference.GetNotificationHistoryResponse> getGetNotificationHistoryMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetNotificationHistory",
      requestType = com.university.proto.reference.GetNotificationHistoryRequest.class,
      responseType = com.university.proto.reference.GetNotificationHistoryResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.university.proto.reference.GetNotificationHistoryRequest,
      com.university.proto.reference.GetNotificationHistoryResponse> getGetNotificationHistoryMethod() {
    io.grpc.MethodDescriptor<com.university.proto.reference.GetNotificationHistoryRequest, com.university.proto.reference.GetNotificationHistoryResponse> getGetNotificationHistoryMethod;
    if ((getGetNotificationHistoryMethod = ReferenceServiceGrpc.getGetNotificationHistoryMethod) == null) {
      synchronized (ReferenceServiceGrpc.class) {
        if ((getGetNotificationHistoryMethod = ReferenceServiceGrpc.getGetNotificationHistoryMethod) == null) {
          ReferenceServiceGrpc.getGetNotificationHistoryMethod = getGetNotificationHistoryMethod =
              io.grpc.MethodDescriptor.<com.university.proto.reference.GetNotificationHistoryRequest, com.university.proto.reference.GetNotificationHistoryResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetNotificationHistory"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.university.proto.reference.GetNotificationHistoryRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.university.proto.reference.GetNotificationHistoryResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ReferenceServiceMethodDescriptorSupplier("GetNotificationHistory"))
              .build();
        }
      }
    }
    return getGetNotificationHistoryMethod;
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
    default void validateStudent(com.university.proto.reference.ValidateStudentRequest request,
        io.grpc.stub.StreamObserver<com.university.proto.reference.ValidateStudentResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getValidateStudentMethod(), responseObserver);
    }

    /**
     */
    default void validateLesson(com.university.proto.reference.ValidateLessonRequest request,
        io.grpc.stub.StreamObserver<com.university.proto.reference.ValidateLessonResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getValidateLessonMethod(), responseObserver);
    }

    /**
     */
    default void listCourses(com.university.proto.reference.ListCoursesRequest request,
        io.grpc.stub.StreamObserver<com.university.proto.reference.ListCoursesResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getListCoursesMethod(), responseObserver);
    }

    /**
     */
    default void saveStudent(com.university.proto.reference.SaveStudentRequest request,
        io.grpc.stub.StreamObserver<com.university.proto.reference.SaveStudentResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSaveStudentMethod(), responseObserver);
    }

    /**
     */
    default void saveGrade(com.university.proto.reference.SaveGradeRequest request,
        io.grpc.stub.StreamObserver<com.university.proto.reference.SaveGradeResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSaveGradeMethod(), responseObserver);
    }

    /**
     */
    default void saveAttendance(com.university.proto.reference.SaveAttendanceRequest request,
        io.grpc.stub.StreamObserver<com.university.proto.reference.SaveAttendanceResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSaveAttendanceMethod(), responseObserver);
    }

    /**
     */
    default void getStudentGrades(com.university.proto.reference.GetStudentGradesRequest request,
        io.grpc.stub.StreamObserver<com.university.proto.reference.GetStudentGradesResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetStudentGradesMethod(), responseObserver);
    }

    /**
     */
    default void getStudentAverage(com.university.proto.reference.GetStudentAverageRequest request,
        io.grpc.stub.StreamObserver<com.university.proto.reference.GetStudentAverageResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetStudentAverageMethod(), responseObserver);
    }

    /**
     */
    default void saveLesson(com.university.proto.reference.SaveLessonRequest request,
        io.grpc.stub.StreamObserver<com.university.proto.reference.SaveLessonResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSaveLessonMethod(), responseObserver);
    }

    /**
     */
    default void getSchedule(com.university.proto.reference.ReferenceScheduleRequest request,
        io.grpc.stub.StreamObserver<com.university.proto.reference.ReferenceScheduleResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetScheduleMethod(), responseObserver);
    }

    /**
     */
    default void getNotificationHistory(com.university.proto.reference.GetNotificationHistoryRequest request,
        io.grpc.stub.StreamObserver<com.university.proto.reference.GetNotificationHistoryResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetNotificationHistoryMethod(), responseObserver);
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
    public void validateStudent(com.university.proto.reference.ValidateStudentRequest request,
        io.grpc.stub.StreamObserver<com.university.proto.reference.ValidateStudentResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getValidateStudentMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void validateLesson(com.university.proto.reference.ValidateLessonRequest request,
        io.grpc.stub.StreamObserver<com.university.proto.reference.ValidateLessonResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getValidateLessonMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void listCourses(com.university.proto.reference.ListCoursesRequest request,
        io.grpc.stub.StreamObserver<com.university.proto.reference.ListCoursesResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getListCoursesMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void saveStudent(com.university.proto.reference.SaveStudentRequest request,
        io.grpc.stub.StreamObserver<com.university.proto.reference.SaveStudentResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSaveStudentMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void saveGrade(com.university.proto.reference.SaveGradeRequest request,
        io.grpc.stub.StreamObserver<com.university.proto.reference.SaveGradeResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSaveGradeMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void saveAttendance(com.university.proto.reference.SaveAttendanceRequest request,
        io.grpc.stub.StreamObserver<com.university.proto.reference.SaveAttendanceResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSaveAttendanceMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getStudentGrades(com.university.proto.reference.GetStudentGradesRequest request,
        io.grpc.stub.StreamObserver<com.university.proto.reference.GetStudentGradesResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetStudentGradesMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getStudentAverage(com.university.proto.reference.GetStudentAverageRequest request,
        io.grpc.stub.StreamObserver<com.university.proto.reference.GetStudentAverageResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetStudentAverageMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void saveLesson(com.university.proto.reference.SaveLessonRequest request,
        io.grpc.stub.StreamObserver<com.university.proto.reference.SaveLessonResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSaveLessonMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getSchedule(com.university.proto.reference.ReferenceScheduleRequest request,
        io.grpc.stub.StreamObserver<com.university.proto.reference.ReferenceScheduleResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetScheduleMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getNotificationHistory(com.university.proto.reference.GetNotificationHistoryRequest request,
        io.grpc.stub.StreamObserver<com.university.proto.reference.GetNotificationHistoryResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetNotificationHistoryMethod(), getCallOptions()), request, responseObserver);
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
    public com.university.proto.reference.ValidateStudentResponse validateStudent(com.university.proto.reference.ValidateStudentRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getValidateStudentMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.university.proto.reference.ValidateLessonResponse validateLesson(com.university.proto.reference.ValidateLessonRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getValidateLessonMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.university.proto.reference.ListCoursesResponse listCourses(com.university.proto.reference.ListCoursesRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getListCoursesMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.university.proto.reference.SaveStudentResponse saveStudent(com.university.proto.reference.SaveStudentRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSaveStudentMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.university.proto.reference.SaveGradeResponse saveGrade(com.university.proto.reference.SaveGradeRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSaveGradeMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.university.proto.reference.SaveAttendanceResponse saveAttendance(com.university.proto.reference.SaveAttendanceRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSaveAttendanceMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.university.proto.reference.GetStudentGradesResponse getStudentGrades(com.university.proto.reference.GetStudentGradesRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetStudentGradesMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.university.proto.reference.GetStudentAverageResponse getStudentAverage(com.university.proto.reference.GetStudentAverageRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetStudentAverageMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.university.proto.reference.SaveLessonResponse saveLesson(com.university.proto.reference.SaveLessonRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSaveLessonMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.university.proto.reference.ReferenceScheduleResponse getSchedule(com.university.proto.reference.ReferenceScheduleRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetScheduleMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.university.proto.reference.GetNotificationHistoryResponse getNotificationHistory(com.university.proto.reference.GetNotificationHistoryRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetNotificationHistoryMethod(), getCallOptions(), request);
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
    public com.google.common.util.concurrent.ListenableFuture<com.university.proto.reference.ValidateStudentResponse> validateStudent(
        com.university.proto.reference.ValidateStudentRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getValidateStudentMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.university.proto.reference.ValidateLessonResponse> validateLesson(
        com.university.proto.reference.ValidateLessonRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getValidateLessonMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.university.proto.reference.ListCoursesResponse> listCourses(
        com.university.proto.reference.ListCoursesRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getListCoursesMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.university.proto.reference.SaveStudentResponse> saveStudent(
        com.university.proto.reference.SaveStudentRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSaveStudentMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.university.proto.reference.SaveGradeResponse> saveGrade(
        com.university.proto.reference.SaveGradeRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSaveGradeMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.university.proto.reference.SaveAttendanceResponse> saveAttendance(
        com.university.proto.reference.SaveAttendanceRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSaveAttendanceMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.university.proto.reference.GetStudentGradesResponse> getStudentGrades(
        com.university.proto.reference.GetStudentGradesRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetStudentGradesMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.university.proto.reference.GetStudentAverageResponse> getStudentAverage(
        com.university.proto.reference.GetStudentAverageRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetStudentAverageMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.university.proto.reference.SaveLessonResponse> saveLesson(
        com.university.proto.reference.SaveLessonRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSaveLessonMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.university.proto.reference.ReferenceScheduleResponse> getSchedule(
        com.university.proto.reference.ReferenceScheduleRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetScheduleMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.university.proto.reference.GetNotificationHistoryResponse> getNotificationHistory(
        com.university.proto.reference.GetNotificationHistoryRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetNotificationHistoryMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_VALIDATE_STUDENT = 0;
  private static final int METHODID_VALIDATE_LESSON = 1;
  private static final int METHODID_LIST_COURSES = 2;
  private static final int METHODID_SAVE_STUDENT = 3;
  private static final int METHODID_SAVE_GRADE = 4;
  private static final int METHODID_SAVE_ATTENDANCE = 5;
  private static final int METHODID_GET_STUDENT_GRADES = 6;
  private static final int METHODID_GET_STUDENT_AVERAGE = 7;
  private static final int METHODID_SAVE_LESSON = 8;
  private static final int METHODID_GET_SCHEDULE = 9;
  private static final int METHODID_GET_NOTIFICATION_HISTORY = 10;

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
        case METHODID_VALIDATE_STUDENT:
          serviceImpl.validateStudent((com.university.proto.reference.ValidateStudentRequest) request,
              (io.grpc.stub.StreamObserver<com.university.proto.reference.ValidateStudentResponse>) responseObserver);
          break;
        case METHODID_VALIDATE_LESSON:
          serviceImpl.validateLesson((com.university.proto.reference.ValidateLessonRequest) request,
              (io.grpc.stub.StreamObserver<com.university.proto.reference.ValidateLessonResponse>) responseObserver);
          break;
        case METHODID_LIST_COURSES:
          serviceImpl.listCourses((com.university.proto.reference.ListCoursesRequest) request,
              (io.grpc.stub.StreamObserver<com.university.proto.reference.ListCoursesResponse>) responseObserver);
          break;
        case METHODID_SAVE_STUDENT:
          serviceImpl.saveStudent((com.university.proto.reference.SaveStudentRequest) request,
              (io.grpc.stub.StreamObserver<com.university.proto.reference.SaveStudentResponse>) responseObserver);
          break;
        case METHODID_SAVE_GRADE:
          serviceImpl.saveGrade((com.university.proto.reference.SaveGradeRequest) request,
              (io.grpc.stub.StreamObserver<com.university.proto.reference.SaveGradeResponse>) responseObserver);
          break;
        case METHODID_SAVE_ATTENDANCE:
          serviceImpl.saveAttendance((com.university.proto.reference.SaveAttendanceRequest) request,
              (io.grpc.stub.StreamObserver<com.university.proto.reference.SaveAttendanceResponse>) responseObserver);
          break;
        case METHODID_GET_STUDENT_GRADES:
          serviceImpl.getStudentGrades((com.university.proto.reference.GetStudentGradesRequest) request,
              (io.grpc.stub.StreamObserver<com.university.proto.reference.GetStudentGradesResponse>) responseObserver);
          break;
        case METHODID_GET_STUDENT_AVERAGE:
          serviceImpl.getStudentAverage((com.university.proto.reference.GetStudentAverageRequest) request,
              (io.grpc.stub.StreamObserver<com.university.proto.reference.GetStudentAverageResponse>) responseObserver);
          break;
        case METHODID_SAVE_LESSON:
          serviceImpl.saveLesson((com.university.proto.reference.SaveLessonRequest) request,
              (io.grpc.stub.StreamObserver<com.university.proto.reference.SaveLessonResponse>) responseObserver);
          break;
        case METHODID_GET_SCHEDULE:
          serviceImpl.getSchedule((com.university.proto.reference.ReferenceScheduleRequest) request,
              (io.grpc.stub.StreamObserver<com.university.proto.reference.ReferenceScheduleResponse>) responseObserver);
          break;
        case METHODID_GET_NOTIFICATION_HISTORY:
          serviceImpl.getNotificationHistory((com.university.proto.reference.GetNotificationHistoryRequest) request,
              (io.grpc.stub.StreamObserver<com.university.proto.reference.GetNotificationHistoryResponse>) responseObserver);
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
          getValidateStudentMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.university.proto.reference.ValidateStudentRequest,
              com.university.proto.reference.ValidateStudentResponse>(
                service, METHODID_VALIDATE_STUDENT)))
        .addMethod(
          getValidateLessonMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.university.proto.reference.ValidateLessonRequest,
              com.university.proto.reference.ValidateLessonResponse>(
                service, METHODID_VALIDATE_LESSON)))
        .addMethod(
          getListCoursesMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.university.proto.reference.ListCoursesRequest,
              com.university.proto.reference.ListCoursesResponse>(
                service, METHODID_LIST_COURSES)))
        .addMethod(
          getSaveStudentMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.university.proto.reference.SaveStudentRequest,
              com.university.proto.reference.SaveStudentResponse>(
                service, METHODID_SAVE_STUDENT)))
        .addMethod(
          getSaveGradeMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.university.proto.reference.SaveGradeRequest,
              com.university.proto.reference.SaveGradeResponse>(
                service, METHODID_SAVE_GRADE)))
        .addMethod(
          getSaveAttendanceMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.university.proto.reference.SaveAttendanceRequest,
              com.university.proto.reference.SaveAttendanceResponse>(
                service, METHODID_SAVE_ATTENDANCE)))
        .addMethod(
          getGetStudentGradesMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.university.proto.reference.GetStudentGradesRequest,
              com.university.proto.reference.GetStudentGradesResponse>(
                service, METHODID_GET_STUDENT_GRADES)))
        .addMethod(
          getGetStudentAverageMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.university.proto.reference.GetStudentAverageRequest,
              com.university.proto.reference.GetStudentAverageResponse>(
                service, METHODID_GET_STUDENT_AVERAGE)))
        .addMethod(
          getSaveLessonMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.university.proto.reference.SaveLessonRequest,
              com.university.proto.reference.SaveLessonResponse>(
                service, METHODID_SAVE_LESSON)))
        .addMethod(
          getGetScheduleMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.university.proto.reference.ReferenceScheduleRequest,
              com.university.proto.reference.ReferenceScheduleResponse>(
                service, METHODID_GET_SCHEDULE)))
        .addMethod(
          getGetNotificationHistoryMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.university.proto.reference.GetNotificationHistoryRequest,
              com.university.proto.reference.GetNotificationHistoryResponse>(
                service, METHODID_GET_NOTIFICATION_HISTORY)))
        .build();
  }

  private static abstract class ReferenceServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    ReferenceServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.university.proto.reference.ReferenceServiceOuterClass.getDescriptor();
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
              .addMethod(getValidateStudentMethod())
              .addMethod(getValidateLessonMethod())
              .addMethod(getListCoursesMethod())
              .addMethod(getSaveStudentMethod())
              .addMethod(getSaveGradeMethod())
              .addMethod(getSaveAttendanceMethod())
              .addMethod(getGetStudentGradesMethod())
              .addMethod(getGetStudentAverageMethod())
              .addMethod(getSaveLessonMethod())
              .addMethod(getGetScheduleMethod())
              .addMethod(getGetNotificationHistoryMethod())
              .build();
        }
      }
    }
    return result;
  }
}
