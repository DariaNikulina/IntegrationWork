package com.example.notification.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "eventType"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = TicketEvent.class, name = "RESERVED"),
        @JsonSubTypes.Type(value = TicketCancelledEvent.class, name = "CANCELLED")
})
public class TicketEvent {
    private Long ticketId;
    private String fromCity;
    private String toCity;
    private String email;
    private String departureTime;
    private Integer seatNumber;
}
