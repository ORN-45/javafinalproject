package service.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import model.Event; // Server-side model.Event
import java.time.LocalDate;

public interface EventService extends Remote {
    String scheduleEvent(Event event) throws RemoteException;
    String updateEvent(Event event) throws RemoteException;
    String cancelEvent(Event event) throws RemoteException;
    List<Event> getEventsByCaseId(int caseId) throws RemoteException;
    List<Event> getEventsByDateRange(LocalDate startDate, LocalDate endDate) throws RemoteException;
    Event getEventById(int eventId) throws RemoteException;

    // New methods for Calendar Functionality
    public java.util.List<String> getAvailableEventTypes() throws RemoteException;
    public java.util.List<String> getAvailableEventStatuses() throws RemoteException;
}
