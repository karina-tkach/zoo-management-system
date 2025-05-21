package org.university.zoomanagementsystem.ticket.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;
import org.university.zoomanagementsystem.events.Event;
import org.university.zoomanagementsystem.events.service.EventService;
import org.university.zoomanagementsystem.exception.not_found.EventNotFoundException;
import org.university.zoomanagementsystem.exception.not_found.TicketPricingNotFoundException;
import org.university.zoomanagementsystem.exception.validation.EventValidationException;
import org.university.zoomanagementsystem.exception.validation.TicketPricingValidationException;
import org.university.zoomanagementsystem.exception.validation.ValidationException;
import org.university.zoomanagementsystem.ticket.TicketPricing;
import org.university.zoomanagementsystem.ticket.TicketPricingMapper;
import org.university.zoomanagementsystem.ticket.repository.TicketPricingRepository;

import java.util.List;

@Service
public class TicketPricingService {
    private final TicketPricingRepository ticketPricingRepository;

    private final Logger logger = LoggerFactory.getLogger(TicketPricingService.class);

    public TicketPricingService(TicketPricingRepository ticketPricingRepository) {
        this.ticketPricingRepository = ticketPricingRepository;
    }

    public List<TicketPricing> getTicketPricings() {
        try {
            logger.info("Try to get ticket pricings");
            List<TicketPricing> ticketPricings = ticketPricingRepository.getTicketPricings();
            logger.info("Ticket pricings were fetched successfully");
            return ticketPricings;
        } catch (DataAccessException exception) {
            logger.warn("Ticket pricings weren't fetched\n{}", exception.getMessage());
            throw exception;
        }
    }

    public TicketPricing getTicketPricingById(int id) {
        try {
            logger.info("Try to get ticket pricing by id");
            TicketPricing ticketPricing = ticketPricingRepository.getTicketPricingById(id);
            if(ticketPricing == null) {
                throw new TicketPricingNotFoundException(String.format("Ticket pricing with id %d was not found", id));
            }
            logger.info("Ticket pricing was fetched successfully");
            return ticketPricing;
        } catch (TicketPricingNotFoundException | DataAccessException exception) {
            logger.warn("Ticket pricing wasn't fetched\n{}", exception.getMessage());
            throw exception;
        }
    }

    public TicketPricing updateTicketPricingById(int price, int id) {
        try {
            getTicketPricingById(id);
            logger.info("Try to update ticket pricing");
            if (price < 0) {
                throw new TicketPricingValidationException("Ticket price cannot be negative");
            }
            ticketPricingRepository.updateTicketPricingById(price, id);

            logger.info("Ticket pricing was updated");
            return getTicketPricingById(id);
        } catch (TicketPricingNotFoundException | TicketPricingValidationException | DataAccessException exception) {
            logger.warn("Ticket pricing wasn't updated: {}\n{}", id, exception.getMessage());
            throw exception;
        }
    }

    public TicketPricing getTicketPricingByTicketAndVisitType(String ticketType, String visitType) {
        try {
            logger.info("Try to get ticket pricing by ticket and visit type");
            if (ticketType == null || visitType == null || ticketType.isBlank() || visitType.isBlank()) {
                throw new TicketPricingValidationException("Ticket type and visit type cannot be null or empty");
            }
            TicketPricing ticketPricing = ticketPricingRepository.getTicketPricingByTicketAndVisitType(ticketType, visitType);

            if (ticketPricing == null) {
                throw new TicketPricingNotFoundException(String.format("Ticket pricing with ticket type %s and visit type %s was not found", ticketType, visitType));
            }
            logger.info("Ticket pricing was fetched successfully");
            return ticketPricing;
        } catch (TicketPricingValidationException | TicketPricingNotFoundException | DataAccessException exception) {
            logger.warn("Ticket pricing wasn't fetched\n{}", exception.getMessage());
            throw exception;
        }
    }
}
