package repositorio;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Puerto de acceso a datos para la entidad CLASE (patrón Repository).
 * Las capas de servicio/UI dependen de esta abstracción y no de JDBC
 * directamente (Principio de Inversión de Dependencias - SOLID).
 */
public interface ClaseRepository {

    /** Lista los tipos de servicio disponibles (descripcionSERV). */
    List<String> listarTiposServicio() throws SQLException;

    /** Obtiene el id_servicio a partir de su descripción, o null si no existe. */
    Integer obtenerIdServicioPorDescripcion(String descripcion) throws SQLException;

    /**
     * Profesores habilitados para dictar clases en una sucursal.
     * REGLA DE NEGOCIO: solo cargos PROFESOR o MIXTO (ver {@link modelo.CargoEmpleado}).
     * @return mapa ordenado "Nombre Apellido-CARGO" -> id_empleado, listo para poblar un combo.
     */
    LinkedHashMap<String, Integer> listarProfesoresHabilitados(int idSucursal) throws SQLException;

    /** Filas para la tabla de clases de una sucursal. */
    List<ClaseFila> listarClasesPorSucursal(int idSucursal) throws SQLException;

    int registrarClase(int idServicio, int idSucursal, Integer idEmpleado, String nombre,
                        String horario, int cupo, float precio) throws SQLException;

    int actualizarClase(int idClase, String nombre, String horario, int cupo,
                         float precio, String estado, Integer idEmpleado) throws SQLException;

    int eliminarClase(int idClase) throws SQLException;

    /** Vincula (idempotente) el servicio a la sucursal donde se dicta la clase. */
    void vincularServicioConSucursal(int idSucursal, int idServicio) throws SQLException;

    /** Representa una fila de la tabla de clases mostrada en la UI. */
    record ClaseFila(int idClase, String nombre, String tipoServicio, String estado,
                      String horario, String profesor, int cupo, double precio) {}
}
