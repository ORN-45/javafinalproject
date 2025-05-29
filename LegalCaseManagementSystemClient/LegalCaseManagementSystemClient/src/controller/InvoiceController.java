package controller;

import dao.InvoiceDao;
import model.Invoice;

import java.util.List;

/**
 * Controller class for Invoice operations.
 */
public class InvoiceController {
    private InvoiceDao invoiceDao;

    public InvoiceController() {
        this.invoiceDao = new InvoiceDao();
    }

    /**
     * Register (save) a new invoice
     */
    public String createInvoice(Invoice invoice) {
        if (invoice == null) {
            return "Invoice cannot be null";
        }
        return invoiceDao.registerInvoice(invoice);
    }

    /**
     * Update an existing invoice
     */
    public String updateInvoice(Invoice invoice) {
        if (invoice == null) {
            return "Invoice cannot be null";
        }
        return invoiceDao.updateInvoice(invoice);
    }

    /**
     * Delete an invoice
     */
    public String deleteInvoice(Invoice invoice) {
        if (invoice == null) {
            return "Invoice cannot be null";
        }
        return invoiceDao.deleteInvoice(invoice);
    }

    /**
     * Retrieve an invoice by its ID
     */
    public Invoice getInvoiceById(int id) {
        Invoice temp = new Invoice();
        temp.setId(id);
        return invoiceDao.retrieveById(temp);
    }

    /**
     * Retrieve all invoices
     */
    public List<Invoice> getAllInvoices() {
        return invoiceDao.retrieveAll();
    }
}
