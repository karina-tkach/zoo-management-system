package org.university.zoomanagementsystem.ticket;

public enum VisitType {
    GENERAL("GENERAL"),
    EXCURSION("EXCURSION");
    private final String name;

    VisitType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
