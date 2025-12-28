package com.example.booking.repositiry;

import com.example.booking.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TripRepository extends JpaRepository<Trip, Long> {
    List<Trip> findByRouteFromCityIgnoreCaseAndRouteToCityIgnoreCaseAndDepartureTimeAfter(
            String fromCity, String toCity, LocalDateTime fromTime);
}
