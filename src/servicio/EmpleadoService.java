package servicio;

import modelo.CargoEmpleado;
import repositorio.EmpleadoRepository;
import repositorio.EmpleadoRepository.EmpleadoFila;

import java.sql.SQLException;
import java.util.List;

/**
 * Orquesta las reglas de negocio del módulo EMPLEADO.
 */
public class EmpleadoService {

    private static final String ETIQUETA_SUELDO_POR_CLASE = "SUELDO (POR CLASE)";
    private static final String ETIQUETA_SUELDO_MENSUAL = "SUELDO MENSUAL";

    private final EmpleadoRepository repositorio;

    public EmpleadoService(EmpleadoRepository repositorio) {
        this.repositorio = repositorio;
    }

    public List<CargoEmpleado> obtenerCargosDisponibles() throws SQLException {
        return repositorio.listarCargosDisponibles();
        
    }
    
    public List<CargoEmpleado> obtenerCargosDisponiblesSupervisor() throws SQLException{
        return repositorio.listarCargosDisponiblesAdmin();}

    /**
     * Etiqueta a mostrar junto al campo sueldo, según el cargo seleccionado.
     * <p>
     * CORRECCIÓN DE BUG: en la versión original, esta lógica comparaba el
     * literal completo del ENUM de MySQL (p. ej. "ENTRENADOR,PROFESOR,
     * RECEPCIÓN,ADMINISTRACIÓN,MIXTO") contra la cadena "PROFESOR", una
     * comparación que nunca podía ser verdadera, por lo que la etiqueta
     * quedaba fija en "SUELDO MENSUAL" sin importar el cargo elegido.
     * Ahora se evalúa el cargo realmente seleccionado y se aplica la regla:
     * PROFESOR o MIXTO se remuneran por clase.
     */
    public String etiquetaSueldoPara(CargoEmpleado cargo) {
        return cargo != null && cargo.seRemuneraPorClase()
                ? ETIQUETA_SUELDO_POR_CLASE
                : ETIQUETA_SUELDO_MENSUAL;
    }

    public List<EmpleadoFila> listarPorSucursal(int idSucursal) throws SQLException {
        return repositorio.listarPorSucursal(idSucursal);
    }
    
    public List<EmpleadoFila> listarPorSucursalSupervisor(int idSucursal) throws SQLException {
        return repositorio.listarPorSucursalSupervisor(idSucursal);
    }

    public List<EmpleadoFila> buscarPorDni(int idSucursal, String dniParcial) throws SQLException {
        return repositorio.buscarPorDni(idSucursal, dniParcial);
    }

    public EmpleadoFila obtenerPorId(int idEmpleado) throws SQLException {
        return repositorio.obtenerPorId(idEmpleado);
    }

    public Resultado registrar(int idSucursal, String nombre, String apellido, String dni,
                                CargoEmpleado cargo, String telefono, float sueldo, String contrasenia) {
        try {
            int filasAfectadas = repositorio.registrar(
                    idSucursal, nombre, apellido, dni, cargo, telefono, sueldo, contrasenia);
            return filasAfectadas == 1
                    ? Resultado.ok("EMPLEADO REGISTRADO")
                    : Resultado.error("ERROR AL REGISTRAR");
        } catch (SQLException e) {
            return Resultado.error("Error al registrar el empleado: " + e.getMessage());
        }
    }

    public Resultado actualizar(int idEmpleado, int idSucursal, String nombre, String apellido, String dni,
                                 CargoEmpleado cargo, String telefono, float sueldo, String nuevaContrasenia) {
        try {
            int filasAfectadas = repositorio.actualizar(
                    idEmpleado, idSucursal, nombre, apellido, dni, cargo, telefono, sueldo);
            if (nuevaContrasenia != null && !nuevaContrasenia.isBlank()) {
                repositorio.actualizarContrasenia(idEmpleado, nuevaContrasenia);
            }
            return filasAfectadas == 1
                    ? Resultado.ok("EMPLEADO MODIFICADO")
                    : Resultado.error("ERROR AL MODIFICAR");
        } catch (SQLException e) {
            return Resultado.error("Error al modificar el empleado: " + e.getMessage());
        }
    }

    public Resultado eliminar(int idEmpleado) {
        try {
            int filasAfectadas = repositorio.eliminar(idEmpleado);
            return filasAfectadas == 1
                    ? Resultado.ok("EMPLEADO ELIMINADO")
                    : Resultado.error("ERROR AL ELIMINAR");
        } catch (SQLException e) {
            return Resultado.errorEliminar(e, "el empleado");
        }
    }
}
