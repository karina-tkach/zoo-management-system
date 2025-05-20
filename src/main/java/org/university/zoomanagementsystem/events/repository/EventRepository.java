package org.university.zoomanagementsystem.events.repository;

import org.university.zoomanagementsystem.events.Event;

import java.util.List;

public interface EventRepository {
    int addEvent(Event event);

    Event getEventById(int id);

    void updateEventById(Event event, int id);

    void deleteEventById(int id);

    List<Event> getEventsWithPagination(int pageNumber, int limit);

    int getEventsRowsCount();
}
