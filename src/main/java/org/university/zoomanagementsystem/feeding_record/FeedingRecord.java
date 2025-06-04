package org.university.zoomanagementsystem.feeding_record;

import org.university.zoomanagementsystem.feeding_schedule.FeedingSchedule;

import java.time.LocalDate;
import java.util.Objects;

public class FeedingRecord {
    private int id;
    private FeedingSchedule feedingSchedule;
    private LocalDate feedingDate;

    public FeedingRecord(int id, FeedingSchedule feedingSchedule, LocalDate feedingDate) {
        this.id = id;
        this.feedingSchedule = feedingSchedule;
        this.feedingDate = feedingDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public FeedingSchedule getFeedingSchedule() {
        return feedingSchedule;
    }

    public void setFeedingSchedule(FeedingSchedule feedingSchedule) {
        this.feedingSchedule = feedingSchedule;
    }

    public LocalDate getFeedingDate() {
        return feedingDate;
    }

    public void setFeedingDate(LocalDate feedingDate) {
        this.feedingDate = feedingDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FeedingRecord that = (FeedingRecord) o;
        return id == that.id && Objects.equals(feedingSchedule, that.feedingSchedule) && Objects.equals(feedingDate, that.feedingDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, feedingSchedule, feedingDate);
    }

    @Override
    public String toString() {
        return "FeedingRecord{" +
                "id=" + id +
                ", feedingSchedule=" + feedingSchedule +
                ", feedingDate=" + feedingDate +
                '}';
    }
}
