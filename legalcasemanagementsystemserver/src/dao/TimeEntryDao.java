/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
import model.TimeEntry;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author orny
 */
public class TimeEntryDao {
    public String registerTimeEntry(TimeEntry timeentry){
        try{
            Session ss= HibernateUtil.getSessionFactory().openSession();
            Transaction tr= ss.beginTransaction();
            ss.save(timeentry);
            tr.commit();
            ss.close();
            return "Data saved succesffully";
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
      public String updateTimeEntry(TimeEntry timeentry){
        try{
            Session ss= HibernateUtil.getSessionFactory().openSession();
            Transaction tr= ss.beginTransaction();
            ss.update(timeentry);
            tr.commit();
            ss.close();
            return "Data updated succesffully";
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
      }
       public String deleteTimeEntry(TimeEntry timeentry){
        try{
            Session ss= HibernateUtil.getSessionFactory().openSession();
            Transaction tr= ss.beginTransaction();
            ss.delete(timeentry);
            tr.commit();
            ss.close();
            return "Data deleted succesffully";
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
}
         public List<TimeEntry> retrieveAll(){
             Session ss= HibernateUtil.getSessionFactory().openSession();
             List<TimeEntry> timeentryList= ss.createQuery("select  ord from TimeEntry ord").list(); // Corrected HQL
             ss.close();
             return timeentryList;
          }
         public TimeEntry retrieveById(TimeEntry timeentry){
        Session ss= HibernateUtil.getSessionFactory().openSession();
        TimeEntry timeentrys = (TimeEntry)ss.get(TimeEntry.class, timeentry.getId());
        ss.close();
        return timeentrys;
    }  

    public List<model.TimeEntry> getTimeEntriesByCaseId(int caseId) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            String hql = "FROM TimeEntry te WHERE te.associatedCase.id = :caseId";
            org.hibernate.query.Query<model.TimeEntry> query = session.createQuery(hql, model.TimeEntry.class);
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

    public List<model.TimeEntry> getTimeEntriesByAttorneyId(int attorneyId) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            String hql = "FROM TimeEntry te WHERE te.attorney.id = :attorneyId";
            org.hibernate.query.Query<model.TimeEntry> query = session.createQuery(hql, model.TimeEntry.class);
            query.setParameter("attorneyId", attorneyId);
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
