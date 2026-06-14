package repos;

import models.Publication;
import java.util.Collection;
import java.util.Optional;

public interface PublicationRepository {
    void save(Publication publication);
    Optional<Publication> findById(int id);
    Collection<Publication> findAll();
    void delete(int id);
    boolean exists(int id);
}