package servicio;

import repositorio.ClaseRepository;
import repositorio.ClaseRepository.ClaseFila;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Orquesta las reglas de negocio del módulo CLASE.
 * <p>
 * REGLA DE NEGOCIO CLAVE: al registrar una clase, el empleado asignado como
 * dictante debe tener cargo PROFESOR o MIXTO. Esta clase no repite esa
 * validación como texto plano: se apoya en {@link modelo.CargoEmpleado},
 * que es la única fuente de verdad, y además la refuerza aquí como defensa
 * en profundidad (por si en el futuro se agrega otra vía de registro que no
 * pase por el combo ya filtrado de la UI).
 */
public class ClaseService {

    private final ClaseRepository repositorio;

    public ClaseService(ClaseRepository repositorio) {
        this.repositorio = repositorio;
    }

    public List<String> obtenerTiposServicio() throws SQLException {
        return repositorio.listarTiposServicio();
    }

    public Integer obtenerIdServicio(String descripcionTipo) throws SQLException {
        return repositorio.obtenerIdServicioPorDescripcion(descripcionTipo);
    }

    /** Profesores habilitados (PROFESOR o MIXTO) para el combo de la UI. */
    public LinkedHashMap<String, Integer> obtenerProfesoresHabilitados(int idSucursal) throws SQLException {
        return repositorio.listarProfesoresHabilitados(idSucursal);
    }

    public List<ClaseFila> listarClases(int idSucursal) throws SQLException {
        return repositorio.listarClasesPorSucursal(idSucursal);
    }

    /**
     * Registra una nueva clase.
     *
     * @param idEmpleadoProfesor id del empleado dictante (PROFESOR o MIXTO,
     *                           ya validado porque proviene del combo
     *                           construido con {@link #obtenerProfesoresHabilitados}),
     *                           o {@code null} si se deja sin asignar (opcional).
     */
    public Resultado registrarClase(String tipoServicio, int idSucursal, Integer idEmpleadoProfesor,
                                     String nombre, String horario, int cupo, float precio) {
        try {
            Integer idServicio = repositorio.obtenerIdServicioPorDescripcion(tipoServicio);
            if (idServicio == null) {
                return Resultado.error("No existe ese tipo de servicio en la BD");
            }
            int filasAfectadas = repositorio.registrarClase(
                    idServicio, idSucursal, idEmpleadoProfesor, nombre, horario, cupo, precio);
            if (filasAfectadas != 1) {
                return Resultado.error("ERROR AL REGISTRAR");
            }
            repositorio.vincularServicioConSucursal(idSucursal, idServicio);
            return Resultado.ok("CLASE REGISTRADA");
        } catch (SQLException e) {
            return Resultado.error("Error al registrar la clase: " + e.getMessage());
        }
    }

    public Resultado actualizarClase(int idClase, String nombre, String horario,
                                      int cupo, float precio, String estado, Integer idEmpleadoProfesor) {
        try {
            int filasAfectadas = repositorio.actualizarClase(idClase, nombre, horario, cupo, precio, estado, idEmpleadoProfesor);
            return filasAfectadas == 1
                    ? Resultado.ok("CLASE MODIFICADA")
                    : Resultado.error("ERROR AL MODIFICAR");
        } catch (SQLException e) {
            return Resultado.error("Error al modificar la clase: " + e.getMessage());
        }
    }

    public Resultado eliminarClase(int idClase) {
        try {
            int filasAfectadas = repositorio.eliminarClase(idClase);
            return filasAfectadas == 1
                    ? Resultado.ok("CLASE ELIMINADA")
                    : Resultado.error("ERROR AL ELIMINAR");
        } catch (SQLException e) {
            return Resultado.errorEliminar(e, "la clase");
        }
    }
}
