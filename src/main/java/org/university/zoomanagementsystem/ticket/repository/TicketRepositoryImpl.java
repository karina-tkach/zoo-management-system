package org.university.zoomanagementsystem.ticket.repository;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.university.zoomanagementsystem.ticket.Ticket;
import org.university.zoomanagementsystem.ticket.TicketMapper;

import java.sql.Date;
import java.util.List;

@Repository
public class TicketRepositoryImpl implements TicketRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public TicketRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int addTicket(Ticket ticket, int pricingId) {
        String query = """
            INSERT INTO tickets (uuid, full_name, pricing_id, visit_date, excursion_id,
            purchase_method)
            VALUES (:uuid, :full_name, :pricing_id, :visit_date, :excursion_id, :purchase_method)
            """;

        KeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        SqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("uuid", ticket.getUuid().toString())
                .addValue("full_name", ticket.getFullName())
                .addValue("pricing_id", pricingId)
                .addValue("visit_date", Date.valueOf(ticket.getVisitDate()))
                .addValue("excursion_id", ticket.getExcursionId())
                .addValue("purchase_method", ticket.getPurchaseMethod());

        jdbcTemplate.update(query, mapSqlParameterSource, generatedKeyHolder);

        var keys = generatedKeyHolder.getKeys();
        if (keys != null) {
            return (int) keys.get("id");
        }

        return -1;
    }

    @Override
    public Ticket getTicketById(int id) {

        String query = """
            SELECT t.*, e.topic AS excursion_name, tp.ticket_type, tp.visit_type, tp.price
            FROM tickets t
            LEFT JOIN excursions e ON t.excursion_id = e.id
            JOIN ticket_pricings tp ON t.pricing_id = tp.id
            WHERE t.id = :id
            """;

        SqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("id", id);

        return jdbcTemplate.query(query, mapSqlParameterSource, TicketMapper::mapToPojo)
                .stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Ticket> getTickets() {
        String query = """
            SELECT t.*, e.topic AS excursion_name, tp.ticket_type, tp.visit_type, tp.price
            FROM tickets t
            LEFT JOIN excursions e ON t.excursion_id = e.id
            JOIN ticket_pricings tp ON t.pricing_id = tp.id
            ORDER BY t.id
            """;

        return jdbcTemplate.query(query, TicketMapper::mapToPojo);
    }
}
