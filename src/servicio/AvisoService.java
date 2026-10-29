package servicio;

import repositorio.AvisoRepository;
import repositorio.AvisoRepository.AvisoFila;

import java.sql.SQLException;
import java.util.Optional;

public class AvisoService {

    private final AvisoRepository repositorio;

    public AvisoService(AvisoRepository repositorio) {
        this.repositorio = repositorio;
    }

    public Optional<AvisoFila> obtenerUltimo() throws SQLException {
        return repositorio.obtenerUltimo();
    }

    public Optional<AvisoFila> obtenerUltimoSucursal(int idSucursal) throws SQLException {
        return repositorio.obtenerUltimoSucursal(idSucursal);
    }

    public Resultado publicar(int idEmpleado, String mensaje) {
        try {
            int filasAfectadas = repositorio.publicar(idEmpleado, mensaje);
            return filasAfectadas == 1 ? Resultado.ok("AVISO PUBLICADO") : Resultado.error("ERROR AL PUBLICAR");
        } catch (SQLException e) {
            return Resultado.error("Error al publicar el aviso: " + e.getMessage());
        }
    }
    public Resultado publicarSupervisor(int idSucursal, String mensaje) {
        try {
            int filasAfectadas = repositorio.publicarSupervisor(idSucursal, mensaje);
            return filasAfectadas == 1 ? Resultado.ok("AVISO PUBLICADO") : Resultado.error("ERROR AL PUBLICAR");
        } catch (SQLException e) {
            return Resultado.error("Error al publicar el aviso: " + e.getMessage());
        }
    }
}
