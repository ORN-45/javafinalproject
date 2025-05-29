package service.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import model.Document; // Server-side model.Document

public interface DocumentService extends Remote {
    String saveDocument(Document document) throws RemoteException;
    String updateDocument(Document document) throws RemoteException;
    String deleteDocument(Document document) throws RemoteException;
    List<Document> getDocumentsByCaseId(int caseId) throws RemoteException;
    Document getDocumentById(int documentId) throws RemoteException;

    // New methods for Document Panel Functionality
    List<Document> findDocumentsByTitle(String title) throws RemoteException;
    List<Document> findDocumentsByType(String documentType) throws RemoteException;
    List<Document> getAllDocuments() throws RemoteException; // To retrieve all documents
    byte[] getDocumentContent(int documentId) throws RemoteException;
    List<String> getAvailableDocumentTypes() throws RemoteException;
}
