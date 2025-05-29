package service.remote.impl;

import dao.EventDao;
import model.Event;
import service.remote.EventService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public class EventServiceImpl extends UnicastRemoteObject implements EventService {

    private final EventDao eventDao;

    public EventServiceImpl() throws RemoteException {
        super();
        this.eventDao = new EventDao();
    }

    @Override
    public String scheduleEvent(Event event) throws RemoteException {
        return eventDao.registerEvent(event);
    }

    @Override
    public String updateEvent(Event event) throws RemoteException {
        return eventDao.updateEvent(event);
    }

    @Override
    public String cancelEvent(Event event) throws RemoteException {
        if (event == null) {
            return "Event cannot be null.";
        }
        // Retrieve the latest state of the event if only ID is passed,
        // or use the passed event directly if it's fully populated.
        // For simplicity, assuming 'event' is a full object or its ID is sufficient for retrieval if needed.
        // Event eventToCancel = eventDao.retrieveById(event); // If only ID is guaranteed.
        // if(eventToCancel == null) return "Event not found";
        
        event.setStatus("Cancelled");
        return eventDao.updateEvent(event);
    }

    @Override
    public List<Event> getEventsByCaseId(int caseId) throws RemoteException {
        try {
            return eventDao.getEventsByCaseId(caseId); // DAO method exists
        } catch (Exception e) {
            System.err.println("EventServiceImpl: Error in getEventsByCaseId: " + e.getMessage());
            e.printStackTrace();
            throw new RemoteException("Server error while fetching events by case ID.", e);
        }
    }

    @Override
    public List<Event> getEventsByDateRange(LocalDate startDate, LocalDate endDate) throws RemoteException {
        try {
            return eventDao.getEventsByDateRange(startDate, endDate); // DAO method exists
        } catch (Exception e) {
            System.err.println("EventServiceImpl: Error in getEventsByDateRange: " + e.getMessage());
            e.printStackTrace();
            throw new RemoteException("Server error while fetching events by date range.", e);
        }
    }

    @Override
    public Event getEventById(int eventId) throws RemoteException {
        try {
            Event tempEvent = new Event();
            tempEvent.setId(eventId); 
            return eventDao.retrieveById(tempEvent);
        } catch (Exception e) {
            System.err.println("EventServiceImpl: Error in getEventById: " + e.getMessage());
            e.printStackTrace();
            throw new RemoteException("Server error while fetching event by ID.", e);
        }
    }

    // Implementation of new methods for Calendar Functionality
    @Override
    public List<String> getAvailableEventTypes() throws RemoteException {
        try {
            // Return a hardcoded list of common event types
            return java.util.Arrays.asList("Meeting", "Court Appearance", "Deadline", "Consultation", "Follow-up", "Other");
        } catch (Exception e) {
            // This part should ideally not fail for a hardcoded list, but for completeness:
            System.err.println("EventServiceImpl: Error in getAvailableEventTypes: " + e.getMessage());
            e.printStackTrace();
            throw new RemoteException("Server error while fetching available event types.", e);
        }
    }

    @Override
    public List<String> getAvailableEventStatuses() throws RemoteException {
        try {
            // Return a hardcoded list of common event statuses
            return java.util.Arrays.asList("Scheduled", "Confirmed", "Completed", "Cancelled", "Postponed");
        } catch (Exception e) {
            // This part should ideally not fail for a hardcoded list, but for completeness:
            System.err.println("EventServiceImpl: Error in getAvailableEventStatuses: " + e.getMessage());
            e.printStackTrace();
            throw new RemoteException("Server error while fetching available event statuses.", e);
        }
    }
}
