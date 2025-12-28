package com.example.notification.service;

import com.example.notification.dto.TicketCancelledEvent;
import com.example.notification.dto.TicketEvent;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Value("${booking-service.url}")
    private String bookingServiceUrl;

    public void sendTicketReservedEmail(TicketEvent event) throws MessagingException {
        Context ctx = buildContext(event);
        ctx.setVariable("viewLink", ticketViewLink(event.getEmail()));

        String htmlContent = templateEngine.process("email/ticket", ctx);

        send(event.getEmail(),
                "Ваш билет — " + event.getFromCity() + " → " + event.getToCity(),
                htmlContent);
    }

    public void sendTicketCancelledEmail(TicketCancelledEvent event) throws MessagingException {
        Context ctx = buildContext(event);
        ctx.setVariable("cancelledAt", event.getCancelledAt());

        String htmlContent = templateEngine.process("email/ticket-cancelled", ctx);

        send(event.getEmail(),
                "Билет отменён — " + event.getFromCity() + " → " + event.getToCity(),
                htmlContent);
    }

    private Context buildContext(TicketEvent event) {
        Context ctx = new Context();

        ctx.setVariable("ticketId", event.getTicketId());
        ctx.setVariable("fromCity", event.getFromCity());
        ctx.setVariable("toCity", event.getToCity());
        ctx.setVariable("departureTime", event.getDepartureTime());
        ctx.setVariable("seatNumber", event.getSeatNumber());
        return ctx;
    }

    private void send(String to, String subject, String html) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper =
                new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(html, true);
        helper.setFrom("no-reply@booking.example");

        mailSender.send(message);
    }

    private String ticketViewLink(String email) {
        return bookingServiceUrl + "/tickets?email=" + email;
    }
}
