package org.university.zoomanagementsystem.medical_record;

import org.university.zoomanagementsystem.vet_examination_schedule.ExaminationSchedule;

import java.time.LocalDateTime;
import java.util.Objects;

public class MedicalRecord {
    private int id;
    private ExaminationSchedule examinationSchedule;
    private String diagnosis;
    private String treatment;
    private String notes;
    private LocalDateTime createdAt;

    public MedicalRecord() {
        this.id = -1;
        this.examinationSchedule = null;
        this.diagnosis = null;
        this.treatment = null;
        this.notes = null;
        this.createdAt = null;
    }

    public MedicalRecord(int id, ExaminationSchedule examinationSchedule, String diagnosis, String treatment, String notes, LocalDateTime createdAt) {
        this.id = id;
        this.examinationSchedule = examinationSchedule;
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.notes = notes;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ExaminationSchedule getExaminationSchedule() {
        return examinationSchedule;
    }

    public void setExaminationSchedule(ExaminationSchedule examinationSchedule) {
        this.examinationSchedule = examinationSchedule;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MedicalRecord that = (MedicalRecord) o;
        return id == that.id && Objects.equals(examinationSchedule, that.examinationSchedule) && Objects.equals(diagnosis, that.diagnosis) && Objects.equals(treatment, that.treatment) && Objects.equals(notes, that.notes) && Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, examinationSchedule, diagnosis, treatment, notes, createdAt);
    }

    @Override
    public String toString() {
        return "MedicalRecord{" +
                "id=" + id +
                ", examinationSchedule=" + examinationSchedule +
                ", diagnosis='" + diagnosis + '\'' +
                ", treatment='" + treatment + '\'' +
                ", notes='" + notes + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
