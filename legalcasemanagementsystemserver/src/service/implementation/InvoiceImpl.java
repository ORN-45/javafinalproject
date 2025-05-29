/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.implementation;

import dao.InvoiceDao;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import model.Invoice;
import service.InvoiceInterface;

/**
 *
 * @author orny
 */
public class InvoiceImpl extends UnicastRemoteObject implements InvoiceInterface{
    public InvoiceImpl() throws RemoteException{
        super();
    }
    InvoiceDao dao=new InvoiceDao();

    @Override
    public String registerInvoice(Invoice invoice) throws RemoteException {
        return dao.registerInvoice(invoice);
    }

    @Override
    public String updateInvoice(Invoice invoice) throws RemoteException {
        return dao.updateInvoice(invoice);
        
    }

    @Override
    public String deleteInvoice(Invoice invoice) throws RemoteException {
       return dao.deleteInvoice(invoice);
    }

    @Override
    public List<Invoice> retrieveAll() throws RemoteException {
        return dao.retrieveAll();
    }
    

    @Override
    public Invoice retrieveById(Invoice invoice) throws RemoteException {
        return dao.retrieveById(invoice);
        
    }

    
}
