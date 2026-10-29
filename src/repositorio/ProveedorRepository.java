package repositorio;

import java.sql.SQLException;
import java.util.List;

public interface ProveedorRepository {

    List<ProveedorFila> listarTodos() throws SQLException;

    int registrar(String nombre, String ruc, String direccion, String telefono, String email) throws SQLException;

    int actualizar(int idProveedor, String nombre, String ruc, String direccion, String telefono, String email) throws SQLException;

    int eliminar(int idProveedor) throws SQLException;

    record ProveedorFila(int idProveedor, String nombre, String ruc, String direccion, String telefono, String email) {}
}
