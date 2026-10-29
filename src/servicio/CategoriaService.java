package servicio;

import repositorio.CategoriaRepository;
import repositorio.CategoriaRepository.CategoriaFila;

import java.sql.SQLException;
import java.util.List;

public class CategoriaService {

    private final CategoriaRepository repositorio;

    public CategoriaService(CategoriaRepository repositorio) {
        this.repositorio = repositorio;
    }

    public List<CategoriaFila> listarTodas() throws SQLException {
        return repositorio.listarTodas();
    }

    public Resultado registrar(String nombre, String descripcion) {
        try {
            int filasAfectadas = repositorio.registrar(nombre, descripcion);
            return filasAfectadas == 1 ? Resultado.ok("CATEGORÍA REGISTRADA") : Resultado.error("ERROR AL REGISTRAR");
        } catch (SQLException e) { return Resultado.error("Error al registrar la categoría: " + e.getMessage()); }
    }

    public Resultado actualizar(int idCategoria, String nombre, String descripcion) {
        try {
            int filasAfectadas = repositorio.actualizar(idCategoria, nombre, descripcion);
            return filasAfectadas == 1 ? Resultado.ok("CATEGORÍA MODIFICADA") : Resultado.error("ERROR AL MODIFICAR");
        } catch (SQLException e) { return Resultado.error("Error al modificar la categoría: " + e.getMessage()); }
    }

    public Resultado eliminar(int idCategoria) {
        try {
            int filasAfectadas = repositorio.eliminar(idCategoria);
            return filasAfectadas == 1 ? Resultado.ok("CATEGORÍA ELIMINADA") : Resultado.error("ERROR AL ELIMINAR");
        } catch (SQLException e) { return Resultado.errorEliminar(e, "la categoría"); }
    }
}
