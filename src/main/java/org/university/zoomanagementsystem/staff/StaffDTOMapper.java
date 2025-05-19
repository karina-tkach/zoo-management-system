package org.university.zoomanagementsystem.staff;

import org.springframework.stereotype.Component;
import org.university.zoomanagementsystem.user.Role;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class StaffDTOMapper {
    private StaffDTOMapper() {}

    @SuppressWarnings("java:S1172")
    public static StaffDTO mapToPojo(ResultSet rs, int ignoredRowNum) throws SQLException {
        return new StaffDTO(rs.getInt("staff_id"),
                rs.getInt("user_id"),
                rs.getString("name"),
                rs.getString("email"),
                Role.valueOf(rs.getString("role")),
                rs.getDate("hire_date").toLocalDate(),
                rs.getLong("salary"),
                rs.getString("working_days"),
                rs.getTime("shift_start").toLocalTime(),
                rs.getTime("shift_end").toLocalTime());
    }
}
