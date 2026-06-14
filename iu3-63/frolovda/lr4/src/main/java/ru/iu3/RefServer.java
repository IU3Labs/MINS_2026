package ru.iu3;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import ru.iu3.entity.Pass;
import ru.iu3.entity.interfaces.Room;
import ru.iu3.grpc.ReferenceService;
import ru.iu3.repository.LPassRepositoryImpl;
import ru.iu3.repository.LRoomRepositoryImpl;
import ru.iu3.repository.interfaces.Repository;
import ru.iu3.service.DefaultRoomFactory;
import ru.iu3.service.PassServiceImpl;
import ru.iu3.service.RoomServiceImpl;
import ru.iu3.service.interfaces.PassService;
import ru.iu3.service.interfaces.RoomFactory;
import ru.iu3.service.interfaces.RoomService;
import ru.iu3.util.AppLogger;
import ru.iu3.util.FileAppLogger;
import ru.iu3.validation.PassValidator;
import ru.iu3.validation.RoomValidator;

public class RefServer {
    private static int PORT = 50052;
    private static final String LOG_FILE = "logs/app.log";

    public static void main(String[] args) throws Exception {
        Repository<Room, Integer> roomRepository = new LRoomRepositoryImpl();
        Repository<Pass, Integer> passRepository = new LPassRepositoryImpl();

        RoomFactory roomFactory = new DefaultRoomFactory();
        RoomValidator roomValidator = new RoomValidator(roomRepository);
        PassValidator passValidator = new PassValidator(passRepository);
        AppLogger logger = new FileAppLogger(LOG_FILE);

        RoomService roomService = new RoomServiceImpl(roomRepository, roomFactory, roomValidator);
        PassService passService = new PassServiceImpl(passRepository, passValidator);

        Server server = ServerBuilder.forPort(PORT)
                .addService(new ReferenceService(roomService, passService, logger))
                .build();

        server.start();

        System.out.println("Reference gRPC server started on port " + PORT);

        server.awaitTermination();
    }
}
