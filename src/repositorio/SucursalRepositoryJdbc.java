package repositorio;

import UTPsports.UTPsports;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SucursalRepositoryJdbc implements SucursalRepository {

    @Override
    public List<SucursalFila> listarTodas() throws SQLException {
        List<SucursalFila> filas = new ArrayList<>();
        try (PreparedStatement ps = new UTPsports().getConnection().prepareStatement(
                "SELECT s.id_sucursal, e.nombreE, s.nombreS, s.direccionS, s.telefonoS " +
                "FROM sucursal s " +
                "JOIN empresa e ON s.id_empresa = e.id_empresa");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                filas.add(new SucursalFila(
                        rs.getInt("id_sucursal"),
                        rs.getString("nombreE"),
                        rs.getString("nombreS"),
                        rs.getString("direccionS"),
                        rs.getString("telefonoS")
                ));
            }
        }
        return filas;
    }

    @Override
    public int registrar(int idEmpresa, String nombre, String direccion, String telefono) throws SQLException {
        return new UTPsports().Rsucursal(idEmpresa, nombre, direccion, telefono);
    }

    @Override
    public int actualizar(int idSucursal, int idEmpresa, String nombre, String direccion, String telefono) throws SQLException {
        return new UTPsports().Msucursal(idSucursal, idEmpresa, nombre, direccion, telefono);
    }

    @Override
    public int eliminar(int idSucursal) throws SQLException {
        return new UTPsports().Esucursal(idSucursal);
    }
}
