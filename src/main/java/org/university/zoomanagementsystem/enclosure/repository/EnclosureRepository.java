package org.university.zoomanagementsystem.enclosure.repository;

import org.university.zoomanagementsystem.enclosure.Enclosure;
import org.university.zoomanagementsystem.enclosure.HabitatType;

import java.util.List;


public interface EnclosureRepository {
    int addEnclosure(Enclosure enclosure);

    Enclosure getEnclosureById(int id);

    void updateEnclosureById(Enclosure enclosure, int id);

    void deleteEnclosureById(int id);

    List<Enclosure> getEnclosuresWithPagination(int pageNumber, int limit);

    int getEnclosuresRowsCount();

    List<Enclosure> getEnclosuresByEnvironmentType(HabitatType environmentType);
}
