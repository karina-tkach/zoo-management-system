package org.university.zoomanagementsystem.excursion;

import org.springframework.stereotype.Component;
import org.university.zoomanagementsystem.user.Role;
import org.university.zoomanagementsystem.user.User;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ExcursionMapper {
    private ExcursionMapper() {
    }

    @SuppressWarnings("java:S1172")
    public static Excursion mapToPojo(ResultSet rs, int ignoredRowNum) throws SQLException {
        return new Excursion(rs.getInt("id"),
                rs.getString("topic"),
                new User(rs.getInt("guide_id"), rs.getString("name"),
                        null,
                        rs.getString("email"), Role.valueOf(rs.getString("role"))),
                rs.getString("description"),
                rs.getDate("date").toLocalDate(),
                rs.getTime("start_time").toLocalTime(),
                rs.getInt("duration_minutes"),
                rs.getInt("max_participants"),
                rs.getInt("booked_count"));
    }
}
