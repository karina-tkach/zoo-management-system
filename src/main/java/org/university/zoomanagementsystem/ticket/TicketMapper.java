package org.university.zoomanagementsystem.ticket;

import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Component
public class TicketMapper {
    private TicketMapper() {}

    public static Ticket mapToPojo(ResultSet rs, int ignoredRowNum) throws SQLException {
        return new Ticket(rs.getInt("id"),
                UUID.fromString(rs.getString("uuid")),
                rs.getString("full_name"),
                rs.getString("ticket_type"),
                rs.getString("visit_type"),
                rs.getInt("price"),
                rs.getDate("visit_date").toLocalDate(),
                rs.getObject("excursion_id", Integer.class),
                rs.getString("excursion_name"),
                rs.getString("purchase_method"),
                rs.getTimestamp("purchase_time").toLocalDateTime());
    }
}
