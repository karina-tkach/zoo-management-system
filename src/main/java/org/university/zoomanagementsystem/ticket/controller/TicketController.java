package org.university.zoomanagementsystem.ticket.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.university.zoomanagementsystem.ticket.Ticket;
import org.university.zoomanagementsystem.ticket.service.TicketService;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {
    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
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
}
