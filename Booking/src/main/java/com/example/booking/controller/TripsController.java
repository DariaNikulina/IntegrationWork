package com.example.booking.controller;

import com.example.booking.service.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class TripsController {
    private final TripService tripService;

    @GetMapping("/")
    public String index() { return "index"; }

    @GetMapping("/trips")
    public String listTrips(@RequestParam String from, @RequestParam String to, Model model) {
        var trips = tripService.search(from, to);
        model.addAttribute("trips", trips);
        model.addAttribute("from", from);
        model.addAttribute("to", to);
        return "trips";
    }

    @GetMapping("/trip/{id}")
    public String tripDetail(@PathVariable Long id, Model model) {
        var trip = tripService.findById(id);
        model.addAttribute("trip", trip);
        return "trip";
    }
}
