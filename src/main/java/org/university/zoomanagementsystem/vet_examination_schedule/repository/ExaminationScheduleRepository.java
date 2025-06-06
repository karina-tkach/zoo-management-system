package org.university.zoomanagementsystem.vet_examination_schedule.repository;

import org.university.zoomanagementsystem.vet_examination_schedule.ExaminationSchedule;
import org.university.zoomanagementsystem.vet_examination_schedule.ExaminationStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface ExaminationScheduleRepository {

    void markAnimalsNeedingCheckup();

    int addExaminationSchedule(ExaminationSchedule examinationSchedule);

    ExaminationSchedule getExaminationScheduleById(int id);

    void updateExaminationScheduleById(ExaminationSchedule examinationSchedule, int id);

    void deleteExaminationScheduleById(int id);

    List<ExaminationSchedule> getExaminationSchedulesWithPagination(int pageNumber, int limit);

    int getExaminationSchedulesRowsCount();

    ExaminationSchedule getExaminationScheduleByAnimalAndTime(int animalId, LocalDateTime time);

    ExaminationSchedule getExaminationScheduleByVeterinarianAndTime(int veterinarianId, LocalDateTime time);

    List<ExaminationSchedule> getExaminationSchedulesByAnimalWithPagination(int animalId, int pageNumber, int limit);

    int getExaminationSchedulesByAnimalRowsCount(int animalId);

    List<ExaminationSchedule> getExaminationSchedulesByVeterinarianWithPagination(int veterinarianId, int pageNumber, int limit);

    int getExaminationSchedulesByVeterinarianRowsCount(int veterinarianId);

    List<ExaminationSchedule> getExaminationSchedulesByStatusWithPagination(ExaminationStatus status, int pageNumber, int limit);

    int getExaminationSchedulesByStatusRowsCount(ExaminationStatus status);
}
