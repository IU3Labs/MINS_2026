package command;

import java.time.LocalDateTime;
import core.ParkingSystem;
import model.ParkingTicket;

public class EnterVehicleCommand implements ParkingCommand {
    private final ParkingSystem system;
    private final String plate;
    private final LocalDateTime time;
    private ParkingTicket resultTicket;

    public EnterVehicleCommand(ParkingSystem system, String plate, LocalDateTime time) {
        this.system = system;
        this.plate = plate;
        this.time = time;
    }

    @Override
    public void execute() {
        resultTicket = system.enter(plate, time);
    }

    @Override
    public String getLogEntry() {
        if (resultTicket != null) {
            return "ВЪЕЗД | Авто: " + plate + " | Место: " + resultTicket.getSpaceId() + " | Время: " + time;
        }
        return "ВЪЕЗД | Авто: " + plate + " | Статус: Не выполнено";
    }

    public ParkingTicket getResultTicket() {
        return resultTicket;
    }
}