package servicio;

import repositorio.ReporteRepository;
import repositorio.ReporteRepository.ReporteFila;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.StringUtils;

import java.sql.SQLException;
import java.util.List;

public class ReporteService {

    private final ReporteRepository repositorio;

    public ReporteService(ReporteRepository repositorio) {
        this.repositorio = Preconditions.checkNotNull(repositorio, "repositorio");
    }

    public Resultado enviar(int idEmpleado, String mensaje) {
        try {
            int filasAfectadas = repositorio.enviar(idEmpleado, StringUtils.trimToEmpty(mensaje));
            return filasAfectadas == 1 ? Resultado.ok("REPORTE ENVIADO") : Resultado.error("ERROR AL ENVIAR");
        } catch (SQLException e) {
            return Resultado.error("Error al enviar el reporte: " + e.getMessage());
        }
    }

    public List<ReporteFila> listarPorSucursal(int idSucursal) throws SQLException {
        return ImmutableList.copyOf(repositorio.listarPorSucursal(idSucursal));
    }

    public Resultado marcarResuelto(int idReporte) {
        try {
            int filasAfectadas = repositorio.marcarResuelto(idReporte);
            return filasAfectadas == 1 ? Resultado.ok("Marcado como resuelto") : Resultado.error("No se pudo actualizar");
        } catch (SQLException e) {
            return Resultado.error("Error: " + e.getMessage());
        }
    }

    public Resultado eliminar(int idReporte) {
        try {
            int filasAfectadas = repositorio.eliminar(idReporte);
            return filasAfectadas == 1 ? Resultado.ok("REPORTE ELIMINADO") : Resultado.error("No se pudo eliminar");
        } catch (SQLException e) {
            return Resultado.error("Error al eliminar el reporte: " + e.getMessage());
        }
    }
}