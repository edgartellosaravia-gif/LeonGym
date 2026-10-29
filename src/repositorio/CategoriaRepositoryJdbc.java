package repositorio;

import UTPsports.UTPsports;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoriaRepositoryJdbc implements CategoriaRepository {

    @Override
    public List<CategoriaFila> listarTodas() throws SQLException {
        List<CategoriaFila> filas = new ArrayList<>();
        try (PreparedStatement ps = new UTPsports().getConnection().prepareStatement(
                "SELECT id_categoria, nombreCAT, descripcionCAT FROM categoria ORDER BY nombreCAT");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                filas.add(new CategoriaFila(
                        rs.getInt("id_categoria"), rs.getString("nombreCAT"), rs.getString("descripcionCAT")));
            }
        }
        return filas;
    }

    @Override
    public int registrar(String nombre, String descripcion) throws SQLException {
        return new UTPsports().Rcategoria(nombre, descripcion);
    }

    @Override
    public int actualizar(int idCategoria, String nombre, String descripcion) throws SQLException {
        return new UTPsports().Mcategoria(idCategoria, nombre, descripcion);
    }

    @Override
    public int eliminar(int idCategoria) throws SQLException {
        return new UTPsports().Ecategoria(idCategoria);
    }
}
