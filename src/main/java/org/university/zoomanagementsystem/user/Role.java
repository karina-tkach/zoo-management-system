package org.university.zoomanagementsystem.user;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ADMIN("ADMIN"),
    CARETAKER("CARETAKER"),
    VETERINARIAN("VETERINARIAN"),
    GUIDE("GUIDE"),
    TICKET_AGENT("TICKET_AGENT"),
    EVENT_MANAGER("EVENT_MANAGER"),
    VISITOR("VISITOR");
    private final String name;

    public String getName() {
        return name;
    }

    Role(String name) {
        this.name = name;
    }

    @Override
    public String getAuthority() {
        return name;
    }
}
