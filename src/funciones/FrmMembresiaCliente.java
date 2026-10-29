/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package funciones;

import Diseño.diseño;
import PRINCIPAL.FrmInicio;
import fabrica.AppFactory;
import servicio.MembresiaClienteService;
import servicio.MembresiaClienteService.ResultadoRutina;
import servicio.AvisoService;
import repositorio.MembresiaClienteRepository.MembresiaFila;
import repositorio.MembresiaClienteRepository.RutinaFila;
import java.util.*;
import javax.swing.*;
import static java.awt.image.ImageObserver.ALLBITS;
import javax.swing.table.DefaultTableModel;

public class FrmMembresiaCliente extends javax.swing.JFrame {
    private final MembresiaClienteService membresiaClienteService = AppFactory.getInstance().membresiaClienteService();
    private final AvisoService avisoService = AppFactory.getInstance().avisoService();
    private FrmClientes frmClientes;
    private int idClienteSeleccionado;
    private int idSucursal;
    private int idEmpleado;
    private int idMembresiaSeleccionada;
public FrmMembresiaCliente(int idClienteSeleccionado, String nombreClienteSeleccionado, int idSucursal, int idEmpleado) {
        initComponents();
        setResizable(false);
        setSize(645, 560);
        setLocationRelativeTo(null);
        this.frmClientes           = null;
        this.idClienteSeleccionado = idClienteSeleccionado;
        this.idSucursal            = idSucursal;
        this.idEmpleado            = idEmpleado;
        setTitle("Membresías");
        lblnombrecl.setText(nombreClienteSeleccionado);
        txtidmembresia.setVisible(false);
        tablamembresia.getColumnModel().getColumn(0).setMinWidth(0);
        tablamembresia.getColumnModel().getColumn(0).setMaxWidth(0);
        tablamembresia.getColumnModel().getColumn(0).setWidth(0);
        diseño.estilizarTextArea(jTextArea1);
        diseño.estilizarTextArea(jTextArea2);
        diseño.estilizarTextArea(jTextArea3);
        diseño.estilizarTextArea(jTextArea4);
        diseño.estilizarTextArea(jTextArea5);
        diseño.estilizarTextArea(jTextArea6);
        diseño.estilizarTextArea(jTextArea7);
        diseño.estilizarBotones(btnpagos, btncerrarsesion, btnrutina);
        diseño.estilizarTabla(tablamembresia, jScrollPane1, ALLBITS);
        cargartabla();
        cargarRutinas();
    }
    public FrmMembresiaCliente() {
        initComponents();
    }
    private void cargartabla() {
        // Primero actualizar estados vencidos automáticamente
        membresiaClienteService.actualizarEstadosVencidos();

        DefaultTableModel modelo = (DefaultTableModel) tablamembresia.getModel();
        modelo.setRowCount(0);
        try {
            for (MembresiaFila fila : membresiaClienteService.listarPorCliente(idClienteSeleccionado)) {
                modelo.addRow(new Object[]{
                    fila.idMembresia(),
                    fila.nombrePlan(),
                    fila.fechaInicio(),
                    fila.fechaFin(),
                    fila.clases() != null ? fila.clases() : "Sin clases",
                    "S/ " + String.format("%.2f", fila.total()),
                    fila.estado()
                });
            }
        } catch (Exception e) { JOptionPane.showMessageDialog(null, e.toString()); }
    }
    // Variable de instancia para guardar los id_rutina por día
    private LinkedHashMap<String, Integer> mapaIdRutinas = new LinkedHashMap<>();

    private void cargarRutinas() {
        String[] dias = {"LUNES","MARTES","MIÉRCOLES","JUEVES","VIERNES","SÁBADO","DOMINGO"};

        tbprutina.removeAll();

        // Pestaña de AVISOS primero: es lo primero que ve el cliente al
        // abrir esta ventana (JTabbedPane muestra la pestaña 0 por
        // defecto), de solo lectura, con el último aviso publicado por
        // administración.
        JTextArea txtAviso = new JTextArea();
        txtAviso.setLineWrap(true);
        txtAviso.setWrapStyleWord(true);
        txtAviso.setEditable(false);
        diseño.estilizarTextArea(txtAviso);
        try {
            avisoService.obtenerUltimoSucursal(idSucursal).ifPresentOrElse(
                a -> txtAviso.setText(a.mensaje()),
                () -> txtAviso.setText("No hay avisos publicados por el momento.")
            );
        } catch (Exception e) {
            txtAviso.setText("No se pudo cargar el aviso.");
        }
        tbprutina.addTab("AVISOS", new JScrollPane(txtAviso));

        // Crear pestañas para cada día
        // (antes había una línea de addTab duplicada acá: cada día
        // aparecía dos veces en la barra de pestañas)
        for (String dia : dias) {
            JTextArea textArea = new JTextArea();
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);

            diseño.estilizarTextArea(textArea);

            JScrollPane scroll = new JScrollPane(textArea);
            tbprutina.addTab(dia, scroll);
            mapaIdRutinas.put(dia, -1); // -1 significa que no existe aún
        }

        // Cargar rutinas existentes
        try {
            for (RutinaFila fila : membresiaClienteService.listarRutinas(idClienteSeleccionado)) {
                String dia         = fila.dia();
                String descripcion = fila.descripcion();

                // Buscar la pestaña correspondiente al día
                for (int i = 0; i < tbprutina.getTabCount(); i++) {
                    if (tbprutina.getTitleAt(i).equals(dia)) {
                        JScrollPane scroll = (JScrollPane) tbprutina.getComponentAt(i);
                        JTextArea textArea = (JTextArea) scroll.getViewport().getView();
                        textArea.setText(descripcion);
                        mapaIdRutinas.put(dia, fila.idRutina());
                        break;
                    }
                }
            }
        } catch (Exception e) { JOptionPane.showMessageDialog(null, e.toString()); }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tablamembresia = new javax.swing.JTable();
        lblnombrecl = new javax.swing.JLabel();
        txtidmembresia = new javax.swing.JTextField();
        btnpagos = new javax.swing.JButton();
        btncerrarsesion = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        tbprutina = new javax.swing.JTabbedPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextArea3 = new javax.swing.JTextArea();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTextArea4 = new javax.swing.JTextArea();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTextArea5 = new javax.swing.JTextArea();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTextArea6 = new javax.swing.JTextArea();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTextArea7 = new javax.swing.JTextArea();
        btnrutina = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tablamembresia.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "NOMBRE", "FECHA", "FECHA", "CLASES", "TOTAL", "ESTADOS"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tablamembresia);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, 590, 172));

        lblnombrecl.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblnombrecl.setForeground(new java.awt.Color(255, 255, 255));
        lblnombrecl.setText(" ");
        getContentPane().add(lblnombrecl, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 100, 260, -1));
        getContentPane().add(txtidmembresia, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 90, 71, -1));

        btnpagos.setText(" VER PAGOS");
        btnpagos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnpagosActionPerformed(evt);
            }
        });
        getContentPane().add(btnpagos, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 490, 116, -1));

        btncerrarsesion.setText("CERRAR SESIÓN");
        btncerrarsesion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btncerrarsesionActionPerformed(evt);
            }
        });
        getContentPane().add(btncerrarsesion, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 490, -1, -1));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/LeonGymLogo.png"))); // NOI18N
        jLabel1.setText("CENTRO DE CONTROL DE CUENTA");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 30, 392, -1));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("CLIENTE");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, -1, -1));

        tbprutina.setBackground(new java.awt.Color(51, 51, 51));
        tbprutina.setBorder(new javax.swing.border.MatteBorder(null));
        tbprutina.setForeground(new java.awt.Color(255, 255, 255));

        jTextArea1.setBackground(new java.awt.Color(51, 51, 51));
        jTextArea1.setColumns(20);
        jTextArea1.setForeground(new java.awt.Color(51, 51, 51));
        jTextArea1.setRows(5);
        jTextArea1.setCaretColor(new java.awt.Color(51, 51, 51));
        jTextArea1.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        jTextArea1.setSelectedTextColor(new java.awt.Color(51, 51, 51));
        jTextArea1.setSelectionColor(new java.awt.Color(51, 51, 51));
        jScrollPane2.setViewportView(jTextArea1);

        tbprutina.addTab("  LUNES  ", jScrollPane2);

        jTextArea2.setBackground(new java.awt.Color(51, 51, 51));
        jTextArea2.setColumns(20);
        jTextArea2.setForeground(new java.awt.Color(51, 51, 51));
        jTextArea2.setRows(5);
        jTextArea2.setCaretColor(new java.awt.Color(51, 51, 51));
        jTextArea2.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        jTextArea2.setSelectionColor(new java.awt.Color(0, 0, 0));
        jScrollPane3.setViewportView(jTextArea2);

        tbprutina.addTab("  MARTES  ", jScrollPane3);

        jTextArea3.setBackground(new java.awt.Color(51, 51, 51));
        jTextArea3.setColumns(20);
        jTextArea3.setForeground(new java.awt.Color(51, 51, 51));
        jTextArea3.setRows(5);
        jTextArea3.setCaretColor(new java.awt.Color(51, 51, 51));
        jTextArea3.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        jTextArea3.setSelectionColor(new java.awt.Color(0, 0, 0));
        jScrollPane4.setViewportView(jTextArea3);

        tbprutina.addTab("  MIÉRCOLES  ", jScrollPane4);

        jTextArea4.setBackground(new java.awt.Color(51, 51, 51));
        jTextArea4.setColumns(20);
        jTextArea4.setForeground(new java.awt.Color(51, 51, 51));
        jTextArea4.setRows(5);
        jTextArea4.setCaretColor(new java.awt.Color(51, 51, 51));
        jTextArea4.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        jTextArea4.setSelectionColor(new java.awt.Color(0, 0, 0));
        jScrollPane5.setViewportView(jTextArea4);

        tbprutina.addTab("  JUEVES  ", jScrollPane5);

        jTextArea5.setBackground(new java.awt.Color(51, 51, 51));
        jTextArea5.setColumns(20);
        jTextArea5.setForeground(new java.awt.Color(51, 51, 51));
        jTextArea5.setRows(5);
        jTextArea5.setCaretColor(new java.awt.Color(51, 51, 51));
        jTextArea5.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        jTextArea5.setSelectionColor(new java.awt.Color(0, 0, 0));
        jScrollPane6.setViewportView(jTextArea5);

        tbprutina.addTab("  VIERNES  ", jScrollPane6);

        jTextArea6.setBackground(new java.awt.Color(51, 51, 51));
        jTextArea6.setColumns(20);
        jTextArea6.setForeground(new java.awt.Color(51, 51, 51));
        jTextArea6.setRows(5);
        jTextArea6.setCaretColor(new java.awt.Color(51, 51, 51));
        jTextArea6.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        jTextArea6.setSelectionColor(new java.awt.Color(0, 0, 0));
        jScrollPane7.setViewportView(jTextArea6);

        tbprutina.addTab("  SÁBADO  ", jScrollPane7);

        jTextArea7.setBackground(new java.awt.Color(51, 51, 51));
        jTextArea7.setColumns(20);
        jTextArea7.setForeground(new java.awt.Color(51, 51, 51));
        jTextArea7.setRows(5);
        jTextArea7.setCaretColor(new java.awt.Color(51, 51, 51));
        jTextArea7.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        jTextArea7.setSelectionColor(new java.awt.Color(0, 0, 0));
        jScrollPane8.setViewportView(jTextArea7);

        tbprutina.addTab("  DOMINGO  ", jScrollPane8);

        getContentPane().add(tbprutina, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 340, 590, 130));

        btnrutina.setText("GUARDAR RUTINA");
        btnrutina.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnrutinaActionPerformed(evt);
            }
        });
        getContentPane().add(btnrutina, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 490, -1, -1));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("REVISA TU RUTINA");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 310, 620, -1));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/fondo.png"))); // NOI18N
        jLabel3.setText("REVISA TU RINA");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnpagosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnpagosActionPerformed
        Diseño.NavegacionUtil.navegar(btnpagos, () -> {
            new FrmPagoClientes(idClienteSeleccionado, lblnombrecl.getText(), this).setVisible(true);
            this.setVisible(false);
        });
    }//GEN-LAST:event_btnpagosActionPerformed

    private void btncerrarsesionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncerrarsesionActionPerformed
        this.dispose();
        new FrmInicio().setVisible(true);
    }//GEN-LAST:event_btncerrarsesionActionPerformed

    private void btnrutinaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnrutinaActionPerformed
        int tabSeleccionada = tbprutina.getSelectedIndex();
        if (tabSeleccionada < 0) {
            JOptionPane.showMessageDialog(null, "Seleccione un día."); return;
        }
        String dia = tbprutina.getTitleAt(tabSeleccionada);
        JScrollPane scroll = (JScrollPane) tbprutina.getComponentAt(tabSeleccionada);
        JTextArea textArea = (JTextArea) scroll.getViewport().getView();
        String descripcion = textArea.getText().trim();

        if (descripcion.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Escribe algo en la rutina."); return;
        }

        try {
            int idRutina = mapaIdRutinas.get(dia);
            ResultadoRutina resultado = membresiaClienteService.guardarRutina(
                idClienteSeleccionado, dia, descripcion, idRutina);
            if (resultado.exito()) {
                if (resultado.nuevoIdRutina() != null) mapaIdRutinas.put(dia, resultado.nuevoIdRutina());
                JOptionPane.showMessageDialog(null, resultado.mensaje());
            }
        } catch (Exception e) { JOptionPane.showMessageDialog(null, e.toString()); }
    }//GEN-LAST:event_btnrutinaActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FrmMembresiaCliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmMembresiaCliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmMembresiaCliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmMembresiaCliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmMembresiaCliente().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btncerrarsesion;
    private javax.swing.JButton btnpagos;
    private javax.swing.JButton btnrutina;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextArea jTextArea3;
    private javax.swing.JTextArea jTextArea4;
    private javax.swing.JTextArea jTextArea5;
    private javax.swing.JTextArea jTextArea6;
    private javax.swing.JTextArea jTextArea7;
    private javax.swing.JLabel lblnombrecl;
    private javax.swing.JTable tablamembresia;
    private javax.swing.JTabbedPane tbprutina;
    private javax.swing.JTextField txtidmembresia;
    // End of variables declaration//GEN-END:variables
}
