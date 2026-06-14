package repository;

import exception.AppointmentConflictException;
import model.Appointment;
import model.Car;

import java.util.List;

public interface AppointmentRepository {
    void scheduleAppointment(Appointment appointment) throws AppointmentConflictException;
    List<Appointment> getAppointmentsByDate(String date);
    List<Appointment> getAllAppointments();
    void markAppointmentCompleted(Appointment appointment);
}