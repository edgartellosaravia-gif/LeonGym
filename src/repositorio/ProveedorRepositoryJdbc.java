package repositorio;

import UTPsports.UTPsports;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProveedorRepositoryJdbc implements ProveedorRepository {

    @Override
    public List<ProveedorFila> listarTodos() throws SQLException {
        List<ProveedorFila> filas = new ArrayList<>();
        try (PreparedStatement ps = new UTPsports().getConnection().prepareStatement(
                "SELECT id_proveedor, nombreP, rucP, direccionP, telefonoP, emailP FROM proveedor ORDER BY nombreP");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                filas.add(new ProveedorFila(
                        rs.getInt("id_proveedor"), rs.getString("nombreP"), rs.getString("rucP"),
                        rs.getString("direccionP"), rs.getString("telefonoP"), rs.getString("emailP")));
            }
        }
        return filas;
    }

    @Override
    public int registrar(String nombre, String ruc, String direccion, String telefono, String email) throws SQLException {
        return new UTPsports().Rproveedor(nombre, ruc, direccion, telefono, email);
    }

    @Override
    public int actualizar(int idProveedor, String nombre, String ruc, String direccion, String telefono, String email) throws SQLException {
        return new UTPsports().Mproveedor(idProveedor, nombre, ruc, direccion, telefono, email);
    }

    @Override
    public int eliminar(int idProveedor) throws SQLException {
        return new UTPsports().Eproveedor(idProveedor);
    }
}
