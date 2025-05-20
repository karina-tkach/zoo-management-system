package org.university.zoomanagementsystem.events;

import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class EventMapper {
    private EventMapper() {}

    public static Event mapToPojo(ResultSet rs, int ignoredRowNum) throws SQLException {
        return new Event(rs.getInt("id"),
                rs.getString("title"),
                rs.getString("description"),
                rs.getDate("date").toLocalDate(),
                rs.getTime("start_time").toLocalTime(),
                rs.getInt("duration_minutes"),
                rs.getString("location"),
                rs.getString("image"));
    }
}
