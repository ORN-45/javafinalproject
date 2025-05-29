package controller;

import dao.DocumentDao;
import model.Document;

import java.util.List;

/**
 * Controller for document-related operations using current DocumentDao methods.
 */
public class DocumentController {
    private DocumentDao documentDao;

    public DocumentController() {
        this.documentDao = new DocumentDao();
    }

    /**
     * Get all documents
     * @return List of all documents
     */
    public List<Document> getAllDocuments() {
        return documentDao.retrieveAll();
    }

    /**
     * Get a document by ID
     * @param id Document ID
     * @return Document object or null if not found
     */
    public Document getDocumentById(int id) {
        Document temp = new Document();
        temp.setCaseId(id);
        return documentDao.retrieveById(temp);
    }

    /**
     * Create a new document
     * @param document Document object to register
     * @return Success/failure message
     */
    public String createDocument(Document document) {
        if (document == null) {
            return "Document object is null";
        }
        return documentDao.registerDocument(document);
    }

    /**
     * Update an existing document
     * @param document Document object to update
     * @return Success/failure message
     */
    public String updateDocument(Document document) {
        if (document == null) {
            return "Document object is null";
        }
        return documentDao.updateDocument(document);
    }

    /**
     * Delete a document
     * @param document Document object to delete
     * @return Success/failure message
     */
    public String deleteDocument(Document document) {
        if (document == null) {
            return "Document object is null";
        }
        return documentDao.deleteDocument(document);
    }
}
