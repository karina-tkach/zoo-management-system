package org.university.zoomanagementsystem.animal.repository;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.university.zoomanagementsystem.animal.Animal;
import org.university.zoomanagementsystem.animal.AnimalMapper;
import org.university.zoomanagementsystem.animal.enums.AnimalGroup;
import org.university.zoomanagementsystem.animal.enums.HealthStatus;
import org.university.zoomanagementsystem.enclosure.HabitatType;

import java.sql.Date;
import java.sql.Types;
import java.util.List;

@Repository
public class AnimalRepositoryImpl implements AnimalRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public AnimalRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int addAnimal(Animal animal) {
        String query = """
            INSERT INTO animals (name, species, animal_group, habitat_type, gender, date_of_birth, enclosure_id, image)
            VALUES (:name, :species, :animal_group, :habitat_type, :gender, :date_of_birth, :enclosure_id, :image)
            """;

        KeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        SqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("name", animal.getName())
                .addValue("species", animal.getSpecies())
                .addValue("animal_group", animal.getAnimalGroup(), Types.OTHER)
                .addValue("habitat_type", animal.getHabitatType(), Types.OTHER)
                .addValue("gender", animal.getGender(), Types.OTHER)
                .addValue("date_of_birth", Date.valueOf(animal.getBirthDate()))
                .addValue("enclosure_id", animal.getEnclosure().getId())
                .addValue("image", animal.getImage());

        jdbcTemplate.update(query, mapSqlParameterSource, generatedKeyHolder);

        var keys = generatedKeyHolder.getKeys();
        if (keys != null) {
            return (int) keys.get("id");
        }

        return -1;
    }

    @Override
    public Animal getAnimalById(int id) {
        String query = """
                SELECT a.*, e.name AS enclosure_name, e.location, e.area_m2
                FROM animals a
                JOIN enclosures e ON a.enclosure_id = e.id
                WHERE a.id = :id
            """;

        SqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("id", id);

        return jdbcTemplate.query(query, mapSqlParameterSource, AnimalMapper::mapToPojo)
                .stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public void updateAnimalById(Animal animal, int id) {
        String query = """
            UPDATE animals SET
            name=:name, species=:species, animal_group=:animal_group, habitat_type=:habitat_type,
            gender=:gender, date_of_birth=:date_of_birth, enclosure_id=:enclosure_id, image=:image
            WHERE id=:id
            """;
        SqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("name", animal.getName())
                .addValue("species", animal.getSpecies())
                .addValue("animal_group", animal.getAnimalGroup(), Types.OTHER)
                .addValue("habitat_type", animal.getHabitatType(), Types.OTHER)
                .addValue("gender", animal.getGender(), Types.OTHER)
                .addValue("date_of_birth", Date.valueOf(animal.getBirthDate()))
                .addValue("enclosure_id", animal.getEnclosure().getId())
                .addValue("image", animal.getImage())
                .addValue("id", id);

        jdbcTemplate.update(query, mapSqlParameterSource);
    }

    @Override
    public void deleteAnimalById(int id) {
        String query = """
            DELETE FROM animals WHERE id=:id
            """;
        SqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("id", id);

        jdbcTemplate.update(query, mapSqlParameterSource);
    }

    @Override
    public List<Animal> getAnimalsWithPagination(int pageNumber, int limit) {
        String query = """
        SELECT a.*, e.name AS enclosure_name, e.location, e.area_m2
        FROM animals a
        JOIN enclosures e ON a.enclosure_id = e.id
        ORDER BY a.id
        LIMIT :limit
        OFFSET :offset
        """;
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("limit", limit)
                .addValue("offset", getOffset(pageNumber, limit));

        return jdbcTemplate.query(query, parameters, AnimalMapper::mapToPojo);
    }

    @Override
    public int getAnimalsRowsCount() {
        String query = "SELECT COUNT(*) FROM animals";
        Integer count = jdbcTemplate.queryForObject(query, new MapSqlParameterSource(), Integer.class);
        return (count != null) ? count : 0;
    }

    @Override
    public List<Animal> getAnimalsByEnclosureWithPagination(int enclosureId, int pageNumber, int limit) {
        String query = """
        SELECT a.*, e.name AS enclosure_name, e.location, e.area_m2
        FROM animals a
        JOIN enclosures e ON a.enclosure_id = e.id
        WHERE a.enclosure_id=:enclosure_id
        ORDER BY a.id
        LIMIT :limit
        OFFSET :offset
        """;
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("enclosure_id", enclosureId)
                .addValue("limit", limit)
                .addValue("offset", getOffset(pageNumber, limit));

        return jdbcTemplate.query(query, parameters, AnimalMapper::mapToPojo);
    }

    @Override
    public int getAnimalsByEnclosureRowsCount(int enclosureId) {
        String query = "SELECT COUNT(*) FROM animals WHERE enclosure_id = :enclosure_id";
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("enclosure_id", enclosureId);
        Integer count = jdbcTemplate.queryForObject(query, parameters, Integer.class);
        return (count != null) ? count : 0;
    }

    @Override
    public List<Animal> getAnimalsByGroupWithPagination(AnimalGroup animalGroup, int pageNumber, int limit) {
        String query = """
        SELECT a.*, e.name AS enclosure_name, e.location, e.area_m2
        FROM animals a
        JOIN enclosures e ON a.enclosure_id = e.id
        WHERE a.animal_group=:animal_group
        ORDER BY a.id
        LIMIT :limit
        OFFSET :offset
        """;
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("animal_group", animalGroup, Types.OTHER)
                .addValue("limit", limit)
                .addValue("offset", getOffset(pageNumber, limit));

        return jdbcTemplate.query(query, parameters, AnimalMapper::mapToPojo);
    }

    @Override
    public int getAnimalsByGroupRowsCount(AnimalGroup animalGroup) {
        String query = "SELECT COUNT(*) FROM animals WHERE animal_group = :animal_group";
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("animal_group", animalGroup, Types.OTHER);
        Integer count = jdbcTemplate.queryForObject(query, parameters, Integer.class);
        return (count != null) ? count : 0;
    }

    @Override
    public List<Animal> getAnimalsByHabitatWithPagination(HabitatType habitatType, int pageNumber, int limit) {
        String query = """
        SELECT a.*, e.name AS enclosure_name, e.location, e.area_m2
        FROM animals a
        JOIN enclosures e ON a.enclosure_id = e.id
        WHERE a.habitat_type=:habitat_type
        ORDER BY a.id
        LIMIT :limit
        OFFSET :offset
        """;
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("habitat_type", habitatType, Types.OTHER)
                .addValue("limit", limit)
                .addValue("offset", getOffset(pageNumber, limit));

        return jdbcTemplate.query(query, parameters, AnimalMapper::mapToPojo);
    }

    @Override
    public List<Animal> getAnimalsByHabitat(HabitatType habitatType) {
        String query = """
        SELECT a.*, e.name AS enclosure_name, e.location, e.area_m2
        FROM animals a
        JOIN enclosures e ON a.enclosure_id = e.id
        WHERE a.habitat_type=:habitat_type
        ORDER BY a.id
        """;
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("habitat_type", habitatType, Types.OTHER);

        return jdbcTemplate.query(query, parameters, AnimalMapper::mapToPojo);
    }

    @Override
    public int getAnimalsByHabitatRowsCount(HabitatType habitatType) {
        String query = "SELECT COUNT(*) FROM animals WHERE habitat_type = :habitat_type";
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("habitat_type", habitatType, Types.OTHER);
        Integer count = jdbcTemplate.queryForObject(query, parameters, Integer.class);
        return (count != null) ? count : 0;
    }

    @Override
    public List<Animal> getAnimalsByHealthStatusWithPagination(HealthStatus healthStatus, int pageNumber, int limit) {
        String query = """
        SELECT a.*, e.name AS enclosure_name, e.location, e.area_m2
        FROM animals a
        JOIN enclosures e ON a.enclosure_id = e.id
        WHERE a.health_status=:health_status
        ORDER BY a.id
        LIMIT :limit
        OFFSET :offset
        """;
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("health_status", healthStatus, Types.OTHER)
                .addValue("limit", limit)
                .addValue("offset", getOffset(pageNumber, limit));

        return jdbcTemplate.query(query, parameters, AnimalMapper::mapToPojo);
    }

    @Override
    public int getAnimalsByByHealthStatusRowsCount(HealthStatus healthStatus) {
        String query = "SELECT COUNT(*) FROM animals WHERE health_status = :health_status";
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("health_status", healthStatus, Types.OTHER);
        Integer count = jdbcTemplate.queryForObject(query, parameters, Integer.class);
        return (count != null) ? count : 0;
    }

    private static int getOffset(int pageNumber, int pageSize) {
        return (pageNumber - 1) * pageSize;
    }
}
