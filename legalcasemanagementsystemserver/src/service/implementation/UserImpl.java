/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.implementation;

import dao.UserDao;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import model.User;
import service.UserInterface;

/**
 *
 * @author orny
 */
public class UserImpl extends UnicastRemoteObject implements UserInterface{
    public UserImpl() throws RemoteException{
        super();
    }
    UserDao dao=new UserDao();

    @Override
    public String registerUser(User user) throws RemoteException {
        return dao.registerUser(user);
    }

    @Override
    public String updateUser(User user) throws RemoteException {
        return dao.updateUser(user);
        
    }

    @Override
    public String deleteUser(User user) throws RemoteException {
       return dao.deleteUser(user);
    }

    @Override
    public List<User> retrieveAll() throws RemoteException {
        return dao.retrieveAll();
    }
    

    @Override
    public User retrieveById(User user) throws RemoteException {
        return dao.retrieveById(user);
        
    }

    
}
