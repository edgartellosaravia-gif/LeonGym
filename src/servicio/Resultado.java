package servicio;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * Resultado estandarizado de una operación de negocio (patrón Result Object).
 * Reemplaza el esquema repetido de "try/catch + JOptionPane" disperso en
 * cada formulario: la capa de servicio decide y describe el resultado, y la
 * UI solo se limita a mostrarlo, centralizando el manejo de excepciones.
 */
public record Resultado(boolean exito, String mensaje) {

    public static Resultado ok(String mensaje) {
        return new Resultado(true, mensaje);
    }

    public static Resultado error(String mensaje) {
        return new Resultado(false, mensaje);
    }

    /**
     * Construye el mensaje de error para una operación de "eliminar",
     * detectando específicamente si la causa fue una restricción de llave
     * foránea (SQLState clase "23") — es decir, que el registro tiene otros
     * registros relacionados (ventas, clases, membresías, etc.) que
     * dependen de él. Antes esto se mostraba como el texto crudo de la
     * excepción ("ERROR AL ELIMINAR" o el SQLException.toString()), que no
     * le explicaba nada al usuario.
     */
    public static Resultado errorEliminar(SQLException e, String descripcionEntidad) {
        if (e instanceof SQLIntegrityConstraintViolationException) {
            return error("No se puede eliminar " + descripcionEntidad +
                    ": tiene registros relacionados (ventas, clases, membresías u otros) que dependen de él/ella.");
        }
        return error("Error al eliminar " + descripcionEntidad + ": " + e.getMessage());
    }
}
