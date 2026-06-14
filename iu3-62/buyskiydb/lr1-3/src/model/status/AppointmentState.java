package model.status;


import model.Appointment;

public interface AppointmentState {
    void next(Appointment appointment);
    void cancel(Appointment appointment);
    String getStatusName();
}