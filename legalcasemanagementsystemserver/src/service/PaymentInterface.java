/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import model.Payment;

/**
 *
 * @author orny
 */
public interface PaymentInterface extends Remote  {
    public String registerPayment(Payment payment) throws RemoteException;
        public String updatePayment(Payment payment) throws RemoteException;
            public String deletePayment(Payment payment) throws RemoteException;
            public List<Payment>retrieveAll() throws RemoteException;
            public Payment retrieveById(Payment payment)throws RemoteException;
        
            
           
    
    
    
    
}
