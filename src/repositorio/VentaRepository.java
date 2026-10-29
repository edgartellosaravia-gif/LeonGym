package repositorio;

import java.sql.SQLException;
import java.util.List;

public interface VentaRepository {

    /**
     * @param filtroFecha fragmento SQL de filtro (construido a partir de
     *                     opciones fijas de un combo, no de texto libre del
     *                     usuario) que se aplica sobre la columna fechaV.
     */
    DashboardVenta obtenerDashboard(int idSucursal, String filtroFecha) throws SQLException;

    List<VentaFila> listarHistorial(int idSucursal, String filtroFecha) throws SQLException;

    record DashboardVenta(double totalVendido, String membresiasActivas, double totalPorCobrar,
                           String planMasVendido, String productoMasVendido) {}

    record VentaFila(int idVenta, String fecha, String cliente, String empleado,
                      double total, String tipo, String estadoPago) {}
}
