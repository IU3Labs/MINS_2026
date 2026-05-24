package org.example.service.validation;

public abstract class TicketIssuingValidationHandler {
    private TicketIssuingValidationHandler next;

    public TicketIssuingValidationHandler linkWith(TicketIssuingValidationHandler nextHandler) {
        this.next = nextHandler;
        return nextHandler;
    }

    public final void validate(TicketIssuingContext context) {
        check(context);
        if (next != null) {
            next.validate(context);
        }
    }

    protected abstract void check(TicketIssuingContext context);
}
