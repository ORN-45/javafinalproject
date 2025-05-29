package service.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import model.Invoice; // Server-side model.Invoice
import model.Payment; // Server-side model.Payment

public interface InvoiceService extends Remote {
    String createInvoice(Invoice invoice) throws RemoteException;
    String updateInvoice(Invoice invoice) throws RemoteException;
    String deleteInvoice(Invoice invoice) throws RemoteException;
    List<Invoice> getInvoicesByCaseId(int caseId) throws RemoteException;
    List<Invoice> getInvoicesByClientId(int clientId) throws RemoteException;
    Invoice getInvoiceById(int invoiceId) throws RemoteException;
    String recordPayment(Payment payment, int invoiceId) throws RemoteException;
}
