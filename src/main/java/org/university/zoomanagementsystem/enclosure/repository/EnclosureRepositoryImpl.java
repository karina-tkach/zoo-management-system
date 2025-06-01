package org.university.zoomanagementsystem.enclosure.repository;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.university.zoomanagementsystem.enclosure.Enclosure;
import org.university.zoomanagementsystem.enclosure.EnclosureMapper;
import org.university.zoomanagementsystem.enclosure.HabitatType;

import java.sql.Types;
import java.util.List;

@Repository
public class EnclosureRepositoryImpl implements EnclosureRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public EnclosureRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int addEnclosure(Enclosure enclosure) {
        String query = """
            INSERT INTO enclosures (name, location, environment_type, area_m2)
            VALUES (:name, :location, :environment_type, :area_m2)
            """;

        KeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        SqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("name", enclosure.getName())
                .addValue("location", enclosure.getLocation())
                .addValue("environment_type", enclosure.getEnvironmentType(), Types.OTHER)
                .addValue("area_m2", enclosure.getAreaM2());

        jdbcTemplate.update(query, mapSqlParameterSource, generatedKeyHolder);

        var keys = generatedKeyHolder.getKeys();
        if (keys != null) {
            return (int) keys.get("id");
        }

        return -1;
    }

    @Override
    public Enclosure getEnclosureById(int id) {
        String query = """
            SELECT * FROM enclosures WHERE id=:id
            """;

        SqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("id", id);

        return jdbcTemplate.query(query, mapSqlParameterSource, EnclosureMapper::mapToPojo)
                .stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public void updateEnclosureById(Enclosure enclosure, int id) {
        String query = """
            UPDATE enclosures SET
            name=:name, location=:location, environment_type=:environment_type,
            area_m2=:area_m2
            WHERE id=:id
            """;
        SqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("name", enclosure.getName())
                .addValue("location", enclosure.getLocation())
                .addValue("environment_type", enclosure.getEnvironmentType(), Types.OTHER)
                .addValue("area_m2", enclosure.getAreaM2())
                .addValue("id", id);

        jdbcTemplate.update(query, mapSqlParameterSource);
    }

    @Override
    public void deleteEnclosureById(int id) {
        String query = """
            DELETE FROM enclosures WHERE id=:id
            """;
        SqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("id", id);

        jdbcTemplate.update(query, mapSqlParameterSource);
    }

    @Override
    public List<Enclosure> getEnclosuresWithPagination(int pageNumber, int limit) {
        String query = """
            SELECT * FROM enclosures
            ORDER BY id
            LIMIT :limit
            OFFSET :offset
            """;
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("limit", limit)
                .addValue("offset", getOffset(pageNumber, limit));

        return jdbcTemplate.query(query, parameters, EnclosureMapper::mapToPojo);
    }

    @Override
    public int getEnclosuresRowsCount() {
        String query = "SELECT COUNT(*) FROM enclosures";
        Integer count = jdbcTemplate.queryForObject(query, new MapSqlParameterSource(), Integer.class);
        return (count != null) ? count : 0;
    }

    @Override
    public List<Enclosure> getEnclosuresByEnvironmentType(HabitatType environmentType) {
        String query = """
        SELECT * FROM enclosures WHERE environment_type = :environment_type
        """;

        SqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("environment_type", environmentType, Types.OTHER);

        return jdbcTemplate.query(query, mapSqlParameterSource, EnclosureMapper::mapToPojo);
    }

    private static int getOffset(int pageNumber, int pageSize) {
        return (pageNumber - 1) * pageSize;
    }
}
