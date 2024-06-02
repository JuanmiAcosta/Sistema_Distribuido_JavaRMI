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
public class Replica1 {
    public static void main(String[] args) {
        
        if (System.getSecurityManager() == null)
            System.setSecurityManager(new SecurityManager());
        
        try {
            Set<String> conjuntoIds = new LinkedHashSet<>();
            String servidor = "ServerNA";
            conjuntoIds.add(servidor);
            conjuntoIds.add("ServerSA");
            conjuntoIds.add("ServerEUW");
            
            Donacion donacion_r1 = new Donacion(conjuntoIds);
            
            Naming.rebind(servidor, donacion_r1);

            
            System.out.println("Servidor " + servidor + " listo para servir.");
        } catch (RemoteException | MalformedURLException e) {
            System.out.println("Exception: " + e.getMessage());
        }

    }
}