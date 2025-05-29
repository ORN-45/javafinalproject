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
             List<Document> documentList= ss.createQuery("select  doc from Document doc").list(); // Corrected HQL
             ss.close();
             return documentList;
          }
         public Document retrieveById(Document document){
        Session ss= HibernateUtil.getSessionFactory().openSession();
        Document documents = (Document)ss.get(Document.class, document.getId()); // Corrected to use getId()
        ss.close();
        return documents;
    }  

    public List<Document> getDocumentsByCaseId(int caseId) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            String hql = "FROM Document d WHERE d.associatedCase.id = :caseId";
            org.hibernate.query.Query<Document> query = session.createQuery(hql, Document.class);
            query.setParameter("caseId", caseId);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Or an empty list, or rethrow a custom exception
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public List<Document> findByTitle(String title) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            String hql = "FROM Document d WHERE lower(d.title) LIKE lower(:title)";
            org.hibernate.query.Query<Document> query = session.createQuery(hql, Document.class);
            query.setParameter("title", "%" + title.toLowerCase() + "%");
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public List<Document> findDocumentsByType(String documentType) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            String hql = "FROM Document d WHERE d.documentType = :documentType";
            org.hibernate.query.Query<Document> query = session.createQuery(hql, Document.class);
            query.setParameter("documentType", documentType);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    // retrieveAll() already serves as findAll(). No need to duplicate.
    // public List<Document> findAll() { return retrieveAll(); }


    public byte[] getDocumentContent(int documentId) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Document doc = session.get(Document.class, documentId);
            if (doc != null) {
                // Initialize the lazy-loaded field if not already fetched
                // This might not be strictly necessary if your session is open
                // but good practice if the content is fetched and session might close.
                // org.hibernate.Hibernate.initialize(doc.getFileContent()); 
                return doc.getFileContent();
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public List<String> getDistinctDocumentTypes() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            String hql = "SELECT DISTINCT d.documentType FROM Document d ORDER BY d.documentType";
            org.hibernate.query.Query<String> query = session.createQuery(hql, String.class);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
