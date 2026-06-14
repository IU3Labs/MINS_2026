package com.autoservice.observer;


import com.autoservice.model.Appointment;

public interface AppointmentObserver {
    void update(Appointment appointment, String oldStatus, String newStatus);
}