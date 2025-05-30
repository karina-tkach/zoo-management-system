package org.university.zoomanagementsystem.visitlog.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.university.zoomanagementsystem.exception.not_found.GateNotFoundException;
import org.university.zoomanagementsystem.exception.not_found.TicketNotFoundException;
import org.university.zoomanagementsystem.exception.not_found.VisitLogNotFoundException;
import org.university.zoomanagementsystem.exception.validation.ValidationException;
import org.university.zoomanagementsystem.exception.validation.VisitLogValidationException;
import org.university.zoomanagementsystem.gate.Gate;
import org.university.zoomanagementsystem.gate.service.GateService;
import org.university.zoomanagementsystem.ticket.Ticket;
import org.university.zoomanagementsystem.ticket.service.TicketService;
import org.university.zoomanagementsystem.visitlog.VisitLog;
import org.university.zoomanagementsystem.visitlog.VisitLogValidator;
import org.university.zoomanagementsystem.visitlog.repository.VisitLogRepository;

import java.util.List;

@Service
public class VisitLogService {
    private final VisitLogValidator visitLogValidator;
    private final VisitLogRepository visitLogRepository;
    private final TicketService ticketService;
    private final GateService gateService;

    private final Logger logger = LoggerFactory.getLogger(VisitLogService.class);

    public VisitLogService(VisitLogValidator visitLogValidator, VisitLogRepository visitLogRepository,
                           TicketService ticketService, GateService gateService) {
        this.visitLogValidator = visitLogValidator;
        this.visitLogRepository = visitLogRepository;
        this.ticketService = ticketService;
        this.gateService = gateService;
    }

    public VisitLog addVisitLog(VisitLog visitLog) {
        try {
            logger.info("Try to add visit log");

            visitLogValidator.validate(visitLog);
            gateService.getGateById(visitLog.getGate().getId());
            ticketService.getTicketById(visitLog.getTicket().getId());

            VisitLog existsVisitLog = visitLogRepository.getVisitLogByTicketId(visitLog.getTicket().getId());
            if (existsVisitLog != null) {
                throw new VisitLogValidationException("Visit log with such ticket already exists");
            }

            int id = visitLogRepository.addVisitLog(visitLog);
            if (id == -1) {
                throw new VisitLogValidationException("Unable to retrieve the generated key");
            }

            visitLog.setId(id);
            logger.info("Visit log was added:\n{}", visitLog);
            return getVisitLogById(id);
        } catch (VisitLogValidationException | VisitLogNotFoundException |
                 GateNotFoundException | TicketNotFoundException | DataAccessException exception) {
            logger.warn("Visit log wasn't added: {}\n{}", visitLog, exception.getMessage());
            throw exception;
        }
    }

    public VisitLog getVisitLogById(int id) {
        try {
            logger.info("Try to get visit log by id");
            VisitLog visitLog = visitLogRepository.getVisitLogById(id);
            if(visitLog == null) {
                throw new VisitLogNotFoundException(String.format("Visit log with id %d was not found", id));
            }
            logger.info("Visit log was fetched successfully");
            return visitLog;
        } catch (VisitLogNotFoundException | DataAccessException exception) {
            logger.warn("Visit log wasn't fetched\n{}", exception.getMessage());
            throw exception;
        }
    }

    public List<VisitLog> getVisitLogsWithPagination(int pageNumber, int limit) {
        try {
            if (pageNumber < 0 || limit < 0) {
                throw new ValidationException("Page number and limit must be greater than 0");
            }
            logger.info("Try to get visit logs with pagination");
            List<VisitLog> visitLogs = visitLogRepository.getVisitLogsWithPagination(pageNumber, limit);
            logger.info("Visit logs were fetched with pagination successfully");
            return visitLogs;
        } catch (ValidationException | DataAccessException exception) {
            logger.warn("Visit logs weren't fetched with pagination\n{}", exception.getMessage());
            throw exception;
        }
    }

    public int getVisitLogsRowsCount() {
        try {
            logger.info("Try to get visit logs rows count");
            int count = visitLogRepository.getVisitLogsRowsCount();
            logger.info("Visit logs rows count were fetched successfully");
            return count;
        } catch (DataAccessException exception) {
            logger.warn("Visit logs rows count weren't fetched\n{}", exception.getMessage());
            throw exception;
        }
    }
}
