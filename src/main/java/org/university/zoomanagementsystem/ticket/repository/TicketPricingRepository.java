package org.university.zoomanagementsystem.ticket.repository;

import org.university.zoomanagementsystem.ticket.TicketPricing;

import java.util.List;

public interface TicketPricingRepository {
    List<TicketPricing> getTicketPricings();
    TicketPricing getTicketPricingById(int ticketId);
    void updateTicketPricingById(int price, int id);
    TicketPricing getTicketPricingByTicketAndVisitType(String ticketType, String visitType);
}
