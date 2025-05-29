/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
import model.Case;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author orny
 */
public class CaseDao {
    public String registerCase(Case cases){
        try{
            Session ss= HibernateUtil.getSessionFactory().openSession();
            Transaction tr= ss.beginTransaction();
            ss.save(cases);
            tr.commit();
            ss.close();
            return "Data saved succesffully";
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
      public String updateCase(Case cases){
        try{
            Session ss= HibernateUtil.getSessionFactory().openSession();
            Transaction tr= ss.beginTransaction();
            ss.update(cases);
            tr.commit();
            ss.close();
            return "Data updated succesffully";
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
      }
       public String deleteCase(Case cases){
        try{
            Session ss= HibernateUtil.getSessionFactory().openSession();
            Transaction tr= ss.beginTransaction();
            ss.delete(cases);
            tr.commit();
            ss.close();
            return "Data deleted succesffully";
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
}
         public List<Case> retrieveAll(){
             Session ss= HibernateUtil.getSessionFactory().openSession();
             List<Case> casesList= ss.createQuery("select  cas from cases cas").list();
             ss.close();
             return casesList;
          }
         public Case retrieveById(Case cases){
        Session ss= HibernateUtil.getSessionFactory().openSession();
        Case casess = (Case)ss.get(Case.class, cases.getId());
        ss.close();
        return casess;
    } 
         public List<Case> getCasesByStatus(String status) {
    Session session = HibernateUtil.getSessionFactory().openSession();
    List<Case> cases = session.createQuery("FROM Case c WHERE c.status = :status")
        .setParameter("status", status)
        .list();
    session.close();
    return cases;
}
public List<Case> findCasesByStatus(String status) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Case> cases = session.createQuery("FROM Case c WHERE c.status = :status")
            .setParameter("status", status)
            .list();
        session.close();
        return cases;
    }
}
