package org.university.zoomanagementsystem.visitlog;

import org.springframework.stereotype.Component;
import org.university.zoomanagementsystem.exception.validation.VisitLogValidationException;
import org.university.zoomanagementsystem.gate.Gate;
import org.university.zoomanagementsystem.ticket.Ticket;

@Component
public class VisitLogValidator {
    public void validate(VisitLog visitLog) {
        validateVisitLogNotNull(visitLog);
        validateGate(visitLog.getGate());
        validateTicket(visitLog.getTicket());
        validateNotes(visitLog.getNotes());
    }
    private void validateVisitLogNotNull(VisitLog visitLog) {
        if (visitLog == null) {
            throw new VisitLogValidationException("Visit log was null");
        }
    }

    private void validateGate(Gate gate) {
        if (gate == null) {
            throw new VisitLogValidationException("Visit log gate was null");
        }
        else if (gate.getId() < 1) {
            throw new VisitLogValidationException("Visit log gate has invalid id");
        }
    }

    private void validateTicket(Ticket ticket) {
        if (ticket == null) {
            throw new VisitLogValidationException("Visit log ticket was null");
        }
        else if (ticket.getId() < 1) {
            throw new VisitLogValidationException("Visit log ticket has invalid id");
        }
    }

    private void validateNotes(String notes) {
        if (notes != null && notes.length() > 200) {
            throw new VisitLogValidationException("Notes must have length less than 200 characters");
        }
    }
}
