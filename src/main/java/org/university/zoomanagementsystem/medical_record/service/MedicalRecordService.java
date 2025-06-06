package org.university.zoomanagementsystem.medical_record.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.university.zoomanagementsystem.exception.validation.ValidationException;
import org.university.zoomanagementsystem.medical_record.MedicalRecord;
import org.university.zoomanagementsystem.medical_record.repository.MedicalRecordRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class MedicalRecordService {
    private final MedicalRecordRepository medicalRecordRepository;

    private final Logger logger = LoggerFactory.getLogger(MedicalRecordService.class);
    
    public MedicalRecordService(MedicalRecordRepository medicalRecordRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
    }

    public List<MedicalRecord> getMedicalRecordsWithPagination(int pageNumber, int limit) {
        try {
            if (pageNumber <= 0 || limit <= 0) {
                throw new ValidationException("Page number and limit must be greater than 0");
            }
            logger.info("Try to get medical records with pagination");
            List<MedicalRecord> medicalRecords = medicalRecordRepository.getMedicalRecordsWithPagination(pageNumber, limit);
            logger.info("Medical records were fetched with pagination successfully");
            return medicalRecords;
        } catch (ValidationException | DataAccessException exception) {
            logger.warn("Medical records weren't fetched with pagination\n{}", exception.getMessage());
            throw exception;
        }
    }

    public int getMedicalRecordsRowsCount() {
        try {
            logger.info("Try to get medical records rows count");
            int count = medicalRecordRepository.getMedicalRecordsRowsCount();
            logger.info("Medical records rows count were fetched successfully");
            return count;
        } catch (DataAccessException exception) {
            logger.warn("Medical records rows count weren't fetched\n{}", exception.getMessage());
            throw exception;
        }
    }

    public List<MedicalRecord> getMedicalRecordsByDateWithPagination(LocalDate date, int pageNumber, int limit) {
        try {
            if (pageNumber <= 0 || limit <= 0) {
                throw new ValidationException("Page number and limit must be greater than 0");
            }
            logger.info("Try to get medical records by date with pagination");
            List<MedicalRecord> medicalRecords = medicalRecordRepository.getMedicalRecordsByDateWithPagination(date, pageNumber, limit);
            logger.info("Medical records by date were fetched with pagination successfully");
            return medicalRecords;
        } catch (ValidationException | DataAccessException exception) {
            logger.warn("Medical records by date weren't fetched with pagination\n{}", exception.getMessage());
            throw exception;
        }
    }

    public int getMedicalRecordsByDateRowsCount(LocalDate date) {
        try {
            logger.info("Try to get medical records by date rows count");
            int count = medicalRecordRepository.getMedicalRecordsByDateRowsCount(date);
            logger.info("Medical records by date rows count were fetched successfully");
            return count;
        } catch (DataAccessException exception) {
            logger.warn("Medical records by date rows count weren't fetched\n{}", exception.getMessage());
            throw exception;
        }
    }
}
