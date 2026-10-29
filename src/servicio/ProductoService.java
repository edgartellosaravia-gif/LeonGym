package servicio;

import repositorio.ProductoRepository;
import repositorio.ProductoRepository.ProductoDisponibleFila;
import repositorio.ProductoRepository.ProductoFila;

import java.sql.SQLException;
import java.util.List;

public class ProductoService {

    private final ProductoRepository repositorio;

    public ProductoService(ProductoRepository repositorio) {
        this.repositorio = repositorio;
    }

    public List<ProductoFila> listarPorSucursal(int idSucursal) throws SQLException {
        return repositorio.listarPorSucursal(idSucursal);
    }

    public List<ProductoFila> buscarPorSucursalYNombre(int idSucursal, String nombreParcial) throws SQLException {
        return repositorio.buscarPorSucursalYNombre(idSucursal, nombreParcial);
    }

    public List<ProductoDisponibleFila> listarDisponiblesParaVenta(int idSucursal, int idCategoria) throws SQLException {
        return repositorio.listarDisponiblesParaVenta(idSucursal, idCategoria);
    }

    public Resultado registrar(int idCategoria, int idProveedor, int idInventario, String nombre,
                                int stock, String marca, float precio) {
        if (idInventario <= 0) {
            return Resultado.error("No hay inventario para esta sucursal.");
        }
        try {
            int filasAfectadas = repositorio.registrar(idCategoria, idProveedor, idInventario, nombre, stock, marca, precio);
            return filasAfectadas == 1 ? Resultado.ok("PRODUCTO REGISTRADO") : Resultado.error("ERROR AL REGISTRAR");
        } catch (SQLException e) { return Resultado.error("Error al registrar el producto: " + e.getMessage()); }
    }

    public Resultado actualizar(int idProducto, int idCategoria, int idProveedor, String nombre,
                                 int stock, String marca, float precio) {
        try {
            int filasAfectadas = repositorio.actualizar(idProducto, idCategoria, idProveedor, nombre, stock, marca, precio);
            return filasAfectadas == 1 ? Resultado.ok("PRODUCTO MODIFICADO") : Resultado.error("ERROR AL MODIFICAR");
        } catch (SQLException e) { return Resultado.error("Error al modificar el producto: " + e.getMessage()); }
    }

    public Resultado eliminar(int idProducto) {
        try {
            int filasAfectadas = repositorio.eliminar(idProducto);
            return filasAfectadas == 1 ? Resultado.ok("PRODUCTO ELIMINADO") : Resultado.error("ERROR AL ELIMINAR");
        } catch (SQLException e) { return Resultado.errorEliminar(e, "el producto"); }
    }
}
