package org.university.zoomanagementsystem.enclosure;

import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class EnclosureMapper {
    private EnclosureMapper() {
    }

    @SuppressWarnings("java:S1172")
    public static Enclosure mapToPojo(ResultSet rs, int ignoredRowNum) throws SQLException {
        return new Enclosure(rs.getInt("id"),
                rs.getString("name"),
                rs.getString("location"),
                HabitatType.valueOf(rs.getString("environment_type")),
                rs.getInt("area_m2"));
    }
}
