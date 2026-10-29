package servicio;

import repositorio.AsistenciaRepository;
import repositorio.AsistenciaRepository.AsistenciaAdminFila;
import repositorio.AsistenciaRepository.AsistenciaEmpleadoFila;

import java.sql.SQLException;
import java.util.List;

public class AsistenciaService {

    private final AsistenciaRepository repositorio;

    public AsistenciaService(AsistenciaRepository repositorio) {
        this.repositorio = repositorio;
    }

    public List<AsistenciaEmpleadoFila> listarPorEmpleado(int idEmpleado) throws SQLException {
        return repositorio.listarPorEmpleado(idEmpleado);
    }

    public List<AsistenciaAdminFila> listarPorSucursal(int idSucursal) throws SQLException {
        return repositorio.listarPorSucursal(idSucursal);
    }

    public List<AsistenciaAdminFila> buscarPorSucursalYDni(int idSucursal, String dniParcial) throws SQLException {
        return repositorio.buscarPorSucursalYDni(idSucursal, dniParcial);
    }

    public Resultado registrarEntrada(int idEmpleado, String fecha, String hora) {
        try {
            int filasAfectadas = repositorio.registrarEntrada(idEmpleado, fecha, hora);
            return filasAfectadas > 0
                    ? Resultado.ok("Entrada registrada: " + fecha + " a las " + hora)
                    : Resultado.error("No se pudo registrar. ¿Ya tienes una entrada sin registrar salida?");
        } catch (SQLException e) {
            return Resultado.error("Error: " + e);
        }
    }

    public Resultado registrarSalida(int idEmpleado, String fecha, String hora) {
        try {
            int filasAfectadas = repositorio.registrarSalida(idEmpleado, fecha, hora);
            return filasAfectadas > 0
                    ? Resultado.ok("Salida registrada: " + fecha + " a las " + hora)
                    : Resultado.error("No se pudo registrar salida. ¿Registraste entrada hoy?");
        } catch (SQLException e) {
            return Resultado.error("Error: " + e);
        }
    }
}
