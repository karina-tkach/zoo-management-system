package org.university.zoomanagementsystem.visitlog.repository;

import org.university.zoomanagementsystem.visitlog.VisitLog;

import java.util.List;

public interface VisitLogRepository {
    int addVisitLog(VisitLog visitLog);

    VisitLog getVisitLogByTicketId(int ticketId);

    VisitLog getVisitLogById(int id);

    List<VisitLog> getVisitLogsWithPagination(int pageNumber, int limit);

    int getVisitLogsRowsCount();
}
