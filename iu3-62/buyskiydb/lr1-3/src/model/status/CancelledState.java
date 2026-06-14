package model.status;

import model.Appointment;

public class CancelledState implements AppointmentState {
    @Override
    public void next(Appointment appointment) {
        System.out.println("Заявка отменена. Нельзя перевести в следующий статус.");
    }

    @Override
    public void cancel(Appointment appointment) {
        System.out.println("Заявка уже отменена");
    }

    @Override
    public String getStatusName() {
        return "ОТМЕНЕНА";
    }
}