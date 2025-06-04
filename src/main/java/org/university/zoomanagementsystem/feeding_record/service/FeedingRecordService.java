package org.university.zoomanagementsystem.feeding_record.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.university.zoomanagementsystem.exception.validation.ValidationException;
import org.university.zoomanagementsystem.feeding_record.FeedingRecord;
import org.university.zoomanagementsystem.feeding_record.repository.FeedingRecordRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class FeedingRecordService {
    private final FeedingRecordRepository feedingRecordRepository;

    private final Logger logger = LoggerFactory.getLogger(FeedingRecordService.class);

    public FeedingRecordService(FeedingRecordRepository feedingRecordRepository) {
        this.feedingRecordRepository = feedingRecordRepository;
    }

    public List<FeedingRecord> getFeedingRecordsWithPagination(int pageNumber, int limit) {
        try {
            if (pageNumber <= 0 || limit <= 0) {
                throw new ValidationException("Page number and limit must be greater than 0");
            }
            logger.info("Try to get feeding records with pagination");
            List<FeedingRecord> feedingRecords = feedingRecordRepository.getFeedingRecordsWithPagination(pageNumber, limit);
            logger.info("Feeding records were fetched with pagination successfully");
            return feedingRecords;
        } catch (ValidationException | DataAccessException exception) {
            logger.warn("Feeding records weren't fetched with pagination\n{}", exception.getMessage());
            throw exception;
        }
    }

    public int getFeedingRecordsRowsCount() {
        try {
            logger.info("Try to get feeding records rows count");
            int count = feedingRecordRepository.getFeedingRecordsRowsCount();
            logger.info("Feeding records rows count were fetched successfully");
            return count;
        } catch (DataAccessException exception) {
            logger.warn("Feeding records rows count weren't fetched\n{}", exception.getMessage());
            throw exception;
        }
    }

    public List<FeedingRecord> getFeedingRecordsByDateWithPagination(LocalDate date, int pageNumber, int limit) {
        try {
            if (pageNumber <= 0 || limit <= 0) {
                throw new ValidationException("Page number and limit must be greater than 0");
            }
            logger.info("Try to get feeding records by date with pagination");
            List<FeedingRecord> feedingRecords = feedingRecordRepository.getFeedingRecordsByDateWithPagination(date, pageNumber, limit);
            logger.info("Feeding records by date were fetched with pagination successfully");
            return feedingRecords;
        } catch (ValidationException | DataAccessException exception) {
            logger.warn("Feeding records by date weren't fetched with pagination\n{}", exception.getMessage());
            throw exception;
        }
    }

    public int getFeedingRecordsByDateRowsCount(LocalDate date) {
        try {
            logger.info("Try to get feeding records by date rows count");
            int count = feedingRecordRepository.getFeedingRecordsByDateRowsCount(date);
            logger.info("Feeding records by date rows count were fetched successfully");
            return count;
        } catch (DataAccessException exception) {
            logger.warn("Feeding records by date rows count weren't fetched\n{}", exception.getMessage());
            throw exception;
        }
    }
}
