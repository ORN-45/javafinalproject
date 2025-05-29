package service.remote.impl;

import dao.DocumentDao;
import model.Document;
import service.remote.DocumentService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collections;
import java.util.List;

public class DocumentServiceImpl extends UnicastRemoteObject implements DocumentService {

    private final DocumentDao documentDao;

    public DocumentServiceImpl() throws RemoteException {
        super();
        this.documentDao = new DocumentDao();
    }

    @Override
    public String saveDocument(Document document) throws RemoteException {
        return documentDao.registerDocument(document);
    }

    @Override
    public String updateDocument(Document document) throws RemoteException {
        return documentDao.updateDocument(document);
    }

    @Override
    public String deleteDocument(Document document) throws RemoteException {
        return documentDao.deleteDocument(document);
    }

    @Override
    public List<Document> getDocumentsByCaseId(int caseId) throws RemoteException {
        try {
            // DocumentDao.getDocumentsByCaseId(int caseId) was added previously.
            return documentDao.getDocumentsByCaseId(caseId);
        } catch (Exception e) {
            System.err.println("DocumentServiceImpl: Error in getDocumentsByCaseId: " + e.getMessage());
            e.printStackTrace();
            throw new RemoteException("Server error while fetching documents by case ID.", e);
        }
    }

    @Override
    public Document getDocumentById(int documentId) throws RemoteException {
        try {
            Document tempDocument = new Document();
            tempDocument.setId(documentId); // Sets the primary key 'id'
            return documentDao.retrieveById(tempDocument);
        } catch (Exception e) {
            System.err.println("DocumentServiceImpl: Error in getDocumentById: " + e.getMessage());
            e.printStackTrace();
            throw new RemoteException("Server error while fetching document by ID.", e);
        }
    }

    // Implementation of new methods for Document Panel Functionality
    @Override
    public List<Document> findDocumentsByTitle(String title) throws RemoteException {
        try {
            return documentDao.findByTitle(title);
        } catch (Exception e) {
            System.err.println("DocumentServiceImpl: Error in findDocumentsByTitle: " + e.getMessage());
            e.printStackTrace();
            throw new RemoteException("Server error while finding documents by title.", e);
        }
    }

    @Override
    public List<Document> findDocumentsByType(String documentType) throws RemoteException {
        try {
            return documentDao.findDocumentsByType(documentType);
        } catch (Exception e) {
            System.err.println("DocumentServiceImpl: Error in findDocumentsByType: " + e.getMessage());
            e.printStackTrace();
            throw new RemoteException("Server error while finding documents by type.", e);
        }
    }

    @Override
    public List<Document> getAllDocuments() throws RemoteException {
        try {
            return documentDao.retrieveAll(); // retrieveAll serves as findAll
        } catch (Exception e) {
            System.err.println("DocumentServiceImpl: Error in getAllDocuments: " + e.getMessage());
            e.printStackTrace();
            throw new RemoteException("Server error while fetching all documents.", e);
        }
    }

    @Override
    public byte[] getDocumentContent(int documentId) throws RemoteException {
        try {
            return documentDao.getDocumentContent(documentId);
        } catch (Exception e) {
            System.err.println("DocumentServiceImpl: Error in getDocumentContent: " + e.getMessage());
            e.printStackTrace();
            throw new RemoteException("Server error while fetching document content.", e);
        }
    }

    @Override
    public List<String> getAvailableDocumentTypes() throws RemoteException {
        try {
            return documentDao.getDistinctDocumentTypes();
        } catch (Exception e) {
            System.err.println("DocumentServiceImpl: Error in getAvailableDocumentTypes: " + e.getMessage());
            e.printStackTrace();
            throw new RemoteException("Server error while fetching available document types.", e);
        }
    }
}
