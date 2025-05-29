package service.remote.impl;

import dao.CaseDao;
import model.Case;
import service.remote.CaseService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class CaseServiceImpl extends UnicastRemoteObject implements CaseService {

    private final CaseDao caseDao;

    public CaseServiceImpl() throws RemoteException {
        super();
        this.caseDao = new CaseDao();
    }

    @Override
    public String registerCase(Case legalCase) throws RemoteException {
        return caseDao.registerCase(legalCase);
    }

    @Override
    public String updateCase(Case legalCase) throws RemoteException {
        return caseDao.updateCase(legalCase);
    }

    @Override
    public String deleteCase(Case legalCase) throws RemoteException {
        return caseDao.deleteCase(legalCase);
    }

    @Override
    public List<Case> getAllCases() throws RemoteException {
        return caseDao.retrieveAll();
    }

    @Override
    public Case getCaseById(int caseId) throws RemoteException {
        Case tempCase = new Case();
        tempCase.setCaseId(caseId); // Assuming a setter for caseId exists in model.Case
        return caseDao.retrieveById(tempCase);
    }

    @Override
    public List<Case> findCasesByStatus(String status) throws RemoteException {
        // Assuming the method in CaseDao is findCasesByStatus.
        // If it's different, this will need adjustment.
        return caseDao.findCasesByStatus(status);
    }

    @Override
    public Case getCaseByCaseNumber(String caseNumber) throws RemoteException {
        try {
            return caseDao.findByCaseNumber(caseNumber);
        } catch (Exception e) {
            // Log the exception server-side
            System.err.println("Error in CaseServiceImpl.getCaseByCaseNumber: " + e.getMessage());
            e.printStackTrace();
            // Optionally wrap in a RemoteException or a custom serializable exception
            throw new RemoteException("Server error while fetching case by case number: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Case> findCasesByTitle(String title) throws RemoteException {
        try {
            return caseDao.findByTitle(title);
        } catch (Exception e) {
            System.err.println("Error in CaseServiceImpl.findCasesByTitle: " + e.getMessage());
            e.printStackTrace();
            throw new RemoteException("Server error while finding cases by title: " + e.getMessage(), e);
        }
    }
}
