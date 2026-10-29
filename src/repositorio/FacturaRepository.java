package repositorio;

import java.sql.SQLException;
import java.util.List;

/** Puerto de acceso a datos de solo lectura para el reporte de FACTURAS. */
public interface FacturaRepository {

    List<FacturaFila> listarPorSucursal(int idSucursal) throws SQLException;

    record FacturaFila(String numero, String fecha, String cliente,
                        double total, double igv, String tipo) {}
}
