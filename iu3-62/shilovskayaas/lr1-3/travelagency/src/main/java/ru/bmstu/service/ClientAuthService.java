package ru.bmstu.service;

import ru.bmstu.exception.*;
import ru.bmstu.model.Client;
import ru.bmstu.repository.ClientRepository;
import ru.bmstu.service.interfaces.IClientAuthService;
import ru.bmstu.util.EmailValidator;
import ru.bmstu.util.Validator;

import java.util.Optional;

public class ClientAuthService implements IClientAuthService {
    private final ClientRepository clientRepository;

    public ClientAuthService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client register(String name, String email, String password) {
        Validator.validateEmailFormat(EmailValidator.isValidEmail(email), email);
        Validator.validatePassword(password);
        Validator.validateEmailNotExists(clientRepository.findByEmail(email).isPresent(), email);

        Client client = new Client(0, name, email, password);
        return clientRepository.save(client);
    }

    public Optional<Client> login(String email, String password) {
        Optional<Client> clientOpt = clientRepository.findByEmail(email);

        if (clientOpt.isPresent()) {
            Client client = clientOpt.get();
            if (client.getPassword().equals(password)) {
                return Optional.of(client);
            }
        }

        return Optional.empty();
    }
}