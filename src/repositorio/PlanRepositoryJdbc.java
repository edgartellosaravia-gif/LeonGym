package repositorio;

import UTPsports.UTPsports;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PlanRepositoryJdbc implements PlanRepository {

    @Override
    public List<PlanFila> listarPorSucursal(int idSucursal) throws SQLException {
        List<PlanFila> filas = new ArrayList<>();
        try (PreparedStatement ps = new UTPsports().getConnection().prepareStatement(
                "SELECT p.id_plan, p.nombrePL, s.descripcionSERV, " +
                "p.estadoPL, p.duracion_mesesPL, p.precioPL " +
                "FROM plan p " +
                "JOIN servicio s ON p.id_servicio = s.id_servicio " +
                "WHERE p.id_sucursal = ?")) {
            ps.setInt(1, idSucursal);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    filas.add(new PlanFila(
                            rs.getInt("id_plan"), rs.getString("nombrePL"), rs.getString("descripcionSERV"),
                            rs.getString("estadoPL"), rs.getInt("duracion_mesesPL"), rs.getDouble("precioPL")));
                }
            }
        }
        return filas;
    }

    @Override
    public int registrar(int idServicio, int idSucursal, String nombre, int duracionMeses, float precio) throws SQLException {
        return new UTPsports().Rplan(idServicio, idSucursal, nombre, duracionMeses, precio);
    }

    @Override
    public int actualizar(int idPlan, String nombre, int duracionMeses, float precio, String estado) throws SQLException {
        return new UTPsports().Mplan(idPlan, nombre, duracionMeses, precio, estado);
    }

    @Override
    public int eliminar(int idPlan) throws SQLException {
        return new UTPsports().Eplan(idPlan);
    }

    @Override
    public void vincularServicioConSucursal(int idSucursal, int idServicio) throws SQLException {
        new UTPsports().RsucursalServicioIgnore(idSucursal, idServicio);
    }
}
