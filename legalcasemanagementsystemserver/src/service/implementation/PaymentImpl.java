/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.implementation;

import dao.PaymentDao;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import model.Payment;
import service.PaymentInterface;

/**
 *
 * @author orny
 */
public class PaymentImpl extends UnicastRemoteObject implements PaymentInterface{
    public PaymentImpl() throws RemoteException{
        super();
    }
    PaymentDao dao=new PaymentDao();

    @Override
    public String registerPayment(Payment payment) throws RemoteException {
        return dao.registerPayment(payment);
    }

    @Override
    public String updatePayment(Payment payment) throws RemoteException {
        return dao.updatePayment(payment);
        
    }

    @Override
    public String deletePayment(Payment payment) throws RemoteException {
       return dao.deletePayment(payment);
    }

    @Override
    public List<Payment> retrieveAll() throws RemoteException {
        return dao.retrieveAll();
    }
    

    @Override
    public Payment retrieveById(Payment payment) throws RemoteException {
        return dao.retrieveById(payment);
        
    }

    
}
