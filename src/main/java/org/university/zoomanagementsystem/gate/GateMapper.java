package org.university.zoomanagementsystem.gate;

import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class GateMapper {
    private GateMapper() {}

    @SuppressWarnings("java:S1172")
    public static Gate mapToPojo(ResultSet rs, int ignoredRowNum) throws SQLException {
        return new Gate(rs.getInt("id"),
                rs.getString("name"),
                rs.getString("location"));
    }
}
