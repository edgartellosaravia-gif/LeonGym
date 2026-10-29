package servicio;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/** Pruebas del manejo de contraseñas (hash con sal y compatibilidad con claves antiguas). */
class PasswordUtilTest {

    @Test
    void hashNoGuardaLaContraseniaEnTextoPlano() {
        String almacenado = PasswordUtil.hash("clave123");

        assertTrue(almacenado.startsWith("SHA256:"));
        assertFalse(almacenado.contains("clave123"));
    }

    @Test
    void coincideConLaContraseniaCorrecta() {
        String almacenado = PasswordUtil.hash("clave123");

        assertTrue(PasswordUtil.coincide("clave123", almacenado));
    }

    @Test
    void noCoincideConUnaContraseniaIncorrecta() {
        String almacenado = PasswordUtil.hash("clave123");

        assertFalse(PasswordUtil.coincide("otraClave", almacenado));
    }

    @Test
    void laMismaContraseniaGeneraHashesDistintosPorLaSal() {
        String primero = PasswordUtil.hash("clave123");
        String segundo = PasswordUtil.hash("clave123");

        assertNotEquals(primero, segundo);
        assertTrue(PasswordUtil.coincide("clave123", primero));
        assertTrue(PasswordUtil.coincide("clave123", segundo));
    }

    @Test
    void aceptaContraseniasAntiguasEnTextoPlano() {
        assertTrue(PasswordUtil.coincide("1234", "1234"));
        assertFalse(PasswordUtil.coincide("9999", "1234"));
        assertFalse(PasswordUtil.esHashMigrado("1234"));
    }

    @Test
    void reconoceUnHashMigrado() {
        assertTrue(PasswordUtil.esHashMigrado(PasswordUtil.hash("abc")));
    }

    @Test
    void rechazaValoresNulos() {
        assertFalse(PasswordUtil.coincide(null, "1234"));
        assertFalse(PasswordUtil.coincide("1234", null));
        assertFalse(PasswordUtil.esHashMigrado(null));
    }

    @Test
    void rechazaUnHashMalFormado() {
        assertFalse(PasswordUtil.coincide("abc", "SHA256:sinSeparador"));
    }
}
