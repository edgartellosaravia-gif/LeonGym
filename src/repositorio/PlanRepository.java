package repositorio;

import java.sql.SQLException;
import java.util.List;

public interface PlanRepository {

    List<PlanFila> listarPorSucursal(int idSucursal) throws SQLException;

    int registrar(int idServicio, int idSucursal, String nombre, int duracionMeses, float precio) throws SQLException;

    int actualizar(int idPlan, String nombre, int duracionMeses, float precio, String estado) throws SQLException;

    int eliminar(int idPlan) throws SQLException;

    void vincularServicioConSucursal(int idSucursal, int idServicio) throws SQLException;

    record PlanFila(int idPlan, String nombre, String tipoServicio, String estado, int duracionMeses, double precio) {}
}
