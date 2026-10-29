package repositorio;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Puerto de acceso a datos para la gestión ADMINISTRATIVA de membresías
 * (registrar, renovar, modificar, o vender solo clases sueltas "sin plan").
 * <p>
 * Distinto de {@link MembresiaClienteRepository}, que cubre el portal de
 * autoservicio del cliente (solo lectura de sus propias membresías/rutinas)
 * y el panel de alertas de vencimiento; aquí viven las operaciones de
 * escritura multi-paso (venta + pago + membresía + cupos) usadas por el
 * módulo de recepción/administración.
 */
public interface MembresiaRepository {

    List<PlanActivoFila> listarPlanesActivos(int idSucursal) throws Exception;

    List<ClaseActivaFila> listarClasesActivas(int idSucursal) throws Exception;

    /** Variante con LEFT JOIN a plan (admite membresías "sin plan"), a diferencia del portal de cliente. */
    List<MembresiaFila> listarPorCliente(int idCliente) throws Exception;

    List<Integer> listarIdsClaseDeMembresia(int idMembresia) throws Exception;

    Optional<LocalDate> obtenerFechaFinMembresia(int idMembresia) throws Exception;

    /**
     * Registra una membresía nueva con plan (+ clases opcionales) en una
     * única transacción. Devuelve el id de la membresía creada.
     * @param idEntrenador entrenador (PROFESOR/MIXTO) asignado opcionalmente a esta membresía, o null.
     */
    int registrarMembresia(int idCliente, int idEmpleado, int idSucursal, int idPlan, float precioPlan,
                            List<Integer> idsClase, Map<Integer, Float> preciosClase,
                            float totalVenta, LocalDate fechaInicio, LocalDate fechaFin,
                            Integer idEntrenador) throws Exception;

    /**
     * Renueva una membresía existente: calcula la nueva fecha de inicio a
     * partir de la fecha_fin actual (o "hoy" si no existe) y crea una nueva
     * membresía + venta + pago + clases, reduciendo cupos.
     */
    int renovarMembresia(int idMembresiaActual, int idCliente, int idEmpleado, int idSucursal,
                          int idPlan, int duracionMeses, float precioPlan,
                          List<Integer> idsClase, Map<Integer, Float> preciosClase,
                          Integer idEntrenador) throws Exception;

    /**
     * Modifica una membresía existente: libera los cupos de las clases
     * previas, las reemplaza por las nuevas, actualiza fechas/plan y el
     * total de la venta asociada.
     */
    void modificarMembresia(int idMembresia, int idPlan, LocalDate fechaInicio, LocalDate fechaFin,
                             List<Integer> idsClaseNuevas, Map<Integer, Float> preciosClase,
                             float totalVenta, Integer idEntrenador) throws Exception;

    /**
     * Igual que {@link #modificarMembresia}, pero para una membresía que
     * nunca tuvo plan (vendida "sin plan", solo clases sueltas): NO toca la
     * columna id_plan, para no convertirla en una membresía con plan por
     * accidente.
     */
    void modificarSinPlan(int idMembresia, LocalDate fechaInicio, LocalDate fechaFin,
                           List<Integer> idsClaseNuevas, Map<Integer, Float> preciosClase,
                           float totalVenta, Integer idEntrenador) throws Exception;

    /** Vende solo clases sueltas, sin plan asociado (membresía de un día). */
    void registrarSinPlan(int idCliente, int idEmpleado, int idSucursal, String fecha,
                           List<Integer> idsClase, Map<Integer, Float> preciosClase,
                           Integer idEntrenador) throws Exception;

    record PlanActivoFila(int idPlan, String nombre, int duracionMeses, double precio, String tipoServicio) {}

    record ClaseActivaFila(int idClase, String nombre, String horario, int cupo, double precio) {}

    record MembresiaFila(int idMembresia, String nombrePlan, String fechaInicio, String fechaFin,
                          String clases, double total, String estado, String entrenador) {}
}
