package main.java.models;

public class Journal extends Publication {
    private int issueNumber;

    public Journal(int id, String title, String author, int year, int issueNumber) {
        super(id, title, author, year);
        this.issueNumber = issueNumber;
    }

    @Override
    public String getType() { return "Журнал"; }
    public int getIssueNumber() { return issueNumber; }

    @Override
    public String toString() {
        return String.format("%s [Выпуск: %d]", super.toString(), issueNumber);
    }
}