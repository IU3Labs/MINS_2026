package ru.bmstu.travel.reference;

import ru.bmstu.travel.reference.model.Client;
import ru.bmstu.travel.reference.model.Discount;
import ru.bmstu.travel.reference.model.ReferenceDiscountType;
import ru.bmstu.travel.reference.model.Tour;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ReferenceRepository {
    private final Map<String, Client> clients = new LinkedHashMap<>();
    private final Map<String, Tour> tours = new LinkedHashMap<>();

    public ReferenceRepository() {
        clients.put("C-001", new Client(
                "C-001",
                "Иван Петров",
                "+7-900-000-00-01",
                new Discount("Базовая", 10, ReferenceDiscountType.PERCENTAGE)
        ));
        clients.put("C-002", new Client(
                "C-002",
                "Анна Смирнова",
                "+7-900-000-00-02",
                new Discount("VIP Gold", 15, ReferenceDiscountType.VIP)
        ));
        clients.put("C-003", new Client(
                "C-003",
                "Олег Соколов",
                "+7-900-000-00-03",
                null
        ));

        tours.put("T-001", new Tour("T-001", "Морская неделя", "Турция", 65000.0, 10, 0));
        tours.put("T-002", new Tour("T-002", "Альпийские выходные", "Швейцария", 98000.0, 6, 1));
        tours.put("T-003", new Tour("T-003", "Огни мегаполиса", "ОАЭ", 120000.0, 8, 2));
    }

    public synchronized List<Client> listClients() {
        return new ArrayList<>(clients.values());
    }

    public synchronized List<Tour> listTours() {
        return new ArrayList<>(tours.values());
    }

    public synchronized Client getClient(String clientId) {
        return clients.get(clientId);
    }

    public synchronized Tour getTour(String tourId) {
        return tours.get(tourId);
    }

    public synchronized Client addClient(String fullName, String phone, Discount discount) {
        String clientId = nextId(clients.keySet(), "C-");
        Client client = new Client(clientId, fullName.trim(), phone.trim(), discount);
        clients.put(client.id(), client);
        return client;
    }

    public synchronized Tour addTour(String title, String destination, double basePrice, int capacity) {
        String tourId = nextId(tours.keySet(), "T-");
        Tour tour = new Tour(tourId, title.trim(), destination.trim(), basePrice, capacity, 0);
        tours.put(tour.id(), tour);
        return tour;
    }

    public synchronized Tour reserveTourSeat(String tourId) {
        Tour tour = tours.get(tourId);
        if (tour == null) {
            return null;
        }
        if (tour.availableSeats() > 0) {
            tour.reserveSeat();
        }
        return tour;
    }

    public synchronized Tour releaseTourSeat(String tourId) {
        Tour tour = tours.get(tourId);
        if (tour == null) {
            return null;
        }
        tour.releaseSeat();
        return tour;
    }

    private static String nextId(Iterable<String> existingIds, String prefix) {
        int max = 0;
        for (String id : existingIds) {
            if (id.startsWith(prefix)) {
                String suffix = id.substring(prefix.length());
                if (suffix.matches("\\d+")) {
                    max = Math.max(max, Integer.parseInt(suffix));
                }
            }
        }
        return String.format("%s%03d", prefix, max + 1);
    }
}
