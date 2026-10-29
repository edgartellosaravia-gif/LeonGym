package repositorio;

import java.sql.SQLException;
import java.util.List;

public interface EquipoRepository {

    /** Devuelve el id_inventario de una sucursal, creándolo si no existe (idempotente). */
    int obtenerOCrearInventario(int idSucursal) throws SQLException;

    List<EquipoFila> listarPorSucursal(int idSucursal) throws SQLException;

    int registrar(int idInventario, String nombre, int stock, String marca, String estado, String fechaAdquisicion) throws SQLException;

    int actualizar(int idEquipo, String nombre, int stock, String marca, String estado) throws SQLException;

    int eliminar(int idEquipo) throws SQLException;

    record EquipoFila(int idEquipo, String nombre, String marca, int stock, String estado, String fechaAdquisicion) {}
}
