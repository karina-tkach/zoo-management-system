package org.university.zoomanagementsystem.excursion;

import org.university.zoomanagementsystem.user.User;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class Excursion {
    private int id;
    private String topic;
    private User guide;
    private String description;
    private LocalDate date;
    private LocalTime startTime;
    private int durationMinutes;
    private int maxParticipants;
    private int bookedCount;

    public Excursion() {
        this.id = -1;
        this.topic = null;
        this.guide = null;
        this.description = null;
        this.date = null;
        this.startTime = null;
        this.durationMinutes = 0;
        this.maxParticipants = 0;
        this.bookedCount = 0;
    }

    public Excursion(int id, String topic, User guide, String description, LocalDate date, LocalTime startTime, int durationMinutes, int maxParticipants, int bookedCount) {
        this.id = id;
        this.topic = topic;
        this.guide = guide;
        this.description = description;
        this.date = date;
        this.startTime = startTime;
        this.durationMinutes = durationMinutes;
        this.maxParticipants = maxParticipants;
        this.bookedCount = bookedCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public User getGuide() {
        return guide;
    }

    public void setGuide(User guide) {
        this.guide = guide;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public int getBookedCount() {
        return bookedCount;
    }

    public void setBookedCount(int bookedCount) {
        this.bookedCount = bookedCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Excursion excursion = (Excursion) o;
        return id == excursion.id && durationMinutes == excursion.durationMinutes && maxParticipants == excursion.maxParticipants && bookedCount == excursion.bookedCount && Objects.equals(topic, excursion.topic) && Objects.equals(guide, excursion.guide) && Objects.equals(description, excursion.description) && Objects.equals(date, excursion.date) && Objects.equals(startTime, excursion.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, topic, guide, description, date, startTime, durationMinutes, maxParticipants, bookedCount);
    }

    @Override
    public String toString() {
        return "Excursion{" +
                "id=" + id +
                ", topic='" + topic + '\'' +
                ", guide=" + guide +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", startTime=" + startTime +
                ", durationMinutes=" + durationMinutes +
                ", maxParticipants=" + maxParticipants +
                ", bookedCount=" + bookedCount +
                '}';
    }
}
