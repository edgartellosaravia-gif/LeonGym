package repositorio;

import java.sql.SQLException;

public interface EmpresaRepository {

    EmpresaDatos obtener() throws SQLException;

    int actualizar(int idEmpresa, String nombre, String ruc, String direccion, String telefono, String email) throws SQLException;

    record EmpresaDatos(int idEmpresa, String nombre, String ruc, String direccion, String telefono, String email) {}
}
