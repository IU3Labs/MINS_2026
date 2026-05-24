package org.example.service.validation;

import org.example.domain.exception.InvalidSeatException;

public class SeatExistsValidationHandler extends TicketIssuingValidationHandler {
    @Override
    protected void check(TicketIssuingContext context) {
        if (!context.session().supportsSeat(context.seat())) {
            throw new InvalidSeatException("Seat does not exist in the selected hall: " + context.seat());
        }
    }
}
