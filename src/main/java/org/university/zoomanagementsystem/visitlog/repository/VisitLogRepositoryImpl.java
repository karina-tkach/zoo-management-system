package org.university.zoomanagementsystem.visitlog.repository;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.university.zoomanagementsystem.excursion.ExcursionMapper;
import org.university.zoomanagementsystem.visitlog.VisitLog;
import org.university.zoomanagementsystem.visitlog.VisitLogMapper;

import java.util.List;

@Repository
public class VisitLogRepositoryImpl implements VisitLogRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    public VisitLogRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int addVisitLog(VisitLog visitLog) {
        String query = """
            INSERT INTO visit_logs (gate_id, ticket_id, notes)
            VALUES (:gate_id, :ticket_id, :notes)
            """;

        KeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        SqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("gate_id", visitLog.getGate().getId())
                .addValue("ticket_id", visitLog.getTicket().getId())
                .addValue("notes", visitLog.getNotes());

        jdbcTemplate.update(query, mapSqlParameterSource, generatedKeyHolder);

        var keys = generatedKeyHolder.getKeys();
        if (keys != null) {
            return (int) keys.get("id");
        }

        return -1;
    }

    @Override
    public VisitLog getVisitLogById(int id) {
        String query = """
            SELECT vl.id, vl.entry_time, vl.notes,
                   vl.gate_id, g.name, g.location,
                   vl.ticket_id, t.uuid, t.full_name, tp.ticket_type, tp.visit_type
            FROM visit_logs vl
            JOIN gates g ON vl.gate_id = g.id
            JOIN tickets t ON vl.ticket_id = t.id
            JOIN ticket_pricings tp ON t.pricing_id = tp.id
            WHERE vl.id=:id
        """;

        SqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("id", id);

        return jdbcTemplate.query(query, mapSqlParameterSource, VisitLogMapper::mapToPojo)
                .stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public VisitLog getVisitLogByTicketId(int ticketId) {
        String query = """
            SELECT vl.id, vl.entry_time, vl.notes,
                   vl.gate_id, g.name, g.location,
                   vl.ticket_id, t.uuid, t.full_name, tp.ticket_type, tp.visit_type
            FROM visit_logs vl
            JOIN gates g ON vl.gate_id = g.id
            JOIN tickets t ON vl.ticket_id = t.id
            JOIN ticket_pricings tp ON t.pricing_id = tp.id
            WHERE vl.ticket_id=:ticket_id
        """;

        SqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("ticket_id", ticketId);

        return jdbcTemplate.query(query, mapSqlParameterSource, VisitLogMapper::mapToPojo)
                .stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<VisitLog> getVisitLogsWithPagination(int pageNumber, int limit) {
        String query = """
            SELECT vl.id, vl.entry_time, vl.notes,
                   vl.gate_id, g.name, g.location,
                   vl.ticket_id, t.uuid, t.full_name, tp.ticket_type, tp.visit_type
            FROM visit_logs vl
            JOIN gates g ON vl.gate_id = g.id
            JOIN tickets t ON vl.ticket_id = t.id
            JOIN ticket_pricings tp ON t.pricing_id = tp.id
            ORDER BY vl.entry_time DESC
            LIMIT :limit OFFSET :offset
        """;

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("limit", limit)
                .addValue("offset", getOffset(pageNumber, limit));

        return jdbcTemplate.query(query, parameters, VisitLogMapper::mapToPojo);
    }

    @Override
    public int getVisitLogsRowsCount() {
        String query = "SELECT COUNT(*) FROM visit_logs";
        Integer count = jdbcTemplate.queryForObject(query, new MapSqlParameterSource(), Integer.class);
        return (count != null) ? count : 0;
    }

    private static int getOffset(int pageNumber, int pageSize) {
        return (pageNumber - 1) * pageSize;
    }
}
