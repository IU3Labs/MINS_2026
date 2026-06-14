package main.java.repos;

import main.java.models.User;
import java.util.Collection;
import java.util.Optional;

public interface UserRepository {
    void save(User user);
    Optional<User> findById(int id);
    Collection<User> findAll();
    void delete(int id);
    boolean exists(int id);
}