package repositorio;

import UTPsports.UTPsports;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductoRepositoryJdbc implements ProductoRepository {

    private static final String SQL_BASE =
            "SELECT p.id_producto, p.nombrePRO, p.precioPRO, p.stockPRO, " +
            "c.nombreCAT, pr.nombreP, p.marcaPRO " +
            "FROM producto p " +
            "JOIN categoria c ON p.id_categoria = c.id_categoria " +
            "JOIN proveedor pr ON p.id_proveedor = pr.id_proveedor " +
            "JOIN inventario i ON p.id_inventario = i.id_inventario " +
            "WHERE i.id_sucursal = ?";

    private ProductoFila mapear(ResultSet rs) throws SQLException {
        return new ProductoFila(
                rs.getInt("id_producto"), rs.getString("nombrePRO"), rs.getDouble("precioPRO"),
                rs.getInt("stockPRO"), rs.getString("nombreCAT"), rs.getString("nombreP"), rs.getString("marcaPRO"));
    }

    @Override
    public List<ProductoFila> listarPorSucursal(int idSucursal) throws SQLException {
        List<ProductoFila> filas = new ArrayList<>();
        try (PreparedStatement ps = new UTPsports().getConnection().prepareStatement(SQL_BASE)) {
            ps.setInt(1, idSucursal);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) filas.add(mapear(rs));
            }
        }
        return filas;
    }

    @Override
    public List<ProductoFila> buscarPorSucursalYNombre(int idSucursal, String nombreParcial) throws SQLException {
        boolean sinFiltro = nombreParcial == null || nombreParcial.isBlank();
        String sql = SQL_BASE + (sinFiltro ? "" : " AND p.nombrePRO LIKE ?");
        List<ProductoFila> filas = new ArrayList<>();
        try (PreparedStatement ps = new UTPsports().getConnection().prepareStatement(sql)) {
            ps.setInt(1, idSucursal);
            if (!sinFiltro) ps.setString(2, "%" + nombreParcial.trim() + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) filas.add(mapear(rs));
            }
        }
        return filas;
    }

    @Override
    public List<ProductoDisponibleFila> listarDisponiblesParaVenta(int idSucursal, int idCategoria) throws SQLException {
        String sql = "SELECT p.id_producto, p.nombrePRO, p.marcaPRO, " +
                     "p.precioPRO, p.stockPRO, c.nombreCAT " +
                     "FROM producto p " +
                     "JOIN categoria c ON p.id_categoria = c.id_categoria " +
                     "JOIN inventario i ON p.id_inventario = i.id_inventario " +
                     "WHERE p.stockPRO > 0 AND i.id_sucursal = ?" +
                     (idCategoria > 0 ? " AND p.id_categoria = ?" : "");
        List<ProductoDisponibleFila> filas = new ArrayList<>();
        try (PreparedStatement ps = new UTPsports().getConnection().prepareStatement(sql)) {
            ps.setInt(1, idSucursal);
            if (idCategoria > 0) ps.setInt(2, idCategoria);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    filas.add(new ProductoDisponibleFila(
                            rs.getInt("id_producto"), rs.getString("nombrePRO"), rs.getString("marcaPRO"),
                            rs.getDouble("precioPRO"), rs.getInt("stockPRO"), rs.getString("nombreCAT")));
                }
            }
        }
        return filas;
    }

    @Override
    public int registrar(int idCategoria, int idProveedor, int idInventario, String nombre,
                          int stock, String marca, float precio) throws SQLException {
        return new UTPsports().Rproducto(idCategoria, idProveedor, idInventario, nombre, stock, marca, precio);
    }

    @Override
    public int actualizar(int idProducto, int idCategoria, int idProveedor, String nombre,
                           int stock, String marca, float precio) throws SQLException {
        return new UTPsports().Mproducto(idProducto, idCategoria, idProveedor, nombre, stock, marca, precio);
    }

    @Override
    public int eliminar(int idProducto) throws SQLException {
        return new UTPsports().Eproducto(idProducto);
    }
}
