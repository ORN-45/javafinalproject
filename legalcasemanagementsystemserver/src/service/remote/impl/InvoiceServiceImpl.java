package service.remote.impl;

import dao.InvoiceDao;
import dao.PaymentDao;
import model.Invoice;
import model.Payment;
import service.remote.InvoiceService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collections;
import java.util.List;

public class InvoiceServiceImpl extends UnicastRemoteObject implements InvoiceService {

    private final InvoiceDao invoiceDao;
    private final PaymentDao paymentDao;

    public InvoiceServiceImpl() throws RemoteException {
        super();
        this.invoiceDao = new InvoiceDao();
        this.paymentDao = new PaymentDao();
    }

    @Override
    public String createInvoice(Invoice invoice) throws RemoteException {
        return invoiceDao.registerInvoice(invoice);
    }

    @Override
    public String updateInvoice(Invoice invoice) throws RemoteException {
        return invoiceDao.updateInvoice(invoice);
    }

    @Override
    public String deleteInvoice(Invoice invoice) throws RemoteException {
        return invoiceDao.deleteInvoice(invoice);
    }

    @Override
    public List<Invoice> getInvoicesByCaseId(int caseId) throws RemoteException {
        // TODO: Implement this method once InvoiceDao.getInvoicesByCaseId(int caseId) is available.
        System.err.println("Warning: InvoiceDao.getInvoicesByCaseId(int caseId) is not yet implemented.");
        return Collections.emptyList();
    }

    @Override
    public List<Invoice> getInvoicesByClientId(int clientId) throws RemoteException {
        // TODO: Implement this method once InvoiceDao.getInvoicesByClientId(int clientId) is available.
        System.err.println("Warning: InvoiceDao.getInvoicesByClientId(int clientId) is not yet implemented.");
        return Collections.emptyList();
    }

    @Override
    public Invoice getInvoiceById(int invoiceId) throws RemoteException {
        Invoice tempInvoice = new Invoice();
        tempInvoice.setId(invoiceId);
        return invoiceDao.retrieveById(tempInvoice);
    }

    @Override
    public String recordPayment(Payment payment, int invoiceId) throws RemoteException {
        if (payment == null) {
            return "Payment object cannot be null.";
        }

        Invoice tempQueryInvoice = new Invoice();
        tempQueryInvoice.setId(invoiceId);
        Invoice invoice = invoiceDao.retrieveById(tempQueryInvoice);

        if (invoice == null) {
            return "Invoice not found with ID: " + invoiceId;
        }

        // Associate payment with invoice
        payment.setInvoice(invoice); // This also sets payment.invoiceId and payment.clientId via Invoice.addPayment logic
                                     // if setInvoice also calls invoice.addPayment or similar.
                                     // model.Payment.setInvoice() calls invoice.recalculateAmountPaid() if invoice is not null.
                                     // And model.Invoice.addPayment() calls payment.setInvoice(this) and recalculateAmountPaid().
                                     // To be safe and explicit:
        invoice.addPayment(payment); // This ensures bi-directional link and recalculates amountPaid in Invoice object

        String paymentResult = paymentDao.registerPayment(payment);
        if (paymentResult == null || !paymentResult.toLowerCase().contains("success")) {
            // Consider rolling back or logging the failure more formally
            return "Failed to record payment: " + (paymentResult == null ? "Unknown error" : paymentResult);
        }
        
        // Update invoice to reflect new payment and potentially status change
        // The invoice object in memory (invoice) has already been updated by invoice.addPayment()
        String invoiceUpdateResult = invoiceDao.updateInvoice(invoice);
        if (invoiceUpdateResult == null || !invoiceUpdateResult.toLowerCase().contains("success")) {
            return "Payment recorded, but failed to update invoice status: " + (invoiceUpdateResult == null ? "Unknown error" : invoiceUpdateResult) ;
        }

        return "Payment recorded and invoice updated successfully.";
    }
}
