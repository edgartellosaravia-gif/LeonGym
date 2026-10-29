package repositorio;

import java.sql.SQLException;
import java.util.List;

public interface SucursalRepository {

    List<SucursalFila> listarTodas() throws SQLException;

    int registrar(int idEmpresa, String nombre, String direccion, String telefono) throws SQLException;

    int actualizar(int idSucursal, int idEmpresa, String nombre, String direccion, String telefono) throws SQLException;

    int eliminar(int idSucursal) throws SQLException;

    record SucursalFila(int idSucursal, String nombreEmpresa, String nombreSucursal,
                         String direccion, String telefono) {}
}
