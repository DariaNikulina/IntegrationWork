package com.example.booking.service;

import com.example.booking.dto.TicketCancelledEvent;
import com.example.booking.dto.TicketEvent;
import com.example.booking.model.Seat;
import com.example.booking.model.Ticket;
import com.example.booking.model.Trip;
import com.example.booking.model.enums.SeatStatus;
import com.example.booking.model.enums.TicketStatus;
import com.example.booking.repositiry.SeatRepository;
import com.example.booking.repositiry.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final SeatRepository seatRepository;
    private final TicketRepository ticketRepository;
    private final KafkaProducerService kafkaProducerService;
    private final TripService tripService;

    @Transactional
    public Ticket book(Long tripId, Long seatId, String passengerName, String email) {
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new RuntimeException("Место не найдено"));
        if (seat.getStatus() != SeatStatus.FREE) {
            throw new IllegalStateException("Место уже зарезервировано");
        }
        seat.setStatus(SeatStatus.BOOKED);
        seatRepository.save(seat);

        Trip trip = tripService.findById(tripId);

        Ticket ticket = new Ticket();
        ticket.setTrip(trip);
        ticket.setSeat(seat);
        ticket.setPassengerName(passengerName);
        ticket.setEmail(email);
        ticket.setStatus(TicketStatus.RESERVED);

        ticket = ticketRepository.save(ticket);

        TicketEvent event = new TicketEvent(TicketStatus.RESERVED.name(), ticket.getId(), trip.getRoute().getFromCity(),
                trip.getRoute().getToCity(), email, trip.getDepartureTime().toString(), seat.getSeatNumber());

        kafkaProducerService.publishTicketReserved(event);

        return ticket;
    }

    @Transactional
    public void cancelTicket(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Билет не найден"));

        if (ticket.getStatus() == TicketStatus.CANCELLED) {
            throw new IllegalStateException("Билет уже отменён");
        }
        Seat seat = ticket.getSeat();
        seat.setStatus(SeatStatus.FREE);
        ticket.setStatus(TicketStatus.CANCELLED);
        ticketRepository.save(ticket);

        kafkaProducerService.publishTicketCancelled(new TicketCancelledEvent(ticket));
    }
}
