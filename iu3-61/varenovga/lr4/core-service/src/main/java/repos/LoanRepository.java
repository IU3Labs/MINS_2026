package repos;

import models.LoanRecord;
import java.util.Collection;
import java.util.Optional;

public interface LoanRepository {
    void save(LoanRecord record);
    Optional<LoanRecord> findByPublicationId(int pubId);
    Collection<LoanRecord> findAllActive();
    void delete(int pubId);
    boolean isActive(int pubId);
}