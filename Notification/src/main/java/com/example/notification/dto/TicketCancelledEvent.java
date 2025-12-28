package com.example.notification.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class TicketCancelledEvent extends TicketEvent {
    private LocalDateTime cancelledAt;
}