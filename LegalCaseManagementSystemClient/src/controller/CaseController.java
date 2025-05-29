package controller;

// Assuming model.Case is compatible with server's model.Case for now
// If not, manual mapping or DTOs would be needed.
// For this step, we assume the client was using a version of model.Case
// that is compatible with what the server-side CaseService expects and returns.
import model.Case; 
import service.remote.CaseService; // Interface from the server project

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
// Import RemoteException for method signatures if needed, though service calls will throw it.
import java.rmi.RemoteException; 

public class CaseController {
    private CaseService caseService; // RMI service stub

    public CaseController() {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            this.caseService = (CaseService) registry.lookup("CaseService");
        } catch (Exception e) {
            System.err.println("CaseController: Error looking up CaseService: " + e.getMessage());
            e.printStackTrace();
            // Optionally, rethrow as a runtime exception or handle more gracefully
            // For now, if caseService is null, methods will throw NullPointerException
        }
    }

    // Create a new case
    public String createCase(Case legalCase) throws RemoteException {
        if (this.caseService == null) throw new RemoteException("CaseService not initialized.");
        return caseService.registerCase(legalCase);
    }

    // Update an existing case
    public String updateCase(Case legalCase) throws RemoteException {
        if (this.caseService == null) throw new RemoteException("CaseService not initialized.");
        return caseService.updateCase(legalCase);
    }

    // Delete a case
    public String deleteCase(Case legalCase) throws RemoteException {
        if (this.caseService == null) throw new RemoteException("CaseService not initialized.");
        return caseService.deleteCase(legalCase);
    }

    // Retrieve all cases
    public List<Case> getAllCases() throws RemoteException {
        if (this.caseService == null) throw new RemoteException("CaseService not initialized.");
        return caseService.getAllCases();
    }

    // Retrieve a case by ID
    public Case getCaseById(int caseId) throws RemoteException {
        if (this.caseService == null) throw new RemoteException("CaseService not initialized.");
        // The CaseService.getCaseById directly takes an int.
        // The previous implementation created a tempCase and set its ID,
        // which was specific to how the old DAO's retrieveById worked.
        // The remote interface CaseService.getCaseById(int caseId) is simpler.
        return caseService.getCaseById(caseId);
    }

    public List<Case> findCasesByStatus(String status) throws RemoteException {
        if (this.caseService == null) throw new RemoteException("CaseService not initialized.");
        // Assuming CaseService has findCasesByStatus.
        // The old DAO call was getCasesByStatus. The remote interface is findCasesByStatus.
        return caseService.findCasesByStatus(status);
    }

    public legalcasemanagementsystemserver.model.Case getCaseByCaseNumber(String caseNumber) throws java.rmi.RemoteException {
        if (this.caseService == null) {
            // System.err.println("CaseService is not available."); // Logging to console might not be best for UI app
            throw new RemoteException("CaseService not initialized in CaseController.");
        }
        // No try-catch needed here for RemoteException if the method signature already declares it.
        // The caller (e.g., CasesPanel) will handle it.
        return caseService.getCaseByCaseNumber(caseNumber);
    }

    public java.util.List<legalcasemanagementsystemserver.model.Case> findCasesByTitle(String title) throws java.rmi.RemoteException {
        if (this.caseService == null) {
            // System.err.println("CaseService is not available."); // Logging to console might not be best for UI app
            throw new RemoteException("CaseService not initialized in CaseController.");
        }
        // No try-catch needed here for RemoteException if the method signature already declares it.
        // The caller (e.g., CasesPanel) will handle it.
        return caseService.findCasesByTitle(title);
    }
}
