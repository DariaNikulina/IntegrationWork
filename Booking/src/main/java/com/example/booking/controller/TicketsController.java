package com.example.booking.controller;

import com.example.booking.repositiry.TicketRepository;
import com.example.booking.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class TicketsController {
    private final BookingService bookingService;
    private final TicketRepository ticketRepository;

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
