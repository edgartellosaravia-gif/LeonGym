package repositorio;

import java.sql.SQLException;
import java.util.Optional;

/** Puerto de acceso a datos para los avisos que el administrador publica para los clientes. */
public interface AvisoRepository {

    /** El aviso más reciente publicado (si existe alguno). */
    Optional<AvisoFila> obtenerUltimo() throws SQLException;
    Optional<AvisoFila> obtenerUltimoSucursal(int idSucursal) throws SQLException;

    int publicar(int idEmpleado, String mensaje) throws SQLException;
    int publicarSupervisor (int idSucursal, String mensaje) throws SQLException;
    record AvisoFila(int idAviso, String mensaje, String fechaPublicacion) {}
}
