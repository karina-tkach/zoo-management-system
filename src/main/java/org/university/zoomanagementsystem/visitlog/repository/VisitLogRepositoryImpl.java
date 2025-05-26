package org.university.zoomanagementsystem.visitlog.repository;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.university.zoomanagementsystem.visitlog.VisitLog;

import java.util.List;

@Repository
public class VisitLogRepositoryImpl implements VisitLogRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    public VisitLogRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int addVisitLog(VisitLog visitLog) {
        return 0;
    }

    @Override
    public List<VisitLog> getVisitLogsWithPagination(int pageNumber, int limit) {
        return null;
    }

    @Override
    public int getVisitLogsRowsCount() {
        return 0;
    }
}
