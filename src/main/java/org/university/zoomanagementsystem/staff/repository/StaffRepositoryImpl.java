package org.university.zoomanagementsystem.staff.repository;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.university.zoomanagementsystem.staff.Staff;
import org.university.zoomanagementsystem.staff.StaffDTOMapper;
import org.university.zoomanagementsystem.staff.StaffMapper;
import org.university.zoomanagementsystem.staff.StaffDTO;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

@Repository
@SuppressWarnings({"java:S1192", "java:S2259"})
public class StaffRepositoryImpl implements StaffRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public StaffRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int addStaff(Staff staff) {
        String query = """
            INSERT INTO staff (user_id, hire_date, salary, working_days, shift_start, shift_end)
            VALUES (:user_id, :hire_date, :salary, :working_days, :shift_start, :shift_end)
            """;

        KeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        SqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("user_id", staff.getUserId())
                .addValue("hire_date", Date.valueOf(staff.getHireDate()))
                .addValue("salary", staff.getSalary())
                .addValue("working_days", staff.getWorkingDays())
                .addValue("shift_start", Time.valueOf(staff.getShiftStart()))
                .addValue("shift_end", Time.valueOf(staff.getShiftEnd()));

        jdbcTemplate.update(query, mapSqlParameterSource, generatedKeyHolder);

        var keys = generatedKeyHolder.getKeys();
        if (keys != null) {
            return (int) keys.get("id");
        }

        return -1;
    }

    @Override
    public Staff getStaffById(int id) {
        String query = """
            SELECT * FROM staff WHERE id=:id
            """;

        SqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("id", id);

        return jdbcTemplate.query(query, mapSqlParameterSource, StaffMapper::mapToPojo)
                .stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public void updateStaffById(Staff staff, int id) {
        String query = """
            UPDATE staff SET
            hire_date=:hire_date, salary=:salary, working_days=:working_days,
            shift_start=:shift_start, shift_end=:shift_end
            WHERE id=:id
            """;
        SqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("hire_date", Date.valueOf(staff.getHireDate()))
                .addValue("salary", staff.getSalary())
                .addValue("working_days", staff.getWorkingDays())
                .addValue("shift_start", Time.valueOf(staff.getShiftStart()))
                .addValue("shift_end", Time.valueOf(staff.getShiftEnd()))
                .addValue("id", id);

        jdbcTemplate.update(query, mapSqlParameterSource);
    }

    @Override
    public List<StaffDTO> getStaffWithPagination(int pageNumber, int limit) {
        String query = """
        SELECT 
            s.id AS staff_id, s.user_id, u.name, u.email, u.role,
            s.hire_date, s.salary, s.working_days, s.shift_start, s.shift_end
        FROM staff s
        JOIN users u ON s.user_id = u.id
        ORDER BY s.id
        LIMIT :limit
        OFFSET :offset
        """;
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("limit", limit)
                .addValue("offset", getOffset(pageNumber, limit));

        return jdbcTemplate.query(query, parameters, StaffDTOMapper::mapToPojo);
    }

    @Override
    public int getStaffRowsCount() {
        String query = "SELECT COUNT(*) FROM staff";
        Integer count = jdbcTemplate.queryForObject(query, new MapSqlParameterSource(), Integer.class);
        return (count != null) ? count : 0;
    }

    private static int getOffset(int pageNumber, int pageSize) {
        return (pageNumber - 1) * pageSize;
    }

}

