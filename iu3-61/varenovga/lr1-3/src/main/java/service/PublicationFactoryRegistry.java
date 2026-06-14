package main.java.service;

import main.java.exceptions.LibrarySystemException;
import main.java.interfaces.PublicationFactory;
import main.java.models.Publication;

import java.util.HashMap;
import java.util.Map;

public class PublicationFactoryRegistry {
    private final Map<LibraryService.PublicationType, PublicationFactory> factories = new HashMap<>();

    public void register(LibraryService.PublicationType type, PublicationFactory factory) {
        factories.put(type, factory);
    }

    public Publication create(LibraryService.PublicationType type, int id, String title,
                              String author, int year, String extraInfo) throws LibrarySystemException {
        PublicationFactory factory = factories.get(type);
        if (factory == null) {
            throw new LibrarySystemException("Нет фабрики для типа публикации: " + type);
        }
        return factory.create(id, title, author, year, extraInfo);
    }

    public boolean isSupported(LibraryService.PublicationType type) {
        return factories.containsKey(type);
    }
}