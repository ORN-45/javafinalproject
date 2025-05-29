/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
import model.Invoice;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author orny
 */
public class InvoiceDao {
    public String registerInvoice(Invoice invoice){
        try{
            Session ss= HibernateUtil.getSessionFactory().openSession();
            Transaction tr= ss.beginTransaction();
            ss.save(invoice);
            tr.commit();
            ss.close();
            return "Data saved succesffully";
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
      public String updateInvoice(Invoice invoice){
        try{
            Session ss= HibernateUtil.getSessionFactory().openSession();
            Transaction tr= ss.beginTransaction();
            ss.update(invoice);
            tr.commit();
            ss.close();
            return "Data updated succesffully";
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
      }
       public String deleteInvoice(Invoice invoice){
        try{
            Session ss= HibernateUtil.getSessionFactory().openSession();
            Transaction tr= ss.beginTransaction();
            ss.delete(invoice);
            tr.commit();
            ss.close();
            return "Data deleted succesffully";
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
}
         public List<Invoice> retrieveAll(){
             Session ss= HibernateUtil.getSessionFactory().openSession();
             List<Invoice> invoiceList= ss.createQuery("select  ord from Invoice ord").list(); // Corrected HQL
             ss.close();
             return invoiceList;
          }
         public Invoice retrieveById(Invoice invoice){
        Session ss= HibernateUtil.getSessionFactory().openSession();
        Invoice invoices = (Invoice)ss.get(Invoice.class, invoice.getId());
        ss.close();
        return invoices;
    }  

    public List<model.Invoice> getInvoicesByCaseId(int caseId) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            String hql = "FROM Invoice i WHERE i.legalCase.id = :caseId";
            org.hibernate.query.Query<model.Invoice> query = session.createQuery(hql, model.Invoice.class);
            query.setParameter("caseId", caseId);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public List<model.Invoice> getInvoicesByClientId(int clientId) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            String hql = "FROM Invoice i WHERE i.client.id = :clientId";
            org.hibernate.query.Query<model.Invoice> query = session.createQuery(hql, model.Invoice.class);
            query.setParameter("clientId", clientId);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
