package org.university.zoomanagementsystem.user.repository;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.university.zoomanagementsystem.user.User;
import org.university.zoomanagementsystem.user.UserMapper;


@Repository
@SuppressWarnings({"java:S1192", "java:S2259"})
public class UserRepositoryImpl implements UserRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public UserRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int addUser(User user) {
        String query = """
            INSERT INTO users (name, password, email, role)
            VALUES (:name, :password, :email, :role)
            """;

        KeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        SqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("name", user.getName())
                .addValue("password", user.getPassword())
                .addValue("email", user.getEmail())
                .addValue("role", user.getRole().toString());

        jdbcTemplate.update(query, mapSqlParameterSource, generatedKeyHolder);

        var keys = generatedKeyHolder.getKeys();
        if (keys != null) {
            return (int) keys.get("id");
        }

        return -1;
    }

    @Override
    public User getUserById(int id) {
        String query = """
            SELECT * FROM users WHERE id=:id
            """;

        SqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("id", id);

        return jdbcTemplate.query(query, mapSqlParameterSource, UserMapper::mapToPojo)
            .stream()
            .findFirst()
            .orElse(null);
    }

    @Override
    public User getUserByEmail(String email) {
        String query = """
            SELECT * FROM users WHERE email=:email
            """;
        SqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("email", email);

        return jdbcTemplate.query(query, mapSqlParameterSource, UserMapper::mapToPojo)
            .stream()
            .findFirst()
            .orElse(null);
    }

    @Override
    public void deleteUserById(int id) {
        String query = """
            DELETE FROM users WHERE id=:id
            """;
        SqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("id", id);

        jdbcTemplate.update(query, mapSqlParameterSource);
    }

    @Override
    public void updateUserWithoutPasswordChangeById(User user, int id) {
        String query = """
            UPDATE users SET
            name=:name, email=:email, role=:role
            WHERE id=:id
            """;
        SqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("name", user.getName())
            .addValue("email", user.getEmail())
            .addValue("role", user.getRole().toString())
            .addValue("id", id);

        jdbcTemplate.update(query, mapSqlParameterSource);
    }

}
