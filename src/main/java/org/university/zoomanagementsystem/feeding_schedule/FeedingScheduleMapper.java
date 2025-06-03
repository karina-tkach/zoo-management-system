package org.university.zoomanagementsystem.feeding_schedule;

import org.springframework.stereotype.Component;
import org.university.zoomanagementsystem.animal.Animal;
import org.university.zoomanagementsystem.animal.enums.HealthStatus;
import org.university.zoomanagementsystem.enclosure.Enclosure;
import org.university.zoomanagementsystem.enclosure.HabitatType;
import org.university.zoomanagementsystem.user.User;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FeedingScheduleMapper {
    private FeedingScheduleMapper() {
    }

    @SuppressWarnings("java:S1172")
    public static FeedingSchedule mapToPojo(ResultSet rs, int ignoredRowNum) throws SQLException {
        return new FeedingSchedule(rs.getInt("id"),
                new Animal(rs.getInt("animal_id"),
                        rs.getString("name"),
                        null, null,
                        HabitatType.valueOf(rs.getString("habitat_type")),
                        null,
                        null,
                        new Enclosure(rs.getInt("enclosure_id"), rs.getString("enclosure_name"),
                                rs.getString("location"),
                                null, rs.getInt("area_m2")),
                        HealthStatus.valueOf(rs.getString("health_status")),
                        rs.getString("image"),
                        null,
                        rs.getTimestamp("last_fed_up_at").toLocalDateTime()),
                new User(rs.getInt("caretaker_id"),rs.getString("user_name"), null, rs.getString("email"), null),
                rs.getString("food_type"),
                rs.getTime("time").toLocalTime(),
                rs.getInt("portion_size_grams"),
                rs.getBoolean("is_done_today"));
    }
}
