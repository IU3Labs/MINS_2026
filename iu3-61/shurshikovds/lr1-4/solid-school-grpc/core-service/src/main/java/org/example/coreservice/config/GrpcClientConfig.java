package org.example.coreservice.config;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.example.reference.grpc.ReferenceServiceGrpc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcClientConfig {

    @Bean
    public ReferenceServiceGrpc.ReferenceServiceBlockingStub referenceServiceStub() {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", 9090)
                .usePlaintext()
                .build();
        return ReferenceServiceGrpc.newBlockingStub(channel);
    }
}
