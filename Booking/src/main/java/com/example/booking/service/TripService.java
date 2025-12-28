package com.example.booking.service;

import com.example.booking.model.Seat;
import com.example.booking.model.Trip;
import com.example.booking.repositiry.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TripService {
    private final TripRepository tripRepository;

    public List<Trip> findAll() {
        return tripRepository.findAll();
    }

    public List<Trip> search(String from, String to) {
        return tripRepository.findByRouteFromCityIgnoreCaseAndRouteToCityIgnoreCaseAndDepartureTimeAfter(from, to, LocalDateTime.now());
    }

    public Trip findById(Long id) {
        Trip trip = tripRepository.findById(id).orElseThrow(() -> new RuntimeException("Поездка не найдена"));
        trip.getSeats().sort(Comparator.comparing(Seat::getSeatNumber));
        return trip;
    }
}
