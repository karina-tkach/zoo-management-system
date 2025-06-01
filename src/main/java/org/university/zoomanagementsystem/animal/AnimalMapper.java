package org.university.zoomanagementsystem.animal;

import org.springframework.stereotype.Component;
import org.university.zoomanagementsystem.animal.enums.AnimalGender;
import org.university.zoomanagementsystem.animal.enums.AnimalGroup;
import org.university.zoomanagementsystem.animal.enums.HealthStatus;
import org.university.zoomanagementsystem.enclosure.Enclosure;
import org.university.zoomanagementsystem.enclosure.HabitatType;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class AnimalMapper {
    private AnimalMapper() {
    }

    @SuppressWarnings("java:S1172")
    public static Animal mapToPojo(ResultSet rs, int ignoredRowNum) throws SQLException {
        return new Animal(rs.getInt("id"),
                rs.getString("name"),
                rs.getString("species"),
                AnimalGroup.valueOf(rs.getString("animal_group")),
                HabitatType.valueOf(rs.getString("habitat_type")),
                AnimalGender.valueOf(rs.getString("gender")),
                rs.getDate("date_of_birth").toLocalDate(),
                new Enclosure(rs.getInt("enclosure_id"), rs.getString("enclosure_name"),
                        rs.getString("location"),
                        null, rs.getInt("area_m2")),
                HealthStatus.valueOf(rs.getString("health_status")),
                rs.getString("image"),
                rs.getTimestamp("last_checked_up_at").toLocalDateTime(),
                rs.getTimestamp("last_fed_up_at").toLocalDateTime());
    }
}
