package com.cinema.reference.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface Service<T> {
    List<T> getAll();
    Optional<T> getById(UUID id);
}
