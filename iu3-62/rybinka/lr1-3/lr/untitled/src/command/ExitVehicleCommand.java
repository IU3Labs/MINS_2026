package command;

import java.time.LocalDateTime;
import core.ParkingSystem;

public class ExitVehicleCommand implements ParkingCommand {
    private final ParkingSystem system;
    private final int spaceId;
    private final LocalDateTime time;
    private double cost;
    private boolean success;

    public ExitVehicleCommand(ParkingSystem system, int spaceId, LocalDateTime time) {
        this.system = system;
        this.spaceId = spaceId;
        this.time = time;
    }

    @Override
    public void execute() {
        cost = system.exit(spaceId, time);
        success = true;
    }

    @Override
    public String getLogEntry() {
        String status = success ? "Успешно" : "Ошибка";
        return "ВЫЕЗД | Место: " + spaceId + " | Сумма: " + String.format("%.2f", cost) + " руб. | Статус: " + status + " | Время: " + time;
    }

    public double getCost() { return cost; }
    public boolean isSuccess() { return success; }
}