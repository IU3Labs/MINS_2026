package com.autoservice.service;

import com.autoservice.exception.CarNotFoundException;
import com.autoservice.exception.AppointmentConflictException;
import com.autoservice.grpc.ReferenceServiceGrpcClient;
import com.autoservice.model.Appointment;
import com.autoservice.model.Car;
import com.autoservice.model.Service;
import com.autoservice.model.WorkHistory;
import com.autoservice.repository.AppointmentRepository;
import com.autoservice.repository.CarRepository;
import com.autoservice.repository.HistoryRepository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AutoServiceAppointmentService {
    private final CarRepository carRepository;
    private final AppointmentRepository appointmentRepository;
    private final HistoryRepository historyRepository;
    private final ReferenceServiceGrpcClient referenceClient;

    public AutoServiceAppointmentService(
            CarRepository carRepository,
            AppointmentRepository appointmentRepository,
            HistoryRepository historyRepository,
            ReferenceServiceGrpcClient referenceClient) {
        this.carRepository = carRepository;
        this.appointmentRepository = appointmentRepository;
        this.historyRepository = historyRepository;
        this.referenceClient = referenceClient;
    }

    public Appointment createAppointment(String licensePlate, String serviceName, String dateTime, String traceId)
            throws CarNotFoundException, AppointmentConflictException {

        Car car = carRepository.findCarByLicensePlate(licensePlate);

        var response = referenceClient.getServiceByName(serviceName, traceId);

        if (!response.getFound()) {
            throw new IllegalArgumentException("Ошибка: " + response.getErrorMessage());
        }

        Service service = new Service(response.getName(), response.getPrice(), response.getDurationMinutes());

        Appointment appointment = new Appointment(car, service, dateTime);
        int visitsCount = historyRepository.getHistoryByCar(car).size() + 1;
        appointment.setClientVisitsCount(visitsCount);
        appointmentRepository.scheduleAppointment(appointment);

        return appointment;
    }

    public void completeAppointment(Appointment appointment) {
        if (!appointment.isCompleted()) {
            appointment.nextStatus();
            if (!appointment.isCompleted()) {
                appointment.nextStatus();
            }
        }

        appointmentRepository.markAppointmentCompleted(appointment);

        WorkHistory history = new WorkHistory(
                appointment,
                "Выполнены работы по " + appointment.getService().getName(),
                new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date())
        );
        historyRepository.addToHistory(history);
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.getAllAppointments();
    }
}