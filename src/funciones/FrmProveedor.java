/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package funciones;

import Diseño.diseño;
import fabrica.AppFactory;
import servicio.ProveedorService;
import servicio.Resultado;
import repositorio.ProveedorRepository.ProveedorFila;
import static java.awt.image.ImageObserver.ALLBITS;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class FrmProveedor extends javax.swing.JFrame {

    private final ProveedorService proveedorService = AppFactory.getInstance().proveedorService();
    private FrmProductos frmProductos;
    private int idProveedorSeleccionado = 0;

    public FrmProveedor(FrmProductos frmProductos) {
        initComponents();
        setResizable(false);
        setSize(750, 430);
        setLocationRelativeTo(null);
        this.frmProductos = frmProductos;
        txtidproveedor.setVisible(false);
        diseño.estilizarBotones(btnmodificar, btneliminar, btnlimpiar, btnregistrar, btnvolver);
        diseño.estilizarTabla(tablaproveedor, jScrollPane1, ALLBITS);
        cargartabla();        
        tablaproveedor.getColumnModel().getColumn(0).setMinWidth(0);
        tablaproveedor.getColumnModel().getColumn(0).setMaxWidth(0);
        tablaproveedor.getColumnModel().getColumn(0).setWidth(0);
    }

    public FrmProveedor() { initComponents(); }

    private void cargartabla() {
        DefaultTableModel modelo = (DefaultTableModel) tablaproveedor.getModel();
        modelo.setRowCount(0);
        try {
            for (ProveedorFila fila : proveedorService.listarTodos()) {
                modelo.addRow(new Object[]{
                    fila.idProveedor(), fila.nombre(), fila.ruc(), fila.direccion(), fila.telefono(), fila.email()
                });
            }
        } catch (Exception e) { JOptionPane.showMessageDialog(null, e.toString()); }
    }

    private void limpiar() {
        txtidproveedor.setText(""); txtnombre.setText(""); txtruc.setText("");
        txtdireccion.setText(""); txttelefono.setText(""); txtemail.setText("");
        idProveedorSeleccionado = 0;
        btnregistrar.setEnabled(true);
        tablaproveedor.clearSelection();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtidproveedor = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaproveedor = new javax.swing.JTable();
        txtnombre = new javax.swing.JTextField();
        txtruc = new javax.swing.JTextField();
        txtdireccion = new javax.swing.JTextField();
        txttelefono = new javax.swing.JTextField();
        txtemail = new javax.swing.JTextField();
        btnregistrar = new javax.swing.JButton();
        btnvolver = new javax.swing.JButton();
        btnlimpiar = new javax.swing.JButton();
        btnmodificar = new javax.swing.JButton();
        btneliminar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        getContentPane().add(txtidproveedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 340, 71, -1));

        tablaproveedor.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID", "NOMBRE", "RUC", "DIRECCIÓN", "TELEFONO", "EMAIL"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaproveedor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaproveedorMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablaproveedor);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 30, -1, 275));

        txtnombre.setBackground(new java.awt.Color(51, 51, 51));
        txtnombre.setForeground(new java.awt.Color(255, 255, 255));
        getContentPane().add(txtnombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(113, 97, 153, -1));

        txtruc.setBackground(new java.awt.Color(51, 51, 51));
        txtruc.setForeground(new java.awt.Color(255, 255, 255));
        getContentPane().add(txtruc, new org.netbeans.lib.awtextra.AbsoluteConstraints(113, 137, 153, -1));

        txtdireccion.setBackground(new java.awt.Color(51, 51, 51));
        txtdireccion.setForeground(new java.awt.Color(255, 255, 255));
        getContentPane().add(txtdireccion, new org.netbeans.lib.awtextra.AbsoluteConstraints(113, 177, 153, -1));

        txttelefono.setBackground(new java.awt.Color(51, 51, 51));
        txttelefono.setForeground(new java.awt.Color(255, 255, 255));
        getContentPane().add(txttelefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(113, 217, 153, -1));

        txtemail.setBackground(new java.awt.Color(51, 51, 51));
        txtemail.setForeground(new java.awt.Color(255, 255, 255));
        getContentPane().add(txtemail, new org.netbeans.lib.awtextra.AbsoluteConstraints(113, 257, 153, -1));

        btnregistrar.setText("REGISTRAR");
        btnregistrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnregistrarActionPerformed(evt);
            }
        });
        getContentPane().add(btnregistrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(38, 291, -1, -1));

        btnvolver.setText("VOLVER");
        btnvolver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnvolverActionPerformed(evt);
            }
        });
        getContentPane().add(btnvolver, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 360, -1, -1));

        btnlimpiar.setText("LIMPIAR");
        btnlimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnlimpiarActionPerformed(evt);
            }
        });
        getContentPane().add(btnlimpiar, new org.netbeans.lib.awtextra.AbsoluteConstraints(38, 326, 90, -1));

        btnmodificar.setText("MODIFICAR");
        btnmodificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnmodificarActionPerformed(evt);
            }
        });
        getContentPane().add(btnmodificar, new org.netbeans.lib.awtextra.AbsoluteConstraints(174, 291, -1, -1));

        btneliminar.setText("ELIMINAR");
        btneliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btneliminarActionPerformed(evt);
            }
        });
        getContentPane().add(btneliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(174, 326, 92, -1));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 3, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/LeonGymLogo.png"))); // NOI18N
        jLabel1.setText("PROVEEDORES");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 228, -1));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText(" NOMBRE");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 90, -1));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText(" RUC");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 90, -1));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("DIRECCIÓN");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(11, 180, 90, -1));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("TELEFONO");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(11, 220, 90, -1));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("EMAIL");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(11, 260, 90, -1));

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/fondo.png"))); // NOI18N
        jLabel7.setText("jLabel7");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tablaproveedorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaproveedorMouseClicked
        int fila = tablaproveedor.getSelectedRow();
        if (fila >= 0) {
            DefaultTableModel modelo = (DefaultTableModel) tablaproveedor.getModel();
            idProveedorSeleccionado = Integer.parseInt(modelo.getValueAt(fila, 0).toString());
            txtnombre.setText(modelo.getValueAt(fila, 1).toString());
            txtruc.setText(modelo.getValueAt(fila, 2).toString());
            txtdireccion.setText(modelo.getValueAt(fila, 3).toString());
            txttelefono.setText(modelo.getValueAt(fila, 4).toString());
            txtemail.setText(modelo.getValueAt(fila, 5).toString());
            btnregistrar.setEnabled(false);
        }
    }//GEN-LAST:event_tablaproveedorMouseClicked

    private void btnvolverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnvolverActionPerformed
        this.setVisible(false);
        if (frmProductos != null) {
            frmProductos.recargarCombos();
            frmProductos.setVisible(true);
        }
    }//GEN-LAST:event_btnvolverActionPerformed

    private void btnlimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnlimpiarActionPerformed
        limpiar();
    }//GEN-LAST:event_btnlimpiarActionPerformed

    private void btnregistrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnregistrarActionPerformed
        if (txtnombre.getText().isEmpty() || txtruc.getText().isEmpty() ||
            txtdireccion.getText().isEmpty() || txttelefono.getText().isEmpty() ||
            txtemail.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Complete todos los campos."); return;
        }
        try {
            Resultado resultado = proveedorService.registrar(txtnombre.getText(), txtruc.getText(),
                txtdireccion.getText(), txttelefono.getText(), txtemail.getText());
            JOptionPane.showMessageDialog(null, resultado.mensaje());
            if (resultado.exito()) { limpiar(); cargartabla(); }
        } catch (Exception e) { JOptionPane.showMessageDialog(null, e.toString()); }
    }//GEN-LAST:event_btnregistrarActionPerformed

    private void btnmodificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnmodificarActionPerformed
        if (idProveedorSeleccionado == 0) {
            JOptionPane.showMessageDialog(null, "Seleccione un proveedor."); return;
        }
        try {
            Resultado resultado = proveedorService.actualizar(idProveedorSeleccionado, txtnombre.getText(),
                txtruc.getText(), txtdireccion.getText(), txttelefono.getText(), txtemail.getText());
            JOptionPane.showMessageDialog(null, resultado.mensaje());
            if (resultado.exito()) { limpiar(); cargartabla(); }
        } catch (Exception e) { JOptionPane.showMessageDialog(null, e.toString()); }
    }//GEN-LAST:event_btnmodificarActionPerformed

    private void btneliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btneliminarActionPerformed
        if (idProveedorSeleccionado == 0) {
            JOptionPane.showMessageDialog(null, "Seleccione un proveedor."); return;
        }
        int conf = JOptionPane.showConfirmDialog(null, "¿Eliminar proveedor?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (conf == JOptionPane.YES_OPTION) {
            try {
                Resultado resultado = proveedorService.eliminar(idProveedorSeleccionado);
                JOptionPane.showMessageDialog(null, resultado.mensaje());
                if (resultado.exito()) { limpiar(); cargartabla(); }
            } catch (Exception e) { JOptionPane.showMessageDialog(null, e.toString()); }
        }
    }//GEN-LAST:event_btneliminarActionPerformed

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
            java.util.logging.Logger.getLogger(FrmProveedor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmProveedor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmProveedor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmProveedor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmProveedor().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btneliminar;
    private javax.swing.JButton btnlimpiar;
    private javax.swing.JButton btnmodificar;
    private javax.swing.JButton btnregistrar;
    private javax.swing.JButton btnvolver;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tablaproveedor;
    private javax.swing.JTextField txtdireccion;
    private javax.swing.JTextField txtemail;
    private javax.swing.JTextField txtidproveedor;
    private javax.swing.JTextField txtnombre;
    private javax.swing.JTextField txtruc;
    private javax.swing.JTextField txttelefono;
    // End of variables declaration//GEN-END:variables
}
