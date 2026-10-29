package servicio;

import repositorio.EmpresaRepository;
import repositorio.EmpresaRepository.EmpresaDatos;

import java.sql.SQLException;

public class EmpresaService {

    private final EmpresaRepository repositorio;

    public EmpresaService(EmpresaRepository repositorio) {
        this.repositorio = repositorio;
    }

    public EmpresaDatos obtener() throws SQLException {
        return repositorio.obtener();
    }

    public Resultado actualizar(int idEmpresa, String nombre, String ruc, String direccion, String telefono, String email) {
        try {
            int filasAfectadas = repositorio.actualizar(idEmpresa, nombre, ruc, direccion, telefono, email);
            return filasAfectadas == 1 ? Resultado.ok("Informacion modificada") : Resultado.error("Error al modificar");
        } catch (SQLException e) {
            return Resultado.error("Error al modificar la empresa: " + e.getMessage());
        }
    }
}
