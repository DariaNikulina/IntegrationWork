package com.example.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketEvent {
    private String eventType;
    private Long ticketId;
    private String fromCity;
    private String toCity;
    private String email;
    private String departureTime;
    private Integer seatNumber;
}
