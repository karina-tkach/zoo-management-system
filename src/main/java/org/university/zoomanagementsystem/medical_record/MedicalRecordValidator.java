package org.university.zoomanagementsystem.medical_record;

import org.springframework.stereotype.Component;
import org.university.zoomanagementsystem.exception.validation.MedicalRecordValidationException;
import org.university.zoomanagementsystem.vet_examination_schedule.ExaminationSchedule;

@Component
public class MedicalRecordValidator {
    public void validate(MedicalRecord medicalRecord) {
        validateExaminationSchedule(medicalRecord.getExaminationSchedule());
        validateDiagnosisAndTreatment(medicalRecord.getDiagnosis(), medicalRecord.getTreatment());
        validateNotes(medicalRecord.getNotes());
    }

    private void validateExaminationSchedule(ExaminationSchedule examinationSchedule) {
        if (examinationSchedule == null) {
            throw new MedicalRecordValidationException("Examination schedule was null");
        }
        else if (examinationSchedule.getId() < 1) {
            throw new MedicalRecordValidationException("Examination schedule id must be greater than 0");
        }
    }

    private void validateDiagnosisAndTreatment(String diagnosis, String treatment) {
        if ((diagnosis == null || diagnosis.isBlank()) && (treatment != null && !treatment.isBlank())) {
            throw new MedicalRecordValidationException("Treatment must be null or blank as diagnosis is not set");
        }
        else if ((diagnosis != null && !diagnosis.isBlank()) && (treatment == null || treatment.isBlank())) {
            throw new MedicalRecordValidationException("Treatment must be provided as diagnosis is set");
        }
        else if ((diagnosis != null && !diagnosis.isBlank() && (diagnosis.length() < 2 || diagnosis.length() > 500)) || (treatment!=null && !treatment.isBlank() && (treatment.length() < 2 || treatment.length() > 500))) {
            throw new MedicalRecordValidationException("Diagnosis and treatment must be between 2 and 500 characters");
        }
    }

    private void validateNotes(String notes) {
        if (notes != null && !notes.isBlank() && (notes.length() < 2 || notes.length() > 500)) {
            throw new MedicalRecordValidationException("Notes must be between 2 and 500 characters or empty");
        }
    }
}
