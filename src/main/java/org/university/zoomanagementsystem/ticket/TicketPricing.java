package org.university.zoomanagementsystem.ticket;

import java.util.Objects;

public class TicketPricing {
    private int id;
    private TicketType ticketType;
    private VisitType visitType;
    private int price;

    public TicketPricing() {
        this.id = -1;
        this.ticketType = null;
        this.visitType = null;
        this.price = -1;
    }

    public TicketPricing(int id, TicketType ticketType, VisitType visitType, int price) {
        this.id = id;
        this.ticketType = ticketType;
        this.visitType = visitType;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TicketType getTicketType() {
        return ticketType;
    }

    public void setTicketType(TicketType ticketType) {
        this.ticketType = ticketType;
    }

    public VisitType getVisitType() {
        return visitType;
    }

    public void setVisitType(VisitType visitType) {
        this.visitType = visitType;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TicketPricing that = (TicketPricing) o;
        return id == that.id && price == that.price && ticketType == that.ticketType && visitType == that.visitType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ticketType, visitType, price);
    }

    @Override
    public String toString() {
        return "TicketPricing{" +
                "id=" + id +
                ", ticketType=" + ticketType +
                ", visitType=" + visitType +
                ", price=" + price +
                '}';
    }
}
