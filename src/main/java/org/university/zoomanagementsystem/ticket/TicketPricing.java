package org.university.zoomanagementsystem.ticket;

import java.util.Objects;

public class TicketPricing {
    private int id;
    private String ticketType;
    private String visitType;
    private int price;

    public TicketPricing() {
        this.id = -1;
        this.ticketType = null;
        this.visitType = null;
        this.price = -1;
    }

    public TicketPricing(int id, String ticketType, String visitType, int price) {
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

    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }

    public String getVisitType() {
        return visitType;
    }

    public void setVisitType(String visitType) {
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
        return id == that.id && price == that.price && Objects.equals(ticketType, that.ticketType) && Objects.equals(visitType, that.visitType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ticketType, visitType, price);
    }

    @Override
    public String toString() {
        return "TicketPricing{" +
                "id=" + id +
                ", ticketType='" + ticketType + '\'' +
                ", visitType='" + visitType + '\'' +
                ", price=" + price +
                '}';
    }
}
