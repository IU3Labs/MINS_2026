package model;

import java.time.LocalDate;

public class AttendanceRecord {
    private final int id;
    private final Student student;
    private final Lesson lesson;
    private final LocalDate date;
    private AttendanceStatus status;

    public AttendanceRecord(int id, Student student, Lesson lesson, LocalDate date, AttendanceStatus status) {
        this.id = id;
        this.student = student;
        this.lesson = lesson;
        this.date = date;
        this.status = status;
    }

    public int getId() { return id; }
    public Student getStudent() { return student; }
    public Lesson getLesson() { return lesson; }
    public LocalDate getDate() { return date; }
    public AttendanceStatus getStatus() { return status; }

    public void updateStatus(AttendanceStatus newStatus) {
        this.status = newStatus;
    }

    @Override
    public String toString() {
        return student.getName() + " | " + lesson.getCourse().getName() + " | " + date + " | " + status;
    }
}
