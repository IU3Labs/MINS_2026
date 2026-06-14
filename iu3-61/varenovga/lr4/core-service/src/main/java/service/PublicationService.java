package service;

import exceptions.LibrarySystemException;
import interfaces.PublicationServiceInterface;
import models.Publication;
import repos.PublicationRepository;
import service.PublicationFactoryRegistry;
import service.LibraryService;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class PublicationService implements PublicationServiceInterface {
    private final PublicationRepository repository;
    private final IdGenerator idGenerator;
    private final PublicationFactoryRegistry factoryRegistry;

    public PublicationService(PublicationRepository repository,
                              IdGenerator idGenerator,
                              PublicationFactoryRegistry factoryRegistry) {
        this.repository = repository;
        this.idGenerator = idGenerator;
        this.factoryRegistry = factoryRegistry;
    }

    @Override
    public Publication createPublication(
            LibraryService.PublicationType type,
            String title,
            String author,
            int year,
            String extraInfo) throws LibrarySystemException {
        if (!factoryRegistry.isSupported(type)) {
            throw new LibrarySystemException("Неподдерживаемый тип публикации: " + type);
        }
        int id = idGenerator.generate();
        try {
            Publication publication = factoryRegistry.create(type, id, title, author, year, extraInfo);
            repository.save(publication);
            return publication;
        } catch (LibrarySystemException e) {
            idGenerator.release(id);
            throw e;
        }
    }

    private List<Publication> searchBy(Predicate<Publication> predicate) {
        List<Publication> result = new ArrayList<>();
        for (Publication p : repository.findAll()) {
            if (predicate.test(p)) result.add(p);
        }
        return result;
    }

    @Override
    public List<Publication> searchByTitle(String query) {
        String lq = query.toLowerCase();
        return searchBy(p -> p.getTitle().toLowerCase().contains(lq));
    }

    @Override
    public List<Publication> searchByAuthor(String query) {
        String lq = query.toLowerCase();
        return searchBy(p -> p.getAuthor().toLowerCase().contains(lq));
    }

    @Override
    public List<Publication> getCatalog() { return new ArrayList<>(repository.findAll()); }

    @Override
    public Publication findById(int id) throws LibrarySystemException {
        return repository.findById(id).orElseThrow(() -> new exceptions.PublicationNotFoundException(id));
    }

    @Override
    public void deletePublication(int pubId) throws LibrarySystemException {
        Publication pub = findById(pubId);
        if (!pub.isAvailable()) throw new LibrarySystemException("Нельзя удалить издание ID=" + pubId + ": оно выдано");
        repository.delete(pubId);
        idGenerator.release(pubId);
    }

    @Override
    public int getCount() { return (int) repository.findAll().stream().count(); }
}