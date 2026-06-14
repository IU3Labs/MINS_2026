package service;

import exceptions.LibrarySystemException;
import exceptions.UserNotFoundException;
import interfaces.UserServiceInterface;
import models.User;
import repos.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class UserService implements UserServiceInterface {
    private final UserRepository repository;
    private final IdGenerator idGenerator;

    public UserService(UserRepository repository, IdGenerator idGenerator) {
        this.repository = repository;
        this.idGenerator = idGenerator;
    }

    @Override
    public User createUser(String name, String email) {
        int id = idGenerator.generate();
        User user = new User(id, name, email);
        repository.save(user);
        return user;
    }

    @Override
    public User getUser(int userId) throws UserNotFoundException {
        return repository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(repository.findAll());
    }

    @Override
    public void deleteUser(int userId) throws LibrarySystemException {
        User user = getUser(userId);
        repository.delete(userId);
        idGenerator.release(userId);
    }

    @Override
    public int getCount() {
        return (int) repository.findAll().stream().count();
    }
}