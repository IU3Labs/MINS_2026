package com.autoservice.repository;


import com.autoservice.exception.AppointmentConflictException;
import com.autoservice.model.Appointment;

import java.util.List;

public interface AppointmentRepository {
    void scheduleAppointment(Appointment appointment) throws AppointmentConflictException;
    List<Appointment> getAppointmentsByDate(String date);
    List<Appointment> getAllAppointments();
    void markAppointmentCompleted(Appointment appointment);

}