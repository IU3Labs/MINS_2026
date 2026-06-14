package entity.state;

public class UsedState implements TicketState {
    @Override
    public void pay(TicketContext context) {
    }

    @Override
    public void use(TicketContext context) {
    }

    @Override
    public void cancel(TicketContext context) {
    }

    @Override
    public String getStatusName() {
        return "USED";
    }

    @Override
    public boolean canCancel() {
        return false;
    }

    @Override
    public boolean isAvailable() {
        return false;
    }

    @Override
    public boolean isPaid() {
        return false;
    }

    @Override
    public boolean isUsed() {
        return true;
    }
}