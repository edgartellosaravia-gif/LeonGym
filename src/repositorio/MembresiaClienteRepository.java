package repositorio;

import java.sql.SQLException;
import java.util.List;

public interface MembresiaClienteRepository {

    /** Recalcula estados VENCIDA / POR_VENCER en base a la fecha actual (efecto de lote, sin resultado). */
    void actualizarEstadosVencidos() throws SQLException;

    List<MembresiaFila> listarPorCliente(int idCliente) throws SQLException;

    List<RutinaFila> listarRutinas(int idCliente) throws SQLException;

    /**
     * Clientes de una sucursal cuya membresía está VENCIDA o vence en los
     * próximos 15 días, junto con su deuda pendiente (para el panel de
     * alertas administrativo). Reutilizado por FrmClientes.
     */
    List<AlertaFila> listarAlertasPorSucursal(int idSucursal) throws SQLException;

    int registrarRutina(int idCliente, String dia, String descripcion) throws SQLException;

    int actualizarRutina(int idRutina, String dia, String descripcion) throws SQLException;

    record MembresiaFila(int idMembresia, String nombrePlan, String fechaInicio, String fechaFin,
                          String clases, double total, String estado) {}

    record RutinaFila(int idRutina, String dia, String descripcion) {}

    record AlertaFila(String nombreCliente, String dni, String fechaInicio, String fechaFin,
                       String estado, double deuda, String alerta) {}
}
