package Diseño;

import servicio.ReporteService;
import servicio.Resultado;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.Component;


public final class ReporteDialogUtil {

    private ReporteDialogUtil() {
    }

    public static void mostrarDialogoReporte(Component parent, ReporteService reporteService, int idEmpleado) {
        JTextArea txtReporte = new JTextArea(8, 32);
        txtReporte.setLineWrap(true);
        txtReporte.setWrapStyleWord(true);

        int opcion = JOptionPane.showConfirmDialog(parent, new JScrollPane(txtReporte),
                "Reportar incidencia (máquina defectuosa, etc.)",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (opcion != JOptionPane.OK_OPTION) return;

        String mensaje = txtReporte.getText().trim();
        if (mensaje.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "El reporte no puede estar vacío.");
            return;
        }
        Resultado resultado = reporteService.enviar(idEmpleado, mensaje);
        JOptionPane.showMessageDialog(parent, resultado.mensaje());
    }
}
