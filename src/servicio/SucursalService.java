package servicio;

import repositorio.SucursalRepository;
import repositorio.SucursalRepository.SucursalFila;

import java.sql.SQLException;
import java.util.List;

public class SucursalService {

    /** La empresa dueña de todas las sucursales (sistema mono-empresa, ver tabla EMPRESA). */
    private static final int ID_EMPRESA_UNICA = 1;

    private final SucursalRepository repositorio;

    public SucursalService(SucursalRepository repositorio) {
        this.repositorio = repositorio;
    }

    public List<SucursalFila> listarTodas() throws SQLException {
        return repositorio.listarTodas();
    }

    public Resultado registrar(String nombre, String direccion, String telefono) {
        try {
            int filasAfectadas = repositorio.registrar(ID_EMPRESA_UNICA, nombre, direccion, telefono);
            return filasAfectadas == 1
                    ? Resultado.ok("SUCURSAL REGISTRADA")
                    : Resultado.error("ERROR AL REGISTRAR");
        } catch (SQLException e) {
            return Resultado.error("Error al registrar la sucursal: " + e.getMessage());
        }
    }

    public Resultado actualizar(int idSucursal, String nombre, String direccion, String telefono) {
        try {
            int filasAfectadas = repositorio.actualizar(idSucursal, ID_EMPRESA_UNICA, nombre, direccion, telefono);
            return filasAfectadas == 1
                    ? Resultado.ok("SUCURSAL MODIFICADA")
                    : Resultado.error("ERROR AL MODIFICAR");
        } catch (SQLException e) {
            return Resultado.error("Error al modificar la sucursal: " + e.getMessage());
        }
    }

    public Resultado eliminar(int idSucursal) {
        try {
            int filasAfectadas = repositorio.eliminar(idSucursal);
            return filasAfectadas == 1 ? Resultado.ok("SUCURSAL ELIMINADA") : Resultado.error("ERROR AL ELIMINAR");
        } catch (SQLException e) {
            return Resultado.errorEliminar(e, "la sucursal");
        }
    }
}
