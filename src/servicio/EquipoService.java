package servicio;

import repositorio.EquipoRepository;
import repositorio.EquipoRepository.EquipoFila;

import java.sql.SQLException;
import java.util.List;

public class EquipoService {

    private final EquipoRepository repositorio;

    public EquipoService(EquipoRepository repositorio) {
        this.repositorio = repositorio;
    }

    public int obtenerOCrearInventario(int idSucursal) throws SQLException {
        return repositorio.obtenerOCrearInventario(idSucursal);
    }

    public List<EquipoFila> listarPorSucursal(int idSucursal) throws SQLException {
        return repositorio.listarPorSucursal(idSucursal);
    }

    public Resultado registrar(int idInventario, String nombre, int stock, String marca, String estado, String fechaAdquisicion) {
        if (idInventario <= 0) {
            return Resultado.error("No hay inventario para esta sucursal.");
        }
        try {
            int filasAfectadas = repositorio.registrar(idInventario, nombre, stock, marca, estado, fechaAdquisicion);
            return filasAfectadas == 1 ? Resultado.ok("EQUIPO REGISTRADO") : Resultado.error("ERROR AL REGISTRAR");
        } catch (SQLException e) { return Resultado.error("Error al registrar el equipo: " + e.getMessage()); }
    }

    public Resultado actualizar(int idEquipo, String nombre, int stock, String marca, String estado) {
        try {
            int filasAfectadas = repositorio.actualizar(idEquipo, nombre, stock, marca, estado);
            return filasAfectadas == 1 ? Resultado.ok("EQUIPO MODIFICADO") : Resultado.error("ERROR AL MODIFICAR");
        } catch (SQLException e) { return Resultado.error("Error al modificar el equipo: " + e.getMessage()); }
    }

    public Resultado eliminar(int idEquipo) {
        try {
            int filasAfectadas = repositorio.eliminar(idEquipo);
            return filasAfectadas == 1 ? Resultado.ok("EQUIPO ELIMINADO") : Resultado.error("ERROR AL ELIMINAR");
        } catch (SQLException e) { return Resultado.errorEliminar(e, "el equipo"); }
    }
}
