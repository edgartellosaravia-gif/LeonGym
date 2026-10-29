package repositorio;

import UTPsports.UTPsports;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MembresiaClienteRepositoryJdbc implements MembresiaClienteRepository {

    @Override
    public void actualizarEstadosVencidos() throws SQLException {
        new UTPsports().getConnection().prepareStatement(
            "UPDATE membresia SET estado = 'VENCIDA' " +
            "WHERE fecha_fin < CURDATE() AND estado != 'VENCIDA'"
        ).executeUpdate();

        new UTPsports().getConnection().prepareStatement(
            "UPDATE membresia SET estado = 'POR_VENCER' " +
            "WHERE fecha_fin >= CURDATE() " +
            "AND fecha_fin <= DATE_ADD(CURDATE(), INTERVAL 15 DAY) " +
            "AND estado = 'ACTIVA'"
        ).executeUpdate();
    }

    @Override
    public List<MembresiaFila> listarPorCliente(int idCliente) throws SQLException {
        List<MembresiaFila> filas = new ArrayList<>();
        try (PreparedStatement ps = new UTPsports().getConnection().prepareStatement(
                "SELECT m.id_membresia, p.nombrePL, m.fecha_inicio, m.fecha_fin, " +
                "GROUP_CONCAT(cl.nombreCL SEPARATOR ', ') AS clases, " +
                "v.totalV, m.estado " +
                "FROM membresia m " +
                "JOIN plan p ON m.id_plan = p.id_plan " +
                "JOIN venta v ON m.id_venta = v.id_venta " +
                "LEFT JOIN membresia_clase mc ON m.id_membresia = mc.id_membresia " +
                "LEFT JOIN clase cl ON mc.id_clase = cl.id_clase " +
                "WHERE m.id_cliente = ? " +
                "GROUP BY m.id_membresia, p.nombrePL, m.fecha_inicio, m.fecha_fin, v.totalV, m.estado " +
                "ORDER BY m.fecha_inicio DESC")) {
            ps.setInt(1, idCliente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    filas.add(new MembresiaFila(
                            rs.getInt("id_membresia"), rs.getString("nombrePL"),
                            rs.getString("fecha_inicio"), rs.getString("fecha_fin"),
                            rs.getString("clases"), rs.getDouble("totalV"), rs.getString("estado")));
                }
            }
        }
        return filas;
    }

    @Override
    public List<RutinaFila> listarRutinas(int idCliente) throws SQLException {
        List<RutinaFila> filas = new ArrayList<>();
        ResultSet rs = new UTPsports().LrutinaCliente(idCliente);
        while (rs != null && rs.next()) {
            filas.add(new RutinaFila(rs.getInt("id_rutina"), rs.getString("diaSemana"), rs.getString("descripcion")));
        }
        return filas;
    }

    @Override
    public List<AlertaFila> listarAlertasPorSucursal(int idSucursal) throws SQLException {
        List<AlertaFila> filas = new ArrayList<>();
        try (PreparedStatement ps = new UTPsports().getConnection().prepareStatement(
                "SELECT CONCAT(c.nombreC,' ',c.apellidoC) AS nombreC, c.dniC, " +
                "m.fecha_inicio, m.fecha_fin, m.estado, " +
                "COALESCE(SUM(pa.montoP), 0) AS deuda, " +
                "CASE " +
                "  WHEN m.estado = 'VENCIDA' " +
                "    THEN CONCAT('Finalizó hace ', DATEDIFF(CURDATE(), m.fecha_fin), ' día(s)') " +
                "  WHEN DATEDIFF(m.fecha_fin, CURDATE()) BETWEEN 0 AND 15 " +
                "    THEN CONCAT('Fin en ', DATEDIFF(m.fecha_fin, CURDATE()), ' día(s)') " +
                "END AS alerta " +
                "FROM membresia m " +
                "JOIN cliente c ON m.id_cliente = c.id_cliente " +
                "JOIN plan p ON m.id_plan = p.id_plan " +
                "LEFT JOIN venta v ON m.id_venta = v.id_venta " +
                "LEFT JOIN pago pa ON v.id_venta = pa.id_venta AND pa.estadoP = 'PENDIENTE' " +
                "WHERE c.id_sucursal = ? " +
                "AND ( " +
                "  DATEDIFF(m.fecha_fin, CURDATE()) BETWEEN 0 AND 15 " +
                "  OR m.estado = 'VENCIDA' " +
                ") " +
                "GROUP BY m.id_membresia, c.nombreC, c.apellidoC, c.dniC, " +
                "m.fecha_inicio, m.fecha_fin, m.estado " +
                "ORDER BY m.fecha_fin ASC")) {
            ps.setInt(1, idSucursal);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    filas.add(new AlertaFila(
                            rs.getString("nombreC"), rs.getString("dniC"), rs.getString("fecha_inicio"),
                            rs.getString("fecha_fin"), rs.getString("estado"), rs.getDouble("deuda"),
                            rs.getString("alerta")));
                }
            }
        }
        return filas;
    }

    @Override
    public int registrarRutina(int idCliente, String dia, String descripcion) throws SQLException {
        return new UTPsports().Rrutina(idCliente, dia, descripcion, dia);
    }

    @Override
    public int actualizarRutina(int idRutina, String dia, String descripcion) throws SQLException {
        return new UTPsports().Mrutina(idRutina, dia, descripcion);
    }
}
