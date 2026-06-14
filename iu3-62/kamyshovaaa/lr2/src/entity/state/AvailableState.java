package entity.state;

public class AvailableState implements TicketState {
    @Override
    public void pay(TicketContext context) {
        context.setState(new PaidState());
    }

    @Override
    public void use(TicketContext context) {
    }

    @Override
    public void cancel(TicketContext context) {
    }

    @Override
    public String getStatusName() {
        return "AVAILABLE";
    }

    @Override
    public boolean canCancel() {
        return false;
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public boolean isPaid() {
        return false;
    }

    @Override
    public boolean isUsed() {
        return false;
    }
}