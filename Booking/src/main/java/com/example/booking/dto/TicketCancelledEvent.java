package com.example.booking.dto;

import com.example.booking.model.Ticket;
import com.example.booking.model.enums.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketCancelledEvent extends TicketEvent {
    private LocalDateTime cancelledAt;

    public TicketCancelledEvent(Ticket ticket) {
        super(TicketStatus.CANCELLED.name(), ticket.getId(), ticket.getTrip().getRoute().getFromCity(), ticket.getTrip().getRoute().getToCity(),
                ticket.getEmail(), String.valueOf(ticket.getTrip().getDepartureTime()), ticket.getSeat().getSeatNumber());
        this.cancelledAt = LocalDateTime.now();
    }
}