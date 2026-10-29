package servicio;

import repositorio.ProveedorRepository;
import repositorio.ProveedorRepository.ProveedorFila;

import java.sql.SQLException;
import java.util.List;

public class ProveedorService {

    private final ProveedorRepository repositorio;

    public ProveedorService(ProveedorRepository repositorio) {
        this.repositorio = repositorio;
    }

    public List<ProveedorFila> listarTodos() throws SQLException {
        return repositorio.listarTodos();
    }

    public Resultado registrar(String nombre, String ruc, String direccion, String telefono, String email) {
        try {
            int filasAfectadas = repositorio.registrar(nombre, ruc, direccion, telefono, email);
            return filasAfectadas == 1 ? Resultado.ok("PROVEEDOR REGISTRADO") : Resultado.error("ERROR AL REGISTRAR");
        } catch (SQLException e) { return Resultado.error("Error al registrar el proveedor: " + e.getMessage()); }
    }

    public Resultado actualizar(int idProveedor, String nombre, String ruc, String direccion, String telefono, String email) {
        try {
            int filasAfectadas = repositorio.actualizar(idProveedor, nombre, ruc, direccion, telefono, email);
            return filasAfectadas == 1 ? Resultado.ok("PROVEEDOR MODIFICADO") : Resultado.error("ERROR AL MODIFICAR");
        } catch (SQLException e) { return Resultado.error("Error al modificar el proveedor: " + e.getMessage()); }
    }

    public Resultado eliminar(int idProveedor) {
        try {
            int filasAfectadas = repositorio.eliminar(idProveedor);
            return filasAfectadas == 1 ? Resultado.ok("PROVEEDOR ELIMINADO") : Resultado.error("ERROR AL ELIMINAR");
        } catch (SQLException e) { return Resultado.errorEliminar(e, "el proveedor"); }
    }
}
