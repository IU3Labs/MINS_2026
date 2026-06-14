package com.autoservice;


import com.autoservice.proto.ReferenceServiceGrpc;
import com.autoservice.proto.ReferenceServiceProto;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import com.autoservice.model.Service;
import com.autoservice.repository.ServiceRepository;
import com.autoservice.repository.impl.InMemoryServiceRepository;

import java.io.IOException;
import java.util.logging.Logger;

public class ServiceBServer {
    private static final Logger logger = Logger.getLogger(ServiceBServer.class.getName());
    private final int port = 50051;
    private Server server;
    private final ServiceRepository serviceRepo = new InMemoryServiceRepository();

    private void initServices() {
        serviceRepo.addService(new Service("Замена масла", 1500.0, 30));
        serviceRepo.addService(new Service("Шиномонтаж", 2000.0, 60));
        serviceRepo.addService(new Service("Диагностика двигателя", 2500.0, 45));
        serviceRepo.addService(new Service("Замена тормозных колодок", 3000.0, 90));
        serviceRepo.addService(new Service("Регулировка развала-схождения", 1800.0, 50));
        serviceRepo.addService(new Service("Замена ремня ГРМ", 5000.0, 120));
        serviceRepo.addService(new Service("Заправка кондиционера", 2200.0, 40));
        serviceRepo.addService(new Service("Чистка инжектора", 2800.0, 70));
    }

    private void start() throws IOException {
        initServices();
        server = ServerBuilder.forPort(port)
                .addService(new ReferenceServiceImpl())
                .build()
                .start();
        logger.info("Сервис B (справочник) запущен на порту " + port);
    }

    private void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    private class ReferenceServiceImpl extends ReferenceServiceGrpc.ReferenceServiceImplBase {
        @Override
        public void getServiceByName(ReferenceServiceProto.ServiceRequest request,
                                     StreamObserver<ReferenceServiceProto.ServiceResponse> responseObserver) {
            String traceId = request.getTraceId();
            String serviceName = request.getName();
            logger.info("[TraceID: " + traceId + "] Запрос услуги: " + serviceName);

            Service service = serviceRepo.findServiceByName(serviceName);
            ReferenceServiceProto.ServiceResponse.Builder builder = ReferenceServiceProto.ServiceResponse.newBuilder();

            if (service == null) {
                builder.setFound(false)
                        .setErrorMessage("Услуга не найдена: " + serviceName);
            } else {
                builder.setFound(true)
                        .setName(service.getName())
                        .setPrice(service.getPrice())
                        .setDurationMinutes(service.getDurationMinutes());
            }

            responseObserver.onNext(builder.build());
            responseObserver.onCompleted();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        ServiceBServer server = new ServiceBServer();
        server.start();
        server.blockUntilShutdown();
    }
}