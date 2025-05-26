package org.university.zoomanagementsystem.gate.repository;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.university.zoomanagementsystem.gate.Gate;
import org.university.zoomanagementsystem.gate.GateMapper;
import java.util.List;

@Repository
public class GateRepositoryImpl implements GateRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public GateRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int addGate(Gate gate) {
        String query = """
            INSERT INTO gates (name, location)
            VALUES (:name, :location)
            """;

        KeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        SqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("name", gate.getName())
                .addValue("location", gate.getLocation());

        jdbcTemplate.update(query, mapSqlParameterSource, generatedKeyHolder);

        var keys = generatedKeyHolder.getKeys();
        if (keys != null) {
            return (int) keys.get("id");
        }

        return -1;
    }

    @Override
    public Gate getGateById(int id) {
        String query = """
            SELECT * FROM gates
             WHERE id=:id
            """;

        SqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("id", id);

        return jdbcTemplate.query(query, mapSqlParameterSource, GateMapper::mapToPojo)
                .stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public Gate getGateByName(String name) {
        String query = """
            SELECT * FROM gates
             WHERE name=:name
            """;

        SqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("name", name);

        return jdbcTemplate.query(query, mapSqlParameterSource, GateMapper::mapToPojo)
                .stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public void updateGateById(Gate gate, int id) {
        String query = """
            UPDATE gates SET
            name=:name, location=:location
            WHERE id=:id
            """;
        SqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("name", gate.getName())
                .addValue("location", gate.getLocation())
                .addValue("id", id);

        jdbcTemplate.update(query, mapSqlParameterSource);
    }

    @Override
    public void deleteGateById(int id) {
        String query = """
            DELETE FROM gates WHERE id=:id
            """;
        SqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("id", id);

        jdbcTemplate.update(query, mapSqlParameterSource);
    }

    @Override
    public List<Gate> getGatesWithPagination(int pageNumber, int limit) {
        String query = """
        SELECT * FROM gates
        ORDER BY id
        LIMIT :limit
        OFFSET :offset
        """;
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("limit", limit)
                .addValue("offset", getOffset(pageNumber, limit));

        return jdbcTemplate.query(query, parameters, GateMapper::mapToPojo);
    }

    @Override
    public int getGatesRowsCount() {
        String query = "SELECT COUNT(*) FROM gates";
        Integer count = jdbcTemplate.queryForObject(query, new MapSqlParameterSource(), Integer.class);
        return (count != null) ? count : 0;
    }

    private static int getOffset(int pageNumber, int pageSize) {
        return (pageNumber - 1) * pageSize;
    }
}
