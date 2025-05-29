package controller;

// Assuming model.TimeEntry is compatible with server's model.TimeEntry
import model.TimeEntry; 
import service.remote.TimeEntryService; // Interface from the server project

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.rmi.RemoteException; 

/**
 * Controller class for TimeEntry operations using RMI.
 */
public class TimeEntryController {
    private TimeEntryService timeEntryService; // RMI service stub

    public TimeEntryController() {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            this.timeEntryService = (TimeEntryService) registry.lookup("TimeEntryService");
        } catch (Exception e) {
            System.err.println("TimeEntryController: Error looking up TimeEntryService: " + e.getMessage());
            e.printStackTrace();
            // timeEntryService will be null, methods should handle this.
        }
    }

    /**
     * Record a new time entry.
     */
    public String recordTimeEntry(TimeEntry timeEntry) throws RemoteException {
        if (this.timeEntryService == null) throw new RemoteException("TimeEntryService not initialized.");
        if (timeEntry == null) {
            throw new IllegalArgumentException("TimeEntry cannot be null");
        }
        return timeEntryService.recordTimeEntry(timeEntry);
    }

    /**
     * Update an existing time entry.
     */
    public String updateTimeEntry(TimeEntry timeEntry) throws RemoteException {
        if (this.timeEntryService == null) throw new RemoteException("TimeEntryService not initialized.");
        if (timeEntry == null) {
            throw new IllegalArgumentException("TimeEntry cannot be null");
        }
        return timeEntryService.updateTimeEntry(timeEntry);
    }

    /**
     * Delete a time entry.
     */
    public String deleteTimeEntry(TimeEntry timeEntry) throws RemoteException {
        if (this.timeEntryService == null) throw new RemoteException("TimeEntryService not initialized.");
        if (timeEntry == null) {
            throw new IllegalArgumentException("TimeEntry cannot be null");
        }
        return timeEntryService.deleteTimeEntry(timeEntry);
    }

    /**
     * Retrieve a time entry by its ID.
     */
    public TimeEntry getTimeEntryById(int id) throws RemoteException {
        if (this.timeEntryService == null) throw new RemoteException("TimeEntryService not initialized.");
        return timeEntryService.getTimeEntryById(id);
    }

    /**
     * Retrieve time entries by case ID.
     */
    public List<TimeEntry> getTimeEntriesByCaseId(int caseId) throws RemoteException {
        if (this.timeEntryService == null) throw new RemoteException("TimeEntryService not initialized.");
        return timeEntryService.getTimeEntriesByCaseId(caseId);
    }

    /**
     * Retrieve time entries by attorney ID.
     */
    public List<TimeEntry> getTimeEntriesByAttorneyId(int attorneyId) throws RemoteException {
        if (this.timeEntryService == null) throw new RemoteException("TimeEntryService not initialized.");
        return timeEntryService.getTimeEntriesByAttorneyId(attorneyId);
    }

    // Method getAllTimeEntries() was removed as it's not directly supported by TimeEntryService.
}
