package repositorio;

import UTPsports.UTPsports;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReporteRepositoryJdbc implements ReporteRepository {

    @Override
    public int enviar(int idEmpleado, String mensaje) throws SQLException {
        // id_sucursal se deriva del propio empleado (subconsulta) para que
        // esta operación funcione igual desde cualquier pantalla, incluso
        // las que no tienen a mano el id_sucursal (p. ej. el autoservicio
        // de asistencia de un profesor).
        try (PreparedStatement ps = new UTPsports().getConnection().prepareStatement(
                "INSERT INTO reporte(id_empleado, id_sucursal, mensaje) " +
                "SELECT ?, id_sucursal, ? FROM empleado WHERE id_empleado = ?")) {
            ps.setInt(1, idEmpleado);
            ps.setString(2, mensaje);
            ps.setInt(3, idEmpleado);
            return ps.executeUpdate();
        }
    }

    @Override
    public List<ReporteFila> listarPorSucursal(int idSucursal) throws SQLException {
        List<ReporteFila> filas = new ArrayList<>();
        try (PreparedStatement ps = new UTPsports().getConnection().prepareStatement(
                "SELECT r.id_reporte, CONCAT(e.nombreEM,' ',e.apellidoEM) AS empleado, " +
                "r.mensaje, r.fecha_reporte, r.estado " +
                "FROM reporte r " +
                "JOIN empleado e ON r.id_empleado = e.id_empleado " +
                "WHERE r.id_sucursal = ? " +
                "ORDER BY (r.estado = 'PENDIENTE') DESC, r.fecha_reporte DESC")) {
            ps.setInt(1, idSucursal);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    filas.add(new ReporteFila(
                            rs.getInt("id_reporte"), rs.getString("empleado"), rs.getString("mensaje"),
                            rs.getString("fecha_reporte"), rs.getString("estado")));
                }
            }
        }
        return filas;
    }

    @Override
    public int marcarResuelto(int idReporte) throws SQLException {
        try (PreparedStatement ps = new UTPsports().getConnection().prepareStatement(
                "UPDATE reporte SET estado = 'RESUELTO' WHERE id_reporte = ?")) {
            ps.setInt(1, idReporte);
            return ps.executeUpdate();
        }
    }

    @Override
    public int eliminar(int idReporte) throws SQLException {
        try (PreparedStatement ps = new UTPsports().getConnection().prepareStatement(
                "DELETE FROM reporte WHERE id_reporte = ?")) {
            ps.setInt(1, idReporte);
            return ps.executeUpdate();
        }
    }
}