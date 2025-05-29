package controller;

// Client-side DTO
import model.Event; 
// Server-side Model & related
import legalcasemanagementsystemserver.model.Event_SERVER; 
import legalcasemanagementsystemserver.model.Case_SERVER; // For setting associated case in server model
import service.remote.EventService; 

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.rmi.RemoteException; 
import java.time.LocalDate; 

/**
 * Controller for event-related operations using RMI with DTO mapping.
 */
public class EventController {
    private EventService eventService; // RMI service stub

    public EventController() {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            this.eventService = (EventService) registry.lookup("EventService");
        } catch (Exception e) {
            System.err.println("EventController: Error looking up EventService: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // --- Mapping Helper Methods ---
    private Event_SERVER mapEventDtoToServerModel(Event eventDto) {
        if (eventDto == null) return null;
        Event_SERVER serverEvent = new Event_SERVER();
        serverEvent.setId(eventDto.getId());
        serverEvent.setEventId(eventDto.getEventId());
        serverEvent.setTitle(eventDto.getTitle());
        serverEvent.setDescription(eventDto.getDescription());
        serverEvent.setEventType(eventDto.getEventType());
        serverEvent.setEventDate(eventDto.getEventDate());
        serverEvent.setStartTime(eventDto.getStartTime());
        serverEvent.setEndTime(eventDto.getEndTime());
        serverEvent.setLocation(eventDto.getLocation());
        serverEvent.setStatus(eventDto.getStatus());
        serverEvent.setReminderSet(eventDto.isReminderSet());
        serverEvent.setReminderDays(eventDto.getReminderDays());
        
        if (eventDto.getCaseId() > 0) {
            Case_SERVER caseRef = new Case_SERVER();
            caseRef.setId(eventDto.getCaseId());
            serverEvent.setAssociatedCase(caseRef);
        }
        return serverEvent;
    }

    private Event mapServerModelToEventDto(Event_SERVER serverEvent) {
        if (serverEvent == null) return null;
        Event eventDto = new Event();
        eventDto.setId(serverEvent.getId());
        eventDto.setEventId(serverEvent.getEventId());
        eventDto.setTitle(serverEvent.getTitle());
        eventDto.setDescription(serverEvent.getDescription());
        eventDto.setEventType(serverEvent.getEventType());
        eventDto.setEventDate(serverEvent.getEventDate());
        eventDto.setStartTime(serverEvent.getStartTime());
        eventDto.setEndTime(serverEvent.getEndTime());
        eventDto.setLocation(serverEvent.getLocation());
        eventDto.setStatus(serverEvent.getStatus());
        eventDto.setReminderSet(serverEvent.isReminderSet());
        eventDto.setReminderDays(serverEvent.getReminderDays());
        
        if (serverEvent.getAssociatedCase() != null) {
            eventDto.setCaseId(serverEvent.getAssociatedCase().getId());
        }
        return eventDto;
    }
    
    // --- Public API using DTOs ---

    public Event getEventById(int id) throws RemoteException {
        if (this.eventService == null) throw new RemoteException("EventService not initialized.");
        Event_SERVER serverEvent = eventService.getEventById(id);
        return mapServerModelToEventDto(serverEvent);
    }

    public String scheduleEvent(Event eventDto) throws RemoteException {
        if (this.eventService == null) throw new RemoteException("EventService not initialized.");
        if (eventDto == null) throw new IllegalArgumentException("Event DTO cannot be null");
        Event_SERVER serverEvent = mapEventDtoToServerModel(eventDto);
        return eventService.scheduleEvent(serverEvent);
    }

    public String updateEvent(Event eventDto) throws RemoteException {
        if (this.eventService == null) throw new RemoteException("EventService not initialized.");
        if (eventDto == null) throw new IllegalArgumentException("Event DTO cannot be null");
        Event_SERVER serverEvent = mapEventDtoToServerModel(eventDto);
        return eventService.updateEvent(serverEvent);
    }

    public String cancelEvent(Event eventDto) throws RemoteException {
        if (this.eventService == null) throw new RemoteException("EventService not initialized.");
        if (eventDto == null) throw new IllegalArgumentException("Event DTO cannot be null");
        Event_SERVER serverEvent = mapEventDtoToServerModel(eventDto);
        return eventService.cancelEvent(serverEvent);
    }
    
    public String deleteEvent(int eventId) throws RemoteException {
        if (this.eventService == null) throw new RemoteException("EventService not initialized.");
        Event_SERVER serverEvent = eventService.getEventById(eventId); // Fetch server model
        if (serverEvent == null) {
            return "Event not found with ID: " + eventId + ", cannot cancel/delete.";
        }
        return eventService.cancelEvent(serverEvent); // Use cancelEvent for what "delete" implies
    }

    public List<Event> getEventsByCaseId(int caseId) throws RemoteException {
        if (this.eventService == null) throw new RemoteException("EventService not initialized.");
        List<Event_SERVER> serverEvents = eventService.getEventsByCaseId(caseId);
        if (serverEvents == null) return new ArrayList<>();
        return serverEvents.stream()
                           .map(this::mapServerModelToEventDto)
                           .collect(Collectors.toList());
    }

    public List<Event> getEventsByDateRange(LocalDate startDate, LocalDate endDate) throws RemoteException {
        if (this.eventService == null) throw new RemoteException("EventService not initialized.");
        List<Event_SERVER> serverEvents = eventService.getEventsByDateRange(startDate, endDate);
        if (serverEvents == null) return new ArrayList<>();
        return serverEvents.stream()
                           .map(this::mapServerModelToEventDto)
                           .collect(Collectors.toList());
    }

    public String[] getEventTypes() throws RemoteException {
        if (this.eventService == null) throw new RemoteException("EventService not initialized.");
        List<String> typesList = eventService.getAvailableEventTypes();
        if (typesList == null) return new String[0];
        return typesList.toArray(new String[0]);
    }

    public String[] getEventStatuses() throws RemoteException {
        if (this.eventService == null) throw new RemoteException("EventService not initialized.");
        List<String> statusesList = eventService.getAvailableEventStatuses();
        if (statusesList == null) return new String[0];
        return statusesList.toArray(new String[0]);
    }
}
