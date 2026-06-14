package ru.iu3.grpc;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import ru.iu3.entity.Pass;
import ru.iu3.entity.interfaces.Room;
import ru.iu3.enums.RoomEnum;
import ru.iu3.exceptions.ConflictException;
import ru.iu3.exceptions.NotFoundExeption;
import ru.iu3.exceptions.ValidationException;
import ru.iu3.service.interfaces.PassService;
import ru.iu3.service.interfaces.RoomService;
import ru.iu3.util.AppLogger;

public class ReferenceService extends ReferenceServiceGrpc.ReferenceServiceImplBase {
    private final RoomService roomService;
    private final PassService passService;
    private final AppLogger logger;

    public ReferenceService(RoomService roomService, PassService passService, AppLogger logger) {
        this.roomService = roomService;
        this.passService = passService;
        this.logger = logger;
    }

    @Override
    public void addRoom(ReferenceContract.AddRoomRequest request,
            StreamObserver<ReferenceContract.EmptyResponse> responseObserver) {
        log(request.getTraceId(), "AddRoom id=" + request.getId());
        try {
            roomService.addRoom(RoomEnum.valueOf(request.getType()), request.getId(), request.getName(),
                    request.getHourlyRate());
            completeEmpty(responseObserver);
        } catch (RuntimeException e) {
            fail(responseObserver, e);
        }
    }

    @Override
    public void lockRoom(ReferenceContract.RoomIdRequest request,
            StreamObserver<ReferenceContract.EmptyResponse> responseObserver) {
        log(request.getTraceId(), "LockRoom id=" + request.getId());
        try {
            roomService.lockRoom(request.getId());
            completeEmpty(responseObserver);
        } catch (RuntimeException e) {
            fail(responseObserver, e);
        }
    }

    @Override
    public void getRoom(ReferenceContract.RoomIdRequest request,
            StreamObserver<ReferenceContract.RoomResponse> responseObserver) {
        log(request.getTraceId(), "GetRoom id=" + request.getId());
        try {
            Room room = roomService.getRoomById(request.getId());
            responseObserver.onNext(toRoomResponse(room));
            responseObserver.onCompleted();
        } catch (RuntimeException e) {
            fail(responseObserver, e);
        }
    }

    @Override
    public void listRooms(ReferenceContract.TraceRequest request,
            StreamObserver<ReferenceContract.RoomListResponse> responseObserver) {
        log(request.getTraceId(), "ListRooms");
        try {
            ReferenceContract.RoomListResponse.Builder response = ReferenceContract.RoomListResponse.newBuilder();
            for (Room room : roomService.getAllRooms()) {
                response.addRooms(toRoomResponse(room));
            }
            responseObserver.onNext(response.build());
            responseObserver.onCompleted();
        } catch (RuntimeException e) {
            fail(responseObserver, e);
        }
    }

    @Override
    public void issuePass(ReferenceContract.IssuePassRequest request,
            StreamObserver<ReferenceContract.EmptyResponse> responseObserver) {
        log(request.getTraceId(), "IssuePass id=" + request.getId());
        try {
            passService.issuePass(request.getId(), request.getHolderName());
            completeEmpty(responseObserver);
        } catch (RuntimeException e) {
            fail(responseObserver, e);
        }
    }

    @Override
    public void deactivatePass(ReferenceContract.PassIdRequest request,
            StreamObserver<ReferenceContract.EmptyResponse> responseObserver) {
        log(request.getTraceId(), "DeactivatePass id=" + request.getId());
        try {
            passService.deactivatePass(request.getId());
            completeEmpty(responseObserver);
        } catch (RuntimeException e) {
            fail(responseObserver, e);
        }
    }

    @Override
    public void getPass(ReferenceContract.PassIdRequest request,
            StreamObserver<ReferenceContract.PassResponse> responseObserver) {
        log(request.getTraceId(), "GetPass id=" + request.getId());
        try {
            Pass pass = passService.getPassById(request.getId());
            responseObserver.onNext(toPassResponse(pass));
            responseObserver.onCompleted();
        } catch (RuntimeException e) {
            fail(responseObserver, e);
        }
    }

    @Override
    public void listPasses(ReferenceContract.TraceRequest request,
            StreamObserver<ReferenceContract.PassListResponse> responseObserver) {
        log(request.getTraceId(), "ListPasses");
        try {
            ReferenceContract.PassListResponse.Builder response = ReferenceContract.PassListResponse.newBuilder();
            for (Pass pass : passService.getAllPasses()) {
                response.addPasses(toPassResponse(pass));
            }
            responseObserver.onNext(response.build());
            responseObserver.onCompleted();
        } catch (RuntimeException e) {
            fail(responseObserver, e);
        }
    }

    private ReferenceContract.RoomResponse toRoomResponse(Room room) {
        return ReferenceContract.RoomResponse.newBuilder()
                .setId(room.getId())
                .setType(room.getType().name())
                .setName(room.getName())
                .setHourlyRate(room.getHourlyRate())
                .setLocked(room.getIsLocked())
                .build();
    }

    private ReferenceContract.PassResponse toPassResponse(Pass pass) {
        return ReferenceContract.PassResponse.newBuilder()
                .setId(pass.getId())
                .setHolderName(pass.getHolderName())
                .setActive(pass.isActive())
                .build();
    }

    private void completeEmpty(StreamObserver<ReferenceContract.EmptyResponse> responseObserver) {
        responseObserver.onNext(ReferenceContract.EmptyResponse.newBuilder().build());
        responseObserver.onCompleted();
    }

    private void fail(StreamObserver<?> responseObserver, RuntimeException e) {
        Status status = Status.INTERNAL;
        if (e instanceof NotFoundExeption) {
            status = Status.NOT_FOUND;
        } else if (e instanceof ValidationException || e instanceof ConflictException || e instanceof IllegalArgumentException) {
            status = Status.INVALID_ARGUMENT;
        }
        responseObserver.onError(status.withDescription(e.getMessage()).withCause(e).asRuntimeException());
    }

    private void log(String traceId, String message) {
        logger.log("REFERENCE", traceId, message);
    }
}
