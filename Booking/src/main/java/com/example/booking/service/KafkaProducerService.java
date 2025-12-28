package com.example.booking.service;

import com.example.booking.dto.TicketCancelledEvent;
import com.example.booking.dto.TicketEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    @Value("${kafka.notification.topic:notification-topic}")
    private String notificationTopic;

    public void publishTicketReserved(TicketEvent event) {
        kafkaTemplate.send(notificationTopic, String.valueOf(event.getTicketId()), event);
    }

    public void publishTicketCancelled(TicketCancelledEvent event) {
        kafkaTemplate.send(notificationTopic, event);
    }
}
