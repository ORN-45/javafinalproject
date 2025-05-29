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
             List<Case> casesList= ss.createQuery("FROM Case cas").list(); // Corrected HQL
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

    public model.Case findByCaseNumber(String caseNumber) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            String hql = "FROM Case c WHERE c.caseNumber = :caseNumber";
            org.hibernate.query.Query<model.Case> query = session.createQuery(hql, model.Case.class);
            query.setParameter("caseNumber", caseNumber);
            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            // Consider logging the error or throwing a custom DAO exception
            return null;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public java.util.List<model.Case> findByTitle(String title) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            String hql = "FROM Case c WHERE lower(c.title) LIKE lower(:title)";
            org.hibernate.query.Query<model.Case> query = session.createQuery(hql, model.Case.class);
            query.setParameter("title", "%" + title + "%");
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            // Consider logging the error or throwing a custom DAO exception
            return new java.util.ArrayList<>(); // Return empty list on error
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }
}
