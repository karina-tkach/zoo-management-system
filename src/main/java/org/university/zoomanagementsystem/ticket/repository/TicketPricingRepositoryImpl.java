package org.university.zoomanagementsystem.ticket.repository;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.university.zoomanagementsystem.ticket.TicketPricing;
import org.university.zoomanagementsystem.ticket.TicketPricingMapper;

import java.sql.Types;
import java.util.List;

@Repository
public class TicketPricingRepositoryImpl implements TicketPricingRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public TicketPricingRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }
    @Override
    public List<TicketPricing> getTicketPricings() {
        String query = """
            SELECT * FROM ticket_pricings
            ORDER BY id
            """;

        return namedParameterJdbcTemplate.query(query, TicketPricingMapper::mapToPojo);
    }
    @Override
    public TicketPricing getTicketPricingById(int ticketId) {
        String query = """
            SELECT * FROM ticket_pricings WHERE id=:id
            """;

        SqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("id", ticketId);

        return namedParameterJdbcTemplate.query(query, mapSqlParameterSource, TicketPricingMapper::mapToPojo)
                .stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public void updateTicketPricingById(int price, int id) {
        String query = """
            UPDATE ticket_pricings SET
            price=:price
            WHERE id=:id
            """;
        SqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("price", price)
                .addValue("id", id);

        namedParameterJdbcTemplate.update(query, mapSqlParameterSource);
    }

    @Override
    public TicketPricing getTicketPricingByTicketAndVisitType(String ticketType, String visitType) {
        String query = """
            SELECT * FROM ticket_pricings WHERE ticket_type=:ticket_type AND visit_type=:visit_type
            """;

        SqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("ticket_type", ticketType, Types.OTHER)
                .addValue("visit_type", visitType, Types.OTHER);

        return namedParameterJdbcTemplate.query(query, mapSqlParameterSource, TicketPricingMapper::mapToPojo)
                .stream()
                .findFirst()
                .orElse(null);
    }
}
