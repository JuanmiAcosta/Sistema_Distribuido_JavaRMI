# Sistema Distribuido Java RMI

Este repositorio contiene el código fuente y documentación para el proyecto del curso de Desarrollo de Sistemas Distribuidos, enfocado en la implementación de un sistema de donaciones utilizando Java RMI.

## Descripción del Proyecto

Este proyecto se compone de dos partes principales:

1. **Revisión de ejemplos básicos de RMI**: Se implementan y analizan varios ejemplos básicos de RMI para comprender su funcionamiento.
2. **Sistema de donaciones con RMI**: Implementación de un sistema de donaciones distribuido, con funcionalidades de registro, inicio de sesión y donaciones.

## Contenido

1. **Revisión de ejemplos**
   - Primer ejemplo: Implementación básica de un servicio y un cliente RMI.
   - Segundo ejemplo: Implementación de un servicio RMI con múltiples hebras.
   - Tercer ejemplo: Contador distribuido con RMI.

2. **Sistema de donaciones con RMI**
   - Implementación de servidores replicados (ServerEUW y ServerNA).
   - Funcionalidades de registro, inicio de sesión y donaciones.
   - Comunicación entre servidores replicados.

## Ejecución de los Ejemplos

### Primer Ejemplo

Para ejecutar el primer ejemplo, primero compile y luego ejecute los siguientes comandos:

```sh
javac Ejemplo.java Cliente_Ejemplo.java
bash macro.sh
```

### Segundo Ejemplo

Para el segundo ejemplo, compile y ejecute:

```sh
javac Ejemplo_MultiThread.java Cliente_Ejemplo_MultiThread.java
bash macro_multithread.sh
```
### Tercer Ejemplo

Para el tercer ejemplo, compile y ejecute:

```sh
javac Contador.java Servidor_Contador.java Cliente_Contador.java
bash macro_contador.sh
```
##Sistema de donaciones

####Configuración del servidor

El servidor y la réplica se configuran de la siguiente manera:

```java
String servidor = "ServerEUW";
String replica = "ServerNA";

System.setProperty("java.rmi.server.hostname","localhost");
Registry reg = LocateRegistry.createRegistry(1099);
Donacion donacion = new Donacion(servidor, replica);
Naming.rebind(servidor, donacion);
```

####Funcionalidades
<ul>
  <li>Registrar Usuario: Permite a los usuarios registrarse en el sistema distribuyendo la carga entre el servidor principal y la réplica.
</li>
  <li>Iniciar Sesión: Los usuarios pueden iniciar sesión en el sistema.
</li>
  <li>Donar: Los usuarios registrados pueden realizar donaciones.
</li>
  <li>Demás operaciones ....</li>
</ul>

####Ejecución del sistema
<ol>
  <li>Inicie el servidor principal y la réplica.
</li>
  <li>Ejecute el cliente para realizar operaciones de registro, inicio de sesión y donaciones.
</li>
</ol>







