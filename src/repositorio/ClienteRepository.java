package repositorio;

import java.sql.SQLException;
import java.util.List;

public interface ClienteRepository {

    List<ClienteFila> listarPorSucursal(int idSucursal) throws SQLException;

    List<ClienteFila> buscarPorSucursalYNombre(int idSucursal, String nombreParcial) throws SQLException;

    ClienteFila obtenerPorId(int idCliente) throws SQLException;

    int registrar(int idSucursal, String nombre, String apellido, String dni, String telefono, String email) throws SQLException;

    int actualizar(int idCliente, String nombre, String apellido, String dni, String telefono, String email) throws SQLException;

    int eliminar(int idCliente) throws SQLException;

    record ClienteFila(int idCliente, String nombre, String apellido, String dni, String telefono, String email) {}
}
