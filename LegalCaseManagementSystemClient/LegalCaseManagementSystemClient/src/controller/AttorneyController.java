package controller;

import dao.AttorneyDao;
import model.Attorney;
import model.Case;

import java.util.List;

/**
 * Controller for attorney-related operations.
 */
public class AttorneyController {
    private AttorneyDao attorneyDao;

    public AttorneyController() {
        this.attorneyDao = new AttorneyDao();
    }

    // 1. Get all attorneys
    public List<Attorney> getAllAttorneys() {
        return attorneyDao.retrieveAll();
    }

    // 2. Get attorney by ID (you must create a dummy Attorney to use current DAO method)
    public Attorney getAttorneyById(String attorneyId) {
        Attorney dummy = new Attorney();
        dummy.setAttorneyId(attorneyId);  // assuming attorneyId is String
        return attorneyDao.retrieveById(dummy);
    }

    // 3. Create new attorney
    public boolean createAttorney(Attorney attorney) {
        String result = attorneyDao.registerAttorney(attorney);
        return result != null && result.contains("success");
    }

    // 4. Update attorney
    public boolean updateAttorney(Attorney attorney) {
        String result = attorneyDao.updateAttorney(attorney);
        return result != null && result.contains("success");
    }

    // 5. Delete attorney
    public boolean deleteAttorney(String attorneyId) {
        Attorney dummy = new Attorney();
        dummy.setAttorneyId(attorneyId);
        Attorney existing = attorneyDao.retrieveById(dummy);
        if (existing == null) {
            return false;
        }
        String result = attorneyDao.deleteAttorney(existing);
        return result != null && result.contains("success");
    }

    // 6. NOT IMPLEMENTED in DAO: You’d have to create these methods if needed
    public List<Attorney> findAttorneysByName(String name) {
        throw new UnsupportedOperationException("Method not implemented in DAO.");
    }

    public List<Attorney> findAttorneysBySpecialization(String specialization) {
        throw new UnsupportedOperationException("Method not implemented in DAO.");
    }

    public List<Case> getAttorneyCases(String attorneyId) {
        throw new UnsupportedOperationException("Method not implemented in DAO.");
    }
}
