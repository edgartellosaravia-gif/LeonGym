package repositorio;

import UTPsports.UTPsports;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class AvisoRepositoryJdbc implements AvisoRepository {

    @Override
    public Optional<AvisoFila> obtenerUltimo() throws SQLException {
        try (PreparedStatement ps = new UTPsports().getConnection().prepareStatement(
                "SELECT id_aviso, mensaje, fecha_publicacion FROM aviso " +
                "ORDER BY fecha_publicacion DESC, id_aviso DESC LIMIT 1");
             ResultSet rs = ps.executeQuery()) {
            if (!rs.next()) return Optional.empty();
            return Optional.of(new AvisoFila(
                    rs.getInt("id_aviso"), rs.getString("mensaje"), rs.getString("fecha_publicacion")));
        }
    }
    
    @Override
    public Optional<AvisoFila> obtenerUltimoSucursal(int idSucursal) throws SQLException {
        String sql = "SELECT id_aviso, mensaje, fecha_publicacion FROM aviso " +
                     "WHERE id_sucursal = ? " +
                     "ORDER BY fecha_publicacion DESC, id_aviso DESC LIMIT 1";

        try (PreparedStatement ps = new UTPsports().getConnection().prepareStatement(sql)) {

            ps.setInt(1, idSucursal);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();

                return Optional.of(new AvisoFila(
                    rs.getInt("id_aviso"),
                    rs.getString("mensaje"),
                    rs.getString("fecha_publicacion")
                ));
            }
        } catch (Exception e) {
            System.out.println("Error al obtener aviso de la sucursal " + idSucursal + ": " + e.getMessage());
            return Optional.empty();
        }
    }
    

    @Override
    public int publicar(int idEmpleado, String mensaje) throws SQLException {
        try (PreparedStatement ps = new UTPsports().getConnection().prepareStatement(
                "INSERT INTO aviso(mensaje, id_empleado) VALUES (?, ?)")) {
            ps.setString(1, mensaje);
            ps.setInt(2, idEmpleado);
            return ps.executeUpdate();
        }
    }
    
    @Override
    public int publicarSupervisor(int idSucursal, String mensaje) throws SQLException {
        try (PreparedStatement ps = new UTPsports().getConnection().prepareStatement(
                "INSERT INTO aviso(mensaje, id_sucursal) VALUES (?, ?)")) {
            ps.setString(1, mensaje);
            ps.setInt(2, idSucursal);
            return ps.executeUpdate();
        }
    }
}
