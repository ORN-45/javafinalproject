package service.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import model.Case; // Assuming this refers to the server-side model.Case for now

public interface CaseService extends Remote {
    String registerCase(Case legalCase) throws RemoteException;
    String updateCase(Case legalCase) throws RemoteException;
    String deleteCase(Case legalCase) throws RemoteException;
    List<Case> getAllCases() throws RemoteException;
    Case getCaseById(int caseId) throws RemoteException;
    List<Case> findCasesByStatus(String status) throws RemoteException;
    legalcasemanagementsystemserver.model.Case getCaseByCaseNumber(String caseNumber) throws java.rmi.RemoteException;
    public java.util.List<legalcasemanagementsystemserver.model.Case> findCasesByTitle(String title) throws java.rmi.RemoteException;
}
