package org.university.zoomanagementsystem.visitlog;

import org.springframework.stereotype.Component;
import org.university.zoomanagementsystem.gate.Gate;
import org.university.zoomanagementsystem.ticket.Ticket;
import org.university.zoomanagementsystem.ticket.TicketType;
import org.university.zoomanagementsystem.ticket.VisitType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Component
public class VisitLogMapper {
    private VisitLogMapper() {}

    @SuppressWarnings("java:S1172")
    public static VisitLog mapToPojo(ResultSet rs, int ignoredRowNum) throws SQLException {
        return new VisitLog(rs.getInt("id"),
                new Gate(rs.getInt("gate_id"), rs.getString("name"),
                        rs.getString("location")),
                new Ticket(rs.getInt("ticket_id"), UUID.fromString(rs.getString("uuid")),
                        rs.getString("full_name"), TicketType.valueOf(rs.getString("ticket_type")),
                        VisitType.valueOf(rs.getString("visit_type")),0,null, null,null,null,null),
                rs.getTimestamp("entry_time").toLocalDateTime(),
                rs.getString("notes"));
    }
}
