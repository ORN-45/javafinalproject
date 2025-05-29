package service.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import model.Attorney; // Server-side model.Attorney

public interface AttorneyService extends Remote {
    String registerAttorney(Attorney attorney) throws RemoteException;
    String updateAttorney(Attorney attorney) throws RemoteException;
    String deleteAttorney(Attorney attorney) throws RemoteException;
    List<Attorney> getAllAttorneys() throws RemoteException;
    Attorney getAttorneyById(int attorneyId) throws RemoteException;
}
