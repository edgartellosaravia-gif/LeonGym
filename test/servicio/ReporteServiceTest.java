package servicio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import repositorio.ReporteRepository;
import repositorio.ReporteRepository.ReporteFila;

/** Pruebas de ReporteService con un repositorio falso en memoria. */
class ReporteServiceTest {

    static class RepositorioFalso implements ReporteRepository {
        int filasAfectadas = 1;
        SQLException error;
        List<ReporteFila> filas = new ArrayList<>();
        String ultimoMensaje;

        private void fallarSiHaceFalta() throws SQLException {
            if (error != null) throw error;
        }

        @Override
        public int enviar(int idEmpleado, String mensaje) throws SQLException {
            fallarSiHaceFalta();
            ultimoMensaje = mensaje;
            return filasAfectadas;
        }

        @Override
        public List<ReporteFila> listarPorSucursal(int idSucursal) throws SQLException {
            fallarSiHaceFalta();
            return filas;
        }

        @Override
        public int marcarResuelto(int idReporte) throws SQLException {
            fallarSiHaceFalta();
            return filasAfectadas;
        }

        @Override
        public int eliminar(int idReporte) throws SQLException {
            fallarSiHaceFalta();
            return filasAfectadas;
        }
    }

    private final RepositorioFalso repo = new RepositorioFalso();
    private final ReporteService servicio = new ReporteService(repo);

    @Test
    void noPermiteCrearseSinRepositorio() {
        assertThrows(NullPointerException.class, () -> new ReporteService(null));
    }

    @Test
    void enviarQuitaLosEspaciosDelMensaje() {
        Resultado r = servicio.enviar(3, "   Maquina rota   ");

        assertTrue(r.exito());
        assertEquals("REPORTE ENVIADO", r.mensaje());
        assertEquals("Maquina rota", repo.ultimoMensaje);
    }

    @Test
    void enviarDevuelveErrorSiFallaLaBaseDeDatos() {
        repo.error = new SQLException("sin conexion");

        Resultado r = servicio.enviar(3, "Maquina rota");

        assertFalse(r.exito());
        assertTrue(r.mensaje().contains("Error al enviar el reporte"));
    }

    @Test
    void marcarResueltoDevuelveExito() {
        Resultado r = servicio.marcarResuelto(10);

        assertTrue(r.exito());
        assertEquals("Marcado como resuelto", r.mensaje());
    }

    @Test
    void marcarResueltoDevuelveErrorSiNoActualizaNada() {
        repo.filasAfectadas = 0;

        Resultado r = servicio.marcarResuelto(10);

        assertFalse(r.exito());
        assertEquals("No se pudo actualizar", r.mensaje());
    }

    @Test
    void eliminarDevuelveExito() {
        Resultado r = servicio.eliminar(10);

        assertTrue(r.exito());
        assertEquals("REPORTE ELIMINADO", r.mensaje());
    }

    @Test
    void listarDevuelveUnaListaQueNoSePuedeModificar() throws Exception {
        repo.filas.add(new ReporteFila(1, "Juan", "Maquina rota", "2026-10-01", "PENDIENTE"));

        List<ReporteFila> lista = servicio.listarPorSucursal(1);

        assertEquals(1, lista.size());
        assertThrows(UnsupportedOperationException.class, () -> lista.add(null));
    }
}
