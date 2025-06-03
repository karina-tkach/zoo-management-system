package org.university.zoomanagementsystem.feeding_schedule.repository;

import org.university.zoomanagementsystem.feeding_schedule.FeedingSchedule;

import java.time.LocalTime;
import java.util.List;

public interface FeedingScheduleRepository {
    void resetFeedingFlagsByTime();

    int addFeedingSchedule(FeedingSchedule feedingSchedule);

    FeedingSchedule getFeedingScheduleById(int id);

    void updateFeedingScheduleById(FeedingSchedule feedingSchedule, int id);

    void updateFeedingScheduleStatusToTrueById(int id);

    void deleteFeedingScheduleById(int id);

    List<FeedingSchedule> getFeedingSchedulesWithPagination(int pageNumber, int limit);

    int getFeedingSchedulesRowsCount();

    FeedingSchedule getFeedingScheduleByAnimalAndTime(int animalId, LocalTime time);

    FeedingSchedule getFeedingScheduleByCaretakerAndTime(int caretakerId, LocalTime time);

    List<FeedingSchedule> getFeedingSchedulesByAnimalWithPagination(int animalId, int pageNumber, int limit);

    int getFeedingSchedulesByAnimalRowsCount(int animalId);

    List<FeedingSchedule> getFeedingSchedulesByCaretakerWithPagination(int caretakerId, int pageNumber, int limit);

    int getFeedingSchedulesByCaretakerRowsCount(int caretakerId);

    List<FeedingSchedule> getFeedingSchedulesByCompletionWithPagination(boolean isFeedingCompleted, int pageNumber, int limit);

    int getFeedingSchedulesByCompletionRowsCount(boolean isFeedingCompleted);
}
