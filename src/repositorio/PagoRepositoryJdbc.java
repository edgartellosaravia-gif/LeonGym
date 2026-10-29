package repositorio;

import UTPsports.UTPsports;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;
import java.util.Optional;

public class PagoRepositoryJdbc implements PagoRepository {

    @Override
    public Optional<FacturaGenerada> registrarPago(int idPago, float monto, String metodo) throws Exception {
        UTPsports co = new UTPsports();
        Connection con = co.getConnection();
        try {
            con.setAutoCommit(false);

            co.Mpago(idPago, monto, metodo, "PAGADO");

            Optional<FacturaGenerada> resultado = Optional.empty();
            try (PreparedStatement psVenta = con.prepareStatement(
                    "SELECT id_venta FROM pago WHERE id_pago = ?")) {
                psVenta.setInt(1, idPago);
                try (ResultSet rs = psVenta.executeQuery()) {
                    if (rs.next()) {
                        int idVenta = rs.getInt("id_venta");
                        boolean yaTieneFactura;
                        try (PreparedStatement psCheck = con.prepareStatement(
                                "SELECT id_factura FROM factura WHERE id_venta = ?")) {
                            psCheck.setInt(1, idVenta);
                            try (ResultSet rsCheck = psCheck.executeQuery()) {
                                yaTieneFactura = rsCheck.next();
                            }
                        }
                        if (!yaTieneFactura) {
                            double igv = monto * 0.18;
                            // El correlativo se basa en id_factura (AUTO_INCREMENT), que MySQL
                            // garantiza único y atómico. Antes se calculaba con
                            // "SELECT COUNT(*)+1", que tiene una condición de carrera real:
                            // dos pagos procesados casi al mismo tiempo podían recibir el
                            // mismo número de factura. Se inserta con un número provisional
                            // (la columna numeroF es UNIQUE) y se actualiza al real ya con el
                            // id_factura generado.
                            int idFactura;
                            try (PreparedStatement psIns = con.prepareStatement(
                                    "INSERT INTO factura(id_venta, numeroF, totalF, igv) VALUES (?, ?, ?, ?)",
                                    Statement.RETURN_GENERATED_KEYS)) {
                                psIns.setInt(1, idVenta);
                                psIns.setString(2, "TEMP-" + System.nanoTime());
                                psIns.setFloat(3, monto);
                                psIns.setFloat(4, (float) igv);
                                psIns.executeUpdate();
                                try (ResultSet rsIns = psIns.getGeneratedKeys()) {
                                    idFactura = rsIns.next() ? rsIns.getInt(1) : 0;
                                }
                            }
                            String numeroF = String.format("F001-%05d", idFactura);
                            try (PreparedStatement psUpd = con.prepareStatement(
                                    "UPDATE factura SET numeroF = ? WHERE id_factura = ?")) {
                                psUpd.setString(1, numeroF);
                                psUpd.setInt(2, idFactura);
                                psUpd.executeUpdate();
                            }
                            resultado = Optional.of(new FacturaGenerada(numeroF, igv));
                        }
                    }
                }
            }

            con.commit();
            return resultado;
        } catch (Exception e) {
            rollback(con);
            throw e;
        } finally {
            restaurarAutoCommit(con);
        }
    }

    @Override
    public void registrarVentaProductos(int idCliente, int idEmpleado, int idSucursal,
                                         Map<Integer, ItemCarrito> carrito, double total,
                                         String metodo, String estadoPago) throws Exception {
        UTPsports co = new UTPsports();
        Connection con = co.getConnection();
        try {
            con.setAutoCommit(false);

            int idVenta;
            try (PreparedStatement psVenta = con.prepareStatement(
                    "INSERT INTO venta(id_cliente,id_empleado,id_sucursal,totalV) VALUES(?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS)) {
                psVenta.setInt(1, idCliente);
                psVenta.setInt(2, idEmpleado);
                psVenta.setInt(3, idSucursal);
                psVenta.setDouble(4, total);
                psVenta.executeUpdate();
                try (ResultSet rsV = psVenta.getGeneratedKeys()) {
                    idVenta = rsV.next() ? rsV.getInt(1) : 0;
                }
            }

            for (Map.Entry<Integer, ItemCarrito> entrada : carrito.entrySet()) {
                int idProd = entrada.getKey();
                ItemCarrito item = entrada.getValue();
                co.RdetalleVenta(idVenta, "PRODUCTO", idProd, null, null, item.cantidad(), (float) item.precio());
                int filasStock;
                try (PreparedStatement psStock = con.prepareStatement(
                        "UPDATE producto SET stockPRO = stockPRO - ? WHERE id_producto = ? AND stockPRO >= ?")) {
                    psStock.setInt(1, item.cantidad());
                    psStock.setInt(2, idProd);
                    psStock.setInt(3, item.cantidad());
                    filasStock = psStock.executeUpdate();
                }
                if (filasStock == 0) {
                    throw new Exception("No hay stock suficiente para el producto \"" + nombreProducto(con, idProd) + "\".");
                }
            }

            co.Rpago(idVenta, (float) total, metodo, estadoPago);

            con.commit();
        } catch (Exception e) {
            rollback(con);
            throw e;
        } finally {
            restaurarAutoCommit(con);
        }
    }

    private String nombreProducto(Connection con, int idProducto) {
        try (PreparedStatement ps = con.prepareStatement("SELECT nombrePRO FROM producto WHERE id_producto = ?")) {
            ps.setInt(1, idProducto);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getString("nombrePRO") : ("ID " + idProducto);
            }
        } catch (Exception e) {
            return "ID " + idProducto;
        }
    }

    private void rollback(Connection con) {
        try {
            if (con != null) con.rollback();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void restaurarAutoCommit(Connection con) {
        try {
            if (con != null) con.setAutoCommit(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
