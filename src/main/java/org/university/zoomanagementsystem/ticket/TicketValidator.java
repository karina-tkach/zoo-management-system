package org.university.zoomanagementsystem.ticket;

import org.springframework.stereotype.Component;
import org.university.zoomanagementsystem.exception.validation.TicketValidationException;
import org.university.zoomanagementsystem.exception.validation.UserValidationException;

import java.time.LocalDate;
import java.util.UUID;
import java.util.regex.Pattern;

@Component
public class TicketValidator {
    public void validate(Ticket ticket) {
        validateTicketNotNull(ticket);
        validateUuid(ticket.getUuid());
        validateName(ticket.getFullName());
        validateTicketType(ticket.getTicketType());
        validateVisitType(ticket.getVisitType());
        validateDate(ticket.getVisitDate());
        validateExcursionId(ticket.getVisitType(), ticket.getExcursionId());
        validatePurchaseMethod(ticket.getPurchaseMethod());
        validateEmail(ticket.getPurchaseMethod(), ticket.getEmail());
    }

    public void validateForOnlineBuy(Ticket ticket) {
        validateTicketNotNull(ticket);
        validateName(ticket.getFullName());
        validateTicketType(ticket.getTicketType());
        validateVisitType(ticket.getVisitType());
        validateDate(ticket.getVisitDate());
        validateExcursionId(ticket.getVisitType(), ticket.getExcursionId());
        validateEmail(PurchaseMethod.ONLINE, ticket.getEmail());
    }

    private void validateTicketNotNull(Ticket ticket) {
        if (ticket == null) {
            throw new TicketValidationException("Ticket was null");
        }
    }

    private void validateUuid(UUID uuid) {
        if (uuid == null) {
            throw new TicketValidationException("Ticket UUID was null");
        }
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new TicketValidationException("Full name was null or empty");
        }
        if (name.length() > 30 || name.length() < 2) {
            throw new TicketValidationException("Full name was too long (must be 2 to 30 characters)");
        }
        Pattern pattern = Pattern.compile("^[A-Z][a-z]+(?: [A-Z][a-z]*)?$");
        if (!pattern.matcher(name).find()) {
            throw new TicketValidationException("Full name had invalid characters");
        }
    }

    private void validateTicketType(TicketType ticketType) {
        if (ticketType == null) {
            throw new TicketValidationException("Ticket type was null");
        }
    }

    private void validateVisitType(VisitType visitType) {
        if (visitType == null) {
            throw new TicketValidationException("Visit type was null");
        }
    }

    private void validateDate(LocalDate date) {
        if (date == null) {
            throw new TicketValidationException("Ticket date was null");
        }
        if (date.isBefore(LocalDate.now())) {
            throw new TicketValidationException("Ticket date is in the past");
        }
    }

    private void validateExcursionId(VisitType visitType, Integer excursionId) {
        if (visitType == VisitType.EXCURSION && excursionId == null) {
            throw new TicketValidationException("Excursion id was null");
        }
        else if (visitType != VisitType.EXCURSION && excursionId != null) {
            throw new TicketValidationException("Excursion id must be null");
        }
    }

    private void validatePurchaseMethod(PurchaseMethod purchaseMethod) {
        if (purchaseMethod == null) {
            throw new TicketValidationException("Purchase method was null");
        }
    }

    private void validateEmail(PurchaseMethod purchaseMethod, String email) {
        Pattern characters = Pattern.compile("^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
        if (purchaseMethod == PurchaseMethod.ONLINE && (email == null || email.isBlank() || !characters.matcher(email).find())) {
            throw new UserValidationException("User email was null/empty or had invalid characters");
        }
    }
}
