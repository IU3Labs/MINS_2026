package entity;

public class Seat {
    private final int row;
    private final int number;

    public Seat(int row, int number) {
        this.row = row;
        this.number = number;
    }

    public int getRow() { return row; }
    public int getNumber() { return number; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Seat)) return false;
        Seat seat = (Seat) o;
        return row == seat.row && number == seat.number;
    }

    @Override
    public int hashCode() {
        return 31 * row + number;
    }
}