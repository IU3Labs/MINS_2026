package observer;

import model.Appointment;

public interface AppointmentObserver {
    void update(Appointment appointment, String oldStatus, String newStatus);
}