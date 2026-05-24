package org.example.service.validation;

import org.example.domain.exception.ValidationException;

public class CustomerNameValidationHandler extends TicketIssuingValidationHandler {
    @Override
    protected void check(TicketIssuingContext context) {
        String customerName = context.customerName();
        if (customerName == null || customerName.isBlank()) {
            throw new ValidationException("Customer name cannot be empty");
        }
    }
}
