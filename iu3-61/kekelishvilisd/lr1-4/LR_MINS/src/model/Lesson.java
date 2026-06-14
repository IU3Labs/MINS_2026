package model;

public class Lesson {
    private final int id;
    private final Course course;
    private final String teacherName;
    private final String room;
    private final String dateTime; // "MON 10:00-11:30"

    public Lesson(int id, Course course, String teacherName, String room, String dateTime) {
        this.id = id;
        this.course = course;
        this.teacherName = teacherName;
        this.room = room;
        this.dateTime = dateTime;
    }

    public int getId() { return id; }
    public Course getCourse() { return course; }
    public String getTeacherName() { return teacherName; }
    public String getRoom() { return room; }
    public String getDateTime() { return dateTime; }

    @Override
    public String toString() {
        return course.getName() + " | " + teacherName + " | " + room + " | " + dateTime;
    }
}