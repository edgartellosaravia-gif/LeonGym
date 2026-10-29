/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package funciones;

import Diseño.diseño;
import PRINCIPAL.PRINCIPAL;
import fabrica.AppFactory;
import servicio.SucursalService;
import servicio.Resultado;
import repositorio.SucursalRepository.SucursalFila;
import funciones.FrmClientes;
import funciones.FrmEmpleados;
import funciones.FrmInventario;
import funciones.FrmVenta;
import static java.awt.image.ImageObserver.ALLBITS;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
public class FrmSucursal extends javax.swing.JFrame {

    private final SucursalService sucursalService = AppFactory.getInstance().sucursalService();
    PRINCIPAL inicio;
    int idEmpleado=0;
    public FrmSucursal(PRINCIPAL inicio, int idEmpleado) {
        this.inicio=inicio;
        this.idEmpleado=idEmpleado;
        initComponents();
        setResizable(false);
        setSize(1040, 400);
        setLocationRelativeTo(null);
        diseño.estilizarBotones(btnmodificar, btneliminar, btnregistrar, btnvolver, btnlimpiar);
        diseño.estilizarTabla(tabla, jScrollPane1, ALLBITS);
        cargartabla();
        cargarcmb();
        txtidsucursal.setVisible(false);
        // El botón ELIMINAR no tenía ninguna acción conectada en el diseño
        // original (existía en pantalla pero no hacía nada). Se conecta acá,
        // fuera de initComponents(), para no tocar el bloque generado por
        // el editor de formularios.
        btneliminar.addActionListener(evt -> btneliminarActionPerformed(evt));
    }
    
    public FrmSucursal() {
        initComponents();
        
    }
    int idSucursalSeleccionada = 0;
    String nombreSucursalSeleccionada ="";
    private void cargarcmb(){
        cmbaccion.removeAllItems();
        cmbaccion.addItem("VER GESTIÓN DE:");
        cmbaccion.addItem("CLIENTES");
        cmbaccion.addItem("EMPLEADOS");
        cmbaccion.addItem("INVENTARIO");
        cmbaccion.addItem("REPORTES");
        cmbaccion.addItem("SERVICIOS");
    }
    
private void cargartabla() {
    DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
    modelo.setRowCount(0);
    try {
        for (SucursalFila fila : sucursalService.listarTodas()) {
            modelo.addRow(new Object[] {
                fila.idSucursal(), fila.nombreEmpresa(), fila.nombreSucursal(),
                fila.direccion(), fila.telefono()
            });
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, e.toString());
    }
}
    private void limpiar() {
        txtnombreS.setText(""); 
        txtdireccionS.setText("");
        txttelefonoS.setText("");
        tabla.clearSelection(); 
        idSucursalSeleccionada = 0;
        btnregistrar.setEnabled(true);
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        lblempresa = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtnombreS = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtdireccionS = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txttelefonoS = new javax.swing.JTextField();
        txtidsucursal = new javax.swing.JTextField();
        btnregistrar = new javax.swing.JButton();
        btnmodificar = new javax.swing.JButton();
        btnlimpiar = new javax.swing.JButton();
        btneliminar = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();
        cmbaccion = new javax.swing.JComboBox<>();
        btnvolver = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();

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

        lblempresa.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblempresa.setForeground(new java.awt.Color(255, 255, 255));
        lblempresa.setText("León Gym");
        getContentPane().add(lblempresa, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 90, 222, 20));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/LeonGymLogo.png"))); // NOI18N
        jLabel1.setText("SUCURSALES LEÓN GYM");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 18, 335, -1));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("EMPRESA");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(28, 93, 90, -1));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("SEDE");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(28, 130, 90, -1));

        txtnombreS.setBackground(new java.awt.Color(51, 51, 51));
        txtnombreS.setForeground(new java.awt.Color(255, 255, 255));
        getContentPane().add(txtnombreS, new org.netbeans.lib.awtextra.AbsoluteConstraints(162, 127, 222, -1));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("DIRECCION");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(28, 170, 90, -1));

        txtdireccionS.setBackground(new java.awt.Color(51, 51, 51));
        txtdireccionS.setForeground(new java.awt.Color(255, 255, 255));
        txtdireccionS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtdireccionSActionPerformed(evt);
            }
        });
        getContentPane().add(txtdireccionS, new org.netbeans.lib.awtextra.AbsoluteConstraints(162, 167, 222, -1));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("N° TELEFONICO");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(28, 210, -1, -1));

        txttelefonoS.setBackground(new java.awt.Color(51, 51, 51));
        txttelefonoS.setForeground(new java.awt.Color(255, 255, 255));
        getContentPane().add(txttelefonoS, new org.netbeans.lib.awtextra.AbsoluteConstraints(162, 207, 222, -1));

        txtidsucursal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtidsucursalActionPerformed(evt);
            }
        });
        getContentPane().add(txtidsucursal, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 20, 71, -1));

        btnregistrar.setText("REGISTRAR");
        btnregistrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnregistrarActionPerformed(evt);
            }
        });
        getContentPane().add(btnregistrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(28, 241, 112, -1));

        btnmodificar.setText("MODIFICAR");
        btnmodificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnmodificarActionPerformed(evt);
            }
        });
        getContentPane().add(btnmodificar, new org.netbeans.lib.awtextra.AbsoluteConstraints(272, 241, 112, -1));

        btnlimpiar.setText("LIMPIAR");
        btnlimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnlimpiarActionPerformed(evt);
            }
        });
        getContentPane().add(btnlimpiar, new org.netbeans.lib.awtextra.AbsoluteConstraints(28, 282, 112, -1));

        btneliminar.setText("ELIMINAR");
        getContentPane().add(btneliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(272, 282, 112, -1));

        tabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID", "EMPRESA", "SEDE", "DIRECCION", "TELEFONO"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabla.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tabla);

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(402, 81, 620, 275));

        cmbaccion.setBackground(new java.awt.Color(51, 51, 51));
        cmbaccion.setForeground(new java.awt.Color(255, 255, 255));
        cmbaccion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbaccionActionPerformed(evt);
            }
        });
        getContentPane().add(cmbaccion, new org.netbeans.lib.awtextra.AbsoluteConstraints(28, 323, 135, -1));

        btnvolver.setText("VOLVER");
        btnvolver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnvolverActionPerformed(evt);
            }
        });
        getContentPane().add(btnvolver, new org.netbeans.lib.awtextra.AbsoluteConstraints(272, 323, 112, -1));
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(347, 8, 37, -1));

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/fondo.png"))); // NOI18N
        jLabel7.setToolTipText("");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 860, -1));

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/fondo.png"))); // NOI18N
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtdireccionSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtdireccionSActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtdireccionSActionPerformed

    private void btnregistrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnregistrarActionPerformed
    try {
        Resultado resultado = sucursalService.registrar(txtnombreS.getText(), txtdireccionS.getText(), txttelefonoS.getText());
        JOptionPane.showMessageDialog(null, resultado.mensaje());
        if (resultado.exito()) cargartabla();
    } catch (Exception e) { JOptionPane.showMessageDialog(null, e.toString()); }
    }//GEN-LAST:event_btnregistrarActionPerformed

    private void btnvolverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnvolverActionPerformed
        if(inicio!=null){
        inicio.setVisible(true);} 
        this.setVisible(false);
    }//GEN-LAST:event_btnvolverActionPerformed

    private void cmbaccionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbaccionActionPerformed
        
   if (cmbaccion.getSelectedIndex() <= 0) return;
    if (idSucursalSeleccionada == 0) {
        JOptionPane.showMessageDialog(null, "Seleccione una sucursal primero");
        cmbaccion.setSelectedIndex(0);
        return;
    }
    String seleccion = cmbaccion.getSelectedItem().toString();
    Diseño.NavegacionUtil.navegar(cmbaccion, () -> {
    switch (seleccion) {
        case "CLIENTES":
            new FrmClientes(this, idSucursalSeleccionada, idEmpleado,
                tabla.getValueAt(tabla.getSelectedRow(), 2).toString(), "ADMINISTRACIÓN").setVisible(true);
            this.setVisible(false);
            break;
        case "EMPLEADOS":
            System.out.println(""+ idSucursalSeleccionada);
            new FrmEmpleados(this, idSucursalSeleccionada,
            tabla.getValueAt(tabla.getSelectedRow(), 2).toString()).setVisible(true);
            this.setVisible(false);
            break;
        case "INVENTARIO":
            System.out.println(""+ idSucursalSeleccionada);
            new FrmInventario(this, idSucursalSeleccionada,
            tabla.getValueAt(tabla.getSelectedRow(), 2).toString()).setVisible(true);
            this.setVisible(false);
            break;
        case "REPORTES":
            new FrmVenta(this, idSucursalSeleccionada,
                tabla.getValueAt(tabla.getSelectedRow(), 2).toString()).setVisible(true);
            this.setVisible(false);
            break;
        case "SERVICIOS":
            System.out.println(""+ idSucursalSeleccionada);
            new FrmServicio(this, idSucursalSeleccionada,
            tabla.getValueAt(tabla.getSelectedRow(), 2).toString()).setVisible(true);
            this.setVisible(false);
            break;
    }
    });

    }//GEN-LAST:event_cmbaccionActionPerformed

    private void tablaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaMouseClicked
    int fila = tabla.getSelectedRow();
    if (fila >= 0) {
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        txtidsucursal.setText(modelo.getValueAt(fila, 0).toString());
        idSucursalSeleccionada = Integer.parseInt(modelo.getValueAt(fila, 0).toString());
        lblempresa.setText(modelo.getValueAt(fila, 1).toString());
        txtnombreS.setText(modelo.getValueAt(fila, 2).toString());
        txtdireccionS.setText(modelo.getValueAt(fila, 3).toString());
        txttelefonoS.setText(modelo.getValueAt(fila, 4).toString());
        cmbaccion.setEnabled(true);
        cmbaccion.setSelectedIndex(0);
        btnregistrar.setEnabled(false);
    }
    }//GEN-LAST:event_tablaMouseClicked

    private void btnmodificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnmodificarActionPerformed
    try {
        Resultado resultado = sucursalService.actualizar(idSucursalSeleccionada, txtnombreS.getText(), txtdireccionS.getText(), txttelefonoS.getText());
        JOptionPane.showMessageDialog(null, resultado.mensaje());
        if (resultado.exito()) cargartabla();
    } catch (Exception e) { JOptionPane.showMessageDialog(null, e.toString()); }
    }//GEN-LAST:event_btnmodificarActionPerformed

    private void btnlimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnlimpiarActionPerformed
        limpiar();
    }//GEN-LAST:event_btnlimpiarActionPerformed

    private void btneliminarActionPerformed(java.awt.event.ActionEvent evt) {
        if (idSucursalSeleccionada == 0) {
            JOptionPane.showMessageDialog(null, "Seleccione una sucursal de la tabla");
            return;
        }
        int conf = JOptionPane.showConfirmDialog(null, "¿Eliminar sucursal?",
            "Confirmar", JOptionPane.YES_NO_OPTION);
        if (conf == JOptionPane.YES_OPTION) {
            Resultado resultado = sucursalService.eliminar(idSucursalSeleccionada);
            JOptionPane.showMessageDialog(null, resultado.mensaje());
            if (resultado.exito()) { limpiar(); cargartabla(); }
        }
    }

    private void txtidsucursalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtidsucursalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtidsucursalActionPerformed

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
            java.util.logging.Logger.getLogger(FrmSucursal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmSucursal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmSucursal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmSucursal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmSucursal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btneliminar;
    private javax.swing.JButton btnlimpiar;
    private javax.swing.JButton btnmodificar;
    private javax.swing.JButton btnregistrar;
    private javax.swing.JButton btnvolver;
    private javax.swing.JComboBox<String> cmbaccion;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblempresa;
    private javax.swing.JTable tabla;
    private javax.swing.JTextField txtdireccionS;
    private javax.swing.JTextField txtidsucursal;
    private javax.swing.JTextField txtnombreS;
    private javax.swing.JTextField txttelefonoS;
    // End of variables declaration//GEN-END:variables
}
