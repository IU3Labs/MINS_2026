package com.university.model;

public class Student implements Observer {
    private final int id;
    private final String name;

    public Student(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() { return id; }

    @Override
    public String getName() { return name; }

    @Override
    public void update(String message) {
        System.out.println(name + " получил уведомление: " + message);
    }

}
