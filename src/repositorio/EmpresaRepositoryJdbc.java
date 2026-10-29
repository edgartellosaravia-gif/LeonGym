package repositorio;

import UTPsports.UTPsports;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmpresaRepositoryJdbc implements EmpresaRepository {

    @Override
    public EmpresaDatos obtener() throws SQLException {
        try (PreparedStatement ps = new UTPsports().getConnection().prepareStatement(
                "SELECT id_empresa, nombreE, rucE, direccionE, telefonoE, emailE FROM empresa LIMIT 1");
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return new EmpresaDatos(
                        rs.getInt(1), rs.getString(2), rs.getString(3),
                        rs.getString(4), rs.getString(5), rs.getString(6));
            }
            return null;
        }
    }

    @Override
    public int actualizar(int idEmpresa, String nombre, String ruc, String direccion, String telefono, String email) throws SQLException {
        return new UTPsports().Mempresa(idEmpresa, nombre, ruc, direccion, telefono, email);
    }
}
