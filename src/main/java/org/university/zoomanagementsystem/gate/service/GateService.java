package org.university.zoomanagementsystem.gate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.university.zoomanagementsystem.exception.not_found.GateNotFoundException;
import org.university.zoomanagementsystem.exception.validation.GateValidationException;
import org.university.zoomanagementsystem.exception.validation.ValidationException;
import org.university.zoomanagementsystem.gate.Gate;
import org.university.zoomanagementsystem.gate.GateValidator;
import org.university.zoomanagementsystem.gate.repository.GateRepository;

import java.util.List;

@Service
public class GateService {
    private final GateValidator gateValidator;
    private final GateRepository gateRepository;

    private final Logger logger = LoggerFactory.getLogger(GateService.class);

    public GateService(GateValidator gateValidator, GateRepository gateRepository) {
        this.gateValidator = gateValidator;
        this.gateRepository = gateRepository;
    }

    public Gate addGate(Gate gate) {
        try {
            logger.info("Try to add gate");

            gateValidator.validate(gate);

            Gate existsGate = gateRepository.getGateByName(gate.getName());
            if (existsGate != null) {
                throw new GateValidationException("Gate with such name already exists");
            }

            int id = gateRepository.addGate(gate);
            if (id == -1) {
                throw new GateValidationException("Unable to retrieve the generated key");
            }

            gate.setId(id);
            logger.info("Gate was added:\n{}", gate);
            return getGateById(id);
        } catch (GateValidationException | GateNotFoundException | DataAccessException exception) {
            logger.warn("Gate wasn't added: {}\n{}", gate, exception.getMessage());
            throw exception;
        }
    }

    public Gate getGateById(int id) {
        try {
            logger.info("Try to get gate by id");
            Gate gate = gateRepository.getGateById(id);
            if(gate == null) {
                throw new GateNotFoundException(String.format("Gate with id %d was not found", id));
            }
            logger.info("Gate was fetched successfully");
            return gate;
        } catch (GateNotFoundException | DataAccessException exception) {
            logger.warn("Gate wasn't fetched\n{}", exception.getMessage());
            throw exception;
        }
    }

    public Gate updateGateById(Gate gate, int id) {
        try {
            Gate gateToUpdate = getGateById(id);
            logger.info("Try to update gate");

            gateValidator.validateGateForUpdate(gateToUpdate, gate);
            Gate existsGate = gateRepository.getGateByName(gate.getName());
            if (!gate.getName().equals(gateToUpdate.getName()) && existsGate != null) {
                throw new GateValidationException("Gate with such name already exists");
            }
            gateRepository.updateGateById(gate, id);

            logger.info("Gate was updated:\n{}", gate);
            return getGateById(id);
        } catch (GateNotFoundException | GateValidationException | DataAccessException exception) {
            logger.warn("Gate wasn't updated: {}\n{}", id, exception.getMessage());
            throw exception;
        }
    }

    public boolean deleteGateById(int id) {
        try {
            Gate gate = getGateById(id);
            logger.info("Try to delete gate by id");
            gateRepository.deleteGateById(id);
            logger.info("Gate was deleted:\n{}", gate);
            return true;
        } catch (GateNotFoundException | DataAccessException exception) {
            logger.warn("Gate wasn't deleted: {}\n{}", id, exception.getMessage());
            throw exception;
        }
    }

    public List<Gate> getGatesWithPagination(int pageNumber, int limit) {
        try {
            if (pageNumber <= 0 || limit <= 0) {
                throw new ValidationException("Page number and limit must be greater than 0");
            }
            logger.info("Try to get gates with pagination");
            List<Gate> gates = gateRepository.getGatesWithPagination(pageNumber, limit);
            logger.info("Gates were fetched with pagination successfully");
            return gates;
        } catch (ValidationException | DataAccessException exception) {
            logger.warn("Gates weren't fetched with pagination\n{}", exception.getMessage());
            throw exception;
        }
    }

    public int getGatesRowsCount() {
        try {
            logger.info("Try to get gates rows count");
            int count = gateRepository.getGatesRowsCount();
            logger.info("Gates rows count were fetched successfully");
            return count;
        } catch (DataAccessException exception) {
            logger.warn("Gates rows count weren't fetched\n{}", exception.getMessage());
            throw exception;
        }
    }
}
