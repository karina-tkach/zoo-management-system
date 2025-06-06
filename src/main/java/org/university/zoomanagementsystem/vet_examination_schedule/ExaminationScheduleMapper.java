package org.university.zoomanagementsystem.vet_examination_schedule;

import org.university.zoomanagementsystem.animal.Animal;
import org.university.zoomanagementsystem.animal.enums.HealthStatus;
import org.university.zoomanagementsystem.enclosure.Enclosure;
import org.university.zoomanagementsystem.enclosure.HabitatType;
import org.university.zoomanagementsystem.user.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ExaminationScheduleMapper {
    private ExaminationScheduleMapper() {}

    @SuppressWarnings("java:S1172")
    public static ExaminationSchedule mapToPojo(ResultSet rs, int ignoredRowNum) throws SQLException {
        return new ExaminationSchedule(rs.getInt("id"),
                new Animal(rs.getInt("animal_id"),
                        rs.getString("name"),
                        null, null,
                        HabitatType.valueOf(rs.getString("habitat_type")),
                        null,
                        null,
                        new Enclosure(rs.getInt("enclosure_id"), rs.getString("enclosure_name"),
                                rs.getString("location"),
                                null, 0),
                        HealthStatus.valueOf(rs.getString("health_status")),
                        rs.getString("image"),
                        rs.getTimestamp("last_checked_up_at").toLocalDateTime(),
                        null),
                new User(rs.getInt("vet_id"),rs.getString("user_name"), null, rs.getString("email"), null),
                rs.getTimestamp("planned_datetime").toLocalDateTime(),
                rs.getString("reason"),
                ExaminationStatus.valueOf(rs.getString("status")));
    }
}
