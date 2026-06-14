package org.example.coreservice.service;


import org.example.coreservice.entity.user.User;

public interface AuthService {
    User login(String username, String rawPassword);
}
