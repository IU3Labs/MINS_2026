package repos;
import models.Publication;
import java.util.*;

public class InMemoryPublicationRepository implements PublicationRepository {
    private final Map<Integer, Publication> storage = new HashMap<>();

    @Override public void save(Publication p) { storage.put(p.getId(), p); }

    @Override public Optional<Publication> findById(int id) { return Optional.ofNullable(storage.get(id)); }
    @Override public Collection<Publication> findAll() { return new ArrayList<>(storage.values()); }
    @Override public void delete(int id) { storage.remove(id); }
    @Override public boolean exists(int id) { return storage.containsKey(id); }
}