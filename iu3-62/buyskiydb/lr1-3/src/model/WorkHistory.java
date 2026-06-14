package model;

public class WorkHistory {
    private Appointment appointment;
    private String workDescription;
    private String dateCompleted;

    public WorkHistory(Appointment appointment, String workDescription, String dateCompleted) {
        this.appointment = appointment;
        this.workDescription = workDescription;
        this.dateCompleted = dateCompleted;
    }

    @Override
    public String toString() {
        return String.format("%s: %s - %s. Работы: %s",
                dateCompleted, appointment.getCar(), appointment.getService().getName(), workDescription);
    }
}