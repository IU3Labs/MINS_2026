package model;

public class Grade {
    private final Student student;
    private final Course course;
    private final int value;

    public Grade(Student student, Course course, int value) {
        this.student = student;
        this.course = course;
        this.value = value;
    }
    public Student getStudent() { return student; }
    public Course getCourse() { return course; }
    public int getValue() { return value; }
}
