package report;

import java.util.Map;
import model.ParkingTicket;

public interface ReportGenerator {
    String generate(int total, Map<Integer, ParkingTicket> activeSpaces, Map<Integer, Double> estimatedCosts);
}