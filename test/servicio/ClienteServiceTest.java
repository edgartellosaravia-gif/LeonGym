package servicio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import org.junit.jupiter.api.Test;
import repositorio.ClienteRepository;
import repositorio.ClienteRepository.ClienteFila;

/**
 * Pruebas de ClienteService. El servicio depende de la interfaz
 * ClienteRepository (principio D de SOLID), así que se prueba con un
 * repositorio falso en memoria, sin necesidad de MySQL.
 */
class ClienteServiceTest {

    /** Repositorio falso: devuelve lo que se le configure y recuerda con qué datos lo llamaron. */
    static class RepositorioFalso implements ClienteRepository {
        int filasAfectadas = 1;
        SQLException error;
        List<ClienteFila> filas = List.of();
        String[] ultimosDatos;

        private void fallarSiHaceFalta() throws SQLException {
            if (error != null) throw error;
        }

        @Override
        public List<ClienteFila> listarPorSucursal(int idSucursal) throws SQLException {
            fallarSiHaceFalta();
            return filas;
        }

        @Override
        public List<ClienteFila> buscarPorSucursalYNombre(int idSucursal, String nombreParcial) throws SQLException {
            fallarSiHaceFalta();
            return filas;
        }

        @Override
        public ClienteFila obtenerPorId(int idCliente) throws SQLException {
            fallarSiHaceFalta();
            return filas.isEmpty() ? null : filas.get(0);
        }

        @Override
        public int registrar(int idSucursal, String nombre, String apellido, String dni, String telefono, String email) throws SQLException {
            fallarSiHaceFalta();
            ultimosDatos = new String[]{nombre, apellido, dni, telefono, email};
            return filasAfectadas;
        }

        @Override
        public int actualizar(int idCliente, String nombre, String apellido, String dni, String telefono, String email) throws SQLException {
            fallarSiHaceFalta();
            ultimosDatos = new String[]{nombre, apellido, dni, telefono, email};
            return filasAfectadas;
        }

        @Override
        public int eliminar(int idCliente) throws SQLException {
            fallarSiHaceFalta();
            return filasAfectadas;
        }
    }

    private final RepositorioFalso repo = new RepositorioFalso();
    private final ClienteService servicio = new ClienteService(repo);

    @Test
    void noPermiteCrearseSinRepositorio() {
        assertThrows(NullPointerException.class, () -> new ClienteService(null));
    }

    @Test
    void registrarDevuelveExitoCuandoSeInsertaUnaFila() {
        Resultado r = servicio.registrar(1, "Ana", "Lopez", "12345678", "999888777", "ana@mail.com");

        assertTrue(r.exito());
        assertEquals("CLIENTE REGISTRADO", r.mensaje());
    }

    @Test
    void registrarQuitaLosEspaciosSobrantes() throws Exception {
        servicio.registrar(1, "  Ana ", " Lopez", "12345678  ", " 999888777 ", "  ana@mail.com ");

        assertEquals("Ana", repo.ultimosDatos[0]);
        assertEquals("Lopez", repo.ultimosDatos[1]);
        assertEquals("12345678", repo.ultimosDatos[2]);
        assertEquals("999888777", repo.ultimosDatos[3]);
        assertEquals("ana@mail.com", repo.ultimosDatos[4]);
    }

    @Test
    void registrarConvierteLosNulosEnTextoVacio() {
        servicio.registrar(1, null, null, null, null, null);

        for (String dato : repo.ultimosDatos) {
            assertEquals("", dato);
        }
    }

    @Test
    void registrarDevuelveErrorSiNoSeInsertaNingunaFila() {
        repo.filasAfectadas = 0;

        Resultado r = servicio.registrar(1, "Ana", "Lopez", "12345678", "999888777", "ana@mail.com");

        assertFalse(r.exito());
        assertEquals("ERROR AL REGISTRAR", r.mensaje());
    }

    @Test
    void registrarDevuelveErrorSiFallaLaBaseDeDatos() {
        repo.error = new SQLException("sin conexion");

        Resultado r = servicio.registrar(1, "Ana", "Lopez", "12345678", "999888777", "ana@mail.com");

        assertFalse(r.exito());
        assertTrue(r.mensaje().contains("Error al registrar el cliente"));
        assertTrue(r.mensaje().contains("sin conexion"));
    }

    @Test
    void actualizarDevuelveExitoYLimpiaLosEspacios() {
        Resultado r = servicio.actualizar(5, " Luis ", "Perez ", "87654321", "911222333", "luis@mail.com");

        assertTrue(r.exito());
        assertEquals("CLIENTE MODIFICADO", r.mensaje());
        assertEquals("Luis", repo.ultimosDatos[0]);
    }

    @Test
    void actualizarDevuelveErrorSiNoSeModificaNingunaFila() {
        repo.filasAfectadas = 0;

        Resultado r = servicio.actualizar(5, "Luis", "Perez", "87654321", "911222333", "luis@mail.com");

        assertFalse(r.exito());
        assertEquals("ERROR AL MODIFICAR", r.mensaje());
    }

    @Test
    void eliminarDevuelveExito() {
        Resultado r = servicio.eliminar(7);

        assertTrue(r.exito());
        assertEquals("CLIENTE ELIMINADO", r.mensaje());
    }

    @Test
    void eliminarExplicaCuandoElClienteTieneRegistrosRelacionados() {
        repo.error = new SQLIntegrityConstraintViolationException("fk");

        Resultado r = servicio.eliminar(7);

        assertFalse(r.exito());
        assertTrue(r.mensaje().contains("No se puede eliminar el cliente"));
    }

    @Test
    void eliminarDevuelveErrorSiFallaLaBaseDeDatos() {
        repo.error = new SQLException("sin conexion");

        Resultado r = servicio.eliminar(7);

        assertFalse(r.exito());
        assertTrue(r.mensaje().contains("Error al eliminar el cliente"));
    }

    @Test
    void listarPorSucursalDevuelveLoQueEntregaElRepositorio() throws Exception {
        repo.filas = List.of(new ClienteFila(1, "Ana", "Lopez", "12345678", "999888777", "ana@mail.com"));

        List<ClienteFila> resultado = servicio.listarPorSucursal(1);

        assertSame(repo.filas, resultado);
        assertEquals("Ana", resultado.get(0).nombre());
    }

    @Test
    void listarPropagaLaExcepcionSiFallaLaBaseDeDatos() {
        repo.error = new SQLException("sin conexion");

        assertThrows(SQLException.class, () -> servicio.listarPorSucursal(1));
    }
}
