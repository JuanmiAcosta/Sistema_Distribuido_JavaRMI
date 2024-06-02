package cliente_donacion;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;
import idonacion.Idonacion;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import usuario.Usuario;

public class Cliente {

    public static void main(String[] args) {

        String host;
        String servidor = "ServerEUW";

        // Pedimos como en el ejemplo 3 el host (localhost para las pruebas en local)
        Scanner teclado = new Scanner(System.in);
        System.out.println("Escriba el nombre o IP del servidor: ");
        host = teclado.nextLine();
        // Crea e instala el gestor de seguridad
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        try {
            // Crea el stub para el cliente especificando la dirección del servidor
            Registry mireg = LocateRegistry.getRegistry(host, 1099);
            Idonacion donacion = (Idonacion) mireg.lookup(servidor);

            boolean sesionIniciada = false;
            String nombreUsuario = "";
            String contrasenia = "";
            String servidorActual ="";
            ArrayList<String> nombres = new ArrayList<>();
            Map<String, String> usuario_replica = new HashMap<>();

            while (true) {
                if (!sesionIniciada) {
                    System.out.println("MENU:"+"\n");
                    System.out.println("1. Registrar usuario"+"\n");
                    System.out.println("2. Iniciar sesión"+"\n");
                    System.out.println("3. Salir"+"\n");
                    System.out.print("Seleccione una opción: "+"\n");
                    int opcion = teclado.nextInt();
                    teclado.nextLine(); // Limpiar el buffer de entrada

                    switch (opcion) {
                        case 1:
                            System.out.print("Ingrese nombre de usuario: "+"\n");
                            String nuevoUsuario = teclado.nextLine();
                            System.out.print("Ingrese contraseña: "+"\n");
                            String nuevaContrasenia = teclado.nextLine();
                            nombres.add(nuevoUsuario);
                            donacion.registrar(nuevoUsuario, nuevaContrasenia);
                            System.out.println("Usuario registrado correctamente."+"\n");
                            break;
                        case 2:
                            System.out.print("Ingrese nombre de usuario: "+"\n");
                            nombreUsuario = teclado.nextLine();
                            System.out.print("Ingrese contraseña: "+"\n");
                            contrasenia = teclado.nextLine();
                            // Verificar si el usuario y contraseña son válidos
                            servidorActual = donacion.replicaUsuario (nombreUsuario,contrasenia);
                            if (!(servidorActual.equals(""))) {
                                System.out.println("Inicio de sesión exitoso para el usuario " + nombreUsuario + " en su servidor " + servidorActual +"\n");
                                sesionIniciada = true;
                            } else {
                                System.out.println("Nombre de usuario o contraseña incorrectos."+"\n");
                            }
                            break;
                        case 3:
                            System.out.println("Saliendo del sistema."+"\n");
                            System.exit(0);
                        default:
                            System.out.println("Opción no válida. Por favor, seleccione una opción válida."+"\n");
                            break;
                    }
                } else {
                    System.out.println("MENU:"+"\n");
                    System.out.println("1. Donar"+"\n");
                    System.out.println("2. Consultar total donado al sistema"+"\n");
                    System.out.println("3. Listado de usuarios en sistema\n");
                    System.out.println("4. Comprobar las donaciones realizadas por mí\n");
                    System.out.println("5. Comprobar el podio de donantes\n");
                    System.out.println("6. Obtener listado de donantes en el sistema\n");
                    System.out.println("7. Cerrar sesión"+"\n");
                    System.out.print("Seleccione una opción: "+"\n");
                    int opcion = teclado.nextInt();
                    teclado.nextLine(); // Limpiar el buffer de entrada

                    switch (opcion) {
                        case 1:
                            System.out.print("Ingrese la cantidad a donar: "+"\n");
                            float cantidad = teclado.nextFloat();
                            donacion.donar(nombreUsuario, cantidad);
                            System.out.println("Donación realizada con éxito. "+ nombreUsuario + " donó a su servidor "+ servidorActual+ ": "+ cantidad + " euros.\n");
                            break;
                        case 2:
                            float total_rep = donacion.totalDonadoReplica(nombreUsuario, servidorActual);
                            if(donacion.haDonadoEnSistema(nombreUsuario, servidorActual)){
                                System.out.println("El total donado al sistema asciende a " + donacion.totalDonadoServidor() + " euros."+"\n");
                                System.out.println("En concreto en su servidor "+ servidorActual+ " hay un subtotal de "+ total_rep+"\n");    
                            }else{
                                System.out.println("No se le puede suministrar esa información sin haber donado en primer lugar");
                            }
                            
                            break;
                        case 3:
                            
                            if(donacion.haDonadoEnSistema(nombreUsuario, servidorActual)){
                                System.out.println("El listado de usuarios en su servidor \"" + servidorActual + "\" es el siguiente:\n");
                                for ( Map.Entry<String, Usuario> usuario : donacion.getUsuariosEnReplica(servidorActual).entrySet()){
                                    System.out.println(" - " + usuario.getKey());
                                }

                                System.out.println("El listado de usuarios en el sistema completo es el siguiente:\n");
                                for ( Map.Entry<String, Usuario> usuario : donacion.getUsuariosEnSistema().entrySet()){
                                    System.out.println(" - " + usuario.getKey());
                                }
                            }else{
                                System.out.println("No se le puede suministrar esa información sin haber donado en primer lugar");

                            }
                            
                            break;
                        case 4:
                            Map<String,Usuario> usuarios = donacion.getUsuariosEnReplica(servidorActual);
                            Usuario usu = usuarios.get(nombreUsuario);
                            int num_donaciones = usu.getNumDonaciones();
                            float cantidad_donada = usu.getTotalDonaciones();
                            
                            System.out.println("El número de donaciones que ha realizado es : " +num_donaciones +"\n");
                            System.out.println("El total donado por usted asciende a : " +cantidad_donada +"\n");
                            
                            break;
                        case 5:
                            
                            if(donacion.haDonadoEnSistema(nombreUsuario, servidorActual)){
                                Map<String,Float> podio_replica = donacion.getMayoresDonantesReplica(servidorActual);
                                System.out.println("El podio del servidor en el que usted está registrado \"" + servidorActual + " es el siguiente :\n");
                                for (Map.Entry<String, Float> entry : podio_replica.entrySet()) {
                                    System.out.println(" - " +entry.getKey() + ": " + entry.getValue() +"\n");

                                }

                                Map<String,Float> podio = donacion.getMayoresDonantesSistema();
                                System.out.println("El podio del sistema general es el siguiente :\n");
                                for (Map.Entry<String, Float> entry : podio.entrySet()) {
                                    System.out.println(" - " +entry.getKey() + ": " + entry.getValue() +"\n");

                                }
                            }else{
                                System.out.println("No se le puede suministrar esa información sin haber donado en primer lugar");

                            }
                            
                            break;
                            
                        case 6:
                            
                            if(donacion.haDonadoEnSistema(nombreUsuario, servidorActual)){
                                ArrayList<String> donantes = new ArrayList<>();
                                donantes = donacion.getDonantesEnSistema();
                                System.out.println("A continación se listan los donantes del sistema completo\n");
                                for (int i=0; i< donantes.size(); i++){
                                    System.out.println(" - Donante "+i+": "+donantes.get(i)+"\n");
                                }
                            }else{
                                System.out.println("No se le puede suministrar esa información sin haber donado en primer lugar");

                            }
                            
                            break;
                        case 7:
                            sesionIniciada = false;
                            System.out.println("Cierre de sesión exitoso para el usuario " + nombreUsuario+"\n");
                            nombreUsuario = "";
                            contrasenia = "";
                            break;
                        default:
                            System.out.println("Opción no válida. Por favor, seleccione una opción válida."+"\n");
                            break;
                    }
                }
            }

        } catch (NotBoundException | RemoteException e) {
            System.err.println("Exception del sistema: " + e);
        }
    }
}