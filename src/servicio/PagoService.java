package servicio;

import repositorio.PagoRepository;
import repositorio.PagoRepository.FacturaGenerada;
import repositorio.PagoRepository.ItemCarrito;

import java.util.Map;
import java.util.Optional;

public class PagoService {

    private final PagoRepository repositorio;

    public PagoService(PagoRepository repositorio) {
        this.repositorio = repositorio;
    }

    /**
     * Registra el pago de una deuda. El mensaje refleja si se generó una
     * factura nueva (primera vez que se salda esa venta) o no.
     */
    public Resultado registrarPago(int idPago, float monto, String metodo) {
        try {
            Optional<FacturaGenerada> factura = repositorio.registrarPago(idPago, monto, metodo);
            if (factura.isPresent()) {
                FacturaGenerada f = factura.get();
                return Resultado.ok(
                        "PAGO REGISTRADO\nFactura generada: " + f.numero() +
                        "\nTotal: S/ " + String.format("%.2f", monto) +
                        "\nIGV (18%): S/ " + String.format("%.2f", f.igv()));
            }
            return Resultado.ok("PAGO REGISTRADO");
        } catch (Exception e) {
            return Resultado.error(e.toString());
        }
    }

    public Resultado registrarVentaProductos(int idCliente, int idEmpleado, int idSucursal,
                                              Map<Integer, ItemCarrito> carrito, double total,
                                              String metodo, String estadoPago) {
        try {
            repositorio.registrarVentaProductos(idCliente, idEmpleado, idSucursal, carrito, total, metodo, estadoPago);
            return Resultado.ok("VENTA REGISTRADA");
        } catch (Exception e) {
            return Resultado.error(e.toString());
        }
    }
}
