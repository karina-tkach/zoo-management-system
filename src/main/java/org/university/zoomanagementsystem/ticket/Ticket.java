package org.university.zoomanagementsystem.ticket;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Ticket {
    private int id;
    private UUID uuid;
    private String fullName;
    private String email;
    private TicketType ticketType;
    private VisitType visitType;
    private int price; //for return
    private LocalDate visitDate;
    private Integer excursionId;
    private String excursionName; //for return
    private PurchaseMethod purchaseMethod;
    private LocalDateTime purchaseTime;

    public Ticket() {
        this.id = -1;
        this.uuid = null;
        this.fullName = null;
        this.email = null;
        this.ticketType = null;
        this.visitType = null;
        this.price = -1;
        this.visitDate = null;
        this.excursionId = null;
        this.excursionName = null;
        this.purchaseMethod = null;
        this.purchaseTime = null;
    }

    public Ticket(int id, UUID uuid, String fullName, TicketType ticketType,
                  VisitType visitType, int price, LocalDate visitDate, Integer excursionId,
                  String excursionName, PurchaseMethod purchaseMethod, LocalDateTime purchaseTime) {
        this.id = id;
        this.uuid = uuid;
        this.fullName = fullName;
        this.email = null;
        this.ticketType = ticketType;
        this.visitType = visitType;
        this.price = price;
        this.visitDate = visitDate;
        this.excursionId = excursionId;
        this.excursionName = excursionName;
        this.purchaseMethod = purchaseMethod;
        this.purchaseTime = purchaseTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public LocalDate getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(LocalDate visitDate) {
        this.visitDate = visitDate;
    }

    public Integer getExcursionId() {
        return excursionId;
    }

    public void setExcursionId(Integer excursionId) {
        this.excursionId = excursionId;
    }

    public String getExcursionName() {
        return excursionName;
    }

    public void setExcursionName(String excursionName) {
        this.excursionName = excursionName;
    }

    public PurchaseMethod getPurchaseMethod() {
        return purchaseMethod;
    }

    public void setPurchaseMethod(PurchaseMethod purchaseMethod) {
        this.purchaseMethod = purchaseMethod;
    }

    public LocalDateTime getPurchaseTime() {
        return purchaseTime;
    }

    public void setPurchaseTime(LocalDateTime purchaseTime) {
        this.purchaseTime = purchaseTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return id == ticket.id && price == ticket.price && Objects.equals(uuid, ticket.uuid) && Objects.equals(fullName, ticket.fullName) && Objects.equals(email, ticket.email) && ticketType == ticket.ticketType && visitType == ticket.visitType && Objects.equals(visitDate, ticket.visitDate) && Objects.equals(excursionId, ticket.excursionId) && Objects.equals(excursionName, ticket.excursionName) && purchaseMethod == ticket.purchaseMethod && Objects.equals(purchaseTime, ticket.purchaseTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uuid, fullName, email, ticketType, visitType, price, visitDate, excursionId, excursionName, purchaseMethod, purchaseTime);
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", uuid=" + uuid +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", ticketType=" + ticketType +
                ", visitType=" + visitType +
                ", price=" + price +
                ", visitDate=" + visitDate +
                ", excursionId=" + excursionId +
                ", excursionName='" + excursionName + '\'' +
                ", purchaseMethod=" + purchaseMethod +
                ", purchaseTime=" + purchaseTime +
                '}';
    }
}
