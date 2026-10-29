package funciones;

import fabrica.AppFactory;
import servicio.ReporteService;
import servicio.Resultado;
import repositorio.ReporteRepository.ReporteFila;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Ventana simple para que el administrador revise los reportes de
 * incidencia enviados por cualquier empleado de la sucursal, y los marque
 * como resueltos.
 * <p>
 * A diferencia del resto de pantallas de este proyecto, esta se escribió a
 * mano en vez de con el editor de formularios de NetBeans (no hay un
 * .form asociado): es una ventana nueva y autocontenida, sin necesidad del
 * diseñador visual para algo de este tamaño.
 */
public class DlgReportes extends JDialog {

    private final ReporteService reporteService = AppFactory.getInstance().reporteService();
    private final int idSucursal;
    private final DefaultTableModel modelo;
    private final JTable tabla;

    public DlgReportes(JFrame parent, int idSucursal) {
        super(parent, "Reportes de incidencia", true);
        this.idSucursal = idSucursal;

        setSize(720, 420);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(8, 8));

        modelo = new DefaultTableModel(new Object[]{"ID", "Empleado", "Mensaje", "Fecha", "Estado"}, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tabla = new JTable(modelo);
        tabla.getColumnModel().getColumn(0).setMinWidth(0);
        tabla.getColumnModel().getColumn(0).setMaxWidth(0);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(300);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnResolver = new JButton("Marcar como resuelto");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnExcel = new JButton("Exportar a Excel");
        JButton btnCerrar = new JButton("Cerrar");
        panelBotones.add(btnResolver);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnExcel);
        panelBotones.add(btnCerrar);
        add(panelBotones, BorderLayout.SOUTH);

        btnResolver.addActionListener(e -> Diseño.NavegacionUtil.evitarDobleClic(btnResolver, this::marcarResuelto));
        btnEliminar.addActionListener(e -> Diseño.NavegacionUtil.evitarDobleClic(btnEliminar, this::eliminar));
        btnActualizar.addActionListener(e -> cargarReportes());
        btnExcel.addActionListener(e -> exportarExcel());
        btnCerrar.addActionListener(e -> dispose());

        cargarReportes();
    }

    private void cargarReportes() {
        modelo.setRowCount(0);
        try {
            for (ReporteFila fila : reporteService.listarPorSucursal(idSucursal)) {
                modelo.addRow(new Object[]{
                    fila.idReporte(), fila.empleado(), fila.mensaje(), fila.fecha(), fila.estado()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.toString());
        }
    }

    private void marcarResuelto() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un reporte de la tabla.");
            return;
        }
        String estadoActual = (String) modelo.getValueAt(fila, 4);
        if ("RESUELTO".equals(estadoActual)) {
            JOptionPane.showMessageDialog(this, "Ese reporte ya está resuelto.");
            return;
        }
        int idReporte = (int) modelo.getValueAt(fila, 0);
        Resultado resultado = reporteService.marcarResuelto(idReporte);
        JOptionPane.showMessageDialog(this, resultado.mensaje());
        if (resultado.exito()) cargarReportes();
    }

    private void eliminar() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un reporte de la tabla.");
            return;
        }
        int conf = JOptionPane.showConfirmDialog(this, "¿Eliminar este reporte? Esta acción no se puede deshacer.",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (conf != JOptionPane.YES_OPTION) return;

        int idReporte = (int) modelo.getValueAt(fila, 0);
        Resultado resultado = reporteService.eliminar(idReporte);
        JOptionPane.showMessageDialog(this, resultado.mensaje());
        if (resultado.exito()) cargarReportes();
    }

    private void exportarExcel() {
        JFileChooser selector = new JFileChooser();
        selector.setSelectedFile(new java.io.File("reportes_incidencia.xlsx"));
        if (selector.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
        java.io.File destino = selector.getSelectedFile();
        if (!destino.getName().toLowerCase().endsWith(".xlsx")) {
            destino = new java.io.File(destino.getParentFile(), destino.getName() + ".xlsx");
        }
        try {
            servicio.ExcelExportService.exportarReportes(reporteService.listarPorSucursal(idSucursal), destino);
            JOptionPane.showMessageDialog(this, "Excel generado correctamente.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "No se pudo exportar: " + e.getMessage());
        }
    }
}
