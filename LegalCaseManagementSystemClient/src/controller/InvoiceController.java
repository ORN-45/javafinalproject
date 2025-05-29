package controller;

// Assuming model.Invoice and model.Payment are compatible with server's models
import model.Invoice; 
import model.Payment; // Needed for recordPayment
import service.remote.InvoiceService; // Interface from the server project

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.rmi.RemoteException; 

/**
 * Controller class for Invoice operations using RMI.
 */
public class InvoiceController {
    private InvoiceService invoiceService; // RMI service stub

    public InvoiceController() {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            this.invoiceService = (InvoiceService) registry.lookup("InvoiceService");
        } catch (Exception e) {
            System.err.println("InvoiceController: Error looking up InvoiceService: " + e.getMessage());
            e.printStackTrace();
            // invoiceService will be null, methods should handle this.
        }
    }

    /**
     * Create a new invoice.
     */
    public String createInvoice(Invoice invoice) throws RemoteException {
        if (this.invoiceService == null) throw new RemoteException("InvoiceService not initialized.");
        if (invoice == null) {
            throw new IllegalArgumentException("Invoice cannot be null");
        }
        return invoiceService.createInvoice(invoice);
    }

    /**
     * Update an existing invoice.
     */
    public String updateInvoice(Invoice invoice) throws RemoteException {
        if (this.invoiceService == null) throw new RemoteException("InvoiceService not initialized.");
        if (invoice == null) {
            throw new IllegalArgumentException("Invoice cannot be null");
        }
        return invoiceService.updateInvoice(invoice);
    }

    /**
     * Delete an invoice.
     */
    public String deleteInvoice(Invoice invoice) throws RemoteException {
        if (this.invoiceService == null) throw new RemoteException("InvoiceService not initialized.");
        if (invoice == null) {
            throw new IllegalArgumentException("Invoice cannot be null");
        }
        return invoiceService.deleteInvoice(invoice);
    }

    /**
     * Retrieve an invoice by its ID.
     */
    public Invoice getInvoiceById(int id) throws RemoteException {
        if (this.invoiceService == null) throw new RemoteException("InvoiceService not initialized.");
        return invoiceService.getInvoiceById(id);
    }

    /**
     * Retrieve invoices by case ID.
     */
    public List<Invoice> getInvoicesByCaseId(int caseId) throws RemoteException {
        if (this.invoiceService == null) throw new RemoteException("InvoiceService not initialized.");
        return invoiceService.getInvoicesByCaseId(caseId);
    }

    /**
     * Retrieve invoices by client ID.
     */
    public List<Invoice> getInvoicesByClientId(int clientId) throws RemoteException {
        if (this.invoiceService == null) throw new RemoteException("InvoiceService not initialized.");
        return invoiceService.getInvoicesByClientId(clientId);
    }
    
    /**
     * Record a payment for an invoice.
     */
    public String recordPayment(Payment payment, int invoiceId) throws RemoteException {
        if (this.invoiceService == null) throw new RemoteException("InvoiceService not initialized.");
        if (payment == null) {
            throw new IllegalArgumentException("Payment cannot be null");
        }
        return invoiceService.recordPayment(payment, invoiceId);
    }

    // Method getAllInvoices() was removed as it's not directly supported by InvoiceService.
}
