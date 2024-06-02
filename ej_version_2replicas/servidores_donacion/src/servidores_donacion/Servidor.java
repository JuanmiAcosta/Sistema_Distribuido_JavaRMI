package servidores_donacion;

import donacion.Donacion;
import idonacion.Idonacion;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * 
 * @author juanmi
 */
public class Servidor {
    public static void main(String[] args) {
        
        if (System.getSecurityManager() == null)
            System.setSecurityManager(new SecurityManager());

        try {
            String servidor = "ServerEUW";
            String replica = "ServerNA";
            
            System.setProperty("java.rmi.server.hostname","localhost");
            Registry reg=LocateRegistry.createRegistry(1099);
            Donacion donacion = new Donacion(servidor,replica);
            Naming.rebind(servidor, donacion);
            
            System.out.println("Servidor " + servidor + " listo para servir.");
        } catch (RemoteException | MalformedURLException e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }
}