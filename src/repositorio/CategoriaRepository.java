package repositorio;

import java.sql.SQLException;
import java.util.List;

public interface CategoriaRepository {

    List<CategoriaFila> listarTodas() throws SQLException;

    int registrar(String nombre, String descripcion) throws SQLException;

    int actualizar(int idCategoria, String nombre, String descripcion) throws SQLException;

    int eliminar(int idCategoria) throws SQLException;

    record CategoriaFila(int idCategoria, String nombre, String descripcion) {}
}
