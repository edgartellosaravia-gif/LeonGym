package repositorio;

import java.sql.SQLException;
import java.util.List;

public interface ProductoRepository {

    List<ProductoFila> listarPorSucursal(int idSucursal) throws SQLException;

    /** Igual que {@link #listarPorSucursal}, filtrando por nombre (parcial); nombreParcial vacío = sin filtro. */
    List<ProductoFila> buscarPorSucursalYNombre(int idSucursal, String nombreParcial) throws SQLException;

    /**
     * Productos con stock disponible (&gt; 0) de una sucursal, para el punto
     * de venta. {@code idCategoria} = 0 significa "todas las categorías".
     */
    List<ProductoDisponibleFila> listarDisponiblesParaVenta(int idSucursal, int idCategoria) throws SQLException;

    int registrar(int idCategoria, int idProveedor, int idInventario, String nombre,
                   int stock, String marca, float precio) throws SQLException;

    int actualizar(int idProducto, int idCategoria, int idProveedor, String nombre,
                    int stock, String marca, float precio) throws SQLException;

    int eliminar(int idProducto) throws SQLException;

    record ProductoFila(int idProducto, String nombre, double precio, int stock,
                         String categoria, String proveedor, String marca) {}

    record ProductoDisponibleFila(int idProducto, String nombre, String marca,
                                   double precio, int stock, String categoria) {}
}
