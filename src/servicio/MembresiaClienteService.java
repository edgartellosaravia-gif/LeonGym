package servicio;

import repositorio.MembresiaClienteRepository;
import repositorio.MembresiaClienteRepository.AlertaFila;
import repositorio.MembresiaClienteRepository.MembresiaFila;
import repositorio.MembresiaClienteRepository.RutinaFila;

import java.sql.SQLException;
import java.util.List;

public class MembresiaClienteService {

    private final MembresiaClienteRepository repositorio;

    public MembresiaClienteService(MembresiaClienteRepository repositorio) {
        this.repositorio = repositorio;
    }

    /** Recalcula VENCIDA/POR_VENCER. Silenciosa a propósito: un fallo aquí no debe bloquear la carga de la tabla. */
    public void actualizarEstadosVencidos() {
        try {
            repositorio.actualizarEstadosVencidos();
        } catch (SQLException e) {
            System.out.println("Error actualizando estados: " + e);
        }
    }

    public List<MembresiaFila> listarPorCliente(int idCliente) throws SQLException {
        return repositorio.listarPorCliente(idCliente);
    }

    public List<RutinaFila> listarRutinas(int idCliente) throws SQLException {
        return repositorio.listarRutinas(idCliente);
    }

    public List<AlertaFila> listarAlertasPorSucursal(int idSucursal) throws SQLException {
        return repositorio.listarAlertasPorSucursal(idSucursal);
    }

    /**
     * Inserta o actualiza (según {@code idRutinaExistente}) la rutina de un día.
     * Si es una inserción nueva, re-consulta el id recién generado (misma
     * estrategia que el código original, ya que el INSERT no expone el id).
     */
    public ResultadoRutina guardarRutina(int idCliente, String dia, String descripcion, int idRutinaExistente) {
        try {
            if (idRutinaExistente == -1) {
                int filasAfectadas = repositorio.registrarRutina(idCliente, dia, descripcion);
                if (filasAfectadas != 1) {
                    return new ResultadoRutina(false, "ERROR AL GUARDAR", null);
                }
                Integer nuevoId = repositorio.listarRutinas(idCliente).stream()
                        .filter(r -> r.dia().equals(dia))
                        .map(RutinaFila::idRutina)
                        .findFirst().orElse(null);
                return new ResultadoRutina(true, "Rutina del " + dia + " guardada.", nuevoId);
            } else {
                int filasAfectadas = repositorio.actualizarRutina(idRutinaExistente, dia, descripcion);
                return filasAfectadas == 1
                        ? new ResultadoRutina(true, "Rutina del " + dia + " actualizada.", null)
                        : new ResultadoRutina(false, "ERROR AL ACTUALIZAR", null);
            }
        } catch (SQLException e) {
            return new ResultadoRutina(false, e.getMessage(), null);
        }
    }

    /** Resultado de guardar una rutina; incluye el nuevo id si se trató de una inserción. */
    public record ResultadoRutina(boolean exito, String mensaje, Integer nuevoIdRutina) {}
}
