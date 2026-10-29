package servicio;

import repositorio.AutenticacionRepository;
import repositorio.AutenticacionRepository.ClienteLogin;
import repositorio.AutenticacionRepository.EmpleadoLogin;

import java.sql.SQLException;
import java.util.Optional;

public class AutenticacionService {

    private final AutenticacionRepository repositorio;

    public AutenticacionService(AutenticacionRepository repositorio) {
        this.repositorio = repositorio;
    }

    /** Primer intento de login: como empleado (DNI + contraseña). */
    public Optional<EmpleadoLogin> autenticarEmpleado(String dni, String contrasenia) throws SQLException {
        return repositorio.buscarEmpleado(dni, contrasenia);
    }

    /** Segundo intento de login (solo si no era empleado): como cliente, únicamente por DNI. */
    public Optional<ClienteLogin> autenticarCliente(String dni) throws SQLException {
        return repositorio.buscarCliente(dni);
    }

    public void registrarIngresoAsistencia(int idEmpleado, String fecha, String horaEntrada) throws SQLException {
        repositorio.registrarIngresoAsistencia(idEmpleado, fecha, horaEntrada);
    }

    /** Silenciosa a propósito: el botón "volver/salir" original ignoraba cualquier error al cerrar. */
    public void cerrarConexionCompartida() {
        try {
            repositorio.cerrarConexionCompartida();
        } catch (Exception ignored) { }
    }
}
