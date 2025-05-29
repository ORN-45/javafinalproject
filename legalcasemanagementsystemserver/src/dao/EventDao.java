/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
import model.Event;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author orny
 */
public class EventDao {
    public String registerEvent(Event event){
        try{
            Session ss= HibernateUtil.getSessionFactory().openSession();
            Transaction tr= ss.beginTransaction();
            ss.save(event);
            tr.commit();
            ss.close();
            return "Data saved succesffully";
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
      public String updateEvent(Event event){
        try{
            Session ss= HibernateUtil.getSessionFactory().openSession();
            Transaction tr= ss.beginTransaction();
            ss.update(event);
            tr.commit();
            ss.close();
            return "Data updated succesffully";
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
      }
       public String deleteEvent(Event event){
        try{
            Session ss= HibernateUtil.getSessionFactory().openSession();
            Transaction tr= ss.beginTransaction();
            ss.delete(event);
            tr.commit();
            ss.close();
            return "Data deleted succesffully";
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
}
         public List<Event> retrieveAll(){
             Session ss= HibernateUtil.getSessionFactory().openSession();
             List<Event> eventList= ss.createQuery("select  eve from event eve").list();
             ss.close();
             return eventList;
          }
         public Event retrieveById(Event event){
        Session ss= HibernateUtil.getSessionFactory().openSession();
        Event events = (Event)ss.get(Event.class, event.getEventId());
        ss.close();
        return events;
    }  
}
