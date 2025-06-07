package org.university.zoomanagementsystem.medical_record.repository;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.university.zoomanagementsystem.medical_record.MedicalRecord;
import org.university.zoomanagementsystem.medical_record.MedicalRecordMapper;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

@Repository
public class MedicalRecordRepositoryImpl implements MedicalRecordRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public MedicalRecordRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int addMedicalRecord(MedicalRecord medicalRecord) {
        String query = """
            INSERT INTO medical_records (examination_id, diagnosis, treatment, notes)
            VALUES (:examination_id, :diagnosis, :treatment, :notes)
            """;

        KeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        SqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("examination_id", medicalRecord.getExaminationSchedule().getId())
                .addValue("diagnosis", medicalRecord.getDiagnosis())
                .addValue("treatment", medicalRecord.getTreatment())
                .addValue("notes", medicalRecord.getNotes());

        jdbcTemplate.update(query, mapSqlParameterSource, generatedKeyHolder);

        var keys = generatedKeyHolder.getKeys();
        if (keys != null) {
            return (int) keys.get("id");
        }

        return -1;
    }

    @Override
    public MedicalRecord getMedicalRecordById(int id) {
        String query = """
        SELECT mr.id as record_id, mr.diagnosis, mr.treatment, mr.notes, mr.created_at,
        es.*, a.name, a.habitat_type, a.enclosure_id, a.health_status, a.image,
        a.last_checked_up_at, e.name AS enclosure_name, e.location,
        u.name AS user_name, u.email
        FROM medical_records mr
        JOIN vet_examination_schedules es ON mr.examination_id = es.id
        JOIN animals a ON es.animal_id = a.id
        JOIN enclosures e ON a.enclosure_id = e.id
        JOIN users u ON es.vet_id = u.id
        WHERE mr.id = :id
        """;
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", id);

        return jdbcTemplate.query(query, parameters, MedicalRecordMapper::mapToPojo)
                .stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<MedicalRecord> getMedicalRecordsWithPagination(int pageNumber, int limit) {
        String query = """
        SELECT mr.id as record_id, mr.diagnosis, mr.treatment, mr.notes, mr.created_at,
        es.*, a.name, a.habitat_type, a.enclosure_id, a.health_status, a.image,
        a.last_checked_up_at, e.name AS enclosure_name, e.location,
        u.name AS user_name, u.email
        FROM medical_records mr
        JOIN vet_examination_schedules es ON mr.examination_id = es.id
        JOIN animals a ON es.animal_id = a.id
        JOIN enclosures e ON a.enclosure_id = e.id
        JOIN users u ON es.vet_id = u.id
        ORDER BY mr.created_at DESC
        LIMIT :limit
        OFFSET :offset
        """;
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("limit", limit)
                .addValue("offset", getOffset(pageNumber, limit));

        return jdbcTemplate.query(query, parameters, MedicalRecordMapper::mapToPojo);
    }

    @Override
    public int getMedicalRecordsRowsCount() {
        String query = "SELECT COUNT(*) FROM medical_records";
        Integer count = jdbcTemplate.queryForObject(query, new MapSqlParameterSource(), Integer.class);
        return (count != null) ? count : 0;
    }

    @Override
    public List<MedicalRecord> getMedicalRecordsByDateWithPagination(LocalDate date, int pageNumber, int limit) {
        String query = """
        SELECT mr.id as record_id, mr.diagnosis, mr.treatment, mr.notes, mr.created_at,
        es.*, a.name, a.habitat_type, a.enclosure_id, a.health_status, a.image,
        a.last_checked_up_at, e.name AS enclosure_name, e.location,
        u.name AS user_name, u.email
        FROM medical_records mr
        JOIN vet_examination_schedules es ON mr.examination_id = es.id
        JOIN animals a ON es.animal_id = a.id
        JOIN enclosures e ON a.enclosure_id = e.id
        JOIN users u ON es.vet_id = u.id
        WHERE mr.created_at >= :start
        AND mr.created_at < :end
        ORDER BY mr.created_at DESC
        LIMIT :limit
        OFFSET :offset
        """;
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("start", Timestamp.valueOf(date.atStartOfDay()))
                .addValue("end", Timestamp.valueOf(date.plusDays(1).atStartOfDay()))
                .addValue("limit", limit)
                .addValue("offset", getOffset(pageNumber, limit));

        return jdbcTemplate.query(query, parameters, MedicalRecordMapper::mapToPojo);
    }

    @Override
    public int getMedicalRecordsByDateRowsCount(LocalDate date) {
        String query = "SELECT COUNT(*) FROM medical_records WHERE created_at >= :start AND created_at < :end";
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("start", Timestamp.valueOf(date.atStartOfDay()))
                .addValue("end", Timestamp.valueOf(date.plusDays(1).atStartOfDay()));
        Integer count = jdbcTemplate.queryForObject(query, parameters, Integer.class);
        return (count != null) ? count : 0;
    }

    @Override
    public List<MedicalRecord> getMedicalRecordsByVeterinarianWithPagination(int veterinarianId, int pageNumber, int limit) {
        String query = """
        SELECT mr.id as record_id, mr.diagnosis, mr.treatment, mr.notes, mr.created_at,
        es.*, a.name, a.habitat_type, a.enclosure_id, a.health_status, a.image,
        a.last_checked_up_at, e.name AS enclosure_name, e.location,
        u.name AS user_name, u.email
        FROM medical_records mr
        JOIN vet_examination_schedules es ON mr.examination_id = es.id
        JOIN animals a ON es.animal_id = a.id
        JOIN enclosures e ON a.enclosure_id = e.id
        JOIN users u ON es.vet_id = u.id
        WHERE es.vet_id = :vet_id
        ORDER BY mr.created_at DESC
        LIMIT :limit
        OFFSET :offset
        """;
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("vet_id", veterinarianId)
                .addValue("limit", limit)
                .addValue("offset", getOffset(pageNumber, limit));

        return jdbcTemplate.query(query, parameters, MedicalRecordMapper::mapToPojo);
    }

    @Override
    public int getMedicalRecordsByVeterinarianRowsCount(int veterinarianId) {
        String query = "SELECT COUNT(*) FROM medical_records mr JOIN vet_examination_schedules es ON mr.examination_id = es.id WHERE es.vet_id=:vet_id";
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("vet_id", veterinarianId);
        Integer count = jdbcTemplate.queryForObject(query, parameters, Integer.class);
        return (count != null) ? count : 0;
    }

    private static int getOffset(int pageNumber, int pageSize) {
        return (pageNumber - 1) * pageSize;
    }
    
}
