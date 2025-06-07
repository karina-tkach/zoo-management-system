package org.university.zoomanagementsystem.feeding_record.repository;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.university.zoomanagementsystem.feeding_record.FeedingRecord;
import org.university.zoomanagementsystem.feeding_record.FeedingRecordMapper;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Repository
public class FeedingRecordRepositoryImpl implements FeedingRecordRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public FeedingRecordRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<FeedingRecord> getFeedingRecordsWithPagination(int pageNumber, int limit) {
        String query = """
        SELECT fr.id as record_id, fr.date,
        fs.*, a.name, a.habitat_type, a.enclosure_id, a.health_status, a.image,
        a.last_fed_up_at, e.name AS enclosure_name, e.location, e.area_m2,
        u.name AS user_name, u.email
        FROM feeding_records fr
        JOIN feeding_schedules fs ON fr.feeding_schedule_id = fs.id
        JOIN animals a ON fs.animal_id = a.id
        JOIN enclosures e ON a.enclosure_id = e.id
        JOIN users u ON fs.caretaker_id = u.id
        ORDER BY fr.date DESC, fs.time DESC
        LIMIT :limit
        OFFSET :offset
        """;
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("limit", limit)
                .addValue("offset", getOffset(pageNumber, limit));

        return jdbcTemplate.query(query, parameters, FeedingRecordMapper::mapToPojo);
    }

    @Override
    public int getFeedingRecordsRowsCount() {
        String query = "SELECT COUNT(*) FROM feeding_records";
        Integer count = jdbcTemplate.queryForObject(query, new MapSqlParameterSource(), Integer.class);
        return (count != null) ? count : 0;
    }

    @Override
    public List<FeedingRecord> getFeedingRecordsByDateWithPagination(LocalDate date, int pageNumber, int limit) {
        String query = """
        SELECT fr.id as record_id, fr.date,
        fs.*, a.name, a.habitat_type, a.enclosure_id, a.health_status, a.image,
        a.last_fed_up_at, e.name AS enclosure_name, e.location, e.area_m2,
        u.name AS user_name, u.email
        FROM feeding_records fr
        JOIN feeding_schedules fs ON fr.feeding_schedule_id = fs.id
        JOIN animals a ON fs.animal_id = a.id
        JOIN enclosures e ON a.enclosure_id = e.id
        JOIN users u ON fs.caretaker_id = u.id
        WHERE fr.date=:date
        ORDER BY fs.time DESC
        LIMIT :limit
        OFFSET :offset
        """;
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("date", Date.valueOf(date))
                .addValue("limit", limit)
                .addValue("offset", getOffset(pageNumber, limit));

        return jdbcTemplate.query(query, parameters, FeedingRecordMapper::mapToPojo);
    }

    @Override
    public int getFeedingRecordsByDateRowsCount(LocalDate date) {
        String query = "SELECT COUNT(*) FROM feeding_records WHERE date= :date";
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("date", Date.valueOf(date));
        Integer count = jdbcTemplate.queryForObject(query, parameters, Integer.class);
        return (count != null) ? count : 0;
    }

    private static int getOffset(int pageNumber, int pageSize) {
        return (pageNumber - 1) * pageSize;
    }
}
