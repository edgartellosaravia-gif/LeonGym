package repositorio;

import java.sql.SQLException;
import java.util.List;

public interface AsistenciaRepository {

    List<AsistenciaEmpleadoFila> listarPorEmpleado(int idEmpleado) throws SQLException;

    /** Todas las asistencias de la sucursal (vista administrativa, sin filtro). */
    List<AsistenciaAdminFila> listarPorSucursal(int idSucursal) throws SQLException;

    /** Igual que {@link #listarPorSucursal}, pero filtrando por DNI (parcial) del empleado. */
    List<AsistenciaAdminFila> buscarPorSucursalYDni(int idSucursal, String dniParcial) throws SQLException;

    int registrarEntrada(int idEmpleado, String fecha, String hora) throws SQLException;

    int registrarSalida(int idEmpleado, String fecha, String hora) throws SQLException;

    record AsistenciaEmpleadoFila(String fecha, String horaEntrada, String horaSalida) {}

    record AsistenciaAdminFila(String empleado, String fecha, String horaEntrada, String horaSalida) {}
}
