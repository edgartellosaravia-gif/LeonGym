package repositorio;

import UTPsports.UTPsports;
import modelo.CargoEmpleado;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Implementación JDBC de {@link ClaseRepository}.
 * <p>
 * IMPORTANTE: la conexión a la base de datos y la configuración de nube
 * (host, credenciales, licencia) NO se tocan; se sigue usando exactamente
 * {@link UTPsports#getConnection()} / {@link UTPsports#Rclase} y compañía,
 * tal como en el código original. Esta clase solo reubica el SQL que antes
 * vivía embebido dentro del formulario Swing, para separar responsabilidades
 * (UI vs acceso a datos).
 */
public class ClaseRepositoryJdbc implements ClaseRepository {

    private Connection cn() {
        return new UTPsports().getConnection();
    }

    @Override
    public List<String> listarTiposServicio() throws SQLException {
        List<String> tipos = new ArrayList<>();
        try (PreparedStatement ps = cn().prepareStatement(
                "SELECT descripcionSERV FROM servicio");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) tipos.add(rs.getString("descripcionSERV"));
        }
        return tipos;
    }

    @Override
    public Integer obtenerIdServicioPorDescripcion(String descripcion) throws SQLException {
        try (PreparedStatement ps = cn().prepareStatement(
                "SELECT id_servicio FROM servicio WHERE descripcionSERV = ?")) {
            ps.setString(1, descripcion);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt("id_servicio") : null;
            }
        }
    }

    @Override
    public LinkedHashMap<String, Integer> listarProfesoresHabilitados(int idSucursal) throws SQLException {
        // Regla profesor/mixto centralizada en CargoEmpleado; aquí solo se
        // parametriza el IN (?, ?) evitando cadenas SQL "mágicas".
        String[] cargos = CargoEmpleado.valoresHabilitadosParaClase();
        LinkedHashMap<String, Integer> resultado = new LinkedHashMap<>();
        try (PreparedStatement ps = cn().prepareStatement(
                "SELECT id_empleado, CONCAT(nombreEM,' ',apellidoEM,'-',cargoEM) AS nombre " +
                "FROM empleado WHERE id_sucursal = ? AND cargoEM IN (?, ?)")) {
            ps.setInt(1, idSucursal);
            ps.setString(2, cargos[0]);
            ps.setString(3, cargos[1]);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) resultado.put(rs.getString("nombre"), rs.getInt("id_empleado"));
            }
        }
        return resultado;
    }

    @Override
    public List<ClaseFila> listarClasesPorSucursal(int idSucursal) throws SQLException {
        List<ClaseFila> filas = new ArrayList<>();
        try (PreparedStatement ps = cn().prepareStatement(
                "SELECT cl.id_clase, cl.nombreCL, s.descripcionSERV, " +
                "cl.estadoCL, cl.horarioCL, " +
                "COALESCE(CONCAT(e.nombreEM,' ',e.apellidoEM), 'Sin asignar') AS profesor, " +
                "cl.cupoCL, cl.precioCL " +
                "FROM clase cl " +
                "JOIN servicio s ON cl.id_servicio = s.id_servicio " +
                "LEFT JOIN empleado e ON cl.id_empleado = e.id_empleado " +
                "WHERE cl.id_sucursal = ?")) {
            ps.setInt(1, idSucursal);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    filas.add(new ClaseFila(
                            rs.getInt("id_clase"),
                            rs.getString("nombreCL"),
                            rs.getString("descripcionSERV"),
                            rs.getString("estadoCL"),
                            rs.getString("horarioCL"),
                            rs.getString("profesor"),
                            rs.getInt("cupoCL"),
                            rs.getDouble("precioCL")
                    ));
                }
            }
        }
        return filas;
    }

    @Override
    public int registrarClase(int idServicio, int idSucursal, Integer idEmpleado, String nombre,
                               String horario, int cupo, float precio) throws SQLException {
        // No se usa new UTPsports().Rclase(...) porque ese INSERT exige un
        // id_empleado obligatorio (int primitivo); acá el profesor es
        // opcional, así que se arma el INSERT propio permitiendo NULL.
        try (PreparedStatement ps = cn().prepareStatement(
                "INSERT INTO clase(id_servicio,id_sucursal,id_empleado,nombreCL,horarioCL,cupoCL,precioCL,estadoCL) " +
                "VALUES(?,?,?,?,?,?,?,'ACTIVO')")) {
            ps.setInt(1, idServicio);
            ps.setInt(2, idSucursal);
            if (idEmpleado != null) ps.setInt(3, idEmpleado); else ps.setNull(3, java.sql.Types.INTEGER);
            ps.setString(4, nombre);
            ps.setString(5, horario);
            ps.setInt(6, cupo);
            ps.setFloat(7, precio);
            return ps.executeUpdate();
        }
    }

    @Override
    public int actualizarClase(int idClase, String nombre, String horario, int cupo,
                                float precio, String estado, Integer idEmpleado) throws SQLException {
        // No se usa new UTPsports().Mclase(...) porque ese UPDATE (en
        // UTPsports, sin modificar) nunca incluyó la columna id_empleado --
        // por eso el profesor asignado no se podía cambiar nunca desde
        // "Modificar". Tampoco admite NULL, y acá el profesor es opcional.
        try (PreparedStatement ps = cn().prepareStatement(
                "UPDATE clase SET nombreCL=?, horarioCL=?, cupoCL=?, precioCL=?, estadoCL=?, id_empleado=? " +
                "WHERE id_clase=?")) {
            ps.setString(1, nombre);
            ps.setString(2, horario);
            ps.setInt(3, cupo);
            ps.setFloat(4, precio);
            ps.setString(5, estado);
            if (idEmpleado != null) ps.setInt(6, idEmpleado); else ps.setNull(6, java.sql.Types.INTEGER);
            ps.setInt(7, idClase);
            return ps.executeUpdate();
        }
    }

    @Override
    public int eliminarClase(int idClase) throws SQLException {
        return new UTPsports().Eclase(idClase);
    }

    @Override
    public void vincularServicioConSucursal(int idSucursal, int idServicio) throws SQLException {
        new UTPsports().RsucursalServicioIgnore(idSucursal, idServicio);
    }
}
