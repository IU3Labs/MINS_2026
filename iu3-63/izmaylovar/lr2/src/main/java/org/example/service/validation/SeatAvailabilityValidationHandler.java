package org.example.service.validation;

import org.example.domain.exception.SeatAlreadyBookedException;
import org.example.service.SeatAvailabilityService;

public class SeatAvailabilityValidationHandler extends TicketIssuingValidationHandler {
    private final SeatAvailabilityService seatAvailabilityService;

    public SeatAvailabilityValidationHandler(SeatAvailabilityService seatAvailabilityService) {
        this.seatAvailabilityService = seatAvailabilityService;
    }

    @Override
    protected void check(TicketIssuingContext context) {
        if (!seatAvailabilityService.isSeatAvailable(context.session().getId(), context.seat())) {
            throw new SeatAlreadyBookedException(
                    "Seat is already reserved or purchased: " + context.seat()
            );
        }
    }
}
