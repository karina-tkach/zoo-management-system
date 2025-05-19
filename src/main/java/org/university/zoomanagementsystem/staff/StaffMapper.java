package org.university.zoomanagementsystem.staff;

import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class StaffMapper {
    private StaffMapper() {
    }

    @SuppressWarnings("java:S1172")
    public static Staff mapToPojo(ResultSet rs, int ignoredRowNum) throws SQLException {
        return new Staff(rs.getInt("id"),
                rs.getInt("user_id"),
                rs.getDate("hire_date").toLocalDate(),
                rs.getLong("salary"),
                rs.getString("working_days"),
                rs.getTime("shift_start").toLocalTime(),
                rs.getTime("shift_end").toLocalTime());
    }
}
