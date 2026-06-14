package entity.state;

public class TicketContext {
    private TicketState state;

    public TicketContext() {
        this.state = new AvailableState();
    }

    public TicketContext(TicketState state) {
        this.state = state;
    }

    public TicketState getState() {
        return state;
    }

    public void setState(TicketState state) {
        this.state = state;
    }

    public void pay() {
        state.pay(this);
    }

    public void use() {
        state.use(this);
    }

    public void cancel() {
        state.cancel(this);
    }

    public boolean canCancel() {
        return state.canCancel();
    }

    public TicketStatus getStatus() {
        return state.getStatus();
    }

    public boolean isAvailable() {
        return state.isAvailable();
    }

    public boolean isPaid() {
        return state.isPaid();
    }

    public boolean isUsed() {
        return state.isUsed();
    }
}