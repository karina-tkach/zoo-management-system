package org.university.zoomanagementsystem.ticket.service;

import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.university.zoomanagementsystem.exception.not_found.ExcursionNotFoundException;
import org.university.zoomanagementsystem.exception.not_found.TicketNotFoundException;
import org.university.zoomanagementsystem.exception.not_found.TicketPricingNotFoundException;
import org.university.zoomanagementsystem.exception.validation.TicketPricingValidationException;
import org.university.zoomanagementsystem.exception.validation.TicketValidationException;
import org.university.zoomanagementsystem.excursion.Excursion;
import org.university.zoomanagementsystem.excursion.service.ExcursionService;
import org.university.zoomanagementsystem.mail.MailSenderService;
import org.university.zoomanagementsystem.ticket.Ticket;
import org.university.zoomanagementsystem.ticket.TicketValidator;
import org.university.zoomanagementsystem.ticket.repository.TicketRepository;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.UUID;

@Service
public class TicketService {
    private final TicketValidator ticketValidator;
    private final TicketRepository ticketRepository;
    private final TicketPricingService ticketPricingService;
    private final MailSenderService mailSenderService;
    private final ExcursionService excursionService;


    private final Logger logger = LoggerFactory.getLogger(TicketService.class);

    public TicketService(TicketValidator ticketValidator, TicketRepository ticketRepository,
                         TicketPricingService ticketPricingService, MailSenderService mailSenderService,
                         ExcursionService excursionService) {
        this.ticketValidator = ticketValidator;
        this.ticketRepository = ticketRepository;
        this.ticketPricingService = ticketPricingService;
        this.mailSenderService = mailSenderService;
        this.excursionService = excursionService;
    }

    public Ticket addTicketOffline(Ticket ticket) {
        try {
            logger.info("Try to add ticket offline");
            ticket.setUuid(UUID.randomUUID());
            ticket.setPurchaseMethod("OFFLINE");
            ticketValidator.validate(ticket);

            int pricingId = ticketPricingService.getTicketPricingByTicketAndVisitType(ticket.getTicketType(), ticket.getVisitType()).getId();
            if (ticket.getExcursionId() != null) {
                Excursion excursion = excursionService.getExcursionById(ticket.getExcursionId());
                if (excursion.getBookedCount() == excursion.getMaxParticipants()) {
                    throw new TicketValidationException("Excursion already booked");
                }
                excursion.setBookedCount(excursion.getBookedCount() + 1);
                excursionService.updateExcursionById(excursion, excursion.getId());
                ticket.setVisitDate(excursion.getDate());
            }
            int id = ticketRepository.addTicket(ticket, pricingId);
            if (id == -1) {
                throw new TicketValidationException("Unable to retrieve the generated key");
            }

            ticket.setId(id);
            logger.info("Ticket was added:\n{}", ticket);
            return getTicketById(id);
        } catch (TicketPricingValidationException | TicketPricingNotFoundException | ExcursionNotFoundException |
                 TicketValidationException | TicketNotFoundException | DataAccessException exception) {
            logger.warn("Ticket wasn't added: {}\n{}", ticket, exception.getMessage());
            throw exception;
        }
    }

    /*public Ticket addTicketOnline(Ticket ticket) throws MessagingException, UnsupportedEncodingException {
        try {
            logger.info("Try to add ticket online");
            ticket.setUuid(UUID.randomUUID());
            ticket.setPurchaseMethod("ONLINE");
            ticketValidator.validate(ticket);

            int pricingId = ticketPricingService.getTicketPricingByTicketAndVisitType(ticket.getTicketType(), ticket.getVisitType()).getId();

            int id = ticketRepository.addTicket(ticket, pricingId);
            if (id == -1) {
                throw new TicketValidationException("Unable to retrieve the generated key");
            }
            Ticket addedTicket = getTicketById(id);
            addedTicket.setEmail(ticket.getEmail());

            String emailContent = createEmailContent(addedTicket);
            mailSenderService.sendEmail(addedTicket.getEmail(), "Zoo Ticket", emailContent);

            logger.info("Ticket was added:\n{}", ticket);
            return addedTicket;
        } catch (TicketValidationException | TicketNotFoundException | DataAccessException | MessagingException |
                 UnsupportedEncodingException exception) {
            logger.warn("Ticket wasn't added: {}\n{}", ticket, exception.getMessage());
            throw exception;
        }
    }*/

    public Ticket getTicketById(int id) {
        try {
            logger.info("Try to get ticket by id");
            Ticket ticket = ticketRepository.getTicketById(id);
            if (ticket == null) {
                throw new TicketNotFoundException(String.format("Ticket with id %d was not found", id));
            }
            logger.info("Ticket was fetched successfully");
            return ticket;
        } catch (TicketNotFoundException | DataAccessException exception) {
            logger.warn("Ticket wasn't fetched\n{}", exception.getMessage());
            throw exception;
        }
    }

    public List<Ticket> getTickets() {
        try {
            logger.info("Try to get tickets");
            List<Ticket> tickets = ticketRepository.getTickets();
            logger.info("Tickets were fetched successfully");
            return tickets;
        } catch (DataAccessException exception) {
            logger.warn("Tickets weren't fetched\n{}", exception.getMessage());
            throw exception;
        }
    }

    private String createEmailContent(Ticket ticket) {
        String excursionString = "";
        if (ticket.getExcursionName() != null) {
            excursionString = "<tr>" +
                    "<td style=\"padding: 10px; border-bottom: 1px solid black; border-right: 1px solid black; border-left: 1px solid black;\">Excursion topic</td>" +
                    "<td style=\"padding: 10px; border-bottom: 1px solid black; border-right: 1px solid black;\">" + ticket.getExcursionName() + "</td>" +
                    "</tr>";
        }
        return "<h1 style=\"text-align: center; color: black; margin-top: 20px;\">Ticket Details</h1>" +
                "<table style=\"border-collapse: collapse; border: 2px solid black; margin: 0 auto; width: 400px;\">" +
                "<tr>" +
                "<th style=\"background-color: grey; padding: 10px; border-bottom: 1px solid black; border-right: 1px solid black; border-left: 1px solid black; width: 120px; text-align: left;\">Field</th>" +
                "<th style=\"padding: 10px; border-bottom: 1px solid black; border-right: 1px solid black; border-left: 1px solid black; width: 280px; text-align: left;\">Value</th>" +
                "</tr>" +
                "<tr>" +
                "<td style=\"padding: 10px; border-bottom: 1px solid black; border-right: 1px solid black; border-left: 1px solid black;\">Ticket UUID</td>" +
                "<td style=\"padding: 10px; border-bottom: 1px solid black; border-right: 1px solid black;\">" + ticket.getUuid().toString() + "</td>" +
                "</tr>" +
                "<tr>" +
                "<td style=\"padding: 10px; border-bottom: 1px solid black; border-right: 1px solid black; border-left: 1px solid black;\">Full Name</td>" +
                "<td style=\"padding: 10px; border-bottom: 1px solid black; border-right: 1px solid black;\">" + ticket.getFullName() + "</td>" +
                "</tr>" +
                "<tr>" +
                "<td style=\"padding: 10px; border-bottom: 1px solid black; border-right: 1px solid black; border-left: 1px solid black;\">Email</td>" +
                "<td style=\"padding: 10px; border-bottom: 1px solid black; border-right: 1px solid black;\">" + ticket.getEmail() + "</td>" +
                "</tr>" +
                "<tr>" +
                "<td style=\"padding: 10px; border-bottom: 1px solid black; border-right: 1px solid black; border-left: 1px solid black;\">Price</td>" +
                "<td style=\"padding: 10px; border-bottom: 1px solid black; border-right: 1px solid black;\">" + ticket.getPrice() + "</td>" +
                "</tr>" +
                "<tr>" +
                "<td style=\"padding: 10px; border-bottom: 1px solid black; border-right: 1px solid black; border-left: 1px solid black;\">Ticket type</td>" +
                "<td style=\"padding: 10px; border-bottom: 1px solid black; border-right: 1px solid black;\">" + ticket.getTicketType() + "</td>" +
                "</tr>" +
                "<tr>" +
                "<td style=\"padding: 10px; border-bottom: 1px solid black; border-right: 1px solid black; border-left: 1px solid black;\">Visit type</td>" +
                "<td style=\"padding: 10px; border-bottom: 1px solid black; border-right: 1px solid black;\">" + ticket.getVisitType() + "</td>" +
                "</tr>" +
                excursionString +
                "<tr>" +
                "<td style=\"padding: 10px; border-bottom: 1px solid black; border-right: 1px solid black; border-left: 1px solid black;\">Date</td>" +
                "<td style=\"padding: 10px; border-bottom: 1px solid black; border-right: 1px solid black;\">" + ticket.getVisitDate().toString() + "</td>" +
                "</tr>" +
                "<tr>" +
                "<td style=\"padding: 10px; border-bottom: 1px solid black; border-right: 1px solid black; border-left: 1px solid black;\">Purchase time</td>" +
                "<td style=\"padding: 10px; border-bottom: 1px solid black; border-right: 1px solid black;\">" + ticket.getPurchaseTime().toString() + "</td>" +
                "</tr>" +
                "</table>";
    }
}
