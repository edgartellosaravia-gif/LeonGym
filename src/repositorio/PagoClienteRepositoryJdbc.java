package repositorio;

import UTPsports.UTPsports;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PagoClienteRepositoryJdbc implements PagoClienteRepository {

    private static final String SQL_DESCRIPCION =
            "CASE " +
            "  WHEN EXISTS(SELECT 1 FROM detalle_venta dv WHERE dv.id_venta = v.id_venta AND dv.tipoDV = 'PLAN') " +
            "    THEN CONCAT('Membresía - ', (SELECT p.nombrePL FROM detalle_venta dv2 " +
            "         JOIN plan p ON dv2.id_plan = p.id_plan " +
            "         WHERE dv2.id_venta = v.id_venta AND dv2.tipoDV = 'PLAN' LIMIT 1)) " +
            "  ELSE 'Compra de productos' " +
            "END AS descripcion";

    @Override
    public Optional<MembresiaActiva> obtenerMembresiaActiva(int idCliente) throws SQLException {
        try (PreparedStatement ps = new UTPsports().getConnection().prepareStatement(
                "SELECT p.nombrePL, m.fecha_inicio, m.fecha_fin, m.estado, " +
                "GROUP_CONCAT(cl.nombreCL SEPARATOR ', ') AS clases " +
                "FROM membresia m " +
                "JOIN plan p ON m.id_plan = p.id_plan " +
                "LEFT JOIN membresia_clase mc ON m.id_membresia = mc.id_membresia " +
                "LEFT JOIN clase cl ON mc.id_clase = cl.id_clase " +
                "WHERE m.id_cliente = ? AND m.estado IN ('ACTIVA','POR_VENCER') " +
                "GROUP BY m.id_membresia, p.nombrePL, m.fecha_inicio, m.fecha_fin, m.estado " +
                "ORDER BY m.fecha_inicio DESC LIMIT 1")) {
            ps.setInt(1, idCliente);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                return Optional.of(new MembresiaActiva(
                        rs.getString("nombrePL"), rs.getString("fecha_inicio"), rs.getString("fecha_fin"),
                        rs.getString("estado"), rs.getString("clases")));
            }
        }
    }

    @Override
    public List<DeudaFila> listarDeudas(int idCliente) throws SQLException {
        List<DeudaFila> filas = new ArrayList<>();
        try (PreparedStatement ps = new UTPsports().getConnection().prepareStatement(
                "SELECT pa.id_pago, pa.fechaP, pa.montoP, pa.metodoP, pa.estadoP, " + SQL_DESCRIPCION + " " +
                "FROM pago pa " +
                "JOIN venta v ON pa.id_venta = v.id_venta " +
                "WHERE v.id_cliente = ? AND pa.estadoP = 'PENDIENTE' " +
                "ORDER BY pa.fechaP DESC")) {
            ps.setInt(1, idCliente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    filas.add(new DeudaFila(
                            rs.getInt("id_pago"), rs.getString("fechaP"), rs.getString("descripcion"),
                            rs.getDouble("montoP"), rs.getString("metodoP"), rs.getString("estadoP")));
                }
            }
        }
        return filas;
    }

    @Override
    public List<PagoFila> listarHistorialPagos(int idCliente) throws SQLException {
        List<PagoFila> filas = new ArrayList<>();
        try (PreparedStatement ps = new UTPsports().getConnection().prepareStatement(
                "SELECT pa.fechaP, pa.montoP, pa.metodoP, pa.estadoP, " + SQL_DESCRIPCION + " " +
                "FROM pago pa " +
                "JOIN venta v ON pa.id_venta = v.id_venta " +
                "WHERE v.id_cliente = ? AND pa.estadoP = 'PAGADO' " +
                "ORDER BY pa.fechaP DESC")) {
            ps.setInt(1, idCliente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    filas.add(new PagoFila(
                            rs.getString("fechaP"), rs.getString("descripcion"),
                            rs.getDouble("montoP"), rs.getString("metodoP"), rs.getString("estadoP")));
                }
            }
        }
        return filas;
    }

    @Override
    public List<CompraFila> listarHistorialCompras(int idCliente) throws SQLException {
        List<CompraFila> filas = new ArrayList<>();
        try (PreparedStatement ps = new UTPsports().getConnection().prepareStatement(
                "SELECT v.fechaV, p.nombrePRO, dv.cantidadDV, dv.precio_unit, dv.subtotalDV " +
                "FROM detalle_venta dv " +
                "JOIN venta v ON dv.id_venta = v.id_venta " +
                "JOIN producto p ON dv.id_producto = p.id_producto " +
                "WHERE v.id_cliente = ? AND dv.tipoDV = 'PRODUCTO' " +
                "ORDER BY v.fechaV DESC")) {
            ps.setInt(1, idCliente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    filas.add(new CompraFila(
                            rs.getString("fechaV"), rs.getString("nombrePRO"), rs.getInt("cantidadDV"),
                            rs.getDouble("precio_unit"), rs.getDouble("subtotalDV")));
                }
            }
        }
        return filas;
    }
}
