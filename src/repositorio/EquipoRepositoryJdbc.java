package repositorio;

import UTPsports.UTPsports;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EquipoRepositoryJdbc implements EquipoRepository {

    @Override
    public int obtenerOCrearInventario(int idSucursal) throws SQLException {
        int idInventario = new UTPsports().getIdInventario(idSucursal);
        if (idInventario == -1) {
            new UTPsports().Rinventario(idSucursal);
            idInventario = new UTPsports().getIdInventario(idSucursal);
        }
        return idInventario;
    }

    @Override
    public List<EquipoFila> listarPorSucursal(int idSucursal) throws SQLException {
        List<EquipoFila> filas = new ArrayList<>();
        try (PreparedStatement ps = new UTPsports().getConnection().prepareStatement(
                "SELECT eq.id_equipo, eq.nombreEQ, eq.marcaEQ, eq.stockEQ, " +
                "eq.estadoEQ, eq.fecha_adquisicion " +
                "FROM equipo eq " +
                "JOIN inventario i ON eq.id_inventario = i.id_inventario " +
                "WHERE i.id_sucursal = ? ORDER BY eq.nombreEQ")) {
            ps.setInt(1, idSucursal);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    filas.add(new EquipoFila(
                            rs.getInt("id_equipo"), rs.getString("nombreEQ"), rs.getString("marcaEQ"),
                            rs.getInt("stockEQ"), rs.getString("estadoEQ"), rs.getString("fecha_adquisicion")));
                }
            }
        }
        return filas;
    }

    @Override
    public int registrar(int idInventario, String nombre, int stock, String marca, String estado, String fechaAdquisicion) throws SQLException {
        return new UTPsports().Requipo(idInventario, nombre, stock, marca, estado, fechaAdquisicion);
    }

    @Override
    public int actualizar(int idEquipo, String nombre, int stock, String marca, String estado) throws SQLException {
        return new UTPsports().Mequipo(idEquipo, nombre, stock, marca, estado);
    }

    @Override
    public int eliminar(int idEquipo) throws SQLException {
        return new UTPsports().Eequipo(idEquipo);
    }
}
