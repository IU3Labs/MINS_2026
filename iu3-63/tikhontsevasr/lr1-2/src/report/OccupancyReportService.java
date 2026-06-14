package report;

import model.ParkingSession;
import model.ParkingSpot;
import repository.ParkingSessionRepository;
import repository.ParkingSpotRepository;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class OccupancyReportService {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    private final ParkingSpotRepository spotRepository;
    private final ParkingSessionRepository sessionRepository;

    public OccupancyReportService(ParkingSpotRepository spotRepository, ParkingSessionRepository sessionRepository) {
        this.spotRepository = spotRepository;
        this.sessionRepository = sessionRepository;
    }

    public String buildReport() {
        List<ParkingSpot> spots = spotRepository.findAll();
        List<ParkingSession> activeSessions = sessionRepository.findActiveSessions();
        int total = spots.size();
        int occupied = activeSessions.size();
        int free = total - occupied;
        double occupancy = total == 0 ? 0.0 : occupied * 100.0 / total;

        StringBuilder builder = new StringBuilder();
        builder.append("Отчет по загрузке парковки").append(System.lineSeparator());
        builder.append("Всего мест: ").append(total).append(System.lineSeparator());
        builder.append("Занято: ").append(occupied).append(System.lineSeparator());
        builder.append("Свободно: ").append(free).append(System.lineSeparator());
        builder.append("Процент загрузки: ").append(String.format("%.2f%%", occupancy)).append(System.lineSeparator());
        builder.append("Активные сессии:").append(System.lineSeparator());

        if (activeSessions.isEmpty()) {
            builder.append("  Нет активных сессий.");
        } else {
            for (ParkingSession session : activeSessions) {
                builder.append("  ")
                        .append(session.getVehicle().getLicensePlate())
                        .append(" | место #")
                        .append(session.getParkingSpot().getId())
                        .append(" | въезд: ")
                        .append(session.getEntryTime().format(FORMATTER))
                        .append(" | тариф: ")
                        .append(session.getTariffStrategy().getType().name())
                        .append(System.lineSeparator());
            }
        }

        return builder.toString().trim();
    }
}
