package com.autoservice.model.status;


import com.autoservice.model.Appointment;

public class CompletedState implements AppointmentState {
    @Override
    public void next(Appointment appointment) {
        System.out.println("Заявка уже выполнена. Дальнейших статусов нет.");
    }

    @Override
    public void cancel(Appointment appointment) {
        System.out.println("Нельзя отменить выполненную заявку");
    }

    @Override
    public String getStatusName() {
        return "ВЫПОЛНЕНА";
    }
}