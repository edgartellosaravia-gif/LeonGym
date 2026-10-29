package servicio;

import repositorio.VentaRepository;
import repositorio.VentaRepository.DashboardVenta;
import repositorio.VentaRepository.VentaFila;

import java.sql.SQLException;
import java.util.List;

public class VentaService {

    private final VentaRepository repositorio;

    public VentaService(VentaRepository repositorio) {
        this.repositorio = repositorio;
    }

    public DashboardVenta obtenerDashboard(int idSucursal, String filtroFecha) throws SQLException {
        return repositorio.obtenerDashboard(idSucursal, filtroFecha);
    }

    public List<VentaFila> listarHistorial(int idSucursal, String filtroFecha) throws SQLException {
        return repositorio.listarHistorial(idSucursal, filtroFecha);
    }
}
