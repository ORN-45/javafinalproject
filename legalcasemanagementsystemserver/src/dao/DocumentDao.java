/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
import model.Document;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author orny
 */
public class DocumentDao {
    public String registerDocument(Document document){
        try{
            Session ss= HibernateUtil.getSessionFactory().openSession();
            Transaction tr= ss.beginTransaction();
            ss.save(document);
            tr.commit();
            ss.close();
            return "Data saved succesffully";
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
      public String updateDocument(Document document){
        try{
            Session ss= HibernateUtil.getSessionFactory().openSession();
            Transaction tr= ss.beginTransaction();
            ss.update(document);
            tr.commit();
            ss.close();
            return "Data updated succesffully";
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
      }
       public String deleteDocument(Document document){
        try{
            Session ss= HibernateUtil.getSessionFactory().openSession();
            Transaction tr= ss.beginTransaction();
            ss.delete(document);
            tr.commit();
            ss.close();
            return "Data deleted succesffully";
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
}
         public List<Document> retrieveAll(){
             Session ss= HibernateUtil.getSessionFactory().openSession();
             List<Document> documentList= ss.createQuery("select  doc from document doc").list();
             ss.close();
             return documentList;
          }
         public Document retrieveById(Document document){
        Session ss= HibernateUtil.getSessionFactory().openSession();
        Document documents = (Document)ss.get(Document.class, document.getDocumentId());
        ss.close();
        return documents;
    }  
}
