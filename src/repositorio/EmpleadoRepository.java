package repositorio;

import modelo.CargoEmpleado;

import java.sql.SQLException;
import java.util.List;

/** Puerto de acceso a datos para la entidad EMPLEADO (patrón Repository). */
public interface EmpleadoRepository {

    /** Cargos válidos definidos actualmente en el ENUM de la columna empleado.cargoEM. */
    List<CargoEmpleado> listarCargosDisponibles() throws SQLException;
    
    List<CargoEmpleado> listarCargosDisponiblesAdmin() throws SQLException;

    List<EmpleadoFila> listarPorSucursal(int idSucursal) throws SQLException;
    
    List<EmpleadoFila> listarPorSucursalSupervisor(int idSucursal) throws SQLException;

    /** Búsqueda por DNI (parcial) dentro de una sucursal; si buscar está vacío, lista todos. */
    List<EmpleadoFila> buscarPorDni(int idSucursal, String dniParcial) throws SQLException;

    EmpleadoFila obtenerPorId(int idEmpleado) throws SQLException;

    int registrar(int idSucursal, String nombre, String apellido, String dni,
                   CargoEmpleado cargo, String telefono, float sueldo, String contrasenia) throws SQLException;

    int actualizar(int idEmpleado, int idSucursal, String nombre, String apellido, String dni,
                    CargoEmpleado cargo, String telefono, float sueldo) throws SQLException;

    int actualizarContrasenia(int idEmpleado, String contrasenia) throws SQLException;

    int eliminar(int idEmpleado) throws SQLException;

    record EmpleadoFila(int idEmpleado, String nombre, String apellido, String dni,
                         CargoEmpleado cargo, String telefono, double sueldo) {}
}
