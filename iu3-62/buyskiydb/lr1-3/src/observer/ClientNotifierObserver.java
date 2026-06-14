package observer;

import model.Appointment;

public class ClientNotifierObserver implements AppointmentObserver {
    @Override
    public void update(Appointment appointment, String oldStatus, String newStatus) {
        String clientName = appointment.getCar().getOwner().getName();
        System.out.printf("УВЕДОМЛЕНИЕ для %s: Статус вашей заявки изменен с '%s' на '%s'%n",
                clientName, oldStatus, newStatus);
    }
}