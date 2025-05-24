package org.university.zoomanagementsystem.stripe.controller;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.university.zoomanagementsystem.stripe.service.StripeService;
import org.university.zoomanagementsystem.ticket.service.TicketService;
import org.university.zoomanagementsystem.ticket.Ticket;

@RestController
@RequestMapping("/api/stripe")
public class StripeController {
    private final TicketService ticketService;
    private final StripeService stripeService;

    public StripeController(TicketService ticketService, StripeService stripeService) {
        this.ticketService = ticketService;
        this.stripeService = stripeService;
    }

    @Value("${stripe.secretWebHookKey}")
    private String stripeWebHookKey;

    @PostMapping("/webhook")
    public ResponseEntity<?> handleStripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader
    ) throws MessagingException, UnsupportedEncodingException {
        String endpointSecret = stripeWebHookKey;
        Event event;

        try {
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
        } catch (SignatureVerificationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid signature");
        }

        if ("checkout.session.completed".equals(event.getType())) {
            Session session = (Session) event.getDataObjectDeserializer().getObject().orElse(null);

            if (session != null) {
                String fullName = session.getMetadata().get("fullName");
                String email = session.getMetadata().get("email");
                String ticketType = session.getMetadata().get("ticketType");
                String visitType = session.getMetadata().get("visitType");
                LocalDate visitDate = LocalDate.parse(session.getMetadata().get("visitDate"));
                Integer excursionId = Integer.valueOf(session.getMetadata().get("excursionId"));
                int price = (int) (session.getAmountTotal() / 100);

                Ticket ticket = new Ticket();
                ticket.setFullName(fullName);
                ticket.setEmail(email);
                ticket.setTicketType(ticketType);
                ticket.setVisitType(visitType);
                ticket.setPrice(price);
                ticket.setVisitDate(visitDate);
                ticket.setExcursionId(excursionId);
                ticketService.addTicketOnline(ticket);
            }
        }

        return ResponseEntity.ok("Webhook processed");
    }

    @GetMapping("/get-session")
    public ResponseEntity<?> getSessionDetails(
            @RequestParam("session_id") String sessionId
    ) {
        try {
            // Retrieve session details from Stripe
            Session session = stripeService.getSessionDetails(sessionId);

            // Create a map/Dto with the required fields
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("amount_total", session.getAmountTotal());
            responseData.put("metadata", session.getMetadata());
            responseData.put("payment_status", session.getPaymentStatus());
            responseData.put("status", session.getStatus());

            return ResponseEntity.ok(responseData);
        } catch (StripeException e) {
            // Handle errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve session details: " + e.getMessage());
        }
    }
}
