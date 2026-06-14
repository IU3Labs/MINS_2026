package com.autoservice.model.status;


import com.autoservice.model.Appointment;

public class InProgressState implements AppointmentState {
    @Override
    public void next(Appointment appointment) {
        appointment.setState(new CompletedState());
    }


    @Override
    public void cancel(Appointment appointment) {
        System.out.println("Нельзя отменить заявку, которая уже в работе");
    }

    @Override
    public String getStatusName() {
        return "В РАБОТЕ";
    }
}

