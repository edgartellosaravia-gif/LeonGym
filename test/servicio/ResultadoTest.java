package servicio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import org.junit.jupiter.api.Test;

/** Pruebas del objeto Resultado que usan todos los servicios. */
class ResultadoTest {

    @Test
    void okMarcaExitoYGuardaElMensaje() {
        Resultado r = Resultado.ok("GUARDADO");

        assertTrue(r.exito());
        assertEquals("GUARDADO", r.mensaje());
    }

    @Test
    void errorMarcaFalloYGuardaElMensaje() {
        Resultado r = Resultado.error("FALLO");

        assertFalse(r.exito());
        assertEquals("FALLO", r.mensaje());
    }

    @Test
    void errorEliminarExplicaCuandoHayRegistrosRelacionados() {
        Resultado r = Resultado.errorEliminar(new SQLIntegrityConstraintViolationException("fk"), "el cliente");

        assertFalse(r.exito());
        assertTrue(r.mensaje().contains("No se puede eliminar el cliente"));
        assertTrue(r.mensaje().contains("registros relacionados"));
    }

    @Test
    void errorEliminarMuestraElMensajeEnOtrosErroresSql() {
        Resultado r = Resultado.errorEliminar(new SQLException("sin conexion"), "el cliente");

        assertFalse(r.exito());
        assertEquals("Error al eliminar el cliente: sin conexion", r.mensaje());
    }
}
