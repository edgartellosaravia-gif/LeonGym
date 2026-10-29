package servicio;

import repositorio.MembresiaRepository;
import repositorio.MembresiaRepository.ClaseActivaFila;
import repositorio.MembresiaRepository.MembresiaFila;
import repositorio.MembresiaRepository.PlanActivoFila;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Orquesta las 4 operaciones de escritura del módulo de membresías
 * (registrar, renovar, modificar, vender clase(s) sin plan), delegando la
 * transacción completa al repositorio y traduciendo el resultado a
 * {@link Resultado} para la UI.
 */
public class MembresiaService {

    private final MembresiaRepository repositorio;

    public MembresiaService(MembresiaRepository repositorio) {
        this.repositorio = repositorio;
    }

    public List<PlanActivoFila> obtenerPlanesActivos(int idSucursal) throws Exception {
        return repositorio.listarPlanesActivos(idSucursal);
    }

    public List<ClaseActivaFila> obtenerClasesActivas(int idSucursal) throws Exception {
        return repositorio.listarClasesActivas(idSucursal);
    }

    public List<MembresiaFila> listarPorCliente(int idCliente) throws Exception {
        return repositorio.listarPorCliente(idCliente);
    }

    public List<Integer> obtenerIdsClaseDeMembresia(int idMembresia) throws Exception {
        return repositorio.listarIdsClaseDeMembresia(idMembresia);
    }

    public Resultado registrar(int idCliente, int idEmpleado, int idSucursal, int idPlan, float precioPlan,
                                List<Integer> idsClase, Map<Integer, Float> preciosClase,
                                float totalVenta, LocalDate fechaInicio, LocalDate fechaFin, Integer idEntrenador) {
        try {
            repositorio.registrarMembresia(idCliente, idEmpleado, idSucursal, idPlan, precioPlan,
                    idsClase, preciosClase, totalVenta, fechaInicio, fechaFin, idEntrenador);
            return Resultado.ok("MEMBRESÍA REGISTRADA");
        } catch (Exception e) {
            return Resultado.error(e.toString());
        }
    }

    public Resultado renovar(int idMembresiaActual, int idCliente, int idEmpleado, int idSucursal,
                              int idPlan, int duracionMeses, float precioPlan,
                              List<Integer> idsClase, Map<Integer, Float> preciosClase, Integer idEntrenador) {
        try {
            repositorio.renovarMembresia(idMembresiaActual, idCliente, idEmpleado, idSucursal,
                    idPlan, duracionMeses, precioPlan, idsClase, preciosClase, idEntrenador);
            return Resultado.ok("MEMBRESÍA RENOVADA CORRECTAMENTE");
        } catch (Exception e) {
            return Resultado.error(e.getMessage());
        }
    }

    public Resultado modificar(int idMembresia, int idPlan, LocalDate fechaInicio, LocalDate fechaFin,
                                List<Integer> idsClaseNuevas, Map<Integer, Float> preciosClase, float totalVenta,
                                Integer idEntrenador) {
        try {
            repositorio.modificarMembresia(idMembresia, idPlan, fechaInicio, fechaFin,
                    idsClaseNuevas, preciosClase, totalVenta, idEntrenador);
            return Resultado.ok("MEMBRESÍA MODIFICADA");
        } catch (Exception e) {
            return Resultado.error(e.toString());
        }
    }

    /** Igual que {@link #modificar}, pero preserva que la membresía no tenga plan (no le asigna uno). */
    public Resultado modificarSinPlan(int idMembresia, LocalDate fechaInicio, LocalDate fechaFin,
                                       List<Integer> idsClaseNuevas, Map<Integer, Float> preciosClase, float totalVenta,
                                       Integer idEntrenador) {
        try {
            repositorio.modificarSinPlan(idMembresia, fechaInicio, fechaFin, idsClaseNuevas, preciosClase,
                    totalVenta, idEntrenador);
            return Resultado.ok("MEMBRESÍA MODIFICADA");
        } catch (Exception e) {
            return Resultado.error(e.toString());
        }
    }

    public Resultado registrarSinPlan(int idCliente, int idEmpleado, int idSucursal, String fecha,
                                       List<Integer> idsClase, Map<Integer, Float> preciosClase, Integer idEntrenador) {
        try {
            repositorio.registrarSinPlan(idCliente, idEmpleado, idSucursal, fecha, idsClase, preciosClase, idEntrenador);
            return Resultado.ok("CLASE(S) REGISTRADA(S)");
        } catch (Exception e) {
            return Resultado.error(e.toString());
        }
    }
}
