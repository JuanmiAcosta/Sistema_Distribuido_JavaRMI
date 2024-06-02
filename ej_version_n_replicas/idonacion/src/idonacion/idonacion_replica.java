package idonacion;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;
import usuario.Usuario;

/**
 *
 * @author juanmi
 */
public interface idonacion_replica extends Remote {
    
    //Métodos auxiliares para que un servidor actúe como réplica. Necesario para comunicarse entre servidores.

    public boolean usuarioEnSistema(String usuario) throws RemoteException;
    
    public int usuariosEnReplica() throws RemoteException;
    
    public Map<String,Usuario> getUsuariosEnReplica(String r) throws RemoteException;
    
    public Map<String,Usuario> getUsuariosEnSistema() throws RemoteException;
    
    public idonacion_replica getReplica ( String r ) throws RemoteException;
    
    public Usuario getUsuario (String usuario) throws RemoteException;

    public void insertarUsuario(String usuario, String contrasenia) throws RemoteException;

    public void almacenarUsuario(String usuario, String contrasenia) throws RemoteException;

    public void actualizarDonaciones(float cuantia, String usu)throws RemoteException;

public Map<String, Usuario> getUsuarios() throws RemoteException;

    public void incDonadoReplica(float cuantia)throws RemoteException;
    
    public float getDonado() throws RemoteException;

    public Map<? extends String, ? extends Float> getMayoresDonantesReplica(String replica1)throws RemoteException;
    
    public boolean haDonadoEnSistema(String usuario, String r) throws RemoteException;

    public float obtenerTotalDonadoDelCliente(String usuario) throws RemoteException;
    
    public ArrayList<String> getDonantesEnReplica(String r) throws RemoteException;

    public ArrayList<String> getDonantesEnSistema() throws RemoteException;
    
    public ArrayList<String> getDonantes() throws RemoteException;
    
}
