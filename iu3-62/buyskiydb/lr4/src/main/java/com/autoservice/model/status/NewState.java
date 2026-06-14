package com.autoservice.model.status;


import com.autoservice.model.Appointment;

public class NewState implements AppointmentState {
    @Override
    public void next(Appointment appointment) {
        appointment.setState(new InProgressState());
    }

    @Override
    public void cancel(Appointment appointment) {
        appointment.setState(new CancelledState());
    }

    @Override
    public String getStatusName() {
        return "НОВАЯ";
    }
}