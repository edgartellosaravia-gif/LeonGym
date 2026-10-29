package repositorio;

import UTPsports.UTPsports;
import modelo.CargoEmpleado;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación JDBC de {@link EmpleadoRepository}.
 * La conexión y credenciales siguen gestionadas exclusivamente por
 * {@link UTPsports}, sin modificaciones.
 */
public class EmpleadoRepositoryJdbc implements EmpleadoRepository {

    private Connection cn() {
        return new UTPsports().getConnection();
    }

    @Override
    public List<CargoEmpleado> listarCargosDisponibles() throws SQLException {
        // Se mantiene la estrategia original de leer el propio ENUM de la
        // columna cargoEM desde el catálogo de MySQL (SHOW COLUMNS), en vez
        // de hardcodear la lista de cargos en el código Java.
        List<CargoEmpleado> cargos = new ArrayList<>();
        
        try (PreparedStatement ps = cn().prepareStatement("SHOW COLUMNS FROM empleado LIKE 'cargoEM'");
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                String tipoEnum = rs.getString("Type")
                        .replace("enum(", "")
                        .replace(")", "")
                        .replace("'", "");

                // 2. Iteramos los valores del ENUM y filtramos en Java
            for (String valor : tipoEnum.split(",")) {
                        String valorLimpio = valor.trim().toUpperCase();

                        // Verificamos si el texto comienza con ADMIN o SUPER
                        boolean esAdmin = valorLimpio.startsWith("ADMIN");
                        boolean esSuper = valorLimpio.startsWith("SUPER");

                        // Si no empieza con ninguno de los dos, se agrega a la lista
                        if (!esAdmin && !esSuper) {
                            cargos.add(CargoEmpleado.desdeValorBD(valor));
                        }
                    }
            }
        } catch (Exception e) {
            System.out.println("Error al obtener los cargos del ENUM: " + e.getMessage());
        }

        return cargos;
    }
    //ME QUEDE AQUÍÍIIIIIIÍÍÍÍÍÍÍÍIÍÍÍÍÍÍÍÍÍÍÍÍ´´ÍÍÍI´´II´´IÍ
    @Override
    public List<CargoEmpleado> listarCargosDisponiblesAdmin() throws SQLException {
        // Se mantiene la estrategia original de leer el propio ENUM de la
        // columna cargoEM desde el catálogo de MySQL (SHOW COLUMNS), en vez
        // de hardcodear la lista de cargos en el código Java.
        List<CargoEmpleado> cargos = new ArrayList<>();
        try (PreparedStatement ps = cn().prepareStatement("SHOW COLUMNS FROM empleado LIKE 'cargoEM' ");
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                String tipoEnum = rs.getString("Type")
                        .replace("enum(", "")
                        .replace(")", "")
                        .replace("'", "");
                for (String valor : tipoEnum.split(",")) {
                    cargos.add(CargoEmpleado.desdeValorBD(valor));
                }
            }
        }
        return cargos;
    }
//===========================================================
    @Override
    public List<EmpleadoFila> listarPorSucursalSupervisor(int idSucursal) throws SQLException {
        try (PreparedStatement ps = cn().prepareStatement(
                "SELECT id_empleado, nombreEM, apellidoEM, dniEM, cargoEM, telefonoEM, sueldo " +
                "FROM empleado WHERE id_sucursal = ?")) {
            ps.setInt(1, idSucursal);
            try (ResultSet rs = ps.executeQuery()) {
                return mapearFilas(rs);
            }
        }
    }

    @Override
    public List<EmpleadoFila> listarPorSucursal(int idSucursal) throws SQLException {
        
        String sql = "SELECT id_empleado, nombreEM, apellidoEM, dniEM, cargoEM, telefonoEM, sueldo " +
                "FROM empleado " +
                "WHERE id_sucursal = ? " +
                "AND cargoEM NOT LIKE 'SUPER%' " +
                "AND cargoEM NOT LIKE 'ADMIN%'";
        try(PreparedStatement ps = cn().prepareStatement(sql)){
            ps.setInt(1, idSucursal);
            try(ResultSet rs = ps.executeQuery()){
                return mapearFilas(rs);
            }
        }
            
    }


    @Override
    public List<EmpleadoFila> buscarPorDni(int idSucursal, String dniParcial) throws SQLException {
        boolean sinFiltro = dniParcial == null || dniParcial.isBlank();
        String sql = "SELECT id_empleado, nombreEM, apellidoEM, dniEM, cargoEM, telefonoEM, sueldo " +
                "FROM empleado WHERE id_sucursal = ?" + (sinFiltro ? "" : " AND dniEM LIKE ?");
        try (PreparedStatement ps = cn().prepareStatement(sql)) {
            ps.setInt(1, idSucursal);
            if (!sinFiltro) ps.setString(2, "%" + dniParcial.trim() + "%");
            try (ResultSet rs = ps.executeQuery()) {
                return mapearFilas(rs);
            }
        }
    }

    @Override
    public EmpleadoFila obtenerPorId(int idEmpleado) throws SQLException {
        try (PreparedStatement ps = cn().prepareStatement(
                "SELECT id_empleado, nombreEM, apellidoEM, dniEM, cargoEM, telefonoEM, sueldo " +
                "FROM empleado WHERE id_empleado = ?")) {
            ps.setInt(1, idEmpleado);
            try (ResultSet rs = ps.executeQuery()) {
                List<EmpleadoFila> filas = mapearFilas(rs);
                return filas.isEmpty() ? null : filas.get(0);
            }
        }
    }

    private List<EmpleadoFila> mapearFilas(ResultSet rs) throws SQLException {
        List<EmpleadoFila> filas = new ArrayList<>();
        while (rs.next()) {
            filas.add(new EmpleadoFila(
                    rs.getInt("id_empleado"),
                    rs.getString("nombreEM"),
                    rs.getString("apellidoEM"),
                    rs.getString("dniEM"),
                    CargoEmpleado.desdeValorBD(rs.getString("cargoEM")),
                    rs.getString("telefonoEM"),
                    rs.getDouble("sueldo")
            ));
        }
        return filas;
    }

    @Override
    public int registrar(int idSucursal, String nombre, String apellido, String dni,
                          CargoEmpleado cargo, String telefono, float sueldo, String contrasenia) throws SQLException {
        return new UTPsports().Rempleado(idSucursal, nombre, apellido, dni,
                cargo.getValorBD(), telefono, sueldo, servicio.PasswordUtil.hash(contrasenia));
    }

    @Override
    public int actualizar(int idEmpleado, int idSucursal, String nombre, String apellido, String dni,
                           CargoEmpleado cargo, String telefono, float sueldo) throws SQLException {
        return new UTPsports().Mempleado(idEmpleado, idSucursal, nombre, apellido, dni,
                cargo.getValorBD(), telefono, sueldo);
    }

    @Override
    public int actualizarContrasenia(int idEmpleado, String contrasenia) throws SQLException {
        return new UTPsports().McontraseniaEmpleado(idEmpleado, servicio.PasswordUtil.hash(contrasenia));
    }

    @Override
    public int eliminar(int idEmpleado) throws SQLException {
        return new UTPsports().Eempleado(idEmpleado);
    }
}
