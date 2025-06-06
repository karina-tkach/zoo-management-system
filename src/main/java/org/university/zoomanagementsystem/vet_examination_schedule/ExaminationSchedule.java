package org.university.zoomanagementsystem.vet_examination_schedule;

import org.university.zoomanagementsystem.animal.Animal;
import org.university.zoomanagementsystem.user.User;

import java.time.LocalDateTime;
import java.util.Objects;

public class ExaminationSchedule {
    private int id;
    private Animal animal;
    private User veterinarian;
    private LocalDateTime plannedDateTime;
    private String reason;
    private ExaminationStatus status;

    public ExaminationSchedule() {
        this.id = -1;
        this.animal = null;
        this.veterinarian = null;
        this.plannedDateTime = null;
        this.reason = null;
        this.status = null;
    }

    public ExaminationSchedule(int id, Animal animal, User veterinarian, LocalDateTime plannedDateTime, String reason, ExaminationStatus status) {
        this.id = id;
        this.animal = animal;
        this.veterinarian = veterinarian;
        this.plannedDateTime = plannedDateTime;
        this.reason = reason;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public User getVeterinarian() {
        return veterinarian;
    }

    public void setVeterinarian(User veterinarian) {
        this.veterinarian = veterinarian;
    }

    public LocalDateTime getPlannedDateTime() {
        return plannedDateTime;
    }

    public void setPlannedDateTime(LocalDateTime plannedDateTime) {
        this.plannedDateTime = plannedDateTime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public ExaminationStatus getStatus() {
        return status;
    }

    public void setStatus(ExaminationStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExaminationSchedule that = (ExaminationSchedule) o;
        return id == that.id && Objects.equals(animal, that.animal) && Objects.equals(veterinarian, that.veterinarian) && Objects.equals(plannedDateTime, that.plannedDateTime) && Objects.equals(reason, that.reason) && status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, animal, veterinarian, plannedDateTime, reason, status);
    }

    @Override
    public String toString() {
        return "ExaminationSchedule{" +
                "id=" + id +
                ", animal=" + animal +
                ", veterinarian=" + veterinarian +
                ", plannedDateTime=" + plannedDateTime +
                ", reason='" + reason + '\'' +
                ", status=" + status +
                '}';
    }
}
