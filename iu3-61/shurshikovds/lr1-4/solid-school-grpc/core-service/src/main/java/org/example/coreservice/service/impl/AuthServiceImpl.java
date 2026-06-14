package org.example.coreservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.coreservice.entity.user.User;
import org.example.coreservice.exception.BadCredentialsException;
import org.example.coreservice.exception.UserNotFoundException;
import org.example.coreservice.repository.StudentUserRepository;
import org.example.coreservice.repository.TeacherUserRepository;
import org.example.coreservice.repository.UserRepository;
import org.example.coreservice.service.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static org.example.coreservice.entity.enums.UserRole.*;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final StudentUserRepository studentUserRepository;
    private final TeacherUserRepository teacherUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User login(String username, String rawPassword) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new BadCredentialsException();
        }

        return switch (user.getRole()) {
            case STUDENT -> studentUserRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
            case TEACHER -> teacherUserRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
            case ADMIN   -> user;
        };
    }
}
