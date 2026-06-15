package report;

import java.util.Map;
import model.ParkingTicket;

public class TextReportGenerator implements ReportGenerator {
    @Override
    public String generate(int total, Map<Integer, ParkingTicket> activeSpaces, Map<Integer, Double> estimatedCosts) {
        int occupied = activeSpaces.size();
        double percent = total == 0 ? 0.0 : (occupied * 100.0 / total);

        StringBuilder sb = new StringBuilder();
        sb.append("=== ОТЧЁТ ПО ЗАГРУЗКЕ ===\n");
        sb.append(String.format("Всего мест: %d | Занято: %d | Свободно: %d\n", total, occupied, total - occupied));
        sb.append(String.format("Загрузка: %.1f%%\n\n", percent));

        if (occupied > 0) {
            sb.append("Занятые места и текущая стоимость при выезде сейчас:\n");
            for (Map.Entry<Integer, ParkingTicket> entry : activeSpaces.entrySet()) {
                int spaceId = entry.getKey();
                ParkingTicket ticket = entry.getValue();
                double cost = estimatedCosts.getOrDefault(spaceId, 0.0);
                sb.append(String.format("  #%d: %s | К оплате: %.2f руб.\n", spaceId, ticket.getLicensePlate(), cost));
            }
        } else {
            sb.append("Парковка свободна.\n");
        }
        return sb.toString();
    }
}