package funciones;

import Diseño.diseño;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import fabrica.AppFactory;
import servicio.ClaseService;
import servicio.PlanService;
import servicio.Resultado;
import repositorio.PlanRepository.PlanFila;
import funciones.FrmServicio;
import static java.awt.image.ImageObserver.ALLBITS;

public class FrmPlan extends javax.swing.JFrame {


    private final ClaseService claseService = AppFactory.getInstance().claseService();
    private final PlanService planService = AppFactory.getInstance().planService();

    FrmServicio servicio;
    int idSucursal;
    String nombreSucursal;
    int idPlanSeleccionado = 0;
    int idServicio = 0;

    public FrmPlan(FrmServicio servicio, int idSucursal, String nombreSucursal) {
        initComponents();
        setResizable(false);
        setSize(880, 455);
        setLocationRelativeTo(null);
        this.servicio = servicio;
        this.idSucursal = idSucursal;
        this.nombreSucursal = nombreSucursal;
        txtidplan.setVisible(false);
        diseño.estilizarBotones(btnmodificarpl, btneliminarpl, btnlimpiarpl, btnregistrarplan, btnvolver);
        tablaplan.getColumnModel().getColumn(0).setMinWidth(0);
        tablaplan.getColumnModel().getColumn(0).setMaxWidth(0);
        tablaplan.getColumnModel().getColumn(0).setWidth(0);
        diseño.estilizarTabla(tablaplan, jScrollPane1, ALLBITS);
        cargarTipo();
        cargartabla();
        rdactivo.setSelected(true);
    }

    public FrmPlan() { initComponents(); }

    private void cargarTipo() {
        try {
            cmbtipo.removeAllItems();
            cmbtipo.addItem("Seleccione tipo");
            for (String tipo : claseService.obtenerTiposServicio()) cmbtipo.addItem(tipo);
        } catch (Exception e) { JOptionPane.showMessageDialog(null, e.toString()); }
    }

    private void cargartabla() {
        DefaultTableModel modelo = (DefaultTableModel) tablaplan.getModel();
        modelo.setRowCount(0);
        try {
            for (PlanFila fila : planService.listarPorSucursal(idSucursal)) {
                modelo.addRow(new Object[] {
                    fila.idPlan(), fila.nombre(), fila.tipoServicio(), fila.estado(),
                    fila.duracionMeses(), fila.precio()
                });
            }
        } catch (Exception e) { JOptionPane.showMessageDialog(null, e.toString()); }
    }
    private void limpiar() {
        txtidplan.setText(""); txtnombrepl.setText("");
        txtduracion.setText(""); txtprecio.setText("");
        cmbtipo.setSelectedIndex(0); rdactivo.setSelected(true);
        tablaplan.clearSelection();
        idPlanSeleccionado = 0; idServicio = 0;
        btnregistrarplan.setEnabled(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        buttonGroup1 = new javax.swing.ButtonGroup();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaplan = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        btnregistrarplan = new javax.swing.JButton();
        btnmodificarpl = new javax.swing.JButton();
        btneliminarpl = new javax.swing.JButton();
        btnlimpiarpl = new javax.swing.JButton();
        btnvolver = new javax.swing.JButton();
        txtnombrepl = new javax.swing.JTextField();
        txtduracion = new javax.swing.JTextField();
        txtprecio = new javax.swing.JTextField();
        txtidplan = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        lblservicio = new javax.swing.JLabel();
        rdactivo = new javax.swing.JRadioButton();
        rdinactivo = new javax.swing.JRadioButton();
        cmbtipo = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tablaplan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID", "NOMBRE", "TIPO", "ESTADO", "DURACION", "PRECIO"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaplan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaplanMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tablaplan);

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(298, 23, 567, 275));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/LeonGymLogo.png"))); // NOI18N
        jLabel1.setText("PLANES LEÓN GYM");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 6, 264, -1));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("NOMBRE");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(26, 84, 122, -1));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("DURACION (MESES)");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(28, 225, 122, -1));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("PRECIO");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(28, 265, 122, -1));

        btnregistrarplan.setText("REGISTRAR PLAN");
        btnregistrarplan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnregistrarplanActionPerformed(evt);
            }
        });
        getContentPane().add(btnregistrarplan, new org.netbeans.lib.awtextra.AbsoluteConstraints(34, 302, -1, -1));

        btnmodificarpl.setText("MODIFICAR PLAN");
        btnmodificarpl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnmodificarplActionPerformed(evt);
            }
        });
        getContentPane().add(btnmodificarpl, new org.netbeans.lib.awtextra.AbsoluteConstraints(174, 302, -1, -1));

        btneliminarpl.setText("ELIMINAR PLAN");
        btneliminarpl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btneliminarplActionPerformed(evt);
            }
        });
        getContentPane().add(btneliminarpl, new org.netbeans.lib.awtextra.AbsoluteConstraints(34, 343, 122, -1));

        btnlimpiarpl.setText("LIMPIAR");
        btnlimpiarpl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnlimpiarplActionPerformed(evt);
            }
        });
        getContentPane().add(btnlimpiarpl, new org.netbeans.lib.awtextra.AbsoluteConstraints(174, 343, 124, -1));

        btnvolver.setText("VOLVER");
        btnvolver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnvolverActionPerformed(evt);
            }
        });
        getContentPane().add(btnvolver, new org.netbeans.lib.awtextra.AbsoluteConstraints(97, 384, 122, -1));

        txtnombrepl.setBackground(new java.awt.Color(51, 51, 51));
        txtnombrepl.setForeground(new java.awt.Color(255, 255, 255));
        getContentPane().add(txtnombrepl, new org.netbeans.lib.awtextra.AbsoluteConstraints(166, 81, 124, -1));

        txtduracion.setBackground(new java.awt.Color(51, 51, 51));
        txtduracion.setForeground(new java.awt.Color(255, 255, 255));
        getContentPane().add(txtduracion, new org.netbeans.lib.awtextra.AbsoluteConstraints(168, 222, 124, -1));

        txtprecio.setBackground(new java.awt.Color(51, 51, 51));
        txtprecio.setForeground(new java.awt.Color(255, 255, 255));
        getContentPane().add(txtprecio, new org.netbeans.lib.awtextra.AbsoluteConstraints(168, 262, 124, -1));
        getContentPane().add(txtidplan, new org.netbeans.lib.awtextra.AbsoluteConstraints(432, 344, 71, 31));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("SERVICIO");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(26, 115, 122, -1));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("TIPO DE SERVICIO");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(26, 146, 122, -1));

        lblservicio.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblservicio.setForeground(new java.awt.Color(255, 255, 255));
        lblservicio.setText("PLAN");
        getContentPane().add(lblservicio, new org.netbeans.lib.awtextra.AbsoluteConstraints(166, 115, 124, -1));

        buttonGroup1.add(rdactivo);
        rdactivo.setForeground(new java.awt.Color(255, 255, 255));
        rdactivo.setText("ACTIVO");
        rdactivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdactivoActionPerformed(evt);
            }
        });
        getContentPane().add(rdactivo, new org.netbeans.lib.awtextra.AbsoluteConstraints(26, 183, 98, -1));

        buttonGroup1.add(rdinactivo);
        rdinactivo.setForeground(new java.awt.Color(255, 255, 255));
        rdinactivo.setText("INACTIVO");
        rdinactivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdinactivoActionPerformed(evt);
            }
        });
        getContentPane().add(rdinactivo, new org.netbeans.lib.awtextra.AbsoluteConstraints(192, 183, 98, -1));

        cmbtipo.setBackground(new java.awt.Color(51, 51, 51));
        cmbtipo.setForeground(new java.awt.Color(255, 255, 255));
        cmbtipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbtipo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbtipoActionPerformed(evt);
            }
        });
        getContentPane().add(cmbtipo, new org.netbeans.lib.awtextra.AbsoluteConstraints(166, 143, 124, -1));

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/fondo.png"))); // NOI18N
        jLabel5.setText("jLabel5");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 880, 520));

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void btnvolverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnvolverActionPerformed
        servicio.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_btnvolverActionPerformed

    private void btnmodificarplActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnmodificarplActionPerformed
        if (idPlanSeleccionado == 0) {
            JOptionPane.showMessageDialog(null, "Seleccione un plan de la tabla.");
            return;
        }
        try {
            int duracion = Integer.parseInt(txtduracion.getText());
            float precio = Float.parseFloat(txtprecio.getText());
            if (duracion <= 0) { JOptionPane.showMessageDialog(null, "La duración debe ser mayor a cero."); return; }
            if (precio <= 0) { JOptionPane.showMessageDialog(null, "El precio debe ser mayor a cero."); return; }
            String estadoPL = rdactivo.isSelected() ? "ACTIVO" : "INACTIVO";
            Resultado resultado = planService.actualizar(idPlanSeleccionado, txtnombrepl.getText(),
                duracion, precio, estadoPL);
            JOptionPane.showMessageDialog(null, resultado.mensaje());
            if (resultado.exito()) { limpiar(); cargartabla(); }
        } catch (Exception e) { JOptionPane.showMessageDialog(null, e.toString()); }
    }//GEN-LAST:event_btnmodificarplActionPerformed

    private void tablaplanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaplanMouseClicked
        int fila = tablaplan.getSelectedRow();
        if (fila >= 0) {
            DefaultTableModel modelo = (DefaultTableModel) tablaplan.getModel();
            txtidplan.setText(modelo.getValueAt(fila, 0).toString());
            idPlanSeleccionado = Integer.parseInt(modelo.getValueAt(fila, 0).toString());
            txtnombrepl.setText(modelo.getValueAt(fila, 1).toString());
            String tipo = modelo.getValueAt(fila, 2).toString();
            for (int i = 0; i < cmbtipo.getItemCount(); i++) {
                if (cmbtipo.getItemAt(i).equals(tipo)) { cmbtipo.setSelectedIndex(i); break; }
            }
            txtduracion.setText(modelo.getValueAt(fila, 4).toString());
            txtprecio.setText(modelo.getValueAt(fila, 5).toString());
            // BUG CORREGIDO: el código original volvía a consultar
            // servicio.estadoSERV (el estado del TIPO de servicio, p.ej.
            // "REGULAR") para decidir el radio ACTIVO/INACTIVO del PLAN,
            // en vez de usar el estado propio del plan (columna estadoPL)
            // que la propia tabla ya mostraba. Ambos estados son
            // independientes, así que podía marcar el radio equivocado.
            String estadoPlan = modelo.getValueAt(fila, 3).toString();
            if (estadoPlan.equals("ACTIVO")) rdactivo.setSelected(true);
            else rdinactivo.setSelected(true);
            try {
                Integer id = claseService.obtenerIdServicio(tipo);
                if (id != null) idServicio = id;
            } catch (Exception e) { JOptionPane.showMessageDialog(null, e.toString()); }
            btnregistrarplan.setEnabled(false);
        }
    }//GEN-LAST:event_tablaplanMouseClicked

    private void btnregistrarplanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnregistrarplanActionPerformed
        if (cmbtipo.getSelectedIndex() == 0 || txtnombrepl.getText().isEmpty() ||
            txtduracion.getText().isEmpty() || txtprecio.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Complete todos los campos");
            return;
        }
        try {
            int duracion = Integer.parseInt(txtduracion.getText());
            float precio = Float.parseFloat(txtprecio.getText());
            if (duracion <= 0) { JOptionPane.showMessageDialog(null, "La duración debe ser mayor a cero."); return; }
            if (precio <= 0) { JOptionPane.showMessageDialog(null, "El precio debe ser mayor a cero."); return; }
            Integer idServicioSeleccionado = claseService.obtenerIdServicio(cmbtipo.getSelectedItem().toString());
            Resultado resultado = planService.registrar(idServicioSeleccionado, idSucursal, txtnombrepl.getText(),
                duracion, precio);
            JOptionPane.showMessageDialog(null, resultado.mensaje());
            if (resultado.exito()) { limpiar(); cargartabla(); }
        } catch (Exception e) { JOptionPane.showMessageDialog(null, e.toString()); }
    }//GEN-LAST:event_btnregistrarplanActionPerformed

    private void btnlimpiarplActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnlimpiarplActionPerformed
        limpiar();
    }//GEN-LAST:event_btnlimpiarplActionPerformed

    private void btneliminarplActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btneliminarplActionPerformed
        if (idPlanSeleccionado == 0) {
            JOptionPane.showMessageDialog(null, "Seleccione un plan de la tabla");
            return;
        }
        int conf = JOptionPane.showConfirmDialog(null, "¿Eliminar plan?",
            "Confirmar", JOptionPane.YES_NO_OPTION);
        if (conf == JOptionPane.YES_OPTION) {
            try {
                Resultado resultado = planService.eliminar(idPlanSeleccionado);
                JOptionPane.showMessageDialog(null, resultado.mensaje());
                if (resultado.exito()) { limpiar(); cargartabla(); }
            } catch (Exception e) { JOptionPane.showMessageDialog(null, e.toString()); }
        }
    }//GEN-LAST:event_btneliminarplActionPerformed

    private void cmbtipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbtipoActionPerformed
        if (cmbtipo.getSelectedIndex() <= 0) return;
        try {
            Integer id = claseService.obtenerIdServicio(cmbtipo.getSelectedItem().toString());
            if (id != null) idServicio = id;
        } catch (Exception e) { JOptionPane.showMessageDialog(null, e.toString()); }
    }//GEN-LAST:event_cmbtipoActionPerformed

    private void rdactivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdactivoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdactivoActionPerformed

    private void rdinactivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdinactivoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdinactivoActionPerformed

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
            java.util.logging.Logger.getLogger(FrmPlan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmPlan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmPlan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmPlan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmPlan().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btneliminarpl;
    private javax.swing.JButton btnlimpiarpl;
    private javax.swing.JButton btnmodificarpl;
    private javax.swing.JButton btnregistrarplan;
    private javax.swing.JButton btnvolver;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cmbtipo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblservicio;
    private javax.swing.JRadioButton rdactivo;
    private javax.swing.JRadioButton rdinactivo;
    private javax.swing.JTable tablaplan;
    private javax.swing.JTextField txtduracion;
    private javax.swing.JTextField txtidplan;
    private javax.swing.JTextField txtnombrepl;
    private javax.swing.JTextField txtprecio;
    // End of variables declaration//GEN-END:variables
}
