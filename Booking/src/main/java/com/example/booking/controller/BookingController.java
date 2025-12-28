package com.example.booking.controller;

import com.example.booking.model.Ticket;
import com.example.booking.model.Trip;
import com.example.booking.repositiry.TicketRepository;
import com.example.booking.service.BookingService;
import com.example.booking.service.TripService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Validated
@RequiredArgsConstructor
public class BookingController {
    private final TripService tripService;
    private final BookingService bookingService;
    private final TicketRepository ticketRepository;


    @PostMapping("/book")
    public String book(@RequestParam Long tripId,
                       @RequestParam Long seatId,
                       @RequestParam @NotBlank String passengerName,
                       @RequestParam @Email String email,
                       Model model) {
        try {
            Ticket ticket = bookingService.book(tripId, seatId, passengerName, email);
            model.addAttribute("ticket", ticket);
            model.addAttribute("email", email);
            return "success";
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            Trip trip = tripService.findById(tripId);
            model.addAttribute("trip", trip);
            return "trip";
        }
    }

    @GetMapping("/tickets")
    public String myTickets(@RequestParam String email, Model model) {
        model.addAttribute("tickets", ticketRepository.findByEmail(email));
        return "tickets";
    }


    @PostMapping("/ticket/{id}/cancel")
    public String cancelTicket(@PathVariable Long id, Model model) {
        try {
            bookingService.cancelTicket(id);
            model.addAttribute("success", "Билет успешно отменен");
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
        }
        return "tickets";
    }
}
