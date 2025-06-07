package org.university.zoomanagementsystem.medical_record.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.university.zoomanagementsystem.exception.not_found.ExaminationScheduleNotFoundException;
import org.university.zoomanagementsystem.exception.not_found.MedicalRecordNotFoundException;
import org.university.zoomanagementsystem.exception.validation.MedicalRecordValidationException;
import org.university.zoomanagementsystem.exception.validation.ValidationException;
import org.university.zoomanagementsystem.medical_record.MedicalRecord;
import org.university.zoomanagementsystem.medical_record.MedicalRecordValidator;
import org.university.zoomanagementsystem.medical_record.repository.MedicalRecordRepository;
import org.university.zoomanagementsystem.vet_examination_schedule.ExaminationSchedule;
import org.university.zoomanagementsystem.vet_examination_schedule.ExaminationStatus;
import org.university.zoomanagementsystem.vet_examination_schedule.service.ExaminationScheduleService;

import java.time.LocalDate;
import java.util.List;

@Service
public class MedicalRecordService {
    private final MedicalRecordRepository medicalRecordRepository;
    private final MedicalRecordValidator medicalRecordValidator;
    private final ExaminationScheduleService examinationScheduleService;

    private final Logger logger = LoggerFactory.getLogger(MedicalRecordService.class);
    
    public MedicalRecordService(MedicalRecordRepository medicalRecordRepository,
                                MedicalRecordValidator medicalRecordValidator,
                                ExaminationScheduleService examinationScheduleService) {
        this.medicalRecordRepository = medicalRecordRepository;
        this.medicalRecordValidator = medicalRecordValidator;
        this.examinationScheduleService = examinationScheduleService;
    }

    public MedicalRecord addMedicalRecord(MedicalRecord medicalRecord) {
        try {
            logger.info("Try to add medical record");
            medicalRecordValidator.validate(medicalRecord);

            ExaminationSchedule examinationSchedule = examinationScheduleService.getExaminationScheduleById(medicalRecord.getExaminationSchedule().getId());

            if (examinationSchedule.getStatus().equals(ExaminationStatus.COMPLETED)) {
                throw new MedicalRecordValidationException("Cannot add record to completed examination");
            }

            int id = medicalRecordRepository.addMedicalRecord(medicalRecord);
            if (id == -1) {
                throw new MedicalRecordValidationException("Unable to retrieve the generated key");
            }

            medicalRecord.setId(id);
            logger.info("Medical record was added:\n{}", medicalRecord);
            return getMedicalRecordById(id);
        } catch (MedicalRecordValidationException | MedicalRecordNotFoundException | ExaminationScheduleNotFoundException
                 | DataAccessException exception) {
            logger.warn("Medical record wasn't added: {}\n{}", medicalRecord, exception.getMessage());
            throw exception;
        }
    }

    public MedicalRecord getMedicalRecordById(int id) {
        try {
            logger.info("Try to get medical record by id");
            MedicalRecord medicalRecord = medicalRecordRepository.getMedicalRecordById(id);
            if (medicalRecord == null) {
                throw new MedicalRecordNotFoundException(String.format("Medical record with id %d was not found", id));
            }
            logger.info("Medical record was fetched by id successfully");
            return medicalRecord;
        } catch (ValidationException | DataAccessException exception) {
            logger.warn("Medical record wasn't fetched by id\n{}", exception.getMessage());
            throw exception;
        }
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

    public List<MedicalRecord> getMedicalRecordsByVetWithPagination(int veterinarianId, int pageNumber, int limit) {
        try {
            if (pageNumber <= 0 || limit <= 0) {
                throw new ValidationException("Page number and limit must be greater than 0");
            }
            logger.info("Try to get medical records by vet with pagination");
            List<MedicalRecord> medicalRecords = medicalRecordRepository.getMedicalRecordsByVeterinarianWithPagination(veterinarianId, pageNumber, limit);
            logger.info("Medical records by vet were fetched with pagination successfully");
            return medicalRecords;
        } catch (ValidationException | DataAccessException exception) {
            logger.warn("Medical records by vet weren't fetched with pagination\n{}", exception.getMessage());
            throw exception;
        }
    }

    public int getMedicalRecordsByVetRowsCount(int veterinarianId) {
        try {
            logger.info("Try to get medical records by vet rows count");
            int count = medicalRecordRepository.getMedicalRecordsByVeterinarianRowsCount(veterinarianId);
            logger.info("Medical records by vet rows count were fetched successfully");
            return count;
        } catch (DataAccessException exception) {
            logger.warn("Medical records by vet rows count weren't fetched\n{}", exception.getMessage());
            throw exception;
        }
    }
}
