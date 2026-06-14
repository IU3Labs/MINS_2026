package main.java.repos;

import main.java.models.User;
import java.util.*;

public class InMemoryUserRepository implements UserRepository {
    private final Map<Integer, User> storage = new HashMap<>();

    @Override
    public void save(User user) {
        storage.put(user.getId(), user);
    }

    @Override
    public Optional<User> findById(int id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public Collection<User> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void delete(int id) {
        storage.remove(id);
    }

    @Override
    public boolean exists(int id) {
        return storage.containsKey(id);
    }
}