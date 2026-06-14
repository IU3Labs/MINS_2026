package com.autoservice.service;



import com.autoservice.model.Appointment;
import com.autoservice.model.WorkHistory;
import com.autoservice.repository.AppointmentRepository;
import com.autoservice.repository.HistoryRepository;

import java.util.List;

public class ReportingService {
    private final AppointmentRepository appointmentRepository;
    private final HistoryRepository historyRepository;

    public ReportingService(AppointmentRepository appointmentRepository, HistoryRepository historyRepository) {
        this.appointmentRepository = appointmentRepository;
        this.historyRepository = historyRepository;
    }

    public void printTodaySchedule(String date) {
        System.out.println("\n=== РАСПИСАНИЕ НА " + date + " ===");
        List<Appointment> appointments = appointmentRepository.getAppointmentsByDate(date);
        if (appointments.isEmpty()) {
            System.out.println("Нет записей");
        } else {
            appointments.forEach(System.out::println);
        }
    }

    public void printWorkHistory() {
        System.out.println("\n=== ИСТОРИЯ РАБОТ ===");
        List<WorkHistory> history = historyRepository.getAllHistory();
        if (history.isEmpty()) {
            System.out.println("История пуста");
        } else {
            history.forEach(System.out::println);
        }
    }

    public void calculateTotalEarnings() {
        double total = appointmentRepository.getAllAppointments().stream()
                .filter(Appointment::isCompleted)
                .mapToDouble(Appointment::calculateCost)
                .sum();

        System.out.printf("\n=== ОБЩАЯ ВЫРУЧКА: %.2f руб. ===\n", total);
    }
}