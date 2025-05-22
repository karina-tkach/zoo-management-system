package org.university.zoomanagementsystem.excursion.repository;

import org.university.zoomanagementsystem.excursion.Excursion;

import java.util.List;

public interface ExcursionRepository {
    int addExcursion(Excursion excursion);

    Excursion getExcursionById(int id);

    void updateExcursionById(Excursion excursion, int id);

    void deleteExcursionById(int id);

    List<Excursion> getExcursionsWithPagination(int pageNumber, int limit);

    int getExcursionsRowsCount();

    List<Excursion> getAvailableExcursions();
}
