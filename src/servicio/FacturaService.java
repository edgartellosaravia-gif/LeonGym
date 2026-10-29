package servicio;

import repositorio.FacturaRepository;
import repositorio.FacturaRepository.FacturaFila;

import java.sql.SQLException;
import java.util.List;

public class FacturaService {

    private final FacturaRepository repositorio;

    public FacturaService(FacturaRepository repositorio) {
        this.repositorio = repositorio;
    }

    public List<FacturaFila> listarPorSucursal(int idSucursal) throws SQLException {
        return repositorio.listarPorSucursal(idSucursal);
    }
}
