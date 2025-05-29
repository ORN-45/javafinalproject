/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import model.Invoice;

/**
 *
 * @author orny
 */
public interface InvoiceInterface extends Remote  {
    public String registerInvoice(Invoice invoice) throws RemoteException;
        public String updateInvoice(Invoice invoice) throws RemoteException;
            public String deleteInvoice(Invoice invoice) throws RemoteException;
            public List<Invoice>retrieveAll() throws RemoteException;
            public Invoice retrieveById(Invoice invoice)throws RemoteException;
        
            
           
    
    
    
    
}
