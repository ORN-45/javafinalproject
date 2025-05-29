package service.remote.impl;

import dao.TimeEntryDao;
import model.TimeEntry;
import service.remote.TimeEntryService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collections;
import java.util.List;

public class TimeEntryServiceImpl extends UnicastRemoteObject implements TimeEntryService {

    private final TimeEntryDao timeEntryDao;

    public TimeEntryServiceImpl() throws RemoteException {
        super();
        this.timeEntryDao = new TimeEntryDao();
    }

    @Override
    public String recordTimeEntry(TimeEntry timeEntry) throws RemoteException {
        return timeEntryDao.registerTimeEntry(timeEntry);
    }

    @Override
    public String updateTimeEntry(TimeEntry timeEntry) throws RemoteException {
        return timeEntryDao.updateTimeEntry(timeEntry);
    }

    @Override
    public String deleteTimeEntry(TimeEntry timeEntry) throws RemoteException {
        return timeEntryDao.deleteTimeEntry(timeEntry);
    }

    @Override
    public List<TimeEntry> getTimeEntriesByCaseId(int caseId) throws RemoteException {
        // TODO: Implement this method once TimeEntryDao.getTimeEntriesByCaseId(int caseId) is available.
        System.err.println("Warning: TimeEntryDao.getTimeEntriesByCaseId(int caseId) is not yet implemented.");
        return Collections.emptyList();
    }

    @Override
    public List<TimeEntry> getTimeEntriesByAttorneyId(int attorneyId) throws RemoteException {
        // TODO: Implement this method once TimeEntryDao.getTimeEntriesByAttorneyId(int attorneyId) is available.
        System.err.println("Warning: TimeEntryDao.getTimeEntriesByAttorneyId(int attorneyId) is not yet implemented.");
        return Collections.emptyList();
    }

    @Override
    public TimeEntry getTimeEntryById(int timeEntryId) throws RemoteException {
        TimeEntry tempTimeEntry = new TimeEntry();
        tempTimeEntry.setId(timeEntryId);
        // TimeEntryDao.retrieveById correctly uses getId()
        return timeEntryDao.retrieveById(tempTimeEntry);
    }
}
