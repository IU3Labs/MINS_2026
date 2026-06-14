package repository.impl;

import model.Service;
import repository.ServiceRepository;
import java.util.HashMap;
import java.util.Map;

public class InMemoryServiceRepository implements ServiceRepository {
    private final Map<String, Service> servicesByName = new HashMap<>();

    @Override
    public void addService(Service service) {
        servicesByName.put(service.getName(), service);
    }

    @Override
    public Service findServiceByName(String name) {
        return servicesByName.get(name);
    }

}
