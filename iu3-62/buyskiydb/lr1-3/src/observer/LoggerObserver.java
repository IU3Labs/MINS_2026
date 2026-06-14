package observer;

import model.Appointment;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LoggerObserver implements AppointmentObserver {
    @Override
    public void update(Appointment appointment, String oldStatus, String newStatus) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.printf("[LOG %s] Заявка для %s (услуга: %s) изменила статус: %s -> %s%n",
                timestamp,
                appointment.getCar().getLicensePlate(),
                appointment.getService().getName(),
                oldStatus,
                newStatus);
    }
}