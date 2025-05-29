package controller;

import dao.EventDao;
import model.Event;

import java.util.List;

/**
 * Controller for event-related operations.
 */
public class EventController {
    private EventDao eventDao;

    public EventController() {
        this.eventDao = new EventDao();
    }

    /**
     * Get all events
     * @return List of all events
     */
    public List<Event> getAllEvents() {
        return eventDao.retrieveAll();
    }

    /**
     * Get an event by ID
     * @param id Event ID
     * @return Event object or null if not found
     */
    public Event getEventById(int id) {
        Event temp = new Event();
        temp.setId(id);
        return eventDao.retrieveById(temp);
    }

    /**
     * Create a new event
     * @param event Event object to register
     * @return Success/failure message
     */
    public String createEvent(Event event) {
        if (event == null) {
            return "Event object is null";
        }
        return eventDao.registerEvent(event);
    }

    /**
     * Update an existing event
     * @param event Event object to update
     * @return Success/failure message
     */
    public String updateEvent(Event event) {
        if (event == null) {
            return "Event object is null";
        }
        return eventDao.updateEvent(event);
    }

    /**
     * Delete an event
     * @param event Event object to delete
     * @return Success/failure message
     */
    public String deleteEvent(Event event) {
        if (event == null) {
            return "Event object is null";
        }
        return eventDao.deleteEvent(event);
    }
}
