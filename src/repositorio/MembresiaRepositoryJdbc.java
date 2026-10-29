package repositorio;

import UTPsports.UTPsports;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MembresiaRepositoryJdbc implements MembresiaRepository {

    private Connection cn() {
        return new UTPsports().getConnection();
    }

    @Override
    public List<PlanActivoFila> listarPlanesActivos(int idSucursal) throws Exception {
        List<PlanActivoFila> filas = new ArrayList<>();
        try (PreparedStatement ps = cn().prepareStatement(
                "SELECT p.id_plan, p.nombrePL, p.duracion_mesesPL, p.precioPL, s.descripcionSERV " +
                "FROM plan p " +
                "JOIN servicio s ON p.id_servicio = s.id_servicio " +
                "WHERE p.estadoPL = 'ACTIVO' AND p.id_sucursal = ?")) {
            ps.setInt(1, idSucursal);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    filas.add(new PlanActivoFila(rs.getInt("id_plan"), rs.getString("nombrePL"),
                            rs.getInt("duracion_mesesPL"), rs.getDouble("precioPL"), rs.getString("descripcionSERV")));
                }
            }
        }
        return filas;
    }

    @Override
    public List<ClaseActivaFila> listarClasesActivas(int idSucursal) throws Exception {
        List<ClaseActivaFila> filas = new ArrayList<>();
        try (PreparedStatement ps = cn().prepareStatement(
                "SELECT cl.id_clase, cl.nombreCL, cl.horarioCL, cl.cupoCL, cl.precioCL " +
                "FROM clase cl " +
                "JOIN servicio s ON cl.id_servicio = s.id_servicio " +
                "WHERE cl.estadoCL = 'ACTIVO' AND cl.id_sucursal = ?")) {
            ps.setInt(1, idSucursal);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    filas.add(new ClaseActivaFila(rs.getInt("id_clase"), rs.getString("nombreCL"),
                            rs.getString("horarioCL"), rs.getInt("cupoCL"), rs.getDouble("precioCL")));
                }
            }
        }
        return filas;
    }

    @Override
    public List<MembresiaFila> listarPorCliente(int idCliente) throws Exception {
        List<MembresiaFila> filas = new ArrayList<>();
        try (PreparedStatement ps = cn().prepareStatement(
                "SELECT m.id_membresia, p.nombrePL, m.fecha_inicio, m.fecha_fin, " +
                "GROUP_CONCAT(DISTINCT cl.nombreCL SEPARATOR ', ') AS clases, " +
                "v.totalV, m.estado, " +
                "CONCAT(ent.nombreEM,' ',ent.apellidoEM) AS entrenador " +
                "FROM membresia m " +
                "LEFT JOIN plan p ON m.id_plan = p.id_plan " +
                "JOIN venta v ON m.id_venta = v.id_venta " +
                "LEFT JOIN membresia_clase mc ON m.id_membresia = mc.id_membresia " +
                "LEFT JOIN clase cl ON mc.id_clase = cl.id_clase " +
                "LEFT JOIN empleado ent ON m.id_entrenador = ent.id_empleado " +
                "WHERE m.id_cliente = ? " +
                "GROUP BY m.id_membresia, p.nombrePL, m.fecha_inicio, m.fecha_fin, v.totalV, m.estado, entrenador " +
                "ORDER BY m.fecha_inicio DESC")) {
            ps.setInt(1, idCliente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    filas.add(new MembresiaFila(
                            rs.getInt("id_membresia"), rs.getString("nombrePL"), rs.getString("fecha_inicio"),
                            rs.getString("fecha_fin"), rs.getString("clases"), rs.getDouble("totalV"),
                            rs.getString("estado"), rs.getString("entrenador")));
                }
            }
        }
        return filas;
    }

    @Override
    public List<Integer> listarIdsClaseDeMembresia(int idMembresia) throws Exception {
        List<Integer> ids = new ArrayList<>();
        try (PreparedStatement ps = cn().prepareStatement(
                "SELECT id_clase FROM membresia_clase WHERE id_membresia = ?")) {
            ps.setInt(1, idMembresia);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) ids.add(rs.getInt("id_clase"));
            }
        }
        return ids;
    }

    @Override
    public Optional<LocalDate> obtenerFechaFinMembresia(int idMembresia) throws Exception {
        try (PreparedStatement ps = cn().prepareStatement(
                "SELECT fecha_fin FROM membresia WHERE id_membresia=?")) {
            ps.setInt(1, idMembresia);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(rs.getDate("fecha_fin").toLocalDate());
                return Optional.empty();
            }
        }
    }

    @Override
    public int registrarMembresia(int idCliente, int idEmpleado, int idSucursal, int idPlan, float precioPlan,
                                   List<Integer> idsClase, Map<Integer, Float> preciosClase,
                                   float totalVenta, LocalDate fechaInicio, LocalDate fechaFin,
                                   Integer idEntrenador) throws Exception {
        UTPsports co = new UTPsports();
        Connection con = co.getConnection();
        try {
            con.setAutoCommit(false);

            int idVenta = insertarVenta(con, idCliente, idEmpleado, idSucursal, totalVenta);
            co.RdetalleVenta(idVenta, "PLAN", null, idPlan, null, 1, precioPlan);
            for (int idCL : idsClase) {
                co.RdetalleVenta(idVenta, "CLASE", null, null, idCL, 1, preciosClase.get(idCL));
            }
            co.Rpago(idVenta, totalVenta, "EFECTIVO", "PENDIENTE");
            co.Rmembresia(idCliente, idPlan, idVenta, fechaInicio.toString(), fechaFin.toString());

            int idMembresia = 0;
            try (PreparedStatement psMem = con.prepareStatement(
                    "SELECT id_membresia FROM membresia WHERE id_venta = ?")) {
                psMem.setInt(1, idVenta);
                try (ResultSet rsMem = psMem.executeQuery()) {
                    if (rsMem.next()) idMembresia = rsMem.getInt("id_membresia");
                }
            }
            if (idMembresia != 0) {
                asignarEntrenador(con, idMembresia, idEntrenador);
                for (int idCL : idsClase) {
                    co.RmembresiaClase(idMembresia, idCL);
                    if (co.reducirCupoCL(idCL) == 0) {
                        throw new Exception("La clase seleccionada ya no tiene cupos.");
                    }
                }
            }

            con.commit();
            return idMembresia;
        } catch (Exception e) {
            rollback(con);
            throw e;
        } finally {
            restaurarAutoCommit(con);
        }
    }

    @Override
    public int renovarMembresia(int idMembresiaActual, int idCliente, int idEmpleado, int idSucursal,
                                 int idPlan, int duracionMeses, float precioPlan,
                                 List<Integer> idsClase, Map<Integer, Float> preciosClase,
                                 Integer idEntrenador) throws Exception {
        UTPsports co = new UTPsports();
        Connection con = co.getConnection();
        try {
            con.setAutoCommit(false);

            LocalDate fechaInicio = obtenerFechaFinMembresia(idMembresiaActual)
                    .map(f -> f.plusDays(1))
                    .orElse(LocalDate.now());
            LocalDate fechaFin = fechaInicio.plusMonths(duracionMeses);

            float totalVenta = precioPlan;
            for (int idCL : idsClase) totalVenta += preciosClase.get(idCL);

            int idVenta = insertarVenta(con, idCliente, idEmpleado, idSucursal, totalVenta);
            co.RdetalleVenta(idVenta, "PLAN", null, idPlan, null, 1, precioPlan);
            for (int idCL : idsClase) {
                co.RdetalleVenta(idVenta, "CLASE", null, null, idCL, 1, preciosClase.get(idCL));
            }
            co.Rpago(idVenta, totalVenta, "EFECTIVO", "PENDIENTE");

            int idNuevaMembresia = co.Rmembresia(idCliente, idPlan, idVenta, fechaInicio.toString(), fechaFin.toString());
            if (idNuevaMembresia == 0) {
                throw new Exception("No se pudo registrar la nueva membresía.");
            }
            asignarEntrenador(con, idNuevaMembresia, idEntrenador);

            for (int idCL : idsClase) {
                co.RmembresiaClase(idNuevaMembresia, idCL);
                if (co.reducirCupoCL(idCL) == 0) {
                    throw new Exception("La clase seleccionada ya no tiene cupos.");
                }
            }

            con.commit();
            return idNuevaMembresia;
        } catch (Exception e) {
            rollback(con);
            throw e;
        } finally {
            restaurarAutoCommitYCerrar(con);
        }
    }

    @Override
    public void modificarMembresia(int idMembresia, int idPlan, LocalDate fechaInicio, LocalDate fechaFin,
                                    List<Integer> idsClaseNuevas, Map<Integer, Float> preciosClase,
                                    float totalVenta, Integer idEntrenador) throws Exception {
        modificarMembresiaInterno(idMembresia, idPlan, fechaInicio, fechaFin, idsClaseNuevas, totalVenta, idEntrenador);
    }

    @Override
    public void modificarSinPlan(int idMembresia, LocalDate fechaInicio, LocalDate fechaFin,
                                  List<Integer> idsClaseNuevas, Map<Integer, Float> preciosClase,
                                  float totalVenta, Integer idEntrenador) throws Exception {
        modificarMembresiaInterno(idMembresia, null, fechaInicio, fechaFin, idsClaseNuevas, totalVenta, idEntrenador);
    }

    /**
     * Lógica compartida entre modificar una membresía con plan y una "sin
     * plan". {@code idPlanONull == null} significa "no tocar la columna
     * id_plan" -- se usa {@code new UTPsports().Mmembresia(...)} solo
     * cuando sí hay un plan, porque ese método siempre exige un idPlan;
     * para el caso sin plan se actualiza fecha/estado con SQL propio que
     * ni menciona esa columna.
     */
    private void modificarMembresiaInterno(int idMembresia, Integer idPlanONull, LocalDate fechaInicio,
                                            LocalDate fechaFin, List<Integer> idsClaseNuevas,
                                            float totalVenta, Integer idEntrenador) throws Exception {
        UTPsports co = new UTPsports();
        Connection con = co.getConnection();
        try {
            con.setAutoCommit(false);

            // Devolver cupos de clases anteriores
            for (int idClaseAnterior : listarIdsClaseDeMembresia(idMembresia)) {
                co.aumentarCupoCL(idClaseAnterior);
            }

            // Eliminar clases anteriores
            co.EmembresiaClase(idMembresia);

            // Actualizar membresía
            if (idPlanONull != null) {
                co.Mmembresia(idMembresia, idPlanONull, fechaInicio.toString(), fechaFin.toString(), "ACTIVA");
            } else {
                try (PreparedStatement psFechas = con.prepareStatement(
                        "UPDATE membresia SET fecha_inicio = ?, fecha_fin = ?, estado = ? WHERE id_membresia = ?")) {
                    psFechas.setString(1, fechaInicio.toString());
                    psFechas.setString(2, fechaFin.toString());
                    psFechas.setString(3, "ACTIVA");
                    psFechas.setInt(4, idMembresia);
                    psFechas.executeUpdate();
                }
            }
            asignarEntrenador(con, idMembresia, idEntrenador);

            // Insertar nuevas clases + reducir cupos
            for (int idCL : idsClaseNuevas) {
                co.RmembresiaClase(idMembresia, idCL);
                if (co.reducirCupoCL(idCL) == 0) {
                    throw new Exception("La clase seleccionada ya no tiene cupos.");
                }
            }

            // Actualizar total venta
            try (PreparedStatement psVenta = con.prepareStatement(
                    "UPDATE venta v JOIN membresia m ON v.id_venta = m.id_venta " +
                    "SET v.totalV = ? WHERE m.id_membresia = ?")) {
                psVenta.setFloat(1, totalVenta);
                psVenta.setInt(2, idMembresia);
                psVenta.executeUpdate();
            }

            con.commit();
        } catch (Exception e) {
            rollback(con);
            throw e;
        } finally {
            restaurarAutoCommit(con);
        }
    }

    @Override
    public void registrarSinPlan(int idCliente, int idEmpleado, int idSucursal, String fecha,
                                  List<Integer> idsClase, Map<Integer, Float> preciosClase,
                                  Integer idEntrenador) throws Exception {
        UTPsports co = new UTPsports();
        Connection con = co.getConnection();
        try {
            con.setAutoCommit(false);

            float totalVenta = 0f;
            for (int idCL : idsClase) totalVenta += preciosClase.get(idCL);

            int idVenta = insertarVenta(con, idCliente, idEmpleado, idSucursal, totalVenta);
            for (int idCL : idsClase) {
                co.RdetalleVenta(idVenta, "CLASE", null, null, idCL, 1, preciosClase.get(idCL));
            }
            co.Rpago(idVenta, totalVenta, "EFECTIVO", "PENDIENTE");
            co.RmembresiaSinPlan(idCliente, idVenta, fecha);

            int idMembresia = 0;
            try (PreparedStatement psMem = con.prepareStatement(
                    "SELECT id_membresia FROM membresia WHERE id_venta = ?")) {
                psMem.setInt(1, idVenta);
                try (ResultSet rsMem = psMem.executeQuery()) {
                    if (rsMem.next()) idMembresia = rsMem.getInt("id_membresia");
                }
            }
            if (idMembresia != 0) {
                asignarEntrenador(con, idMembresia, idEntrenador);
                for (int idCL : idsClase) {
                    co.RmembresiaClase(idMembresia, idCL);
                    if (co.reducirCupoCL(idCL) == 0) {
                        throw new Exception("La clase seleccionada ya no tiene cupos.");
                    }
                }
            }

            con.commit();
        } catch (Exception e) {
            rollback(con);
            throw e;
        } finally {
            restaurarAutoCommit(con);
        }
    }

    /** Guarda (o borra, si es null) el entrenador opcionalmente asignado a la membresía. */
    private void asignarEntrenador(Connection con, int idMembresia, Integer idEntrenador) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(
                "UPDATE membresia SET id_entrenador = ? WHERE id_membresia = ?")) {
            if (idEntrenador != null) ps.setInt(1, idEntrenador); else ps.setNull(1, java.sql.Types.INTEGER);
            ps.setInt(2, idMembresia);
            ps.executeUpdate();
        }
    }

    // ── Auxiliares privados ──────────────────────────────────────────────

    private int insertarVenta(Connection con, int idCliente, int idEmpleado, int idSucursal, float totalVenta) throws Exception {
        try (PreparedStatement psVenta = con.prepareStatement(
                "INSERT INTO venta(id_cliente,id_empleado,id_sucursal,totalV) VALUES(?,?,?,?)",
                Statement.RETURN_GENERATED_KEYS)) {
            psVenta.setInt(1, idCliente);
            psVenta.setInt(2, idEmpleado);
            psVenta.setInt(3, idSucursal);
            psVenta.setFloat(4, totalVenta);
            psVenta.executeUpdate();
            try (ResultSet rsVenta = psVenta.getGeneratedKeys()) {
                return rsVenta.next() ? rsVenta.getInt(1) : 0;
            }
        }
    }

    private void rollback(Connection con) {
        try {
            if (con != null) con.rollback();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void restaurarAutoCommit(Connection con) {
        try {
            if (con != null) con.setAutoCommit(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /** Igual que {@link #restaurarAutoCommit}, pero además cierra la conexión (comportamiento original de "renovar"). */
    private void restaurarAutoCommitYCerrar(Connection con) {
        try {
            if (con != null) {
                con.setAutoCommit(true);
                con.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
