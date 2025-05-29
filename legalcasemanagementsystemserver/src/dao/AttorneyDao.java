/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
import model.Attorney;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author orny
 */
public class AttorneyDao {
    public String registerAttorney(Attorney attorney){
        try{
            Session ss= HibernateUtil.getSessionFactory().openSession();
            Transaction tr= ss.beginTransaction();
            ss.save(attorney);
            tr.commit();
            ss.close();
            return "Data saved succesffully";
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
      public String updateAttorney(Attorney attorney){
        try{
            Session ss= HibernateUtil.getSessionFactory().openSession();
            Transaction tr= ss.beginTransaction();
            ss.update(attorney);
            tr.commit();
            ss.close();
            return "Data updated succesffully";
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
      }
       public String deleteAttorney(Attorney attorney){
        try{
            Session ss= HibernateUtil.getSessionFactory().openSession();
            Transaction tr= ss.beginTransaction();
            ss.delete(attorney);
            tr.commit();
            ss.close();
            return "Data deleted succesffully";
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
}
         public List<Attorney> retrieveAll(){
             Session ss= HibernateUtil.getSessionFactory().openSession();
             List<Attorney> attorneyList= ss.createQuery("select  ord from Attorney ord").list(); // Corrected HQL
             ss.close();
             return attorneyList;
          }
         public Attorney retrieveById(Attorney attorney){
        Session ss= HibernateUtil.getSessionFactory().openSession();
        Attorney attorneys = (Attorney)ss.get(Attorney.class, attorney.getId()); // Corrected to use getId()
        ss.close();
        return attorneys;
    }  
}
