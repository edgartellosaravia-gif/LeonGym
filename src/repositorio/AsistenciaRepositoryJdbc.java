package repositorio;

import UTPsports.UTPsports;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AsistenciaRepositoryJdbc implements AsistenciaRepository {

    @Override
    public List<AsistenciaEmpleadoFila> listarPorEmpleado(int idEmpleado) throws SQLException {
        List<AsistenciaEmpleadoFila> filas = new ArrayList<>();
        ResultSet rs = new UTPsports().LasistenciaEmpleado(idEmpleado);
        while (rs != null && rs.next()) {
            filas.add(new AsistenciaEmpleadoFila(
                    rs.getString("fechaEM"), rs.getString("hora_entrada"), rs.getString("hora_salida")));
        }
        return filas;
    }

    @Override
    public List<AsistenciaAdminFila> listarPorSucursal(int idSucursal) throws SQLException {
        List<AsistenciaAdminFila> filas = new ArrayList<>();
        ResultSet rs = new UTPsports().LasistenciaSucursal(idSucursal);
        while (rs != null && rs.next()) {
            filas.add(new AsistenciaAdminFila(
                    rs.getString("empleado"), rs.getString("fechaEM"),
                    rs.getString("hora_entrada"), rs.getString("hora_salida")));
        }
        return filas;
    }

    @Override
    public List<AsistenciaAdminFila> buscarPorSucursalYDni(int idSucursal, String dniParcial) throws SQLException {
        boolean sinFiltro = dniParcial == null || dniParcial.isBlank();
        String sql = "SELECT CONCAT(e.nombreEM,' ',e.apellidoEM) AS empleado, " +
                "a.fechaEM, a.hora_entrada, a.hora_salida " +
                "FROM asistencia a " +
                "JOIN empleado e ON a.id_empleado = e.id_empleado " +
                "WHERE e.id_sucursal = ? " +
                (sinFiltro ? "" : "AND e.dniEM LIKE ? ") +
                "ORDER BY a.fechaEM DESC";
        List<AsistenciaAdminFila> filas = new ArrayList<>();
        try (PreparedStatement ps = new UTPsports().getConnection().prepareStatement(sql)) {
            ps.setInt(1, idSucursal);
            if (!sinFiltro) ps.setString(2, "%" + dniParcial.trim() + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    filas.add(new AsistenciaAdminFila(
                            rs.getString("empleado"), rs.getString("fechaEM"),
                            rs.getString("hora_entrada"), rs.getString("hora_salida")));
                }
            }
        }
        return filas;
    }

    /**
     * REGLA DE NEGOCIO (permitir 2+ entradas/salidas por día): en vez de
     * delegar en {@link UTPsports#Rasistencia}, que dependía de la
     * restricción UNIQUE(id_empleado, fechaEM) para impedir una segunda
     * fila el mismo día, esta implementación solo bloquea una entrada
     * nueva si ya existe una entrada de HOY sin salida registrada. Así se
     * permiten tantos pares entrada/salida por día como se necesiten (en
     * la práctica, normalmente 2), sin tocar UTPsports.java.
     */
    @Override
    public int registrarEntrada(int idEmpleado, String fecha, String hora) throws SQLException {
        if (tieneEntradaAbierta(idEmpleado, fecha)) {
            return 0;
        }
        try (PreparedStatement ps = new UTPsports().getConnection().prepareStatement(
                "INSERT INTO asistencia(id_empleado, fechaEM, hora_entrada) VALUES (?, ?, ?)")) {
            ps.setInt(1, idEmpleado);
            ps.setString(2, fecha);
            ps.setString(3, hora);
            return ps.executeUpdate();
        }
    }

    private boolean tieneEntradaAbierta(int idEmpleado, String fecha) throws SQLException {
        try (PreparedStatement ps = new UTPsports().getConnection().prepareStatement(
                "SELECT 1 FROM asistencia WHERE id_empleado = ? AND fechaEM = ? AND hora_salida IS NULL")) {
            ps.setInt(1, idEmpleado);
            ps.setString(2, fecha);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    /** Cierra la entrada abierta más reciente del día (la que no tiene salida todavía). */
    @Override
    public int registrarSalida(int idEmpleado, String fecha, String hora) throws SQLException {
        try (PreparedStatement ps = new UTPsports().getConnection().prepareStatement(
                "UPDATE asistencia SET hora_salida = ? " +
                "WHERE id_empleado = ? AND fechaEM = ? AND hora_salida IS NULL " +
                "ORDER BY hora_entrada DESC LIMIT 1")) {
            ps.setString(1, hora);
            ps.setInt(2, idEmpleado);
            ps.setString(3, fecha);
            return ps.executeUpdate();
        }
    }
}
