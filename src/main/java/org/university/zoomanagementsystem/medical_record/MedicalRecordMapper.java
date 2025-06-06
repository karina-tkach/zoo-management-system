package org.university.zoomanagementsystem.medical_record;

import org.springframework.stereotype.Component;
import org.university.zoomanagementsystem.vet_examination_schedule.ExaminationScheduleMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MedicalRecordMapper {
    private MedicalRecordMapper() {}

    @SuppressWarnings("java:S1172")
    public static MedicalRecord mapToPojo(ResultSet rs, int ignoredRowNum) throws SQLException {
        return new MedicalRecord(rs.getInt("record_id"),
                ExaminationScheduleMapper.mapToPojo(rs, ignoredRowNum),
                rs.getString("diagnosis"),
                rs.getString("treatment"),
                rs.getString("notes"),
                rs.getTimestamp("created_at").toLocalDateTime());
    }
}
