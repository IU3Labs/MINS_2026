package com.autoservice.model.status;


import com.autoservice.model.Appointment;

public interface AppointmentState {
    void next(Appointment appointment);
    void cancel(Appointment appointment);
    String getStatusName();
}