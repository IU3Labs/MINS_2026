package com.autoservice.repository;


import com.autoservice.exception.ClientNotFoundException;
import com.autoservice.model.Client;

import java.util.List;

public interface ClientRepository {
    void addClient(Client client);
    Client findClientByPhone(String phone) throws ClientNotFoundException;
    List<Client> getAllClients();
}