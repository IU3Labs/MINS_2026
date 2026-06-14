package entity.state;

import entity.Ticket;

public interface TicketState {
    void pay(TicketContext context);
    void use(TicketContext context);
    void cancel(TicketContext context);
    String getStatusName();
    boolean canCancel();
    boolean isAvailable();
    boolean isPaid();
    boolean isUsed();
}