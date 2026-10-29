package repositorio;

import java.sql.SQLException;
import java.util.List;

/** Puerto de acceso a datos para los reportes de incidencia que cualquier empleado puede enviar. */
public interface ReporteRepository {

    /** id_sucursal se obtiene del propio empleado, no hace falta que la pantalla que llama lo conozca. */
    int enviar(int idEmpleado, String mensaje) throws SQLException;

    List<ReporteFila> listarPorSucursal(int idSucursal) throws SQLException;

    int marcarResuelto(int idReporte) throws SQLException;

    int eliminar(int idReporte) throws SQLException;

    record ReporteFila(int idReporte, String empleado, String mensaje, String fecha, String estado) {}
}
