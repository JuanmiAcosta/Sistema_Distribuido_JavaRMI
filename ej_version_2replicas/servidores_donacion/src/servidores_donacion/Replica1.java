package servidores_donacion;

import donacion.Donacion;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;

/**
 * 
 * @author juanmi
 */
public class Replica1 {
    public static void main(String[] args) {
        
        if (System.getSecurityManager() == null)
            System.setSecurityManager(new SecurityManager());
        
        try {
            String servidor = "ServerNA";
            String replica = "ServerEUW";
            Donacion donacion_r = new Donacion(servidor,replica);
            
            Naming.rebind(servidor, donacion_r);

            
            System.out.println("Servidor " + servidor + " listo para servir.");
        } catch (RemoteException | MalformedURLException e) {
            System.out.println("Exception: " + e.getMessage());
        }

    }
}