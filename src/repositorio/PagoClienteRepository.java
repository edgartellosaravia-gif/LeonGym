package repositorio;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface PagoClienteRepository {

    Optional<MembresiaActiva> obtenerMembresiaActiva(int idCliente) throws SQLException;

    List<DeudaFila> listarDeudas(int idCliente) throws SQLException;

    List<PagoFila> listarHistorialPagos(int idCliente) throws SQLException;

    List<CompraFila> listarHistorialCompras(int idCliente) throws SQLException;

    record MembresiaActiva(String nombrePlan, String fechaInicio, String fechaFin, String estado, String clases) {}

    record DeudaFila(int idPago, String fecha, String descripcion, double monto, String metodo, String estado) {}

    record PagoFila(String fecha, String descripcion, double monto, String metodo, String estado) {}

    record CompraFila(String fecha, String producto, int cantidad, double precioUnitario, double subtotal) {}
}
