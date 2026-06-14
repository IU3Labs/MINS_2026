package library;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import library.service.ReferenceServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReferenceServer {
    private static final Logger log = LoggerFactory.getLogger(ReferenceServer.class);
    public static void main(String[] args) throws Exception {
        Server server = ServerBuilder.forPort(9091).addService(new ReferenceServiceImpl()).build().start();
        log.info("✅ Reference Service запущен на порту 9091");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> { log.info("🛑 Остановка..."); server.shutdown(); }));
        server.awaitTermination();
    }
}