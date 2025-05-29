package service.remote.impl;

import dao.AttorneyDao;
import model.Attorney;
import service.remote.AttorneyService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class AttorneyServiceImpl extends UnicastRemoteObject implements AttorneyService {

    private final AttorneyDao attorneyDao;

    public AttorneyServiceImpl() throws RemoteException {
        super();
        this.attorneyDao = new AttorneyDao();
    }

    @Override
    public String registerAttorney(Attorney attorney) throws RemoteException {
        // Assuming AttorneyDao.registerAttorney handles setting the ID if it's auto-generated
        // or expects it to be pre-set if not.
        return attorneyDao.registerAttorney(attorney);
    }

    @Override
    public String updateAttorney(Attorney attorney) throws RemoteException {
        return attorneyDao.updateAttorney(attorney);
    }

    @Override
    public String deleteAttorney(Attorney attorney) throws RemoteException {
        return attorneyDao.deleteAttorney(attorney);
    }

    @Override
    public List<Attorney> getAllAttorneys() throws RemoteException {
        return attorneyDao.retrieveAll();
    }

    @Override
    public Attorney getAttorneyById(int attorneyId) throws RemoteException {
        Attorney tempAttorney = new Attorney();
        tempAttorney.setId(attorneyId); // Sets the primary key 'id'
        // AttorneyDao.retrieveById now correctly uses getId()
        return attorneyDao.retrieveById(tempAttorney);
    }
}
