package org.university.zoomanagementsystem.ticket;

public enum PurchaseMethod {
    ONLINE("ONLINE"),
    OFFLINE("OFFLINE");
    private final String name;

    PurchaseMethod(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

