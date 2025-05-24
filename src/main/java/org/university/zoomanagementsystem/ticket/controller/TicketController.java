package org.university.zoomanagementsystem.ticket.controller;

import com.stripe.exception.StripeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.university.zoomanagementsystem.stripe.CheckoutRequest;
import org.university.zoomanagementsystem.stripe.CheckoutResponse;
import org.university.zoomanagementsystem.stripe.service.StripeService;
import org.university.zoomanagementsystem.ticket.Ticket;
import org.university.zoomanagementsystem.ticket.service.TicketService;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {
    private final TicketService ticketService;
    private final StripeService stripeService;

    public TicketController(TicketService ticketService, StripeService stripeService) {
        this.ticketService = ticketService;
        this.stripeService = stripeService;
    }

    @PreAuthorize("hasAuthority('TICKET_AGENT')")
    @GetMapping
    public ResponseEntity<List<Ticket>> getAll() {
        return ResponseEntity.ok(ticketService.getTickets());
    }

    @PreAuthorize("hasAuthority('TICKET_AGENT')")
    @GetMapping("/{id}")
    public ResponseEntity<Ticket> getTicketDetails(@PathVariable int id) {
        Ticket ticket = ticketService.getTicketById(id);
        return ResponseEntity.ok(ticket);
    }

    @PreAuthorize("hasAuthority('TICKET_AGENT')")
    @PostMapping
    public ResponseEntity<Ticket> addTicket(@RequestBody Ticket ticket) {
        Ticket createdTicket = ticketService.addTicketOffline(ticket);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTicket);
    }

    @PostMapping("/buy-ticket")
    public ResponseEntity<?> buyTicketOnline(@RequestBody Ticket ticket) throws StripeException {
        //validation here
        // Create Stripe Checkout session
        CheckoutRequest checkoutRequest = new CheckoutRequest();
        checkoutRequest.setAmount((long) ticket.getPrice()); // Stripe expects amounts in cents
        checkoutRequest.setSuccessUrl("http://localhost:5173/success"); // Frontend success URL
        checkoutRequest.setCancelUrl("http://localhost:5173/cancel");   // Frontend cancel URL

        CheckoutResponse checkoutResponse = stripeService.createCheckoutSession(ticket.getFullName(), ticket.getEmail(), ticket.getTicketType(),
                ticket.getVisitType(), ticket.getVisitDate(), ticket.getExcursionId(), checkoutRequest);

        return ResponseEntity.ok(checkoutResponse);
    }
}
