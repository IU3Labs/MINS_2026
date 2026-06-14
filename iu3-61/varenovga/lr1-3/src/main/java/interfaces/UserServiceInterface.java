package main.java.interfaces;

import main.java.exceptions.LibrarySystemException;
import main.java.exceptions.UserNotFoundException;
import main.java.models.User;
import java.util.List;

public interface UserServiceInterface {
    User createUser(String name, String email);
    User getUser(int userId) throws UserNotFoundException;
    List<User> getAllUsers();
    void deleteUser(int userId) throws LibrarySystemException;
    int getCount();
}