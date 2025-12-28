package com.example.notification.listener;

import com.example.notification.dto.TicketCancelledEvent;
import com.example.notification.dto.TicketEvent;
import com.example.notification.service.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationListener {
    private final EmailService emailService;

    @KafkaListener(topics = "notification-topic", containerFactory = "kafkaListenerContainerFactory")
    public void handle(TicketEvent event) throws MessagingException {
        if (event instanceof TicketCancelledEvent e) {
            emailService.sendTicketCancelledEmail(e);
        } else {
            emailService.sendTicketReservedEmail(event);
        }
    }
}
