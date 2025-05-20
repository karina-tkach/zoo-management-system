package org.university.zoomanagementsystem.events;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class Event {
    private int id;
    private String title;
    private String description;
    private LocalDate date;
    private LocalTime startTime;
    private int durationMinutes;
    private String location;
    private String image;

    public Event() {
        this.id = -1;
        this.title = null;
        this.description = null;
        this.date = null;
        this.startTime = null;
        this.durationMinutes = 0;
        this.location = null;
        this.image = null;
    }

    public Event(int id, String title, String description, LocalDate date, LocalTime startTime, int durationMinutes, String location, String image) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.startTime = startTime;
        this.durationMinutes = durationMinutes;
        this.location = location;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return id == event.id && durationMinutes == event.durationMinutes && Objects.equals(title, event.title) && Objects.equals(description, event.description) && Objects.equals(date, event.date) && Objects.equals(startTime, event.startTime) && Objects.equals(location, event.location) && Objects.equals(image, event.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, date, startTime, durationMinutes, location, image);
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", startTime=" + startTime +
                ", durationMinutes=" + durationMinutes +
                ", location='" + location + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
