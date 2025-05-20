package org.university.zoomanagementsystem.events.repository;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.university.zoomanagementsystem.events.Event;
import org.university.zoomanagementsystem.events.EventMapper;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

@Repository
public class EventRepositoryImpl implements EventRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public EventRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public int addEvent(Event event) {
        String query = """
            INSERT INTO events (title, description, date, start_time, duration_minutes,
            location, image)
            VALUES (:title, :description, :date, :start_time, :duration_minutes, :location, :image)
            """;

        KeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        SqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("title", event.getTitle())
                .addValue("description", event.getDescription())
                .addValue("date", Date.valueOf(event.getDate()))
                .addValue("start_time", Time.valueOf(event.getStartTime()))
                .addValue("duration_minutes", event.getDurationMinutes())
                .addValue("location", event.getLocation())
                .addValue("image", event.getImage());

        jdbcTemplate.update(query, mapSqlParameterSource, generatedKeyHolder);

        var keys = generatedKeyHolder.getKeys();
        if (keys != null) {
            return (int) keys.get("id");
        }

        return -1;
    }

    @Override
    public Event getEventById(int id) {
        String query = """
            SELECT * FROM events WHERE id=:id
            """;

        SqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("id", id);

        return jdbcTemplate.query(query, mapSqlParameterSource, EventMapper::mapToPojo)
                .stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public void updateEventById(Event event, int id) {
        String query = """
            UPDATE events SET
            title=:title, description=:description, date=:date,
            start_time=:start_time, duration_minutes=:duration_minutes,
            location=:location, image=:image
            WHERE id=:id
            """;
        SqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("title", event.getTitle())
                .addValue("description", event.getDescription())
                .addValue("date", Date.valueOf(event.getDate()))
                .addValue("start_time", Time.valueOf(event.getStartTime()))
                .addValue("duration_minutes", event.getDurationMinutes())
                .addValue("location", event.getLocation())
                .addValue("image", event.getImage())
                .addValue("id", id);

        jdbcTemplate.update(query, mapSqlParameterSource);
    }

    @Override
    public void deleteEventById(int id) {
        String query = """
            DELETE FROM events WHERE id=:id
            """;
        SqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("id", id);

        jdbcTemplate.update(query, mapSqlParameterSource);
    }

    @Override
    public List<Event> getEventsWithPagination(int pageNumber, int limit) {
        String query = """
            SELECT * FROM events
            ORDER BY id
            LIMIT :limit
            OFFSET :offset
            """;
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("limit", limit)
                .addValue("offset", getOffset(pageNumber, limit));

        return jdbcTemplate.query(query, parameters, EventMapper::mapToPojo);
    }

    @Override
    public int getEventsRowsCount() {
        String query = "SELECT COUNT(*) FROM events";
        Integer count = jdbcTemplate.queryForObject(query, new MapSqlParameterSource(), Integer.class);
        return (count != null) ? count : 0;
    }

    private static int getOffset(int pageNumber, int pageSize) {
        return (pageNumber - 1) * pageSize;
    }
}
