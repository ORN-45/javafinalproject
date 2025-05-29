package controller;

// Client-side DTO
import model.Document; 
// Server-side Model & related
import legalcasemanagementsystemserver.model.Document_SERVER; 
import legalcasemanagementsystemserver.model.Case_SERVER; // For setting associated case in server model
import service.remote.DocumentService; 

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.rmi.RemoteException; 

/**
 * Controller for document-related operations using RMI with DTO mapping.
 */
public class DocumentController {
    private DocumentService documentService; // RMI service stub

    public DocumentController() {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            this.documentService = (DocumentService) registry.lookup("DocumentService");
        } catch (Exception e) {
            System.err.println("DocumentController: Error looking up DocumentService: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // --- Mapping Helper Methods ---
    private Document_SERVER mapDocumentDtoToServerModel(Document docDto) {
        if (docDto == null) return null;
        Document_SERVER serverDoc = new Document_SERVER();
        serverDoc.setId(docDto.getId());
        serverDoc.setDocumentId(docDto.getDocumentId());
        serverDoc.setTitle(docDto.getTitle());
        serverDoc.setDescription(docDto.getDescription());
        serverDoc.setDocumentType(docDto.getDocumentType());
        serverDoc.setFilePath(docDto.getFilePath()); // May be null or empty if not used
        serverDoc.setDateAdded(docDto.getDateAdded());
        serverDoc.setDocumentDate(docDto.getDocumentDate());
        serverDoc.setCreatedBy(docDto.getCreatedBy());
        serverDoc.setStatus(docDto.getStatus());
        
        if (docDto.getCaseId() > 0) {
            Case_SERVER caseRef = new Case_SERVER();
            caseRef.setId(docDto.getCaseId());
            serverDoc.setAssociatedCase(caseRef);
        }
        
        serverDoc.setFileName(docDto.getFileName());
        serverDoc.setFileType(docDto.getFileType());
        serverDoc.setFileContent(docDto.getFileContent()); // Content is mapped for save/update
        return serverDoc;
    }

    private Document mapServerModelToDocumentDto(Document_SERVER serverDoc, boolean includeContent) {
        if (serverDoc == null) return null;
        Document docDto = new Document();
        docDto.setId(serverDoc.getId());
        docDto.setDocumentId(serverDoc.getDocumentId());
        docDto.setTitle(serverDoc.getTitle());
        docDto.setDescription(serverDoc.getDescription());
        docDto.setDocumentType(serverDoc.getDocumentType());
        docDto.setFilePath(serverDoc.getFilePath());
        docDto.setDateAdded(serverDoc.getDateAdded());
        docDto.setDocumentDate(serverDoc.getDocumentDate());
        docDto.setCreatedBy(serverDoc.getCreatedBy());
        docDto.setStatus(serverDoc.getStatus());
        if (serverDoc.getAssociatedCase() != null) {
            docDto.setCaseId(serverDoc.getAssociatedCase().getId());
        }
        
        docDto.setFileName(serverDoc.getFileName());
        docDto.setFileType(serverDoc.getFileType());
        if (includeContent) {
            docDto.setFileContent(serverDoc.getFileContent()); // Only map if requested
        }
        return docDto;
    }

    // --- Existing CRUD and Getter Methods (Updated for DTOs) ---
    public Document getDocumentById(int id) throws RemoteException {
        if (this.documentService == null) throw new RemoteException("DocumentService not initialized.");
        Document_SERVER serverDoc = documentService.getDocumentById(id);
        return mapServerModelToDocumentDto(serverDoc, false); // Content not fetched by default here
    }

    public String saveDocument(Document docDto) throws RemoteException {
        if (this.documentService == null) throw new RemoteException("DocumentService not initialized.");
        if (docDto == null) throw new IllegalArgumentException("Document DTO cannot be null");
        Document_SERVER serverDoc = mapDocumentDtoToServerModel(docDto); // Maps content
        return documentService.saveDocument(serverDoc);
    }

    public String updateDocument(Document docDto) throws RemoteException {
        if (this.documentService == null) throw new RemoteException("DocumentService not initialized.");
        if (docDto == null) throw new IllegalArgumentException("Document DTO cannot be null");
        Document_SERVER serverDoc = mapDocumentDtoToServerModel(docDto); // Maps content
        return documentService.updateDocument(serverDoc);
    }

    public String deleteDocument(Document docDto) throws RemoteException {
        if (this.documentService == null) throw new RemoteException("DocumentService not initialized.");
        if (docDto == null) throw new IllegalArgumentException("Document DTO cannot be null");
        Document_SERVER serverDoc = mapDocumentDtoToServerModel(docDto);
        return documentService.deleteDocument(serverDoc);
    }
    
    public List<Document> getDocumentsByCaseId(int caseId) throws RemoteException {
        if (this.documentService == null) throw new RemoteException("DocumentService not initialized.");
        List<Document_SERVER> serverDocs = documentService.getDocumentsByCaseId(caseId);
        if (serverDocs == null) return new ArrayList<>();
        return serverDocs.stream()
                         .map(serverDoc -> mapServerModelToDocumentDto(serverDoc, false)) // No content in lists
                         .collect(Collectors.toList());
    }

    // --- Methods for New DocumentService Operations ---
    public List<Document> findDocumentsByTitle(String title) throws RemoteException {
        if (this.documentService == null) throw new RemoteException("DocumentService not initialized.");
        List<Document_SERVER> serverDocs = documentService.findDocumentsByTitle(title);
        if (serverDocs == null) return new ArrayList<>();
        return serverDocs.stream()
                         .map(serverDoc -> mapServerModelToDocumentDto(serverDoc, false)) // No content
                         .collect(Collectors.toList());
    }

    public List<Document> findDocumentsByType(String documentType) throws RemoteException {
        if (this.documentService == null) throw new RemoteException("DocumentService not initialized.");
        List<Document_SERVER> serverDocs = documentService.findDocumentsByType(documentType);
        if (serverDocs == null) return new ArrayList<>();
        return serverDocs.stream()
                         .map(serverDoc -> mapServerModelToDocumentDto(serverDoc, false)) // No content
                         .collect(Collectors.toList());
    }

    public List<Document> getAllDocuments() throws RemoteException {
        if (this.documentService == null) throw new RemoteException("DocumentService not initialized.");
        List<Document_SERVER> serverDocs = documentService.getAllDocuments();
        if (serverDocs == null) return new ArrayList<>();
        return serverDocs.stream()
                         .map(serverDoc -> mapServerModelToDocumentDto(serverDoc, false)) // No content
                         .collect(Collectors.toList());
    }

    public byte[] getDocumentContent(int documentId) throws RemoteException {
        if (this.documentService == null) throw new RemoteException("DocumentService not initialized.");
        return documentService.getDocumentContent(documentId); // Directly returns byte[]
    }

    public String[] getDocumentTypes() throws RemoteException {
        if (this.documentService == null) throw new RemoteException("DocumentService not initialized.");
        List<String> typesList = documentService.getAvailableDocumentTypes();
        if (typesList == null) return new String[0];
        return typesList.toArray(new String[0]);
    }
}
