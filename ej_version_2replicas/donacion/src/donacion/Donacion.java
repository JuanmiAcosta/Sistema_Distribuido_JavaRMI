package donacion;

import usuario.Usuario;
import idonacion.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;

/**
 * 
 * @author juanmi
 */
public class Donacion extends UnicastRemoteObject implements Idonacion, idonacion_replica{
    
    private final int port = 1099;
    
    private final String host = "localhost";
    
    private Map<String, Usuario> usuarios= new HashMap();  //Estructura de datos que contiene los usuarios 
    private ArrayList<String> donantes= new ArrayList<>();
    
    private float donado_servidor; // subtotal donado a este servidor
    private String replica1; // Identificativo de la replica
    private String servidor; // Identificativo del objeto remoto.
    
    public Donacion(String servidor, String replica1) throws RemoteException {
        this.donado_servidor = 0;
        this.replica1 = replica1;
        this.servidor = servidor;
    }
    
    @Override
    public float getDonado(){
        return this.donado_servidor;
    }
    
    @Override
    public void incDonadoReplica(float cuantia){
        this.donado_servidor += cuantia;
    }
    

    @Override
    public Map<String,Usuario> getUsuarios(){
        return this.usuarios;
    }
    
    @Override
    public boolean usuarioEnSistema(String usuario){
        return this.usuarios.containsKey(usuario);
    }
    
    @Override
    public int usuariosEnReplica(){
        return this.usuarios.size();
    }
    
    @Override
    public idonacion_replica getReplica ( String r ){ // Alg pensado para un servidor con una sola réplica
        idonacion_replica replica = null;
        
        try{
            Registry registry = LocateRegistry.getRegistry(this.host, this.port);
            replica = (idonacion_replica)registry.lookup(r);
        }catch(NotBoundException | RemoteException e) {
            System.err.println("Exception del sistema: " + e);
        }
        
        return replica;
    }
    
    @Override
    public Usuario getUsuario (String usuario){
        return this.usuarios.get(usuario);
    }
    
    @Override
    public void insertarUsuario(String usuario, String contrasenia){
        this.usuarios.put(usuario, new Usuario(usuario,contrasenia));
    }
    
   @Override
    public void almacenarUsuario(String usuario, String contrasenia) throws RemoteException {
        this.insertarUsuario(usuario,contrasenia);

        System.out.println("Usuario "+ usuario +" ha sido registrado en el sistema;"
        + " Número de usuarios en réplica: " + this.servidor + " -> " + this.usuariosEnReplica() +"\n");
    }
    
    @Override
    public Map<String, Usuario> getUsuariosEnReplica ( String r) throws RemoteException{
        Map<String,Usuario> usu = new HashMap<>();
        
        if (r.equals(this.servidor)){
            usu = this.usuarios;
        }else{
            idonacion_replica replica = this.getReplica(this.replica1);
            usu = replica.getUsuarios();
        }
        
        return usu;
    }
    
    @Override
    public boolean registrar(String usuario, String contrasenia) throws RemoteException {
        boolean registro =false;
        
        if (this.usuarioEnSistema(usuario)){
            return registro;
        }else{
            idonacion_replica replica = this.getReplica(this.replica1);
            if (replica.usuarioEnSistema(usuario)){
                return registro;
            }
            else{
                registro = true;
                if (this.usuariosEnReplica() <= replica.usuariosEnReplica()){
                    this.almacenarUsuario(usuario,contrasenia);
                }else{
                    replica.almacenarUsuario(usuario,contrasenia);
                }      
            }   
        }
        
        return registro;
    }

    @Override
    public void donar(String usu, float cuantia) throws RemoteException {
        
        System.out.println("Se dona "+ cuantia + " euros por parte del usuario "+ usu +"\n");
        
        if (this.usuarioEnSistema(usu)){
            this.actualizarDonaciones(cuantia, usu);
            this.incDonadoReplica(cuantia);
        }else{
           idonacion_replica replica = this.getReplica(this.replica1);
           replica.actualizarDonaciones(cuantia, usu);
           replica.incDonadoReplica(cuantia);
        }
    }

    @Override
    public float totalDonadoServidor() throws RemoteException {
        float total=0;
        
        total += this.getDonado();

        System.out.println("Total donado en el servidor: "+total);
        
        idonacion_replica replica = this.getReplica(this.replica1);
        
        total += replica.getDonado();

        System.out.println("Total donado en el sistema: "+total);
        
        return total;
    }
    
    @Override
    public String replicaUsuario(String usuario, String contrasenia) throws RemoteException {
        String id_replica = "";
        
        if (this.usuarioEnSistema(usuario)){
            
            if (contrasenia.equals(this.usuarios.get(usuario).getContrasenia())){
            
                id_replica = this.servidor;
                
            }
            
        } else {
            
            idonacion_replica replica = this.getReplica(this.replica1);
            
            if (replica.usuarioEnSistema(usuario)){
                
                if (contrasenia.equals(replica.getUsuariosEnReplica(this.replica1).get(usuario).getContrasenia())){
            
                    id_replica = this.replica1;
                
                }
            }
        }
        
        return id_replica;
    }

    @Override
    public float obtenerTotalDonadoDelCliente(String usuario) throws RemoteException {
        Usuario usu = this.usuarios.get(usuario);
        return usu.getTotalDonaciones();
    }

    @Override
    public int obtenerNumeroDonaciones(String usuario) throws RemoteException {
        Usuario usu = this.usuarios.get(usuario);
        return usu.getNumDonaciones();
    }

    @Override
    public float totalDonadoReplica(String usuario, String serv) throws RemoteException {
        float total=0;
        
        if (serv.equals(this.servidor)){

            total = this.getDonado();
        }else{
            idonacion_replica replica = this.getReplica(this.replica1);

            total = replica.getDonado();

        }

        return total; 
         
    }

    @Override
    public void actualizarDonaciones(float cuantia, String usu) throws RemoteException{
        Usuario usuario = this.usuarios.get(usu);
        
        usuario.incNumDonaciones();
        usuario.incTotalDonado(cuantia);
        
        this.usuarios.put(usu, usuario); 
        this.donantes.add(usu);
    }
    
    @Override
    public Map<String, Usuario> getUsuariosEnSistema() throws RemoteException {
        Map<String,Usuario> usuarios = new HashMap<>();
        
        usuarios.putAll(this.usuarios);
        
        idonacion_replica replica = this.getReplica(this.replica1);
        
        usuarios.putAll(replica.getUsuariosEnReplica(this.replica1));
        
        return usuarios;
    }

    @Override
    public Map<String, Float> getMayoresDonantesReplica(String r) throws RemoteException {
        Map<String,Float> podio_replica = new LinkedHashMap<>();
        
        if (r.equals(this.servidor)){
            
            List<Usuario> lista = new ArrayList<>(this.usuarios.values());
            Collections.sort(lista, Comparator.comparingDouble(Usuario::getTotalDonaciones).reversed());

            for (int i = 0; i < Math.min(3, lista.size()); i++) {
                Usuario usuario = lista.get(i);
                podio_replica.put(usuario.getUsuario(), (float) usuario.getTotalDonaciones());
            }

        }else{
            idonacion_replica replica = this.getReplica(this.replica1);
            List<Usuario> lista = new ArrayList<>(replica.getUsuariosEnReplica(this.replica1).values());
            Collections.sort(lista, Comparator.comparingDouble(Usuario::getTotalDonaciones).reversed());
            
            for (int i = 0; i < Math.min(3, lista.size()); i++) {
                Usuario usuario = lista.get(i);
                podio_replica.put(usuario.getUsuario(), (float) usuario.getTotalDonaciones());
            }
        }
         
        return podio_replica;
    }

    @Override
    public Map<String, Float> getMayoresDonantesSistema() throws RemoteException {
        Map<String,Float> podio = new LinkedHashMap<>();
        
        podio.putAll(this.getMayoresDonantesReplica(this.servidor));
        System.out.println("Podio servidor: "+podio);
            
        idonacion_replica replica = this.getReplica(this.replica1);
        podio.putAll(replica.getMayoresDonantesReplica(this.replica1));
        System.out.println("Podio replica+servidor: "+podio);
        
        // Convertir el Map a una lista de Map.Entry
        List<Map.Entry<String, Float>> list = new LinkedList<>(podio.entrySet());

        // Ordenar la lista en orden descendente basado en los valores de Float
        Collections.sort(list, new Comparator<Map.Entry<String, Float>>() {
            public int compare(Map.Entry<String, Float> o1, Map.Entry<String, Float> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        // Crear un nuevo LinkedHashMap para almacenar los elementos ordenados
        Map<String, Float> resultadoOrdenado = new LinkedHashMap<>();
        for (Map.Entry<String, Float> entry : list) {
            resultadoOrdenado.put(entry.getKey(), entry.getValue());
        }
       
        
        return resultadoOrdenado;
    }

    @Override
    public boolean haDonadoEnSistema(String usuario, String r) throws RemoteException {
    
        boolean donante= false;

        if (r.equals(this.servidor)){
            float donado = this.obtenerTotalDonadoDelCliente(usuario);
            if (donado == 0){
                return donante;  
            }
            donante=true;
            
        }else{
            idonacion_replica replica = this.getReplica(this.replica1);
            float donado = replica.obtenerTotalDonadoDelCliente(usuario);
            if (donado == 0){
                return donante;  
            }
            donante=true;
            
        }
        
        return donante;
    }

    @Override
    public ArrayList<String> getDonantesEnReplica(String r) throws RemoteException {
        
        ArrayList<String> donantes = new ArrayList<>();
        
        if (r.equals(this.servidor)){
            donantes = this.getDonantes();   
        }else{
            idonacion_replica replica = this.getReplica(this.replica1);
            donantes = replica.getDonantes();   
        }
        
        return donantes;
    }

    @Override
    public ArrayList<String> getDonantesEnSistema() throws RemoteException {
        
        ArrayList<String> donantes = new ArrayList<>();
        
        donantes.addAll(this.getDonantes());
        
        idonacion_replica replica = this.getReplica(this.replica1);
        
        donantes.addAll(replica.getDonantes());
        
        return donantes;

    }

    @Override
    public ArrayList<String> getDonantes() throws RemoteException {
        return this.donantes;
    }
}