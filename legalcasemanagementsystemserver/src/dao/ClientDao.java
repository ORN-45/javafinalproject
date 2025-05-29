/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
import model.Client;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author orny
 */
public class ClientDao {
    public String registerClient(Client client){
        try{
            Session ss= HibernateUtil.getSessionFactory().openSession();
            Transaction tr= ss.beginTransaction();
            ss.save(client);
            tr.commit();
            ss.close();
            return "Data saved succesffully";
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
      public String updateClient(Client client){
        try{
            Session ss= HibernateUtil.getSessionFactory().openSession();
            Transaction tr= ss.beginTransaction();
            ss.update(client);
            tr.commit();
            ss.close();
            return "Data updated succesffully";
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
      }
       public String deleteClient(Client client){
        try{
            Session ss= HibernateUtil.getSessionFactory().openSession();
            Transaction tr= ss.beginTransaction();
            ss.delete(client);
            tr.commit();
            ss.close();
            return "Data deleted succesffully";
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
}
         public List<Client> retrieveAll(){
             Session ss= HibernateUtil.getSessionFactory().openSession();
             List<Client> clientList= ss.createQuery("select  ord from client ord").list();
             ss.close();
             return clientList;
          }
         public Client retrieveById(Client client){
        Session ss= HibernateUtil.getSessionFactory().openSession();
        Client clients = (Client)ss.get(Client.class, client.getClientId());
        ss.close();
        return clients;
    }  
}
