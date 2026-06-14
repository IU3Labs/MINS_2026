package entity.state;


public interface TicketState {
    void pay(TicketContext context);
    void use(TicketContext context);
    void cancel(TicketContext context);
    TicketStatus getStatus();
    boolean canCancel();
    boolean isAvailable();
    boolean isPaid();
    boolean isUsed();
}