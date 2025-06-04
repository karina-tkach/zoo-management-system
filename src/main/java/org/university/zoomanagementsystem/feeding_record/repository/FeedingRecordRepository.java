package org.university.zoomanagementsystem.feeding_record.repository;

import org.university.zoomanagementsystem.feeding_record.FeedingRecord;

import java.time.LocalDate;
import java.util.List;

public interface FeedingRecordRepository {
    List<FeedingRecord> getFeedingRecordsWithPagination(int pageNumber, int limit);

    int getFeedingRecordsRowsCount();

    List<FeedingRecord> getFeedingRecordsByDateWithPagination(LocalDate date, int pageNumber, int limit);

    int getFeedingRecordsByDateRowsCount(LocalDate date);


}
