package servicio;

import repositorio.PagoClienteRepository;
import repositorio.PagoClienteRepository.CompraFila;
import repositorio.PagoClienteRepository.DeudaFila;
import repositorio.PagoClienteRepository.MembresiaActiva;
import repositorio.PagoClienteRepository.PagoFila;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class PagoClienteService {

    private final PagoClienteRepository repositorio;

    public PagoClienteService(PagoClienteRepository repositorio) {
        this.repositorio = repositorio;
    }

    public Optional<MembresiaActiva> obtenerMembresiaActiva(int idCliente) throws SQLException {
        return repositorio.obtenerMembresiaActiva(idCliente);
    }

    public List<DeudaFila> listarDeudas(int idCliente) throws SQLException {
        return repositorio.listarDeudas(idCliente);
    }

    public List<PagoFila> listarHistorialPagos(int idCliente) throws SQLException {
        return repositorio.listarHistorialPagos(idCliente);
    }

    public List<CompraFila> listarHistorialCompras(int idCliente) throws SQLException {
        return repositorio.listarHistorialCompras(idCliente);
    }
}
