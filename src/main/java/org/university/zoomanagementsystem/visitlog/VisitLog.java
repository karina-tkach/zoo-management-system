package org.university.zoomanagementsystem.visitlog;

import org.university.zoomanagementsystem.gate.Gate;
import org.university.zoomanagementsystem.ticket.Ticket;

import java.time.LocalDateTime;
import java.util.Objects;

public class VisitLog {
    private int id;
    private Gate gate;
    private Ticket ticket;
    private LocalDateTime entryTime;
    private String notes;

    public VisitLog() {
        this.id = -1;
        this.gate = null;
        this.ticket = null;
        this.entryTime = null;
        this.notes = null;
    }

    public VisitLog(int id, Gate gate, Ticket ticket, LocalDateTime entryTime, String notes) {
        this.id = id;
        this.gate = gate;
        this.ticket = ticket;
        this.entryTime = entryTime;
        this.notes = notes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Gate getGate() {
        return gate;
    }

    public void setGate(Gate gate) {
        this.gate = gate;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(LocalDateTime entryTime) {
        this.entryTime = entryTime;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VisitLog visitLog = (VisitLog) o;
        return id == visitLog.id && Objects.equals(gate, visitLog.gate) && Objects.equals(ticket, visitLog.ticket) && Objects.equals(entryTime, visitLog.entryTime) && Objects.equals(notes, visitLog.notes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, gate, ticket, entryTime, notes);
    }

    @Override
    public String toString() {
        return "VisitLog{" +
                "id=" + id +
                ", gate=" + gate +
                ", ticket=" + ticket +
                ", entryTime=" + entryTime +
                ", notes='" + notes + '\'' +
                '}';
    }
}
