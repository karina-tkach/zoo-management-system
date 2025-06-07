package org.university.zoomanagementsystem.vet_examination_schedule.repository;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.university.zoomanagementsystem.vet_examination_schedule.ExaminationSchedule;
import org.university.zoomanagementsystem.vet_examination_schedule.ExaminationScheduleMapper;
import org.university.zoomanagementsystem.vet_examination_schedule.ExaminationStatus;

import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class ExaminationScheduleRepositoryImpl implements ExaminationScheduleRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public ExaminationScheduleRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void markAnimalsNeedingCheckup() {
        String sql = "CALL mark_animals_needing_checkup()";
        jdbcTemplate.getJdbcTemplate().execute(sql);
    }

    @Override
    public int addExaminationSchedule(ExaminationSchedule examinationSchedule) {
        String query = """
            INSERT INTO vet_examination_schedules (animal_id, vet_id, planned_datetime, reason)
            VALUES (:animal_id, :vet_id, :planned_datetime, :reason)
            """;

        KeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        SqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("animal_id", examinationSchedule.getAnimal().getId())
                .addValue("vet_id", examinationSchedule.getVeterinarian().getId())
                .addValue("planned_datetime", Timestamp.valueOf(examinationSchedule.getPlannedDateTime()))
                .addValue("reason", examinationSchedule.getReason());

        jdbcTemplate.update(query, mapSqlParameterSource, generatedKeyHolder);

        var keys = generatedKeyHolder.getKeys();
        if (keys != null) {
            return (int) keys.get("id");
        }

        return -1;
    }

    @Override
    public ExaminationSchedule getExaminationScheduleById(int id) {
        String query = """
                SELECT es.*, a.name, a.habitat_type, a.enclosure_id, a.health_status, a.image,
                a.last_checked_up_at, e.name AS enclosure_name, e.location,
                u.name AS user_name, u.email
                FROM vet_examination_schedules es
                JOIN animals a ON es.animal_id = a.id
                JOIN enclosures e ON a.enclosure_id = e.id
                JOIN users u ON es.vet_id = u.id
                WHERE es.id = :id
            """;

        SqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("id", id);

        return jdbcTemplate.query(query, mapSqlParameterSource, ExaminationScheduleMapper::mapToPojo)
                .stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public void updateExaminationScheduleById(ExaminationSchedule examinationSchedule, int id) {
        String query = """
            UPDATE vet_examination_schedules SET
            animal_id=:animal_id, vet_id=:vet_id,
            planned_datetime=:planned_datetime, reason=:reason
            WHERE id=:id
            """;
        SqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("animal_id", examinationSchedule.getAnimal().getId())
                .addValue("vet_id", examinationSchedule.getVeterinarian().getId())
                .addValue("planned_datetime", Timestamp.valueOf(examinationSchedule.getPlannedDateTime()))
                .addValue("reason", examinationSchedule.getReason())
                .addValue("id", id);

        jdbcTemplate.update(query, mapSqlParameterSource);
    }

    @Override
    public void deleteExaminationScheduleById(int id) {
        String query = """
            DELETE FROM vet_examination_schedules WHERE id=:id
            """;
        SqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("id", id);

        jdbcTemplate.update(query, mapSqlParameterSource);
    }

    @Override
    public List<ExaminationSchedule> getExaminationSchedulesWithPagination(int pageNumber, int limit) {
        String query = """
        SELECT es.*, a.name, a.habitat_type, a.enclosure_id, a.health_status, a.image,
                a.last_checked_up_at, e.name AS enclosure_name, e.location,
                u.name AS user_name, u.email
                FROM vet_examination_schedules es
                JOIN animals a ON es.animal_id = a.id
                JOIN enclosures e ON a.enclosure_id = e.id
                JOIN users u ON es.vet_id = u.id
                ORDER BY es.status, es.planned_datetime
                LIMIT :limit
                OFFSET :offset
        """;
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("limit", limit)
                .addValue("offset", getOffset(pageNumber, limit));

        return jdbcTemplate.query(query, parameters, ExaminationScheduleMapper::mapToPojo);
    }

    @Override
    public int getExaminationSchedulesRowsCount() {
        String query = "SELECT COUNT(*) FROM vet_examination_schedules";
        Integer count = jdbcTemplate.queryForObject(query, new MapSqlParameterSource(), Integer.class);
        return (count != null) ? count : 0;
    }

    @Override
    public ExaminationSchedule getExaminationScheduleByAnimalAndTime(int animalId, LocalDateTime time) {
        String query = """
        SELECT es.*, a.name, a.habitat_type, a.enclosure_id, a.health_status, a.image,
                a.last_checked_up_at, e.name AS enclosure_name, e.location,
                u.name AS user_name, u.email
                FROM vet_examination_schedules es
                JOIN animals a ON es.animal_id = a.id
                JOIN enclosures e ON a.enclosure_id = e.id
                JOIN users u ON es.vet_id = u.id
                WHERE es.animal_id=:animal_id AND es.planned_datetime=:planned_datetime
        """;
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("animal_id", animalId)
                .addValue("planned_datetime", Timestamp.valueOf(time));

        return jdbcTemplate.query(query, parameters, ExaminationScheduleMapper::mapToPojo)
                .stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public ExaminationSchedule getExaminationScheduleByVeterinarianAndTime(int veterinarianId, LocalDateTime time) {
        String query = """
        SELECT es.*, a.name, a.habitat_type, a.enclosure_id, a.health_status, a.image,
                a.last_checked_up_at, e.name AS enclosure_name, e.location,
                u.name AS user_name, u.email
                FROM vet_examination_schedules es
                JOIN animals a ON es.animal_id = a.id
                JOIN enclosures e ON a.enclosure_id = e.id
                JOIN users u ON es.vet_id = u.id
                WHERE es.vet_id=:vet_id AND es.planned_datetime=:planned_datetime
        """;
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("vet_id", veterinarianId)
                .addValue("planned_datetime", Timestamp.valueOf(time));

        return jdbcTemplate.query(query, parameters, ExaminationScheduleMapper::mapToPojo)
                .stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<ExaminationSchedule> getExaminationSchedulesByAnimalWithPagination(int animalId, int pageNumber, int limit) {
        String query = """
        SELECT es.*, a.name, a.habitat_type, a.enclosure_id, a.health_status, a.image,
                a.last_checked_up_at, e.name AS enclosure_name, e.location,
                u.name AS user_name, u.email
                FROM vet_examination_schedules es
                JOIN animals a ON es.animal_id = a.id
                JOIN enclosures e ON a.enclosure_id = e.id
                JOIN users u ON es.vet_id = u.id
                WHERE es.animal_id=:animal_id
                ORDER BY es.status, es.planned_datetime
                LIMIT :limit
                OFFSET :offset
        """;
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("animal_id", animalId)
                .addValue("limit", limit)
                .addValue("offset", getOffset(pageNumber, limit));

        return jdbcTemplate.query(query, parameters, ExaminationScheduleMapper::mapToPojo);
    }

    @Override
    public int getExaminationSchedulesByAnimalRowsCount(int animalId) {
        String query = "SELECT COUNT(*) FROM vet_examination_schedules WHERE animal_id= :animal_id";
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("animal_id", animalId);
        Integer count = jdbcTemplate.queryForObject(query, parameters, Integer.class);
        return (count != null) ? count : 0;
    }

    @Override
    public List<ExaminationSchedule> getExaminationSchedulesByVeterinarianWithPagination(int veterinarianId, int pageNumber, int limit) {
        String query = """
        SELECT es.*, a.name, a.habitat_type, a.enclosure_id, a.health_status, a.image,
                a.last_checked_up_at, e.name AS enclosure_name, e.location,
                u.name AS user_name, u.email
                FROM vet_examination_schedules es
                JOIN animals a ON es.animal_id = a.id
                JOIN enclosures e ON a.enclosure_id = e.id
                JOIN users u ON es.vet_id = u.id
                WHERE es.vet_id=:vet_id
                ORDER BY es.status, es.planned_datetime
                LIMIT :limit
                OFFSET :offset
        """;
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("vet_id", veterinarianId)
                .addValue("limit", limit)
                .addValue("offset", getOffset(pageNumber, limit));

        return jdbcTemplate.query(query, parameters, ExaminationScheduleMapper::mapToPojo);
    }

    @Override
    public int getExaminationSchedulesByVeterinarianRowsCount(int veterinarianId) {
        String query = "SELECT COUNT(*) FROM vet_examination_schedules WHERE vet_id = :vet_id";
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("vet_id", veterinarianId);
        Integer count = jdbcTemplate.queryForObject(query, parameters, Integer.class);
        return (count != null) ? count : 0;
    }

    @Override
    public List<ExaminationSchedule> getExaminationSchedulesByStatusWithPagination(ExaminationStatus status, int pageNumber, int limit) {
        String query = """
        SELECT es.*, a.name, a.habitat_type, a.enclosure_id, a.health_status, a.image,
                a.last_checked_up_at, e.name AS enclosure_name, e.location,
                u.name AS user_name, u.email
                FROM vet_examination_schedules es
                JOIN animals a ON es.animal_id = a.id
                JOIN enclosures e ON a.enclosure_id = e.id
                JOIN users u ON es.vet_id = u.id
                WHERE es.status=:status
                ORDER BY es.planned_datetime
                LIMIT :limit
                OFFSET :offset
        """;
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("status", status, Types.OTHER)
                .addValue("limit", limit)
                .addValue("offset", getOffset(pageNumber, limit));

        return jdbcTemplate.query(query, parameters, ExaminationScheduleMapper::mapToPojo);
    }

    @Override
    public int getExaminationSchedulesByStatusRowsCount(ExaminationStatus status) {
        String query = "SELECT COUNT(*) FROM vet_examination_schedules WHERE status = :status";
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("status", status, Types.OTHER);
        Integer count = jdbcTemplate.queryForObject(query, parameters, Integer.class);
        return (count != null) ? count : 0;
    }

    private static int getOffset(int pageNumber, int pageSize) {
        return (pageNumber - 1) * pageSize;
    }
}
