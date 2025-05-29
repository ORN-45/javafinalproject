package controller;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import service.implementation.AttorneyImpl;
import service.implementation.CaseImpl;
import service.implementation.ClientImpl;
import service.implementation.DocumentImpl;
import service.implementation.EventImpl;
import service.implementation.InvoiceImpl;
import service.implementation.PaymentImpl;
import service.implementation.TimeEntryImpl;
import service.implementation.UserImpl;

/**
 *
 * @author Gisabo
 */
public class Server {
      private AttorneyImpl attorneyImp;
     private CaseImpl caseImpl;
     private ClientImpl clientImpl;
     private DocumentImpl documentImpl;
     private EventImpl eventImpl;
     private InvoiceImpl invoiceImpl;
      private PaymentImpl paymentImpl;
       private TimeEntryImpl TimeEntryImpl;
        private UserImpl userImpl;
     
     
     
     public Server() throws RemoteException{
          this.attorneyImp= new AttorneyImpl();
          this.caseImpl= new CaseImpl();
          this.clientImpl= new ClientImpl();
          this.documentImpl= new DocumentImpl();
          this.eventImpl= new EventImpl();
          this.invoiceImpl= new InvoiceImpl();
          this.paymentImpl=new PaymentImpl();
          this.TimeEntryImpl=new TimeEntryImpl();
          this.userImpl=new UserImpl();
          
     
        
    }
    
    public static void main(String[] args) {
        try{
        System.setProperty("java.rmi.server.hostname", "127.0.0.1");
        
            Registry registry= LocateRegistry.createRegistry(6000);
            registry.rebind("attorney", new Server().attorneyImp);
            registry.rebind("case", new Server().caseImpl);
            registry.rebind("client", new Server().clientImpl);
            registry.rebind("document", new Server().documentImpl);
            registry.rebind("event", new Server().eventImpl);
            
            
            registry.rebind("invoice", new Server().eventImpl);
            registry.rebind("payment", new Server().paymentImpl);
            registry.rebind("TimeEntry", new Server().TimeEntryImpl);
                        registry.rebind("user", new Server().userImpl);

            System.out.println("Server is running on 6000");
            
        }catch(Exception ex){
            ex.printStackTrace();
        }

}
}