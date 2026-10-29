package UTPsports;

import java.lang.System.Logger;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class UTPsports {

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(UTPsports.class);

    // ── SINGLETON ─────────────────────────────────────────────────────────────
    private static Connection cnShared = null;
    private static final Object LOCK = new Object();

    private static final String URL =
            "jdbc:mysql://localhost:3306/UTPsports7"
            + "?useSSL=false"
            + "&serverTimezone=UTC"
            + "&allowPublicKeyRetrieval=true";

    private static final String USUARIO = "root";

    private static final String CONTRASEÑA = "root";

    Connection cn;
    PreparedStatement ps;
    ResultSet rs;

    public UTPsports() {
        cn = getSharedConnection();
    }

    public static Connection getSharedConnection() {

        synchronized (LOCK) {

            // Si la conexión todavía está funcionando,
            // reutilizarla.
            if (cnShared != null && conexionRealmenteViva(cnShared)) {
                return cnShared;
            }

            // Cerrar conexión anterior si existe
            if (cnShared != null) {
                try {
                    cnShared.close();
                } catch (Exception ignored) {
                }

                cnShared = null;
            }

            try {

                // Cargar Driver de MySQL
                Class.forName("com.mysql.cj.jdbc.Driver");

                // Conexión directa a UTPsports6
                cnShared = DriverManager.getConnection(
                        URL,
                        USUARIO,
                        CONTRASEÑA
                );

                LOG.info("Conexión exitosa a MySQL");
                LOG.info("Base de datos: UTPsports7");

            } catch (ClassNotFoundException e) {

                LOG.error("No se encontró el Driver de MySQL", e);

            } catch (SQLException e) {

                LOG.error("Error al conectar con MySQL", e);
            }

            return cnShared;
        }
    }

    /**
     * Prueba real de que la conexión sirve para ejecutar operaciones (no
     * solo que el objeto Java diga que no está cerrado). Se ejecuta un
     * SELECT 1 trivial porque es exactamente la capacidad que falla cuando
     * el servidor cerró la sesión pero el cliente todavía no se enteró:
     * "No operations allowed after connection closed". isClosed()/isValid()
     * por sí solos no detectan esto de forma confiable con
     * autoReconnect=true (ver documentación de MySQL Connector/J).
     */
    private static boolean conexionRealmenteViva(Connection cn) {
        try {
            if (cn.isClosed()) return false;
            try (Statement st = cn.createStatement()) {
                st.execute("SELECT 1");
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Método estático directo para obtener conexión sin instanciar
    public static Connection conn() { return getSharedConnection(); }

    public Connection getConnection() { return cn; }

    // ── LOGIN ─────────────────────────────────────────────────────────────────
    public ResultSet login(String dni, String contrasenia) {
        try {
            ps = cn.prepareStatement(
                "SELECT id_empleado, id_sucursal, nombreEM, apellidoEM, cargoEM " +
                "FROM empleado WHERE dniEM = ? AND contrasenia = ?"
            );
            ps.setString(1, dni); ps.setString(2, contrasenia);
            return ps.executeQuery();
        } catch (Exception e) { LOG.error("Error login: ", e); }
        return null;
    }

    public ResultSet loginCliente(String dniC) {
        try {
            ps = cn.prepareStatement(
                "SELECT id_cliente, id_sucursal, nombreC, apellidoC, dniC " +
                "FROM cliente WHERE dniC = ?"
            );
            ps.setString(1, dniC);
            return ps.executeQuery();
        } catch (Exception e) { LOG.error("Error loginCliente: ", e); }
        return null;
    }

    // ── EMPRESA ──────────────────────────────────────────────────────────────
    public int Rempresa(String nombreE, String rucE, String direccionE, String telefonoE, String emailE) {
        try {
            ps = cn.prepareStatement("INSERT INTO empresa(nombreE,rucE,direccionE,telefonoE,emailE) VALUES(?,?,?,?,?)");
            ps.setString(1,nombreE); ps.setString(2,rucE); ps.setString(3,direccionE);
            ps.setString(4,telefonoE); ps.setString(5,emailE);
            return ps.executeUpdate();
        } catch (Exception e) { LOG.error("Error Rempresa: ", e); return 0; }
    }

    public int Mempresa(int id, String nombreE, String rucE, String direccionE, String telefonoE, String emailE) {
        try {
            ps = cn.prepareStatement("UPDATE empresa SET nombreE=?,rucE=?,direccionE=?,telefonoE=?,emailE=? WHERE id_empresa=?");
            ps.setString(1,nombreE); ps.setString(2,rucE); ps.setString(3,direccionE);
            ps.setString(4,telefonoE); ps.setString(5,emailE); ps.setInt(6,id);
            return ps.executeUpdate();
        } catch (Exception e) { LOG.error("Error Mempresa: ", e); return 0; }
    }

    // ── SUCURSAL ─────────────────────────────────────────────────────────────
    public int Rsucursal(int idEmpresa, String nombreS, String direccionS, String telefonoS) {
        try {
            ps = cn.prepareStatement("INSERT INTO sucursal(id_empresa,nombreS,direccionS,telefonoS) VALUES(?,?,?,?)");
            ps.setInt(1,idEmpresa); ps.setString(2,nombreS); ps.setString(3,direccionS); ps.setString(4,telefonoS);
            return ps.executeUpdate();
        } catch (Exception e) { LOG.error("Error Rsucursal: ", e); return 0; }
    }

    public int Msucursal(int id, int idEmpresa, String nombreS, String direccionS, String telefonoS) {
        try {
            ps = cn.prepareStatement("UPDATE sucursal SET id_empresa=?,nombreS=?,direccionS=?,telefonoS=? WHERE id_sucursal=?");
            ps.setInt(1,idEmpresa); ps.setString(2,nombreS); ps.setString(3,direccionS); ps.setString(4,telefonoS); ps.setInt(5,id);
            return ps.executeUpdate();
        } catch (Exception e) { LOG.error("Error Msucursal: ", e); return 0; }
    }

    public int Esucursal(int id) {
        try {
            ps = cn.prepareStatement("DELETE FROM sucursal WHERE id_sucursal=?");
            ps.setInt(1,id); return ps.executeUpdate();
        } catch (Exception e) { LOG.error("Error Esucursal: ", e); return 0; }
    }

    // ── EMPLEADO ─────────────────────────────────────────────────────────────
    public int Rempleado(int idSucursal, String nombreEM, String apellidoEM, String dniEM,
                         String cargoEM, String telefonoEM, float sueldo, String contrasenia) {
        try {
            ps = cn.prepareStatement(
                "INSERT INTO empleado(id_sucursal,nombreEM,apellidoEM,dniEM,cargoEM,telefonoEM,sueldo,contrasenia) VALUES(?,?,?,?,?,?,?,?)");
            ps.setInt(1,idSucursal); ps.setString(2,nombreEM); ps.setString(3,apellidoEM);
            ps.setString(4,dniEM); ps.setString(5,cargoEM); ps.setString(6,telefonoEM);
            ps.setFloat(7,sueldo); ps.setString(8,contrasenia);
            return ps.executeUpdate();
        } catch (Exception e) { LOG.error("Error Rempleado: ", e); return 0; }
    }

    public int Mempleado(int id, int idSucursal, String nombreEM, String apellidoEM, String dniEM,
                         String cargoEM, String telefonoEM, float sueldo) {
        try {
            ps = cn.prepareStatement(
                "UPDATE empleado SET id_sucursal=?,nombreEM=?,apellidoEM=?,dniEM=?,cargoEM=?,telefonoEM=?,sueldo=? WHERE id_empleado=?");
            ps.setInt(1,idSucursal); ps.setString(2,nombreEM); ps.setString(3,apellidoEM);
            ps.setString(4,dniEM); ps.setString(5,cargoEM); ps.setString(6,telefonoEM);
            ps.setFloat(7,sueldo); ps.setInt(8,id);
            return ps.executeUpdate();
        } catch (Exception e) { LOG.error("Error Mempleado: ", e); return 0; }
    }

    public int McontraseniaEmpleado(int idEmpleado, String contrasenia) {
        try {
            ps = cn.prepareStatement("UPDATE empleado SET contrasenia=? WHERE id_empleado=?");
            ps.setString(1,contrasenia); ps.setInt(2,idEmpleado);
            return ps.executeUpdate();
        } catch (Exception e) { LOG.error("Error McontraseniaEmpleado: ", e); return 0; }
    }

    public int Eempleado(int id) {
        try {
            ps = cn.prepareStatement("DELETE FROM empleado WHERE id_empleado=?");
            ps.setInt(1,id); return ps.executeUpdate();
        } catch (Exception e) { LOG.error("Error Eempleado: ", e); return 0; }
    }

    // ── ASISTENCIA ───────────────────────────────────────────────────────────
    public int Rasistencia(int idEmpleado, String fechaEM, String horaEntrada) {
        try {
            ps = cn.prepareStatement("INSERT INTO asistencia(id_empleado,fechaEM,hora_entrada) VALUES(?,?,?)");
            ps.setInt(1,idEmpleado); ps.setString(2,fechaEM); ps.setString(3,horaEntrada);
            return ps.executeUpdate();
        } catch (Exception e) { LOG.error("Error Rasistencia: ", e); return 0; }
    }

    public int registrarSalida(int idEmpleado, String fechaEM, String horaSalida) {
        try {
            ps = cn.prepareStatement("UPDATE asistencia SET hora_salida=? WHERE id_empleado=? AND fechaEM=?");
            ps.setString(1,horaSalida); ps.setInt(2,idEmpleado); ps.setString(3,fechaEM);
            return ps.executeUpdate();
        } catch (Exception e) { LOG.error("Error registrarSalida: ", e); return 0; }
    }

    public ResultSet LasistenciaEmpleado(int idEmpleado) {
        try {
            ps = cn.prepareStatement(
                "SELECT fechaEM, hora_entrada, hora_salida FROM asistencia WHERE id_empleado=? ORDER BY fechaEM DESC");
            ps.setInt(1,idEmpleado); return ps.executeQuery();
        } catch (Exception e) { LOG.error("Error LasistenciaEmpleado: ", e); return null; }
    }

    public ResultSet LasistenciaSucursal(int idSucursal) {
        try {
            ps = cn.prepareStatement(
                "SELECT CONCAT(e.nombreEM,' ',e.apellidoEM) AS empleado, a.fechaEM, a.hora_entrada, a.hora_salida " +
                "FROM asistencia a JOIN empleado e ON a.id_empleado=e.id_empleado " +
                "WHERE e.id_sucursal=? ORDER BY a.fechaEM DESC, e.apellidoEM");
            ps.setInt(1,idSucursal); return ps.executeQuery();
        } catch (Exception e) { LOG.error("Error LasistenciaSucursal: ", e); return null; }
    }

    // ── CLIENTE ──────────────────────────────────────────────────────────────
    public int Rcliente(int idSucursal, String nombreC, String apellidoC, String dniC, String telefonoC, String emailC) {
        try {
            ps = cn.prepareStatement("INSERT INTO cliente(id_sucursal,nombreC,apellidoC,dniC,telefonoC,emailC) VALUES(?,?,?,?,?,?)");
            ps.setInt(1,idSucursal); ps.setString(2,nombreC); ps.setString(3,apellidoC);
            ps.setString(4,dniC); ps.setString(5,telefonoC); ps.setString(6,emailC);
            return ps.executeUpdate();
        } catch (Exception e) { LOG.error("Error Rcliente: ", e); return 0; }
    }

    public int Mcliente(int id, String nombreC, String apellidoC, String dniC, String telefonoC, String emailC) {
        try {
            ps = cn.prepareStatement("UPDATE cliente SET nombreC=?,apellidoC=?,dniC=?,telefonoC=?,emailC=? WHERE id_cliente=?");
            ps.setString(1,nombreC); ps.setString(2,apellidoC); ps.setString(3,dniC);
            ps.setString(4,telefonoC); ps.setString(5,emailC); ps.setInt(6,id);
            return ps.executeUpdate();
        } catch (Exception e) { LOG.error("Error Mcliente: ", e); return 0; }
    }

    public int Ecliente(int id) {
        try {
            ps = cn.prepareStatement("DELETE FROM cliente WHERE id_cliente=?");
            ps.setInt(1,id); return ps.executeUpdate();
        } catch (Exception e) { LOG.error("Error Ecliente: ", e); return 0; }
    }

    // ── CATEGORIA ────────────────────────────────────────────────────────────
    public int Rcategoria(String nombreCAT, String descripcionCAT) {
        try {
            ps = cn.prepareStatement("INSERT INTO categoria(nombreCAT,descripcionCAT) VALUES(?,?)");
            ps.setString(1,nombreCAT); ps.setString(2,descripcionCAT);
            return ps.executeUpdate();
        } catch (Exception e) { LOG.error("Error Rcategoria: ", e); return 0; }
    }

    public int Mcategoria(int id, String nombreCAT, String descripcionCAT) {
        try {
            ps = cn.prepareStatement("UPDATE categoria SET nombreCAT=?,descripcionCAT=? WHERE id_categoria=?");
            ps.setString(1,nombreCAT); ps.setString(2,descripcionCAT); ps.setInt(3,id);
            return ps.executeUpdate();
        } catch (Exception e) { LOG.error("Error Mcategoria: ", e); return 0; }
    }

    public int Ecategoria(int id) {
        try {
            ps = cn.prepareStatement("DELETE FROM categoria WHERE id_categoria=?");
            ps.setInt(1,id); return ps.executeUpdate();
        } catch (Exception e) { LOG.error("Error Ecategoria: ", e); return 0; }
    }

    // ── PROVEEDOR ────────────────────────────────────────────────────────────
    public int Rproveedor(String nombreP, String rucP, String direccionP, String telefonoP, String emailP) {
        try {
            ps = cn.prepareStatement("INSERT INTO proveedor(nombreP,rucP,direccionP,telefonoP,emailP) VALUES(?,?,?,?,?)");
            ps.setString(1,nombreP); ps.setString(2,rucP); ps.setString(3,direccionP);
            ps.setString(4,telefonoP); ps.setString(5,emailP);
            return ps.executeUpdate();
        } catch (Exception e) { LOG.error("Error Rproveedor: ", e); return 0; }
    }

    public int Mproveedor(int id, String nombreP, String rucP, String direccionP, String telefonoP, String emailP) {
        try {
            ps = cn.prepareStatement("UPDATE proveedor SET nombreP=?,rucP=?,direccionP=?,telefonoP=?,emailP=? WHERE id_proveedor=?");
            ps.setString(1,nombreP); ps.setString(2,rucP); ps.setString(3,direccionP);
            ps.setString(4,telefonoP); ps.setString(5,emailP); ps.setInt(6,id);
            return ps.executeUpdate();
        } catch (Exception e) { LOG.error("Error Mproveedor: ", e); return 0; }
    }

    public int Eproveedor(int id) {
        try {
            ps = cn.prepareStatement("DELETE FROM proveedor WHERE id_proveedor=?");
            ps.setInt(1,id); return ps.executeUpdate();
        } catch (Exception e) { LOG.error("Error Eproveedor: ", e); return 0; }
    }

    // ── INVENTARIO ───────────────────────────────────────────────────────────
    public int Rinventario(int idSucursal) {
        try {
            ps = cn.prepareStatement("INSERT INTO inventario(id_sucursal) VALUES(?)");
            ps.setInt(1,idSucursal); return ps.executeUpdate();
        } catch (Exception e) { LOG.error("Error Rinventario: ", e); return 0; }
    }

    public int getIdInventario(int idSucursal) {
        try {
            ps = cn.prepareStatement("SELECT id_inventario FROM inventario WHERE id_sucursal=? LIMIT 1");
            ps.setInt(1,idSucursal); rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("id_inventario");
        } catch (Exception e) { LOG.error("Error getIdInventario: ", e); }
        return -1;
    }

    // ── PRODUCTO ─────────────────────────────────────────────────────────────
    public int Rproducto(int idCategoria, int idProveedor, int idInventario,
                         String nombrePRO, int stockPRO, String marcaPRO, float precioPRO) {
        try {
            ps = cn.prepareStatement(
                "INSERT INTO producto(id_categoria,id_proveedor,id_inventario,nombrePRO,stockPRO,marcaPRO,precioPRO) VALUES(?,?,?,?,?,?,?)");
            ps.setInt(1,idCategoria); ps.setInt(2,idProveedor); ps.setInt(3,idInventario);
            ps.setString(4,nombrePRO); ps.setInt(5,stockPRO); ps.setString(6,marcaPRO); ps.setFloat(7,precioPRO);
            return ps.executeUpdate();
        } catch (Exception e) { LOG.error("Error Rproducto: ", e); return 0; }
    }

    public int Mproducto(int id, int idCategoria, int idProveedor, String nombrePRO,
                         int stockPRO, String marcaPRO, float precioPRO) {
        try {
            ps = cn.prepareStatement(
                "UPDATE producto SET id_categoria=?,id_proveedor=?,nombrePRO=?,stockPRO=?,marcaPRO=?,precioPRO=? WHERE id_producto=?");
            ps.setInt(1,idCategoria); ps.setInt(2,idProveedor); ps.setString(3,nombrePRO);
            ps.setInt(4,stockPRO); ps.setString(5,marcaPRO); ps.setFloat(6,precioPRO); ps.setInt(7,id);
            return ps.executeUpdate();
        } catch (Exception e) { LOG.error("Error Mproducto: ", e); return 0; }
    }

    public int Eproducto(int id) {
        try {
            ps = cn.prepareStatement("DELETE FROM producto WHERE id_producto=?");
            ps.setInt(1,id); return ps.executeUpdate();
        } catch (Exception e) { LOG.error("Error Eproducto: ", e); return 0; }
    }

    // ── EQUIPO ───────────────────────────────────────────────────────────────
    public int Requipo(int idInventario, String nombreEQ, int stockEQ, String marcaEQ, String estadoEQ, String fechaAdq) {
        try {
            ps = cn.prepareStatement(
                "INSERT INTO equipo(id_inventario,nombreEQ,stockEQ,marcaEQ,estadoEQ,fecha_adquisicion) VALUES(?,?,?,?,?,?)");
            ps.setInt(1,idInventario); ps.setString(2,nombreEQ); ps.setInt(3,stockEQ);
            ps.setString(4,marcaEQ); ps.setString(5,estadoEQ); ps.setString(6,fechaAdq);
            return ps.executeUpdate();
        } catch (Exception e) { LOG.error("Error Requipo: ", e); return 0; }
    }

    public int Mequipo(int id, String nombreEQ, int stockEQ, String marcaEQ, String estadoEQ) {
        try {
            ps = cn.prepareStatement("UPDATE equipo SET nombreEQ=?,stockEQ=?,marcaEQ=?,estadoEQ=? WHERE id_equipo=?");
            ps.setString(1,nombreEQ); ps.setInt(2,stockEQ); ps.setString(3,marcaEQ);
            ps.setString(4,estadoEQ); ps.setInt(5,id);
            return ps.executeUpdate();
        } catch (Exception e) { LOG.error("Error Mequipo: ", e); return 0; }
    }

    public int Eequipo(int id) {
        try {
            ps = cn.prepareStatement("DELETE FROM equipo WHERE id_equipo=?");
            ps.setInt(1,id); return ps.executeUpdate();
        } catch (Exception e) { LOG.error("Error Eequipo: ", e); return 0; }
    }

    // ── SERVICIO ─────────────────────────────────────────────────────────────
    public int Rservicio(String descripcionSERV) {
        try {
            ps = cn.prepareStatement("INSERT INTO servicio(descripcionSERV) VALUES(?)");
            ps.setString(1,descripcionSERV); return ps.executeUpdate();
        } catch (Exception e) { LOG.error("Error Rservicio: ", e); return 0; }
    }

    public int Mservicio(int id, String descripcionSERV) {
        try {
            ps = cn.prepareStatement("UPDATE servicio SET descripcionSERV=? WHERE id_servicio=?");
            ps.setString(1,descripcionSERV); ps.setInt(2,id); return ps.executeUpdate();
        } catch (Exception e) { LOG.error("Error Mservicio: ", e); return 0; }
    }

    public int Eservicio(int id) {
        try {
            ps = cn.prepareStatement("DELETE FROM servicio WHERE id_servicio=?");
            ps.setInt(1,id); return ps.executeUpdate();
        } catch (Exception e) { LOG.error("Error Eservicio: ", e); return 0; }
    }

    // ── SUCURSAL_SERVICIO ────────────────────────────────────────────────────
    public int RsucursalServicio(int idSucursal, int idServicio) {
        try {
            ps = cn.prepareStatement("INSERT INTO sucursal_servicio(id_sucursal,id_servicio) VALUES(?,?)");
            ps.setInt(1,idSucursal); ps.setInt(2,idServicio); return ps.executeUpdate();
        } catch (Exception e) { LOG.error("Error RsucursalServicio: ", e); return 0; }
    }

    public int RsucursalServicioIgnore(int idSucursal, int idServicio) {
        try {
            ps = cn.prepareStatement("INSERT IGNORE INTO sucursal_servicio(id_sucursal,id_servicio) VALUES(?,?)");
            ps.setInt(1,idSucursal); ps.setInt(2,idServicio); return ps.executeUpdate();
        } catch (Exception e) { LOG.error("Error RsucursalServicioIgnore: ", e); return 0; }
    }

    // ── PLAN ─────────────────────────────────────────────────────────────────
    public int Rplan(int idServicio, int idSucursal, String nombrePL, int duracionMesesPL, float precioPL) {
        try {
            ps = cn.prepareStatement(
                "INSERT INTO plan(id_servicio,id_sucursal,nombrePL,duracion_mesesPL,precioPL,estadoPL) VALUES(?,?,?,?,?,'ACTIVO')");
            ps.setInt(1,idServicio); ps.setInt(2,idSucursal); ps.setString(3,nombrePL);
            ps.setInt(4,duracionMesesPL); ps.setFloat(5,precioPL);
            return ps.executeUpdate();
        } catch (Exception e) { LOG.error("Error Rplan: ", e); return 0; }
    }

    public int Mplan(int id, String nombrePL, int duracionMesesPL, float precioPL, String estadoPL) {
        try {
            ps = cn.prepareStatement(
                "UPDATE plan SET nombrePL=?,duracion_mesesPL=?,precioPL=?,estadoPL=? WHERE id_plan=?");
            ps.setString(1,nombrePL); ps.setInt(2,duracionMesesPL);
            ps.setFloat(3,precioPL); ps.setString(4,estadoPL); ps.setInt(5,id);
            return ps.executeUpdate();
        } catch (Exception e) { LOG.error("Error Mplan: ", e); return 0; }
    }

    public int Eplan(int id) {
        try {
            ps = cn.prepareStatement("DELETE FROM plan WHERE id_plan=?");
            ps.setInt(1,id); return ps.executeUpdate();
        } catch (Exception e) { LOG.error("Error Eplan: ", e); return 0; }
    }

    // ── CLASE ────────────────────────────────────────────────────────────────
    public int Rclase(int idServicio, int idSucursal, int idEmpleado, String nombreCL,
                      String horarioCL, int cupoCL, float precioCL) {
        try {
            ps = cn.prepareStatement(
                "INSERT INTO clase(id_servicio,id_sucursal,id_empleado,nombreCL,horarioCL,cupoCL,precioCL,estadoCL) VALUES(?,?,?,?,?,?,?,'ACTIVO')");
            ps.setInt(1,idServicio); ps.setInt(2,idSucursal); ps.setInt(3,idEmpleado);
            ps.setString(4,nombreCL); ps.setString(5,horarioCL); ps.setInt(6,cupoCL); ps.setFloat(7,precioCL);
            return ps.executeUpdate();
        } catch (Exception e) { LOG.error("Error Rclase: ", e); return 0; }
    }

    public int Mclase(int id, String nombreCL, String horarioCL, int cupoCL, float precioCL, String estadoCL) {
        try {
            ps = cn.prepareStatement(
                "UPDATE clase SET nombreCL=?,horarioCL=?,cupoCL=?,precioCL=?,estadoCL=? WHERE id_clase=?");
            ps.setString(1,nombreCL); ps.setString(2,horarioCL);
            ps.setInt(3,cupoCL); ps.setFloat(4,precioCL); ps.setString(5,estadoCL); ps.setInt(6,id);
            return ps.executeUpdate();
        } catch (Exception e) { LOG.error("Error Mclase: ", e); return 0; }
    }

    public int Eclase(int id) {
        try {
            ps = cn.prepareStatement("DELETE FROM clase WHERE id_clase=?");
            ps.setInt(1,id); return ps.executeUpdate();
        } catch (Exception e) { LOG.error("Error Eclase: ", e); return 0; }
    }

    public int reducirCupoCL(int idClase) {
        try {
            ps = cn.prepareStatement("UPDATE clase SET cupoCL=cupoCL-1 WHERE id_clase=? AND cupoCL>0");
            ps.setInt(1,idClase); return ps.executeUpdate();
        } catch (Exception e) { LOG.error("Error reducirCupoCL: ", e); return 0; }
    }

    public int aumentarCupoCL(int idClase) {
        try {
            ps = cn.prepareStatement("UPDATE clase SET cupoCL=cupoCL+1 WHERE id_clase=?");
            ps.setInt(1,idClase); return ps.executeUpdate();
        } catch (Exception e) { LOG.error("Error aumentarCupoCL: ", e); return 0; }
    }

    // ── VENTA ────────────────────────────────────────────────────────────────
    public int Rventa(int idCliente, int idEmpleado, int idSucursal, float totalV) {
        try {
            ps = cn.prepareStatement("INSERT INTO venta(id_cliente,id_empleado,id_sucursal,totalV) VALUES(?,?,?,?)");
            ps.setInt(1,idCliente); ps.setInt(2,idEmpleado); ps.setInt(3,idSucursal); ps.setFloat(4,totalV);
            return ps.executeUpdate();
        } catch (Exception e) { LOG.error("Error Rventa: ", e); return 0; }
    }

    // ── DETALLE_VENTA ────────────────────────────────────────────────────────
    public int RdetalleVenta(int idVenta, String tipoDV, Integer idProducto,
                             Integer idPlan, Integer idClase, int cantidadDV, float precioUnit) {
        try {
            ps = cn.prepareStatement(
                "INSERT INTO detalle_venta(id_venta,tipoDV,id_producto,id_plan,id_clase,cantidadDV,precio_unit) VALUES(?,?,?,?,?,?,?)");
            ps.setInt(1,idVenta); ps.setString(2,tipoDV);
            if (idProducto!=null) ps.setInt(3,idProducto); else ps.setNull(3,Types.INTEGER);
            if (idPlan!=null)     ps.setInt(4,idPlan);     else ps.setNull(4,Types.INTEGER);
            if (idClase!=null)    ps.setInt(5,idClase);    else ps.setNull(5,Types.INTEGER);
            ps.setInt(6,cantidadDV); ps.setFloat(7,precioUnit);
            return ps.executeUpdate();
        } catch (Exception e) { LOG.error("Error RdetalleVenta: ", e); return 0; }
    }

    // ── PAGO ─────────────────────────────────────────────────────────────────
    public int Rpago(int idVenta, float montoP, String metodoP, String estadoP) {
        try {
            ps = cn.prepareStatement("INSERT INTO pago(id_venta,montoP,metodoP,estadoP) VALUES(?,?,?,?)");
            ps.setInt(1,idVenta); ps.setFloat(2,montoP); ps.setString(3,metodoP); ps.setString(4,estadoP);
            return ps.executeUpdate();
        } catch (Exception e) { LOG.error("Error Rpago: ", e); return 0; }
    }

    public int Mpago(int id, float montoP, String metodoP, String estadoP) {
        try {
            ps = cn.prepareStatement("UPDATE pago SET montoP=?,metodoP=?,estadoP=? WHERE id_pago=?");
            ps.setFloat(1,montoP); ps.setString(2,metodoP); ps.setString(3,estadoP); ps.setInt(4,id);
            return ps.executeUpdate();
        } catch (Exception e) { LOG.error("Error Mpago: ", e); return 0; }
    }

    // ── FACTURA ──────────────────────────────────────────────────────────────
    public int Rfactura(int idVenta, String numeroF, float totalF, float igv) {
        try {
            ps = cn.prepareStatement("INSERT INTO factura(id_venta,numeroF,totalF,igv) VALUES(?,?,?,?)");
            ps.setInt(1,idVenta); ps.setString(2,numeroF); ps.setFloat(3,totalF); ps.setFloat(4,igv);
            return ps.executeUpdate();
        } catch (Exception e) { LOG.error("Error Rfactura: ", e); return 0; }
    }

    // ── MEMBRESIA ────────────────────────────────────────────────────────────
    public int Rmembresia(int idCliente, int idPlan, int idVenta, String fechaInicio, String fechaFin) {
        try {
            ps = cn.prepareStatement(
                "INSERT INTO membresia(id_cliente,id_plan,id_venta,fecha_inicio,fecha_fin) VALUES(?,?,?,?,?)",
                Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1,idCliente); ps.setInt(2,idPlan); ps.setInt(3,idVenta);
            ps.setString(4,fechaInicio); ps.setString(5,fechaFin);
            ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            return rs.next() ? rs.getInt(1) : 0;
        } catch (Exception e) { LOG.error("Error Rmembresia: ", e); return 0; }
    }

    public int RmembresiaSinPlan(int idCliente, int idVenta, String fecha) {
        try {
            ps = cn.prepareStatement(
                "INSERT INTO membresia(id_cliente,id_plan,id_venta,fecha_inicio,fecha_fin,estado) VALUES(?,NULL,?,?,?,'ACTIVA')",
                Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1,idCliente); ps.setInt(2,idVenta); ps.setString(3,fecha); ps.setString(4,fecha);
            ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            return rs.next() ? rs.getInt(1) : 0;
        } catch (Exception e) { LOG.error("Error RmembresiaSinPlan: ", e); return 0; }
    }

    public int Mmembresia(int id, int idPlan, String fechaInicio, String fechaFin, String estado) {
        try {
            ps = cn.prepareStatement(
                "UPDATE membresia SET id_plan=?,fecha_inicio=?,fecha_fin=?,estado=? WHERE id_membresia=?");
            ps.setInt(1,idPlan); ps.setString(2,fechaInicio);
            ps.setString(3,fechaFin); ps.setString(4,estado); ps.setInt(5,id);
            return ps.executeUpdate();
        } catch (Exception e) { LOG.error("Error Mmembresia: ", e); return 0; }
    }

    public int Emembresia(int id) {
        try {
            ps = cn.prepareStatement("DELETE FROM membresia WHERE id_membresia=?");
            ps.setInt(1,id); return ps.executeUpdate();
        } catch (Exception e) { LOG.error("Error Emembresia: ", e); return 0; }
    }

    // ── MEMBRESIA_CLASE ──────────────────────────────────────────────────────
    public int RmembresiaClase(int idMembresia, int idClase) {
        try {
            ps = cn.prepareStatement("INSERT INTO membresia_clase(id_membresia,id_clase) VALUES(?,?)");
            ps.setInt(1,idMembresia); ps.setInt(2,idClase); return ps.executeUpdate();
        } catch (Exception e) { LOG.error("Error RmembresiaClase: ", e); return 0; }
    }

    public int EmembresiaClase(int idMembresia) {
        try {
            ps = cn.prepareStatement("DELETE FROM membresia_clase WHERE id_membresia=?");
            ps.setInt(1,idMembresia); return ps.executeUpdate();
        } catch (Exception e) { LOG.error("Error EmembresiaClase: ", e); return 0; }
    }

    // ── RUTINA ───────────────────────────────────────────────────────────────
    public int Rrutina(int idCliente, String nombreRutina, String descripcion, String diaSemana) {
        try {
            ps = cn.prepareStatement("INSERT INTO rutina(id_cliente,nombreRutina,descripcion,diaSemana) VALUES(?,?,?,?)");
            ps.setInt(1,idCliente); ps.setString(2,nombreRutina);
            ps.setString(3,descripcion); ps.setString(4,diaSemana);
            return ps.executeUpdate();
        } catch (Exception e) { LOG.error("Error Rrutina: ", e); return 0; }
    }

    public int Mrutina(int id, String nombreRutina, String descripcion) {
        try {
            ps = cn.prepareStatement("UPDATE rutina SET nombreRutina=?,descripcion=? WHERE id_rutina=?");
            ps.setString(1,nombreRutina); ps.setString(2,descripcion); ps.setInt(3,id);
            return ps.executeUpdate();
        } catch (Exception e) { LOG.error("Error Mrutina: ", e); return 0; }
    }

    public int Erutina(int id) {
        try {
            ps = cn.prepareStatement("DELETE FROM rutina WHERE id_rutina=?");
            ps.setInt(1,id); return ps.executeUpdate();
        } catch (Exception e) { LOG.error("Error Erutina: ", e); return 0; }
    }

    public ResultSet LrutinaCliente(int idCliente) {
        try {
            ps = cn.prepareStatement(
                "SELECT id_rutina, diaSemana, nombreRutina, descripcion FROM rutina WHERE id_cliente=? " +
                "ORDER BY FIELD(diaSemana,'LUNES','MARTES','MIERCOLES','JUEVES','VIERNES','SABADO','DOMINGO')");
            ps.setInt(1,idCliente); return ps.executeQuery();
        } catch (Exception e) { LOG.error("Error LrutinaCliente: ", e); return null; }
    }
}