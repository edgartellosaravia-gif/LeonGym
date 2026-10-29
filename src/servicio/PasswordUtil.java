package servicio;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Hashing de contraseñas de empleado con sal aleatoria, usando únicamente
 * clases del propio JDK (sin agregar librerías nuevas al proyecto).
 * <p>
 * Formato almacenado: {@code SHA256:<sal en base64>:<hash en base64>}.
 * <p>
 * <b>Migración sin cortar el acceso a nadie:</b> las contraseñas ya
 * existentes están en texto plano (columna {@code contrasenia}, antes de
 * ancho VARCHAR(10)). En vez de exigir que cada empleado resetee su
 * contraseña, {@link #coincide} reconoce el formato viejo (no empieza con
 * "SHA256:") y compara en texto plano igual que antes; el llamador
 * ({@link servicio.AutenticacionService}) se encarga de re-guardar esa
 * contraseña ya hasheada la primera vez que el login es exitoso, de forma
 * transparente para el usuario ("migración perezosa").
 */
public final class PasswordUtil {

    private static final String PREFIJO = "SHA256:";
    private static final int LARGO_SAL_BYTES = 16;

    private PasswordUtil() {
    }

    public static String hash(String passwordPlano) {
        try {
            byte[] sal = new byte[LARGO_SAL_BYTES];
            new SecureRandom().nextBytes(sal);
            byte[] hash = digerir(passwordPlano, sal);
            return PREFIJO + Base64.getEncoder().encodeToString(sal) + ":" + Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            // No debería pasar (SHA-256 siempre está disponible en el JDK),
            // pero si pasara, no se bloquea el registro: se guarda en texto
            // plano como antes, en vez de perder la operación.
            return passwordPlano;
        }
    }

    public static boolean esHashMigrado(String almacenado) {
        return almacenado != null && almacenado.startsWith(PREFIJO);
    }

    public static boolean coincide(String passwordPlano, String almacenado) {
        if (almacenado == null || passwordPlano == null) return false;
        if (!esHashMigrado(almacenado)) {
            return almacenado.equals(passwordPlano); // formato legado, aún no migrado
        }
        try {
            String[] partes = almacenado.substring(PREFIJO.length()).split(":", 2);
            if (partes.length != 2) return false;
            byte[] sal = Base64.getDecoder().decode(partes[0]);
            byte[] hashEsperado = Base64.getDecoder().decode(partes[1]);
            byte[] hashCalculado = digerir(passwordPlano, sal);
            return MessageDigest.isEqual(hashEsperado, hashCalculado);
        } catch (Exception e) {
            return false;
        }
    }

    private static byte[] digerir(String passwordPlano, byte[] sal) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(sal);
        return md.digest(passwordPlano.getBytes("UTF-8"));
    }
}
