package repositorio;

import UTPsports.UTPsports;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VentaRepositoryJdbc implements VentaRepository {

    @Override
    public DashboardVenta obtenerDashboard(int idSucursal, String filtroFecha) throws SQLException {
        Connection con = new UTPsports().getConnection();
        double totalVendido = 0;
        String membresiasActivas = "0";
        double totalPorCobrar = 0;
        String planMasVendido = "Sin registros";
        String productoMasVendido = "Sin ventas";

        try (PreparedStatement ps1 = con.prepareStatement(
                "SELECT COALESCE(SUM(totalV), 0) AS total FROM venta " +
                "WHERE id_sucursal = ? AND " + filtroFecha)) {
            ps1.setInt(1, idSucursal);
            try (ResultSet rs1 = ps1.executeQuery()) {
                if (rs1.next()) totalVendido = rs1.getDouble("total");
            }
        }

        try (PreparedStatement ps2 = con.prepareStatement(
                "SELECT COUNT(*) AS totalMem FROM membresia m " +
                "JOIN cliente c ON m.id_cliente = c.id_cliente " +
                "WHERE c.id_sucursal = ? AND m.estado = 'ACTIVA'")) {
            ps2.setInt(1, idSucursal);
            try (ResultSet rs2 = ps2.executeQuery()) {
                if (rs2.next()) membresiasActivas = rs2.getString("totalMem");
            }
        }

        try (PreparedStatement ps3 = con.prepareStatement(
                "SELECT COALESCE(SUM(pa.montoP), 0) AS totalDeuda FROM pago pa " +
                "JOIN venta v ON pa.id_venta = v.id_venta " +
                "WHERE v.id_sucursal = ? AND pa.estadoP = 'PENDIENTE' AND " +
                filtroFecha.replace("fechaV", "pa.fechaP"))) {
            ps3.setInt(1, idSucursal);
            try (ResultSet rs3 = ps3.executeQuery()) {
                if (rs3.next()) totalPorCobrar = rs3.getDouble("totalDeuda");
            }
        }

        try (PreparedStatement ps4 = con.prepareStatement(
                "SELECT p.nombrePL, COUNT(*) AS total " +
                "FROM membresia m " +
                "JOIN plan p ON m.id_plan = p.id_plan " +
                "JOIN cliente c ON m.id_cliente = c.id_cliente " +
                "WHERE c.id_sucursal = ? " +
                "GROUP BY p.id_plan, p.nombrePL ORDER BY total DESC LIMIT 1")) {
            ps4.setInt(1, idSucursal);
            try (ResultSet rs4 = ps4.executeQuery()) {
                if (rs4.next()) planMasVendido = rs4.getString("nombrePL") + " (" + rs4.getInt("total") + " ventas)";
            }
        }

        try (PreparedStatement ps5 = con.prepareStatement(
                "SELECT p.nombrePRO, SUM(dv.cantidadDV) AS totalVendido " +
                "FROM detalle_venta dv " +
                "JOIN producto p ON dv.id_producto = p.id_producto " +
                "JOIN venta v ON dv.id_venta = v.id_venta " +
                "WHERE v.id_sucursal = ? AND dv.tipoDV = 'PRODUCTO' AND " + filtroFecha + " " +
                "GROUP BY p.id_producto, p.nombrePRO " +
                "ORDER BY totalVendido DESC LIMIT 1")) {
            ps5.setInt(1, idSucursal);
            try (ResultSet rs5 = ps5.executeQuery()) {
                if (rs5.next()) productoMasVendido = rs5.getString("nombrePRO") + " (" + rs5.getInt("totalVendido") + " unidades)";
            }
        }

        return new DashboardVenta(totalVendido, membresiasActivas, totalPorCobrar, planMasVendido, productoMasVendido);
    }

    @Override
    public List<VentaFila> listarHistorial(int idSucursal, String filtroFecha) throws SQLException {
        List<VentaFila> filas = new ArrayList<>();
        try (PreparedStatement ps = new UTPsports().getConnection().prepareStatement(
                "SELECT v.id_venta, v.fechaV, " +
                "CONCAT(c.nombreC,' ',c.apellidoC) AS cliente, " +
                "CONCAT(e.nombreEM,' ',e.apellidoEM) AS empleado, " +
                "v.totalV, pa.estadoP, " +
                "CASE " +
                "  WHEN EXISTS(SELECT 1 FROM detalle_venta dv WHERE dv.id_venta = v.id_venta AND dv.tipoDV = 'PLAN') " +
                "    THEN 'Membresía' " +
                "  ELSE 'Productos' " +
                "END AS tipo " +
                "FROM venta v " +
                "JOIN cliente c ON v.id_cliente = c.id_cliente " +
                "JOIN empleado e ON v.id_empleado = e.id_empleado " +
                "LEFT JOIN pago pa ON v.id_venta = pa.id_venta " +
                "WHERE v.id_sucursal = ? AND " + filtroFecha +
                " GROUP BY v.id_venta, v.fechaV, cliente, empleado, v.totalV, pa.estadoP, tipo " +
                "ORDER BY v.fechaV DESC")) {
            ps.setInt(1, idSucursal);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    filas.add(new VentaFila(
                            rs.getInt("id_venta"), rs.getString("fechaV"), rs.getString("cliente"),
                            rs.getString("empleado"), rs.getDouble("totalV"), rs.getString("tipo"),
                            rs.getString("estadoP")));
                }
            }
        }
        return filas;
    }
}
