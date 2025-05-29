/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
import model.Payment;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author orny
 */
public class PaymentDao {
    public String registerPayment(Payment payment){
        try{
            Session ss= HibernateUtil.getSessionFactory().openSession();
            Transaction tr= ss.beginTransaction();
            ss.save(payment);
            tr.commit();
            ss.close();
            return "Data saved succesffully";
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
      public String updatePayment(Payment payment){
        try{
            Session ss= HibernateUtil.getSessionFactory().openSession();
            Transaction tr= ss.beginTransaction();
            ss.update(payment);
            tr.commit();
            ss.close();
            return "Data updated succesffully";
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
      }
       public String deletePayment(Payment payment){
        try{
            Session ss= HibernateUtil.getSessionFactory().openSession();
            Transaction tr= ss.beginTransaction();
            ss.delete(payment);
            tr.commit();
            ss.close();
            return "Data deleted succesffully";
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
}
         public List<Payment> retrieveAll(){
             Session ss= HibernateUtil.getSessionFactory().openSession();
             List<Payment> paymentList= ss.createQuery("select  ord from payment ord").list();
             ss.close();
             return paymentList;
          }
         public Payment retrieveById(Payment payment){
        Session ss= HibernateUtil.getSessionFactory().openSession();
        Payment payments = (Payment)ss.get(Payment.class, payment.getPaymentId());
        ss.close();
        return payments;
    }  
}
