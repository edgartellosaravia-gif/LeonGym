package repositorio;

import UTPsports.UTPsports;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FacturaRepositoryJdbc implements FacturaRepository {

    @Override
    public List<FacturaFila> listarPorSucursal(int idSucursal) throws SQLException {
        List<FacturaFila> filas = new ArrayList<>();
        try (PreparedStatement ps = new UTPsports().getConnection().prepareStatement(
                "SELECT f.numeroF, f.fechaF, " +
                "CONCAT(c.nombreC,' ',c.apellidoC) AS cliente, " +
                "f.totalF, f.igv, " +
                "CASE " +
                "  WHEN EXISTS(SELECT 1 FROM detalle_venta dv " +
                "    WHERE dv.id_venta = f.id_venta AND dv.tipoDV = 'PLAN') " +
                "    THEN 'Membresía' ELSE 'Productos' " +
                "END AS tipo " +
                "FROM factura f " +
                "JOIN venta v ON f.id_venta = v.id_venta " +
                "JOIN cliente c ON v.id_cliente = c.id_cliente " +
                "WHERE v.id_sucursal = ? " +
                "ORDER BY f.fechaF DESC")) {
            ps.setInt(1, idSucursal);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    filas.add(new FacturaFila(
                            rs.getString("numeroF"),
                            rs.getString("fechaF"),
                            rs.getString("cliente"),
                            rs.getDouble("totalF"),
                            rs.getDouble("igv"),
                            rs.getString("tipo")
                    ));
                }
            }
        }
        return filas;
    }
}
