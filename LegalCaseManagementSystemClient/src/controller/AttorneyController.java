package controller;

// Client-side DTO
import model.Attorney; 
// Server-side Model (needs to be available in client's classpath for RMI, or use a shared library)
import legalcasemanagementsystemserver.model.Attorney_SERVER; // Using an alias for server model
import service.remote.AttorneyService; 

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.ArrayList;
import java.rmi.RemoteException; 

/**
 * Controller for attorney-related operations using RMI with DTO mapping.
 */
public class AttorneyController {
    private AttorneyService attorneyService; // RMI service stub

    public AttorneyController() {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            this.attorneyService = (AttorneyService) registry.lookup("AttorneyService");
        } catch (Exception e) {
            System.err.println("AttorneyController: Error looking up AttorneyService: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // --- Mapping Helper Methods ---
    private Attorney_SERVER mapAttorneyDtoToServerModel(Attorney attorneyDto) {
        if (attorneyDto == null) return null;
        Attorney_SERVER serverModel = new Attorney_SERVER();
        serverModel.setId(attorneyDto.getId());
        serverModel.setAttorneyId(attorneyDto.getAttorneyId());
        serverModel.setFirstName(attorneyDto.getFirstName());
        serverModel.setLastName(attorneyDto.getLastName());
        serverModel.setEmail(attorneyDto.getEmail());
        serverModel.setPhone(attorneyDto.getPhone());
        serverModel.setSpecialization(attorneyDto.getSpecialization());
        serverModel.setBarNumber(attorneyDto.getBarNumber());
        serverModel.setHourlyRate(attorneyDto.getHourlyRate());
        return serverModel;
    }

    private Attorney mapServerModelToAttorneyDto(Attorney_SERVER serverModel) {
        if (serverModel == null) return null;
        Attorney attorneyDto = new Attorney();
        attorneyDto.setId(serverModel.getId());
        attorneyDto.setAttorneyId(serverModel.getAttorneyId());
        attorneyDto.setFirstName(serverModel.getFirstName());
        attorneyDto.setLastName(serverModel.getLastName());
        attorneyDto.setEmail(serverModel.getEmail());
        attorneyDto.setPhone(serverModel.getPhone());
        attorneyDto.setSpecialization(serverModel.getSpecialization());
        attorneyDto.setBarNumber(serverModel.getBarNumber());
        attorneyDto.setHourlyRate(serverModel.getHourlyRate());
        return attorneyDto;
    }

    // Get all attorneys, returning a list of attorney DTOs
    public List<Attorney> getAllAttorneys() throws RemoteException {
        if (this.attorneyService == null) throw new RemoteException("AttorneyService not initialized.");
        List<Attorney_SERVER> serverAttorneys = attorneyService.getAllAttorneys();
        List<Attorney> attorneyDtos = new ArrayList<>();
        if (serverAttorneys != null) {
            for (Attorney_SERVER serverAttorney : serverAttorneys) {
                attorneyDtos.add(mapServerModelToAttorneyDto(serverAttorney));
            }
        }
        return attorneyDtos;
    }

    // Get attorney by ID (primary key, int), returning an attorney DTO
    public Attorney getAttorneyById(int attorneyId) throws RemoteException {
        if (this.attorneyService == null) throw new RemoteException("AttorneyService not initialized.");
        Attorney_SERVER serverModel = attorneyService.getAttorneyById(attorneyId);
        return mapServerModelToAttorneyDto(serverModel);
    }

    // Create new attorney
    public String createAttorney(Attorney attorneyDto) throws RemoteException {
        if (this.attorneyService == null) throw new RemoteException("AttorneyService not initialized.");
        if (attorneyDto == null) throw new IllegalArgumentException("Attorney DTO cannot be null.");
        Attorney_SERVER serverModel = mapAttorneyDtoToServerModel(attorneyDto);
        return attorneyService.registerAttorney(serverModel);
    }

    // Update attorney
    public String updateAttorney(Attorney attorneyDto) throws RemoteException {
        if (this.attorneyService == null) throw new RemoteException("AttorneyService not initialized.");
        if (attorneyDto == null) throw new IllegalArgumentException("Attorney DTO cannot be null.");
        Attorney_SERVER serverModel = mapAttorneyDtoToServerModel(attorneyDto);
        return attorneyService.updateAttorney(serverModel);
    }

    // Delete attorney
    public String deleteAttorney(Attorney attorneyDto) throws RemoteException {
        if (this.attorneyService == null) throw new RemoteException("AttorneyService not initialized.");
        if (attorneyDto == null) throw new IllegalArgumentException("Attorney DTO to delete cannot be null.");
        Attorney_SERVER serverModel = mapAttorneyDtoToServerModel(attorneyDto);
        return attorneyService.deleteAttorney(serverModel);
    }

    // Methods like findAttorneysByName, findAttorneysBySpecialization, getAttorneyCases
    // are not part of the AttorneyService interface and thus are not implemented here.
    // Local filtering for these would be handled in the UI panel.
}
