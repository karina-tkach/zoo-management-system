package org.university.zoomanagementsystem.ticket.repository;

import org.university.zoomanagementsystem.ticket.Ticket;

import java.util.List;

public interface TicketRepository {
    int addTicket(Ticket ticket, int pricingId);

    Ticket getTicketById(int id);

    List<Ticket> getTickets();

}
