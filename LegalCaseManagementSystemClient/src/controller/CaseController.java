package controller;

import dao.CaseDao;
import model.Case;

import java.util.List;

public class CaseController {
    private CaseDao caseDao;

    public CaseController() {
        this.caseDao = new CaseDao();
    }

    // Create a new case
    public String createCase(Case legalCase) {
        return caseDao.registerCase(legalCase);
    }

    // Update an existing case
    public String updateCase(Case legalCase) {
        return caseDao.updateCase(legalCase);
    }

    // Delete a case
    public String deleteCase(Case legalCase) {
        return caseDao.deleteCase(legalCase);
    }

    // Retrieve all cases
    public List<Case> getAllCases() {
        return caseDao.retrieveAll();
    }

    // Retrieve a case by ID
    public Case getCaseById(int caseId) {
        Case tempCase = new Case();
        tempCase.setId(caseId);
        return caseDao.retrieveById(tempCase);
    }
    public List<Case> findCasesByStatus(String status) {
        return caseDao.getCasesByStatus(status);
    }
    
}
