package controller;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import service.remote.impl.AttorneyServiceImpl;
import service.remote.impl.CaseServiceImpl;
import service.remote.impl.ClientServiceImpl;
import service.remote.impl.DocumentServiceImpl;
import service.remote.impl.EventServiceImpl;
import service.remote.impl.InvoiceServiceImpl;
import service.remote.impl.TimeEntryServiceImpl;
import service.remote.impl.UserServiceImpl;

/**
 *
 * @author Gisabo
 */
public class Server {
     // No need for instance fields for service implementations if only used in main.
     // The constructor public Server() throws RemoteException { ... } is also not needed
     // if we are not creating instances of Server to hold service impls.
    
    public static void main(String[] args) {
        try {
            // System.setProperty("java.rmi.server.hostname", "127.0.0.1"); // Keep if specific IP needed, often not for localhost
            int port = 1099; // Standard RMI port
            Registry registry = LocateRegistry.createRegistry(port);
            System.out.println("RMI Registry started on port " + port);

            // Instantiate and bind new remote services
            registry.rebind("CaseService", new CaseServiceImpl());
            System.out.println("CaseService bound.");

            registry.rebind("ClientService", new ClientServiceImpl());
            System.out.println("ClientService bound.");

            registry.rebind("UserService", new UserServiceImpl());
            System.out.println("UserService bound.");

            registry.rebind("AttorneyService", new AttorneyServiceImpl());
            System.out.println("AttorneyService bound.");

            registry.rebind("DocumentService", new DocumentServiceImpl());
            System.out.println("DocumentService bound.");

            registry.rebind("EventService", new EventServiceImpl());
            System.out.println("EventService bound.");
            
            registry.rebind("InvoiceService", new InvoiceServiceImpl());
            System.out.println("InvoiceService bound.");

            registry.rebind("TimeEntryService", new TimeEntryServiceImpl());
            System.out.println("TimeEntryService bound.");

            System.out.println("All services bound. Server is ready.");
            
        } catch (RemoteException e) {
            System.err.println("Server RMI RemoteException: " + e.toString());
            e.printStackTrace();
        } catch (Exception e) { // Catch other potential exceptions during setup
            System.err.println("Server setup exception: " + e.toString());
            e.printStackTrace();
        }
    }
}