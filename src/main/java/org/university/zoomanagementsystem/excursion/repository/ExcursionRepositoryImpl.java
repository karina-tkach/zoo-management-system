package org.university.zoomanagementsystem.excursion.repository;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.university.zoomanagementsystem.excursion.Excursion;
import org.university.zoomanagementsystem.excursion.ExcursionMapper;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

@Repository
public class ExcursionRepositoryImpl implements ExcursionRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public ExcursionRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public int addExcursion(Excursion excursion) {
        String query = """
            INSERT INTO excursions (topic, guide_id, description, date, start_time, duration_minutes,
            max_participants)
            VALUES (:topic, :guide_id, :description, :date, :start_time, :duration_minutes, :max_participants)
            """;

        KeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        SqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("topic", excursion.getTopic())
                .addValue("guide_id", excursion.getGuide().getId())
                .addValue("description", excursion.getDescription())
                .addValue("date", Date.valueOf(excursion.getDate()))
                .addValue("start_time", Time.valueOf(excursion.getStartTime()))
                .addValue("duration_minutes", excursion.getDurationMinutes())
                .addValue("max_participants", excursion.getMaxParticipants());

        jdbcTemplate.update(query, mapSqlParameterSource, generatedKeyHolder);

        var keys = generatedKeyHolder.getKeys();
        if (keys != null) {
            return (int) keys.get("id");
        }

        return -1;
    }

    @Override
    public Excursion getExcursionById(int id) {
        String query = """
            SELECT excursions.id, excursions.topic, excursions.guide_id, excursions.description, 
            excursions.date, excursions.start_time, excursions.duration_minutes, excursions.max_participants,
            excursions.booked_count, users.name, users.email, users.role
            FROM excursions
            INNER JOIN users ON excursions.guide_id = users.id WHERE excursions.id=:id
            """;

        SqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("id", id);

        return jdbcTemplate.query(query, mapSqlParameterSource, ExcursionMapper::mapToPojo)
                .stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public void updateExcursionById(Excursion excursion, int id) {
        String query = """
            UPDATE excursions SET
            topic=:topic, guide_id=:guide_id, description=:description, date=:date,
            start_time=:start_time, duration_minutes=:duration_minutes,
            max_participants=:max_participants, booked_count=:booked_count
            WHERE id=:id
            """;
        SqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("topic", excursion.getTopic())
                .addValue("guide_id", excursion.getGuide().getId())
                .addValue("description", excursion.getDescription())
                .addValue("date", Date.valueOf(excursion.getDate()))
                .addValue("start_time", Time.valueOf(excursion.getStartTime()))
                .addValue("duration_minutes", excursion.getDurationMinutes())
                .addValue("max_participants", excursion.getMaxParticipants())
                .addValue("booked_count", excursion.getBookedCount())
                .addValue("id", id);

        jdbcTemplate.update(query, mapSqlParameterSource);
    }

    @Override
    public void deleteExcursionById(int id) {
        String query = """
            DELETE FROM excursions WHERE id=:id
            """;
        SqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("id", id);

        jdbcTemplate.update(query, mapSqlParameterSource);
    }

    @Override
    public List<Excursion> getExcursionsWithPagination(int pageNumber, int limit) {
        String query = """
        SELECT 
            e.id, e.topic, e.guide_id, e.description, e.date, e.start_time, e.duration_minutes,
            e.max_participants, e.booked_count, u.name, u.email, u.role
        FROM excursions e
        JOIN users u ON e.guide_id = u.id
        ORDER BY e.id
        LIMIT :limit
        OFFSET :offset
        """;
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("limit", limit)
                .addValue("offset", getOffset(pageNumber, limit));

        return jdbcTemplate.query(query, parameters, ExcursionMapper::mapToPojo);
    }

    @Override
    public int getExcursionsRowsCount() {
        String query = "SELECT COUNT(*) FROM excursions";
        Integer count = jdbcTemplate.queryForObject(query, new MapSqlParameterSource(), Integer.class);
        return (count != null) ? count : 0;
    }

    @Override
    public List<Excursion> getAvailableExcursions() {
        String query = """
        SELECT 
            e.*, u.name, u.email, u.role
        FROM excursions e
        JOIN users u ON e.guide_id = u.id
        WHERE e.booked_count < e.max_participants
        ORDER BY e.date, e.start_time
    """;

        return jdbcTemplate.query(query, ExcursionMapper::mapToPojo);
    }

    private static int getOffset(int pageNumber, int pageSize) {
        return (pageNumber - 1) * pageSize;
    }
}
