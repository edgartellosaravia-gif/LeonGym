package servicio;

import repositorio.PlanRepository;
import repositorio.PlanRepository.PlanFila;

import java.sql.SQLException;
import java.util.List;

public class PlanService {

    private final PlanRepository repositorio;

    public PlanService(PlanRepository repositorio) {
        this.repositorio = repositorio;
    }

    public List<PlanFila> listarPorSucursal(int idSucursal) throws SQLException {
        return repositorio.listarPorSucursal(idSucursal);
    }

    public Resultado registrar(Integer idServicio, int idSucursal, String nombre, int duracionMeses, float precio) {
        if (idServicio == null) {
            return Resultado.error("No existe ese tipo de servicio en la BD");
        }
        try {
            int filasAfectadas = repositorio.registrar(idServicio, idSucursal, nombre, duracionMeses, precio);
            if (filasAfectadas != 1) return Resultado.error("ERROR AL REGISTRAR");
            repositorio.vincularServicioConSucursal(idSucursal, idServicio);
            return Resultado.ok("PLAN REGISTRADO");
        } catch (SQLException e) { return Resultado.error("Error al registrar el plan: " + e.getMessage()); }
    }

    public Resultado actualizar(int idPlan, String nombre, int duracionMeses, float precio, String estado) {
        try {
            int filasAfectadas = repositorio.actualizar(idPlan, nombre, duracionMeses, precio, estado);
            return filasAfectadas == 1 ? Resultado.ok("PLAN MODIFICADO") : Resultado.error("ERROR AL MODIFICAR");
        } catch (SQLException e) { return Resultado.error("Error al modificar el plan: " + e.getMessage()); }
    }

    public Resultado eliminar(int idPlan) {
        try {
            int filasAfectadas = repositorio.eliminar(idPlan);
            return filasAfectadas == 1 ? Resultado.ok("PLAN ELIMINADO") : Resultado.error("ERROR AL ELIMINAR");
        } catch (SQLException e) { return Resultado.errorEliminar(e, "el plan"); }
    }
}
