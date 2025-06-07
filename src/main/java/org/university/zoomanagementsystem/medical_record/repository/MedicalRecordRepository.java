package org.university.zoomanagementsystem.medical_record.repository;

import org.university.zoomanagementsystem.medical_record.MedicalRecord;

import java.time.LocalDate;
import java.util.List;

public interface MedicalRecordRepository {
    int addMedicalRecord(MedicalRecord medicalRecord);

    MedicalRecord getMedicalRecordById(int id);

    List<MedicalRecord> getMedicalRecordsWithPagination(int pageNumber, int limit);

    int getMedicalRecordsRowsCount();

    List<MedicalRecord> getMedicalRecordsByDateWithPagination(LocalDate date, int pageNumber, int limit);

    int getMedicalRecordsByDateRowsCount(LocalDate date);

    List<MedicalRecord> getMedicalRecordsByVeterinarianWithPagination(int veterinarianId, int pageNumber, int limit);

    int getMedicalRecordsByVeterinarianRowsCount(int veterinarianId);
}
