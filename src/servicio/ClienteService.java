package servicio;

import repositorio.ClienteRepository;
import repositorio.ClienteRepository.ClienteFila;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;

import java.sql.SQLException;
import java.util.List;

public class ClienteService {

    private final ClienteRepository repositorio;

    public ClienteService(ClienteRepository repositorio) {
        this.repositorio = Preconditions.checkNotNull(repositorio, "repositorio");
    }

    public List<ClienteFila> listarPorSucursal(int idSucursal) throws SQLException {
        return repositorio.listarPorSucursal(idSucursal);
    }

    public List<ClienteFila> buscarPorSucursalYNombre(int idSucursal, String nombreParcial) throws SQLException {
        return repositorio.buscarPorSucursalYNombre(idSucursal, nombreParcial);
    }

    public ClienteFila obtenerPorId(int idCliente) throws SQLException {
        return repositorio.obtenerPorId(idCliente);
    }

    public Resultado registrar(int idSucursal, String nombre, String apellido, String dni, String telefono, String email) {
        try {
            int filasAfectadas = repositorio.registrar(idSucursal, StringUtils.trimToEmpty(nombre), StringUtils.trimToEmpty(apellido), StringUtils.trimToEmpty(dni), StringUtils.trimToEmpty(telefono), StringUtils.trimToEmpty(email));
            return filasAfectadas == 1 ? Resultado.ok("CLIENTE REGISTRADO") : Resultado.error("ERROR AL REGISTRAR");
        } catch (SQLException e) { return Resultado.error("Error al registrar el cliente: " + e.getMessage()); }
    }

    public Resultado actualizar(int idCliente, String nombre, String apellido, String dni, String telefono, String email) {
        try {
            int filasAfectadas = repositorio.actualizar(idCliente, StringUtils.trimToEmpty(nombre), StringUtils.trimToEmpty(apellido), StringUtils.trimToEmpty(dni), StringUtils.trimToEmpty(telefono), StringUtils.trimToEmpty(email));
            return filasAfectadas == 1 ? Resultado.ok("CLIENTE MODIFICADO") : Resultado.error("ERROR AL MODIFICAR");
        } catch (SQLException e) { return Resultado.error("Error al modificar el cliente: " + e.getMessage()); }
    }

    public Resultado eliminar(int idCliente) {
        try {
            int filasAfectadas = repositorio.eliminar(idCliente);
            return filasAfectadas == 1 ? Resultado.ok("CLIENTE ELIMINADO") : Resultado.error("ERROR AL ELIMINAR");
        } catch (SQLException e) { return Resultado.errorEliminar(e, "el cliente"); }
    }
}
