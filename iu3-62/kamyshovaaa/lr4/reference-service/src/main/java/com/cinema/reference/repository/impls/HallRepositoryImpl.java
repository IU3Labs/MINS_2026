package com.cinema.reference.repository.impls;

import com.cinema.reference.entity.Hall;
import com.cinema.reference.repository.HallRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;

public class HallRepositoryImpl implements HallRepository {
    private static final Logger log = LoggerFactory.getLogger(HallRepositoryImpl.class);

    private final Map<UUID, Hall> halls = new HashMap<>();
    private Map<UUID, Hall> transactionSnapshot = null;
    private boolean inTransaction = false;

    @Override
    public void beginTransaction() {
        if (inTransaction) {
            return;
        }
        transactionSnapshot = new HashMap<>(halls);
        inTransaction = true;
    }

    @Override
    public void commitTransaction() {
        if (!inTransaction) {
            return;
        }
        transactionSnapshot = null;
        inTransaction = false;
    }

    @Override
    public void rollbackTransaction() {
        if (!inTransaction) {
            return;
        }
        if (transactionSnapshot != null) {
            halls.clear();
            halls.putAll(transactionSnapshot);
            transactionSnapshot = null;
        }
        inTransaction = false;
    }

    @Override
    public void save(Hall hall) {
        halls.put(hall.getId(), hall);
    }

    @Override
    public Optional<Hall> findById(UUID id) {
        return Optional.ofNullable(halls.get(id));
    }

    @Override
    public List<Hall> findAll() {
        return new ArrayList<>(halls.values());
    }

    @Override
    public void delete(UUID id) {
        halls.remove(id);
    }
}