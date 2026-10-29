package repositorio;

import java.util.Map;
import java.util.Optional;

/**
 * Puerto de acceso a datos para las operaciones de caja/punto de venta:
 * registrar el pago de una deuda existente (con generación automática de
 * factura si es la primera vez que se salda esa venta) y registrar una
 * venta nueva de productos desde el carrito.
 */
public interface PagoRepository {

    /**
     * Marca un pago como PAGADO y genera la factura de su venta asociada
     * si aún no tenía una.
     * @return la factura recién generada, vacío si la venta ya tenía factura.
     */
    Optional<FacturaGenerada> registrarPago(int idPago, float monto, String metodo) throws Exception;

    /** Registra la venta de los productos del carrito, descuenta stock y registra el pago. */
    void registrarVentaProductos(int idCliente, int idEmpleado, int idSucursal,
                                  Map<Integer, ItemCarrito> carrito, double total,
                                  String metodo, String estadoPago) throws Exception;

    record ItemCarrito(double precio, int cantidad) {}

    record FacturaGenerada(String numero, double igv) {}
}
