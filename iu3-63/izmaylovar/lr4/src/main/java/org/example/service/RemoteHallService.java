package org.example.service;

import org.example.domain.exception.EntityInUseException;
import org.example.domain.model.Hall;
import org.example.domain.repository.SessionRepository;
import org.example.integration.reference.ReferenceServiceClient;
import org.example.infrastructure.trace.ServiceLogger;
import org.example.infrastructure.trace.TraceContext;

import java.util.List;
import java.util.UUID;

public class RemoteHallService implements HallOperations {
    private final ReferenceServiceClient referenceClient;
    private final SessionRepository sessionRepository;
    private final ServiceLogger logger = ServiceLogger.forComponent("core-service", RemoteHallService.class);

    public RemoteHallService(ReferenceServiceClient referenceClient, SessionRepository sessionRepository) {
        this.referenceClient = referenceClient;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public List<Hall> getAllHalls() {
        TraceContext.ensureTraceId();
        logger.info("Listing halls through reference service");
        return referenceClient.getAllHalls();
    }

    @Override
    public Hall getHall(UUID id) {
        TraceContext.ensureTraceId();
        logger.info("Getting hall " + id + " through reference service");
        return referenceClient.getHall(id);
    }

    @Override
    public Hall createHall(String name, int rows, int seatsPerRow) {
        TraceContext.ensureTraceId();
        logger.info("Creating hall through reference service");
        return referenceClient.createHall(name, rows, seatsPerRow);
    }

    @Override
    public Hall updateHall(UUID id, String name, int rows, int seatsPerRow) {
        TraceContext.ensureTraceId();
        logger.info("Updating hall " + id + " through reference service");
        return referenceClient.updateHall(id, name, rows, seatsPerRow);
    }

    @Override
    public void deleteHall(UUID id) {
        TraceContext.ensureTraceId();
        boolean inUse = sessionRepository.findAll().stream()
                .anyMatch(session -> session.getHall().getId().equals(id));
        if (inUse) {
            throw new EntityInUseException("Hall is used in existing sessions and cannot be removed.");
        }
        logger.info("Deleting hall " + id + " through reference service");
        referenceClient.deleteHall(id);
    }
}
