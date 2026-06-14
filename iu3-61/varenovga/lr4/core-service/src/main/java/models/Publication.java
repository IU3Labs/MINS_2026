package models;

public abstract class Publication {
    protected int id;
    protected String title;
    protected String author;
    protected int year;
    protected boolean isAvailable;

    public Publication(int id, String title, String author, int year) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.year = year;
        this.isAvailable = true;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public int getYear() { return year; }
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }
    public abstract String getType();

    @Override
    public String toString() {
        return String.format("%s (%d)", title, year);
    }
}