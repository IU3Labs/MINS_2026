package main.java.repos;

import main.java.models.LoanRecord;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryLoanRepository implements LoanRepository {
    private final Map<Integer, LoanRecord> storage = new HashMap<>();

    @Override
    public void save(LoanRecord record) {
        storage.put(record.getPublication().getId(), record);
    }

    @Override
    public Optional<LoanRecord> findByPublicationId(int pubId) {
        return Optional.ofNullable(storage.get(pubId));
    }

    @Override
    public Collection<LoanRecord> findAllActive() {
        return storage.values().stream()
                .filter(record -> !record.isReturned())
                .collect(Collectors.toList());
    }

    @Override
    public void delete(int pubId) {
        storage.remove(pubId);
    }

    @Override
    public boolean isActive(int pubId) {
        LoanRecord record = storage.get(pubId);
        return record != null && !record.isReturned();
    }
}