package org.example.domain.model.state;

import org.example.domain.exception.ValidationException;
import org.example.domain.model.TicketStatus;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public final class TicketStateRegistry {
    private static final Map<TicketStatus, TicketState> STATES = registerDefaults();

    private TicketStateRegistry() {
    }

    public static TicketState resolve(TicketStatus status) {
        TicketState state = STATES.get(status);
        if (state == null) {
            throw new ValidationException("No state registered for ticket status: " + status);
        }
        return state;
    }

    private static Map<TicketStatus, TicketState> registerDefaults() {
        Map<TicketStatus, TicketState> states = new EnumMap<>(TicketStatus.class);
        for (TicketState state : List.of(
                new ReservedTicketState(),
                new PurchasedTicketState(),
                new CancelledTicketState()
        )) {
            states.put(state.status(), state);
        }
        return states;
    }
}
