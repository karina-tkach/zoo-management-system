package org.university.zoomanagementsystem.feeding_schedule.repository;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.university.zoomanagementsystem.feeding_schedule.FeedingSchedule;
import org.university.zoomanagementsystem.feeding_schedule.FeedingScheduleMapper;

import java.sql.Time;
import java.time.LocalTime;
import java.util.List;

@Repository
public class FeedingScheduleRepositoryImpl implements FeedingScheduleRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public FeedingScheduleRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void resetFeedingFlagsByTime() {
        String query = """
            UPDATE feeding_schedules fs
            SET is_done_today = FALSE
            WHERE fs.is_done_today = TRUE
              AND fs.time <= LOCALTIME
              AND NOT EXISTS (
                SELECT 1
                FROM feeding_records fr
                WHERE fr.feeding_schedule_id = fs.id
                  AND fr.date = CURRENT_DATE);
            """;

        jdbcTemplate.update(query, new MapSqlParameterSource());
    }

    @Override
    public int addFeedingSchedule(FeedingSchedule feedingSchedule) {
        String query = """
            INSERT INTO feeding_schedules (animal_id, caretaker_id, food_type, time, portion_size_grams)
            VALUES (:animal_id, :caretaker_id, :food_type, :time, :portion_size_grams)
            """;

        KeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        SqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("animal_id", feedingSchedule.getAnimal().getId())
                .addValue("caretaker_id", feedingSchedule.getCaretaker().getId())
                .addValue("food_type", feedingSchedule.getFoodType())
                .addValue("time", Time.valueOf(feedingSchedule.getTime()))
                .addValue("portion_size_grams", feedingSchedule.getPortionSizeGrams());

        jdbcTemplate.update(query, mapSqlParameterSource, generatedKeyHolder);

        var keys = generatedKeyHolder.getKeys();
        if (keys != null) {
            return (int) keys.get("id");
        }

        return -1;
    }

    @Override
    public FeedingSchedule getFeedingScheduleById(int id) {
        String query = """
                SELECT fs.*, a.name, a.habitat_type, a.enclosure_id, a.health_status, a.image,
                a.last_fed_up_at, e.name AS enclosure_name, e.location, e.area_m2,
                u.name AS user_name, u.email
                FROM feeding_schedules fs
                JOIN animals a ON fs.animal_id = a.id
                JOIN enclosures e ON a.enclosure_id = e.id
                JOIN users u ON fs.caretaker_id = u.id
                WHERE fs.id = :id
            """;

        SqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("id", id);

        return jdbcTemplate.query(query, mapSqlParameterSource, FeedingScheduleMapper::mapToPojo)
                .stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public void updateFeedingScheduleById(FeedingSchedule feedingSchedule, int id) {
        String query = """
            UPDATE feeding_schedules SET
            animal_id=:animal_id, caretaker_id=:caretaker_id, food_type=:food_type, 
            time=:time, portion_size_grams=:portion_size_grams
            WHERE id=:id
            """;
        SqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("animal_id", feedingSchedule.getAnimal().getId())
                .addValue("caretaker_id", feedingSchedule.getCaretaker().getId())
                .addValue("food_type", feedingSchedule.getFoodType())
                .addValue("time", Time.valueOf(feedingSchedule.getTime()))
                .addValue("portion_size_grams", feedingSchedule.getPortionSizeGrams())
                .addValue("id", id);

        jdbcTemplate.update(query, mapSqlParameterSource);
    }

    @Override
    public void updateFeedingScheduleStatusToTrueById(int id) {
        String query = """
            UPDATE feeding_schedules SET
            is_done_today=:is_done_today
            WHERE id=:id
            """;
        SqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("is_done_today", true)
                .addValue("id", id);

        jdbcTemplate.update(query, mapSqlParameterSource);
    }

    @Override
    public void deleteFeedingScheduleById(int id) {
        String query = """
            DELETE FROM feeding_schedules WHERE id=:id
            """;
        SqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("id", id);

        jdbcTemplate.update(query, mapSqlParameterSource);
    }

    @Override
    public List<FeedingSchedule> getFeedingSchedulesWithPagination(int pageNumber, int limit) {
        String query = """
        SELECT fs.*, a.name, a.habitat_type, a.enclosure_id, a.health_status, a.image,
        a.last_fed_up_at, e.name AS enclosure_name, e.location, e.area_m2,
        u.name AS user_name, u.email
        FROM feeding_schedules fs
        JOIN animals a ON fs.animal_id = a.id
        JOIN enclosures e ON a.enclosure_id = e.id
        JOIN users u ON fs.caretaker_id = u.id
        ORDER BY fs.time
        LIMIT :limit
        OFFSET :offset
        """;
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("limit", limit)
                .addValue("offset", getOffset(pageNumber, limit));

        return jdbcTemplate.query(query, parameters, FeedingScheduleMapper::mapToPojo);
    }

    @Override
    public int getFeedingSchedulesRowsCount() {
        String query = "SELECT COUNT(*) FROM feeding_schedules";
        Integer count = jdbcTemplate.queryForObject(query, new MapSqlParameterSource(), Integer.class);
        return (count != null) ? count : 0;
    }

    @Override
    public FeedingSchedule getFeedingScheduleByAnimalAndTime(int animalId, LocalTime time) {
        String query = """
        SELECT fs.*, a.name, a.habitat_type, a.enclosure_id, a.health_status, a.image,
        a.last_fed_up_at, e.name AS enclosure_name, e.location, e.area_m2,
        u.name AS user_name, u.email
        FROM feeding_schedules fs
        JOIN animals a ON fs.animal_id = a.id
        JOIN enclosures e ON a.enclosure_id = e.id
        JOIN users u ON fs.caretaker_id = u.id
        WHERE fs.animal_id=:animal_id AND fs.time=:time
        """;
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("animal_id", animalId)
                .addValue("time", Time.valueOf(time));

        return jdbcTemplate.query(query, parameters, FeedingScheduleMapper::mapToPojo)
                .stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public FeedingSchedule getFeedingScheduleByCaretakerAndTime(int caretakerId, LocalTime time) {
        String query = """
        SELECT fs.*, a.name, a.habitat_type, a.enclosure_id, a.health_status, a.image,
        a.last_fed_up_at, e.name AS enclosure_name, e.location, e.area_m2,
        u.name AS user_name, u.email
        FROM feeding_schedules fs
        JOIN animals a ON fs.animal_id = a.id
        JOIN enclosures e ON a.enclosure_id = e.id
        JOIN users u ON fs.caretaker_id = u.id
        WHERE fs.caretaker_id=:caretaker_id AND fs.time=:time
        """;
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("caretaker_id", caretakerId)
                .addValue("time", Time.valueOf(time));

        return jdbcTemplate.query(query, parameters, FeedingScheduleMapper::mapToPojo)
                .stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<FeedingSchedule> getFeedingSchedulesByAnimalWithPagination(int animalId, int pageNumber, int limit) {
        String query = """
        SELECT fs.*, a.name, a.habitat_type, a.enclosure_id, a.health_status, a.image,
        a.last_fed_up_at, e.name AS enclosure_name, e.location, e.area_m2,
        u.name AS user_name, u.email
        FROM feeding_schedules fs
        JOIN animals a ON fs.animal_id = a.id
        JOIN enclosures e ON a.enclosure_id = e.id
        JOIN users u ON fs.caretaker_id = u.id
        WHERE fs.animal_id=:animal_id
        ORDER BY fs.time
        LIMIT :limit
        OFFSET :offset
        """;
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("animal_id", animalId)
                .addValue("limit", limit)
                .addValue("offset", getOffset(pageNumber, limit));

        return jdbcTemplate.query(query, parameters, FeedingScheduleMapper::mapToPojo);
    }

    @Override
    public int getFeedingSchedulesByAnimalRowsCount(int animalId) {
        String query = "SELECT COUNT(*) FROM feeding_schedules WHERE animal_id= :animal_id";
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("animal_id", animalId);
        Integer count = jdbcTemplate.queryForObject(query, parameters, Integer.class);
        return (count != null) ? count : 0;
    }

    @Override
    public List<FeedingSchedule> getFeedingSchedulesByCaretakerWithPagination(int caretakerId, int pageNumber, int limit) {
        String query = """
        SELECT fs.*, a.name, a.habitat_type, a.enclosure_id, a.health_status, a.image,
        a.last_fed_up_at, e.name AS enclosure_name, e.location, e.area_m2,
        u.name AS user_name, u.email
        FROM feeding_schedules fs
        JOIN animals a ON fs.animal_id = a.id
        JOIN enclosures e ON a.enclosure_id = e.id
        JOIN users u ON fs.caretaker_id = u.id
        WHERE fs.caretaker_id=:caretaker_id
        ORDER BY fs.time
        LIMIT :limit
        OFFSET :offset
        """;
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("caretaker_id", caretakerId)
                .addValue("limit", limit)
                .addValue("offset", getOffset(pageNumber, limit));

        return jdbcTemplate.query(query, parameters, FeedingScheduleMapper::mapToPojo);
    }

    @Override
    public int getFeedingSchedulesByCaretakerRowsCount(int caretakerId) {
        String query = "SELECT COUNT(*) FROM feeding_schedules WHERE caretaker_id = :caretaker_id";
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("caretaker_id", caretakerId);
        Integer count = jdbcTemplate.queryForObject(query, parameters, Integer.class);
        return (count != null) ? count : 0;
    }

    @Override
    public List<FeedingSchedule> getFeedingSchedulesByCompletionWithPagination(boolean isFeedingCompleted, int pageNumber, int limit) {
        String query = """
        SELECT fs.*, a.name, a.habitat_type, a.enclosure_id, a.health_status, a.image,
        a.last_fed_up_at, e.name AS enclosure_name, e.location, e.area_m2,
        u.name AS user_name, u.email
        FROM feeding_schedules fs
        JOIN animals a ON fs.animal_id = a.id
        JOIN enclosures e ON a.enclosure_id = e.id
        JOIN users u ON fs.caretaker_id = u.id
        WHERE fs.is_done_today=:is_done_today
        ORDER BY fs.time
        LIMIT :limit
        OFFSET :offset
        """;
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("is_done_today", isFeedingCompleted)
                .addValue("limit", limit)
                .addValue("offset", getOffset(pageNumber, limit));

        return jdbcTemplate.query(query, parameters, FeedingScheduleMapper::mapToPojo);
    }

    @Override
    public int getFeedingSchedulesByCompletionRowsCount(boolean isFeedingCompleted) {
        String query = "SELECT COUNT(*) FROM feeding_schedules WHERE is_done_today = :is_done_today";
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("is_done_today", isFeedingCompleted);
        Integer count = jdbcTemplate.queryForObject(query, parameters, Integer.class);
        return (count != null) ? count : 0;
    }

    private static int getOffset(int pageNumber, int pageSize) {
        return (pageNumber - 1) * pageSize;
    }
}
