package repositorio;

import UTPsports.UTPsports;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClienteRepositoryJdbc implements ClienteRepository {

    private static final String COLUMNAS =
            "id_cliente, nombreC, apellidoC, dniC, telefonoC, emailC";

    private ClienteFila mapear(ResultSet rs) throws SQLException {
        return new ClienteFila(
                rs.getInt("id_cliente"), rs.getString("nombreC"), rs.getString("apellidoC"),
                rs.getString("dniC"), rs.getString("telefonoC"), rs.getString("emailC"));
    }

    @Override
    public List<ClienteFila> listarPorSucursal(int idSucursal) throws SQLException {
        List<ClienteFila> filas = new ArrayList<>();
        try (PreparedStatement ps = new UTPsports().getConnection().prepareStatement(
                "SELECT " + COLUMNAS + " FROM cliente WHERE id_sucursal = ?")) {
            ps.setInt(1, idSucursal);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) filas.add(mapear(rs));
            }
        }
        return filas;
    }

    @Override
    public List<ClienteFila> buscarPorSucursalYNombre(int idSucursal, String nombreParcial) throws SQLException {
        boolean sinFiltro = nombreParcial == null || nombreParcial.isBlank();
        String sql = "SELECT " + COLUMNAS + " FROM cliente WHERE id_sucursal = ?" +
                (sinFiltro ? "" : " AND nombreC LIKE ?");
        List<ClienteFila> filas = new ArrayList<>();
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
    public ClienteFila obtenerPorId(int idCliente) throws SQLException {
        try (PreparedStatement ps = new UTPsports().getConnection().prepareStatement(
                "SELECT nombreC, apellidoC, dniC, telefonoC, emailC FROM cliente WHERE id_cliente=?")) {
            ps.setInt(1, idCliente);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return new ClienteFila(idCliente, rs.getString("nombreC"), rs.getString("apellidoC"),
                        rs.getString("dniC"), rs.getString("telefonoC"), rs.getString("emailC"));
            }
        }
    }

    @Override
    public int registrar(int idSucursal, String nombre, String apellido, String dni, String telefono, String email) throws SQLException {
        return new UTPsports().Rcliente(idSucursal, nombre, apellido, dni, telefono, email);
    }

    @Override
    public int actualizar(int idCliente, String nombre, String apellido, String dni, String telefono, String email) throws SQLException {
        return new UTPsports().Mcliente(idCliente, nombre, apellido, dni, telefono, email);
    }

    @Override
    public int eliminar(int idCliente) throws SQLException {
        return new UTPsports().Ecliente(idCliente);
    }
}
