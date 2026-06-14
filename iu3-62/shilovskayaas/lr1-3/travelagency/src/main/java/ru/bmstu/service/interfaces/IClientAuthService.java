package ru.bmstu.service.interfaces;

import ru.bmstu.model.Client;

import java.util.Optional;

public interface IClientAuthService {
    Client register(String name, String email, String password);
    Optional<Client> login(String email, String password);
}
