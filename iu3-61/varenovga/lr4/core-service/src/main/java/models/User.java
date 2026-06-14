package models;

import java.util.ArrayList;
import java.util.List;

public class User {
    private int id;
    private String name;
    private String email;
    private List<Integer> borrowedPublicationIds;

    public User(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.borrowedPublicationIds = new ArrayList<>();
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }

    public List<Integer> getBorrowedPublicationIds() {
        return new ArrayList<>(borrowedPublicationIds);
    }

    public void addBorrowedPublicationId(int pubId) { borrowedPublicationIds.add(pubId); }
    public void removeBorrowedPublicationId(int pubId) { borrowedPublicationIds.remove(Integer.valueOf(pubId)); }

    @Override
    public String toString() {
        return String.format("User[ID=%d, Name=%s, Email=%s]", id, name, email);
    }
}