package org.university.zoomanagementsystem.ticket;

public enum TicketType {
    ADULT("ADULT"),
    CHILD("CHILD"),
    PREFERENTIAL("PREFERENTIAL");
    private final String name;

    TicketType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

