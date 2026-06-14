package entity.state;

public class PaidState implements TicketState {
    @Override
    public void pay(TicketContext context) {
    }

    @Override
    public void use(TicketContext context) {
        context.setState(new UsedState());
    }

    @Override
    public void cancel(TicketContext context) {
        context.setState(new AvailableState());
    }

    @Override
    public TicketStatus getStatus() {
        return TicketStatus.PAID;
    }

    @Override
    public boolean canCancel() {
        return true;
    }

    @Override
    public boolean isAvailable() {
        return false;
    }

    @Override
    public boolean isPaid() {
        return true;
    }

    @Override
    public boolean isUsed() {
        return false;
    }
}