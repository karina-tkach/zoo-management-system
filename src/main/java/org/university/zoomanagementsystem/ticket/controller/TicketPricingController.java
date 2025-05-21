package org.university.zoomanagementsystem.ticket.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.university.zoomanagementsystem.ticket.TicketPricing;
import org.university.zoomanagementsystem.ticket.service.TicketPricingService;

import java.util.List;

@RestController
@RequestMapping("/api/ticket-pricings")
public class TicketPricingController {
    private final TicketPricingService ticketPricingService;

    public TicketPricingController(TicketPricingService ticketPricingService) {
        this.ticketPricingService = ticketPricingService;
    }

    @PreAuthorize("hasAuthority('TICKET_AGENT')")
    @GetMapping
    public ResponseEntity<List<TicketPricing>> getAll() {
        return ResponseEntity.ok(ticketPricingService.getTicketPricings());
    }

    @GetMapping("/by-type")
    public ResponseEntity<TicketPricing> getByTicketAndVisitType(
            @RequestParam String ticketType,
            @RequestParam String visitType) {
        TicketPricing ticketPricing = ticketPricingService.getTicketPricingByTicketAndVisitType(ticketType, visitType);
        return ResponseEntity.ok(ticketPricing);
    }

    @PreAuthorize("hasAuthority('TICKET_AGENT')")
    @PatchMapping("/{id}")
    public ResponseEntity<TicketPricing> updatePrice(
            @PathVariable int id,
            @RequestParam int price) {

        TicketPricing ticketPricing = ticketPricingService.updateTicketPricingById(price, id);
        return ResponseEntity.ok(ticketPricing);
    }
}
