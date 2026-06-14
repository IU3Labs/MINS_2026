package ru.bmstu.service.interfaces;

import ru.bmstu.grpc.ReferenceServiceClient;

import java.util.Optional;

public interface IClientAuthService {
    ReferenceServiceClient.ClientInfo register(String name, String email, String password);
    Optional<ReferenceServiceClient.ClientInfo> login(String email, String password);
}