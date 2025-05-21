package org.university.zoomanagementsystem.ticket;

import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class TicketPricingMapper {
    private TicketPricingMapper() {}

    public static TicketPricing mapToPojo(ResultSet rs, int ignoredRowNum) throws SQLException {
        return new TicketPricing(rs.getInt("id"),
                rs.getString("ticket_type"),
                rs.getString("visit_type"),
                rs.getInt("price"));
    }
}
