package ru.laskan0.travelagency.service;

import java.util.List;

import ru.laskan0.travelagency.exception.DuplicateEntityException;
import ru.laskan0.travelagency.exception.InvalidDataException;
import ru.laskan0.travelagency.exception.NotFoundException;
import ru.laskan0.travelagency.factory.DefaultTourFactory;
import ru.laskan0.travelagency.factory.TourFactory;
import ru.laskan0.travelagency.model.Tour;
import ru.laskan0.travelagency.repository.TourRepository;
import ru.laskan0.travelagency.service.observer.TravelAgencyObserver;
import ru.laskan0.travelagency.util.IdGenerator;

public class TourService {
    private static final String TOUR_ID_PREFIX = "T-";

    private final TourRepository tourRepository;
    private final TourFactory tourFactory;
    private final List<TravelAgencyObserver> observers;

    public TourService(TourRepository tourRepository) {
        this(tourRepository, new DefaultTourFactory(), List.of());
    }

    public TourService(TourRepository tourRepository, TourFactory tourFactory) {
        this(tourRepository, tourFactory, List.of());
    }

    public TourService(TourRepository tourRepository, TourFactory tourFactory, List<TravelAgencyObserver> observers) {
        if (tourRepository == null) {
            throw new InvalidDataException("Репозиторий туров не может быть null");
        }
        if (tourFactory == null) {
            throw new InvalidDataException("Фабрика туров не может быть null");
        }
        if (observers == null) {
            throw new InvalidDataException("Список наблюдателей не может быть null");
        }
        this.tourRepository = tourRepository;
        this.tourFactory = tourFactory;
        this.observers = List.copyOf(observers);
    }

    public Tour addTour(String title, String destination, double basePrice, int capacity) {
        String tourId = generateNextTourId();
        Tour tour = tourFactory.create(tourId, title, destination, basePrice, capacity);
        return addTour(tour);
    }

    public Tour addTour(Tour tour) {
        if (tour == null) {
            throw new InvalidDataException("Тур для сохранения не может быть null");
        }
        if (tourRepository.existsById(tour.getId())) {
            throw new DuplicateEntityException("Тур с таким ID уже существует: " + tour.getId());
        }
        Tour savedTour = tourRepository.save(tour);
        notifyTourAdded(savedTour);
        return savedTour;
    }

    public Tour getTourById(String tourId) {
        return tourRepository.findById(tourId)
                .orElseThrow(() -> new NotFoundException("Тур не найден: " + tourId));
    }

    public List<Tour> getAllTours() {
        return tourRepository.findAll();
    }

    private void notifyTourAdded(Tour tour) {
        for (TravelAgencyObserver observer : observers) {
            observer.onTourAdded(tour);
        }
    }

    private String generateNextTourId() {
        return IdGenerator.nextId(
                tourRepository.findAll().stream().map(Tour::getId).toList(),
                TOUR_ID_PREFIX);
    }
}
