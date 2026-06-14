package com.autoservice.repository;


import com.autoservice.model.Service;

public interface ServiceRepository {
    void addService(Service service);
    Service findServiceByName(String name);
}