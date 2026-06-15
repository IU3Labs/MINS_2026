package storage;

import model.ParkingTicket;

import java.util.List;
import java.util.Map;

public interface ParkingStorage {
    void save(Map<Integer, ParkingTicket> active, List<ParkingTicket> history);
    void load(Map<Integer, ParkingTicket> active, List<ParkingTicket> history);
}