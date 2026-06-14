package com.autoservice.repository.impl;


import com.autoservice.exception.AppointmentConflictException;
import com.autoservice.model.Appointment;
import com.autoservice.repository.AppointmentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InMemoryAppointmentRepository implements AppointmentRepository {
    private final List<Appointment> appointments = new ArrayList<>();

    @Override
    public void scheduleAppointment(Appointment appointment) throws AppointmentConflictException {

        boolean conflict = appointments.stream()
                .filter(a -> !a.isCompleted() && !a.isCancelled())
                .anyMatch(a -> a.getDateTime().equals(appointment.getDateTime()));

        if (conflict) {
            throw new AppointmentConflictException(appointment.getDateTime());
        }
        appointments.add(appointment);
    }

    @Override
    public List<Appointment> getAppointmentsByDate(String date) {
        return appointments.stream()
                .filter(a -> a.getDateTime().startsWith(date))
                .collect(Collectors.toList());
    }

    @Override
    public List<Appointment> getAllAppointments() {
        return new ArrayList<>(appointments);
    }

    @Override
    public void markAppointmentCompleted(Appointment appointment) {
        for (Appointment a : appointments) {
            if (a.equals(appointment) ||
                    (a.getCar().equals(appointment.getCar()) &&
                            a.getDateTime().equals(appointment.getDateTime()) &&
                            a.getService().equals(appointment.getService()))) {

                while (!a.isCompleted() && !a.isCancelled()) {
                    a.nextStatus();
                }
                break;
            }
        }
    }
}