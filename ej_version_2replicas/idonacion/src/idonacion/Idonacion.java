package idonacion;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;
import usuario.Usuario;

/**
 * @brief Interfaz a implementar y a utilizar por los clientes. Contiene los
 *        métodos necesarios para el sistema de donaciones.
 * @author juanmi
 */
public interface Idonacion extends Remote {
    /**
     * @brief Registra al usuario en el servidor. Comprobamos
     *        si el cliente no existe en ninguna de las réplicas, de ser así
     *        lo registra la réplica con menor número de usuarios.  
     * @param usuario usamos los atributos del usuario
     * @param contrasenia a comprobar.
     * @return true si es usuario se registró con éxito
     *         false si no se ha podido registrar
     * @throws RemoteException fallo en las comunicaciones
     */
    boolean registrar(String usuario, String contrasenia) throws RemoteException;
    
    /** @brief Los clientes debe poder donar. La réplica a la que pertenece
     *  el cliente se obtiene con el método "replicaUsuario",
     *         que devuelve el nombre del servidor donde está registrado el cliente.
     * @param usuario que dona. Debe estar previamente registrado
     * @param cuantia debe ser positiva, mayor que 0.
     * @throws RemoteException fallo en las comunicaciones
     */
    void donar(String usuario, float cuantia) throws RemoteException;
    
    /**
     * @brief Consulta la cantidad total donada en total a las réplicas.
     *              
     * @return la cuantía total donada al servicio de donaciones
     * @throws RemoteException fallo en las comunicaciones
     */
    float totalDonadoServidor() throws RemoteException;
    
    /**
     * @brief Consulta la cantidad total donada en la réplica actual.
     * @param serve servidor del que queremos el subtotal.
     * @param usuario el usuario a comprobar.
     * @return la cuantía total donada a esta réplica. Si es -1 no ha donado y no puede obtener esa información.
     * @throws RemoteException fallo en las comunicaciones
     */
    float totalDonadoReplica(String usuario, String serve) throws RemoteException;
    
    /**
     * @brief Comprueba que el usuario esté ya registrado en el sistema o no, y devuelve 
     * en caso afirmativo la réplica en la que está registrado.
     * @param usuario réplica en la que está registrado.
     * @param contrasenia contraseña del usuario.
     * @return "" no se encuentra en ninguna réplica
     *         != "" la réplica en la que se encuentra
     * @throws RemoteException fallo en las comunicaciones
     */
    String replicaUsuario(String usuario, String contrasenia) throws RemoteException; //La usamos también para iniciar sesión
    
    /**
     * @brief Total donado por un cliente.
     * @param usuario usuario del que se comprueba total donado.
     * @return cuantía total donada por el usuario.
     * @throws RemoteException fallo en las comunicaciones
     */
    float obtenerTotalDonadoDelCliente(String usuario) throws RemoteException;
    
    /**
     * @brief Número de donaciones de un usuario.
     * @param usuario usuario registrado en el sistema
     * @return total de donaciones
     * @throws RemoteException fallo en las comunicaciones
     */
    public int obtenerNumeroDonaciones(String usuario) throws RemoteException;
    
    /**
     * @brief Obtiene el número de usuarios en una sola réplica.
     * 
     * @return total de usuarios en una réplica.
     * @throws RemoteException fallo en las comunicaciones
     */
    public int usuariosEnReplica() throws RemoteException;

    /**
     * @brief Comprueba que el usuario esté o no en el sistema.
     * @param usuario usuario a comprobar.
     * @return total de usuarios en una réplica.
     * @throws RemoteException fallo en las comunicaciones
     */
    public boolean usuarioEnSistema(String usuario) throws RemoteException;

    /**
     * @brief Obtenemos la lista de usuarios que hay en una réplica.
     * @param r representa a qué servidor le solicitamos los datos.
     * @return total de usuarios en una réplica.
     * @throws RemoteException fallo en las comunicaciones
     */
    public Map<String,Usuario> getUsuariosEnReplica(String r) throws RemoteException;
    
    /**
     * @brief Obtenemos la lista de usuarios que hay en el sistema.
     * 
     * @return total de usuarios en una réplica.
     * @throws RemoteException fallo en las comunicaciones
     */
    public Map<String,Usuario> getUsuariosEnSistema() throws RemoteException;
    
    /**
     * @brief Obtenemos la lista de donantes que hay en una réplica.
     * @param r representa a qué servidor le solicitamos los datos.
     * @return total de usuarios en una réplica.
     * @throws RemoteException fallo en las comunicaciones
     */
    public ArrayList<String> getDonantesEnReplica(String r) throws RemoteException;
    
    /**
     * @brief Obtenemos la lista de donantes que hay en el sistema.
     * 
     * @return total de usuarios en una réplica.
     * @throws RemoteException fallo en las comunicaciones
     */
    public ArrayList<String> getDonantesEnSistema() throws RemoteException;
    
    /**
     * @brief Obtenemos la lista de usuarios que más han donado en una réplica.
     * @param r se refiere a la réplica a observar.
     * @return lista con usuarios que más han donado.
     * @throws RemoteException fallo en las comunicaciones
     */
    public Map<String,Float> getMayoresDonantesReplica(String r) throws RemoteException;
    
    /**
     * @brief Obtenemos la lista de usuarios que más han donado en el sistema.
     * 
     * @return lista con usuarios que más han donado en el sistema.
     * @throws RemoteException fallo en las comunicaciones
     */
    public Map<String,Float> getMayoresDonantesSistema() throws RemoteException;
    
    /**
     * @brief Conocemos si el usuario ha donado o no en el sistema.
     * @param r se refiere a la réplica a observar.
     * @param usu usuario a comprobar.
     * @return lista con usuarios que más han donado en el sistema.
     * @throws RemoteException fallo en las comunicaciones
     */
    public boolean haDonadoEnSistema(String usuario, String r) throws RemoteException;
    
    /**
     * @brief Obtengo los donantes de la réplica.
     * @return lista con usuarios que más han donado en el sistema.
     * @throws RemoteException fallo en las comunicaciones
     */
    public ArrayList<String> getDonantes() throws RemoteException;
    
    
}