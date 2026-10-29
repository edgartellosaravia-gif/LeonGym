package repositorio;

import UTPsports.UTPsports;
import servicio.PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class AutenticacionRepositoryJdbc implements AutenticacionRepository {

    @Override
    public Optional<EmpleadoLogin> buscarEmpleado(String dni, String contrasenia) throws SQLException {
        Connection con = new UTPsports().getConnection();
        int idEmpleado;
        int idSucursal;
        String nombre;
        String apellido;
        String cargo;
        try (PreparedStatement ps = con.prepareStatement(
                "SELECT id_empleado, id_sucursal, nombreEM, apellidoEM, cargoEM, contrasenia " +
                "FROM empleado WHERE dniEM=?")) {
            ps.setString(1, dni);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                String almacenada = rs.getString("contrasenia");
                if (!PasswordUtil.coincide(contrasenia, almacenada)) {
                    return Optional.empty();
                }
                idEmpleado = rs.getInt("id_empleado");
                idSucursal = rs.getInt("id_sucursal");
                nombre = rs.getString("nombreEM");
                apellido = rs.getString("apellidoEM");
                cargo = rs.getString("cargoEM");

                // Migración perezosa: si la contraseña todavía estaba en
                // texto plano, se re-guarda ya con hash, de forma
                // transparente (el empleado sigue usando la misma
                // contraseña, solo que ahora queda protegida en la BD).
                // Si esto falla (p. ej. porque todavía no se corrió el
                // ALTER TABLE que amplía la columna contrasenia), el login
                // igual debe funcionar -- la migración simplemente se
                // reintenta la próxima vez que la persona inicie sesión.
                if (!PasswordUtil.esHashMigrado(almacenada)) {
                    try {
                        migrarContraseniaAHash(con, idEmpleado, contrasenia);
                    } catch (SQLException ignorada) {
                        // No se pudo migrar todavía; se sigue con el login normalmente.
                    }
                }
            }
        }
        String nombreSede = obtenerNombreSede(con, idSucursal);
        return Optional.of(new EmpleadoLogin(idEmpleado, idSucursal, nombre, apellido, cargo, nombreSede));
    }

    private void migrarContraseniaAHash(Connection con, int idEmpleado, String passwordPlano) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(
                "UPDATE empleado SET contrasenia = ? WHERE id_empleado = ?")) {
            ps.setString(1, PasswordUtil.hash(passwordPlano));
            ps.setInt(2, idEmpleado);
            ps.executeUpdate();
        }
    }

    private String obtenerNombreSede(Connection con, int idSucursal) throws SQLException {
        try (PreparedStatement psSede = con.prepareStatement("SELECT nombreS FROM sucursal WHERE id_sucursal=?")) {
            psSede.setInt(1, idSucursal);
            try (ResultSet rsSede = psSede.executeQuery()) {
                return rsSede.next() ? rsSede.getString("nombreS") : "";
            }
        }
    }

    @Override
    public Optional<ClienteLogin> buscarCliente(String dni) throws SQLException {
        try (PreparedStatement psC = new UTPsports().getConnection().prepareStatement(
                "SELECT id_cliente, id_sucursal, nombreC, apellidoC FROM cliente WHERE dniC=?")) {
            psC.setString(1, dni);
            try (ResultSet rsC = psC.executeQuery()) {
                if (!rsC.next()) return Optional.empty();
                return Optional.of(new ClienteLogin(
                        rsC.getInt("id_cliente"), rsC.getInt("id_sucursal"),
                        rsC.getString("nombreC"), rsC.getString("apellidoC")));
            }
        }
    }

    /**
     * Igual que {@link AsistenciaRepositoryJdbc#registrarEntrada}: solo
     * bloquea si ya hay una entrada de hoy sin salida, permitiendo así
     * varios pares entrada/salida por día también en el auto-registro de
     * asistencia al iniciar sesión (personal de RECEPCIÓN).
     */
    @Override
    public void registrarIngresoAsistencia(int idEmpleado, String fecha, String horaEntrada) throws SQLException {
        try (PreparedStatement psCheck = new UTPsports().getConnection().prepareStatement(
                "SELECT 1 FROM asistencia WHERE id_empleado = ? AND fechaEM = ? AND hora_salida IS NULL")) {
            psCheck.setInt(1, idEmpleado);
            psCheck.setString(2, fecha);
            try (ResultSet rs = psCheck.executeQuery()) {
                if (rs.next()) return; // ya hay una entrada abierta hoy, no duplicar
            }
        }
        try (PreparedStatement ps = new UTPsports().getConnection().prepareStatement(
                "INSERT INTO asistencia(id_empleado, fechaEM, hora_entrada) VALUES (?, ?, ?)")) {
            ps.setInt(1, idEmpleado);
            ps.setString(2, fecha);
            ps.setString(3, horaEntrada);
            ps.executeUpdate();
        }
    }

    @Override
    public void cerrarConexionCompartida() throws SQLException {
        new UTPsports().getConnection().close();
    }
}
