package entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Hall {
    private final UUID id;
    private final String name;
    private final int rows;
    private final int seatsPerRow;

    public Hall(UUID id, String name, int rows, int seatsPerRow) {
        this.id = id;
        this.name = name;
        this.rows = rows;
        this.seatsPerRow = seatsPerRow;
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public int getRows() { return rows; }
    public int getSeatsPerRow() { return seatsPerRow; }


    public List<Seat> getSeats() {
        List<Seat> seats = new ArrayList<>();
        for (int r = 1; r <= rows; r++) {
            for (int n = 1; n <= seatsPerRow; n++) {
                seats.add(new Seat(r, n));
            }
        }
        return seats;
    }
}