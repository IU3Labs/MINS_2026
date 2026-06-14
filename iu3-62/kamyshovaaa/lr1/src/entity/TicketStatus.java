package entity;
// State + Unit of Work
public enum TicketStatus {
    AVAILABLE,
    PAID,
    USED;

    public boolean canTransitionTo(TicketStatus target) {
        switch (this) {
            case AVAILABLE: return target == PAID;
            case PAID: return target == USED || target == AVAILABLE;
            case USED: return false;
            default: return false;
        }
    }
}