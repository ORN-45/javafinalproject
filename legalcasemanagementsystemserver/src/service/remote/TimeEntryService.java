package service.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import model.TimeEntry; // Server-side model.TimeEntry

public interface TimeEntryService extends Remote {
    String recordTimeEntry(TimeEntry timeEntry) throws RemoteException;
    String updateTimeEntry(TimeEntry timeEntry) throws RemoteException;
    String deleteTimeEntry(TimeEntry timeEntry) throws RemoteException;
    List<TimeEntry> getTimeEntriesByCaseId(int caseId) throws RemoteException;
    List<TimeEntry> getTimeEntriesByAttorneyId(int attorneyId) throws RemoteException;
    TimeEntry getTimeEntryById(int timeEntryId) throws RemoteException;
}
