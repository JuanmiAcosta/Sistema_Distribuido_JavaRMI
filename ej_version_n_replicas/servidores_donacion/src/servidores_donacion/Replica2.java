package servidores_donacion;

import donacion.Donacion;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 
 * @author juanmi
 */
public class Replica2 {
    public static void main(String[] args) {
        
        if (System.getSecurityManager() == null)
            System.setSecurityManager(new SecurityManager());
        
        try {
            Set<String> conjuntoIds = new LinkedHashSet<>();
            String servidor = "ServerSA";
            conjuntoIds.add(servidor);
            conjuntoIds.add("ServerEUW");
            conjuntoIds.add("ServerNA");
            
            Donacion donacion_r2 = new Donacion(conjuntoIds);
            
            Naming.rebind(servidor, donacion_r2);

            
            System.out.println("Servidor " + servidor + " listo para servir.");
        } catch (RemoteException | MalformedURLException e) {
            System.out.println("Exception: " + e.getMessage());
        }

    }
}