package repositorio;

import java.sql.SQLException;
import java.util.Optional;

/**
 * Puerto de acceso a datos para el login único (empleado o cliente por DNI).
 * Refleja exactamente las dos consultas que ya existían en FrmInicio:
 * primero intenta autenticar como empleado (DNI + contraseña), y si no
 * hay coincidencia, busca al cliente solo por DNI (la tabla CLIENTE no
 * tiene columna de contraseña en el esquema actual).
 */
public interface AutenticacionRepository {

    Optional<EmpleadoLogin> buscarEmpleado(String dni, String contrasenia) throws SQLException;

    Optional<ClienteLogin> buscarCliente(String dni) throws SQLException;

    /** Registra la hora de ingreso del empleado (usado en el acceso directo de RECEPCIÓN). */
    void registrarIngresoAsistencia(int idEmpleado, String fecha, String horaEntrada) throws SQLException;

    /**
     * Cierra la conexión compartida actual (comportamiento heredado del
     * botón "volver/salir" de recepción). La conexión se reabre de forma
     * transparente en el siguiente uso, por lo que esto no interrumpe al
     * resto de la aplicación.
     */
    void cerrarConexionCompartida() throws SQLException;

    record EmpleadoLogin(int idEmpleado, int idSucursal, String nombre, String apellido,
                          String cargo, String nombreSede) {}

    record ClienteLogin(int idCliente, int idSucursal, String nombre, String apellido) {}
}
