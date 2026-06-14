package com.cinema.reference.repository.impls;

import com.cinema.reference.entity.Screening;
import com.cinema.reference.repository.ScreeningRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.*;

public class ScreeningRepositoryImpl implements ScreeningRepository {
    private static final Logger log = LoggerFactory.getLogger(ScreeningRepositoryImpl.class);

    private final Map<UUID, Screening> screenings = new HashMap<>();
    private Map<UUID, Screening> transactionSnapshot = null;
    private boolean inTransaction = false;

    @Override
    public void save(Screening screening) {
        screenings.put(screening.getId(), screening);
    }

    @Override
    public void beginTransaction() {
        if (inTransaction) {
            return;
        }
        transactionSnapshot = new HashMap<>(screenings);
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
            screenings.clear();
            screenings.putAll(transactionSnapshot);
            transactionSnapshot = null;
        }
        inTransaction = false;
    }


    @Override
    public Optional<Screening> findById(UUID id) {
        return Optional.ofNullable(screenings.get(id));
    }

    @Override
    public List<Screening> findAll() {
        return new ArrayList<>(screenings.values());
    }

    @Override
    public void delete(UUID id) {
        screenings.remove(id);
    }

    public boolean exists(UUID id) {
        return screenings.containsKey(id);
    }

    @Override
    public List<Screening> findByDate(LocalDate date) {
        return List.of();
    }
}