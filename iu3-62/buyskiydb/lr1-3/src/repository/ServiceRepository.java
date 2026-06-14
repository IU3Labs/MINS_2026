package repository;

import model.Service;

import java.util.List;

public interface ServiceRepository {
    void addService(Service service);
    Service findServiceByName(String name);
}