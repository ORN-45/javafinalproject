package controller;

import dao.TimeEntryDao;
import model.TimeEntry;

import java.util.List;

/**
 * Controller class for TimeEntry operations.
 */
public class TimeEntryController {
    private TimeEntryDao timeEntryDao;

    public TimeEntryController() {
        this.timeEntryDao = new TimeEntryDao();
    }

    /**
     * Register (save) a new time entry
     */
    public String createTimeEntry(TimeEntry timeEntry) {
        if (timeEntry == null) {
            return "TimeEntry cannot be null";
        }
        return timeEntryDao.registerTimeEntry(timeEntry);
    }

    /**
     * Update an existing time entry
     */
    public String updateTimeEntry(TimeEntry timeEntry) {
        if (timeEntry == null) {
            return "TimeEntry cannot be null";
        }
        return timeEntryDao.updateTimeEntry(timeEntry);
    }

    /**
     * Delete a time entry
     */
    public String deleteTimeEntry(TimeEntry timeEntry) {
        if (timeEntry == null) {
            return "TimeEntry cannot be null";
        }
        return timeEntryDao.deleteTimeEntry(timeEntry);
    }

    /**
     * Retrieve a time entry by its ID
     */
    public TimeEntry getTimeEntryById(int id) {
        TimeEntry temp = new TimeEntry();
        temp.setId(id);
        return timeEntryDao.retrieveById(temp);
    }

    /**
     * Retrieve all time entries
     */
    public List<TimeEntry> getAllTimeEntries() {
        return timeEntryDao.retrieveAll();
    }
}
