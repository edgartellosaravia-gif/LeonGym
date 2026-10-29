package funciones;

import Diseño.diseño;
import fabrica.AppFactory;
import servicio.CategoriaService;
import servicio.Resultado;
import repositorio.CategoriaRepository.CategoriaFila;
import java.awt.Color;
import static java.awt.image.ImageObserver.ALLBITS;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class FrmCategoria extends javax.swing.JFrame {

    private final CategoriaService categoriaService = AppFactory.getInstance().categoriaService();
    private FrmProductos frmProductos;
    private int idCategoriaSeleccionada = 0;

    public FrmCategoria(FrmProductos frmProductos) {
        initComponents();
        setSize(700, 370);
        setResizable(false);
        setLocationRelativeTo(null); 
        this.frmProductos = frmProductos;
        txtidCategoria.setVisible(false);
        diseño.estilizarBotones(btnregistrar, btnmodificar, btnlimpiar, btneliminar, btnvolver);
        diseño.estilizarTabla(tablacategoria, jScrollPane1, ALLBITS);
        cargartabla();        
        tablacategoria.getColumnModel().getColumn(0).setMinWidth(0);
        tablacategoria.getColumnModel().getColumn(0).setMaxWidth(0);
        tablacategoria.getColumnModel().getColumn(0).setWidth(0);
    }

    public FrmCategoria() { initComponents(); }

    private void cargartabla() {
        DefaultTableModel modelo = (DefaultTableModel) tablacategoria.getModel();
        modelo.setRowCount(0);
        try {
            for (CategoriaFila fila : categoriaService.listarTodas()) {
                modelo.addRow(new Object[]{ fila.idCategoria(), fila.nombre(), fila.descripcion() });
            }
        } catch (Exception e) { JOptionPane.showMessageDialog(null, e.toString()); }
    }

    private void limpiar() {
        txtidCategoria.setText("");
        txtnombre.setText("");
        txtdescripcion.setText("");
        idCategoriaSeleccionada = 0;
        btnregistrar.setEnabled(true);
        tablacategoria.clearSelection();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tablacategoria = new javax.swing.JTable();
        btnvolver = new javax.swing.JButton();
        btneliminar = new javax.swing.JButton();
        btnlimpiar = new javax.swing.JButton();
        btnmodificar = new javax.swing.JButton();
        btnregistrar = new javax.swing.JButton();
        txtidCategoria = new javax.swing.JTextField();
        txtnombre = new javax.swing.JTextField();
        txtdescripcion = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tablacategoria.setBackground(new java.awt.Color(102, 102, 102));
        tablacategoria.setBorder(new javax.swing.border.MatteBorder(null));
        tablacategoria.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        tablacategoria.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "ID", "NOMBRE", "DESCRIPCIÓN"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablacategoria.setGridColor(new java.awt.Color(51, 51, 51));
        tablacategoria.setSelectionBackground(new java.awt.Color(153, 153, 153));
        tablacategoria.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablacategoriaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablacategoria);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 20, 375, 270));

        btnvolver.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        btnvolver.setForeground(new java.awt.Color(255, 255, 255));
        btnvolver.setText("VOLVER");
        btnvolver.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(204, 0, 0)));
        btnvolver.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnvolverMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnvolverMouseExited(evt);
            }
        });
        btnvolver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnvolverActionPerformed(evt);
            }
        });
        getContentPane().add(btnvolver, new org.netbeans.lib.awtextra.AbsoluteConstraints(103, 265, 90, -1));

        btneliminar.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        btneliminar.setForeground(new java.awt.Color(255, 255, 255));
        btneliminar.setText("ELIMINAR");
        btneliminar.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(204, 0, 0)));
        btneliminar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btneliminarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btneliminarMouseExited(evt);
            }
        });
        btneliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btneliminarActionPerformed(evt);
            }
        });
        getContentPane().add(btneliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 230, 111, -1));

        btnlimpiar.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        btnlimpiar.setForeground(new java.awt.Color(255, 255, 255));
        btnlimpiar.setText("LIMPIAR");
        btnlimpiar.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(204, 0, 0)));
        btnlimpiar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnlimpiarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnlimpiarMouseExited(evt);
            }
        });
        btnlimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnlimpiarActionPerformed(evt);
            }
        });
        getContentPane().add(btnlimpiar, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 230, 110, -1));

        btnmodificar.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        btnmodificar.setForeground(new java.awt.Color(255, 255, 255));
        btnmodificar.setText("MODIFICAR");
        btnmodificar.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(204, 0, 0)));
        btnmodificar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnmodificarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnmodificarMouseExited(evt);
            }
        });
        btnmodificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnmodificarActionPerformed(evt);
            }
        });
        getContentPane().add(btnmodificar, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 190, 110, -1));

        btnregistrar.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        btnregistrar.setForeground(new java.awt.Color(255, 255, 255));
        btnregistrar.setText("REGISTRAR");
        btnregistrar.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(204, 0, 0)));
        btnregistrar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnregistrarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnregistrarMouseExited(evt);
            }
        });
        btnregistrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnregistrarActionPerformed(evt);
            }
        });
        getContentPane().add(btnregistrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 190, 110, -1));
        getContentPane().add(txtidCategoria, new org.netbeans.lib.awtextra.AbsoluteConstraints(103, 73, 71, -1));

        txtnombre.setBackground(new java.awt.Color(51, 51, 51));
        txtnombre.setForeground(new java.awt.Color(255, 255, 255));
        txtnombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtnombreActionPerformed(evt);
            }
        });
        getContentPane().add(txtnombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(103, 107, 148, -1));

        txtdescripcion.setBackground(new java.awt.Color(51, 51, 51));
        txtdescripcion.setForeground(new java.awt.Color(255, 255, 255));
        getContentPane().add(txtdescripcion, new org.netbeans.lib.awtextra.AbsoluteConstraints(103, 147, 148, -1));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("NOMBRE");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 110, 79, -1));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("DESCRIPCIÓN");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 150, -1, -1));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/LeonGymLogo.png"))); // NOI18N
        jLabel3.setText("CATEGORÍA");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 14, 287, -1));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/fondo.png"))); // NOI18N
        jLabel4.setText("jLabel4");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tablacategoriaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablacategoriaMouseClicked
        int fila = tablacategoria.getSelectedRow();
        if (fila >= 0) {
            DefaultTableModel modelo = (DefaultTableModel) tablacategoria.getModel();
            idCategoriaSeleccionada = Integer.parseInt(modelo.getValueAt(fila, 0).toString());
            txtnombre.setText(modelo.getValueAt(fila, 1).toString());
            txtdescripcion.setText(modelo.getValueAt(fila, 2).toString());
            btnregistrar.setEnabled(false);
        }
    }//GEN-LAST:event_tablacategoriaMouseClicked

    private void btnvolverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnvolverActionPerformed
        this.setVisible(false);
        if (frmProductos != null) {
            frmProductos.recargarCombos(); // ← recarga los combos
            frmProductos.setVisible(true);
        }
    }//GEN-LAST:event_btnvolverActionPerformed

    private void btneliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btneliminarActionPerformed
        if (idCategoriaSeleccionada == 0) {
            JOptionPane.showMessageDialog(null, "Seleccione una categoría."); return;
        }
        int conf = JOptionPane.showConfirmDialog(null, "¿Eliminar categoría?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (conf == JOptionPane.YES_OPTION) {
            try {
                Resultado resultado = categoriaService.eliminar(idCategoriaSeleccionada);
                JOptionPane.showMessageDialog(null, resultado.mensaje());
                if (resultado.exito()) { limpiar(); cargartabla(); }
            } catch (Exception e) { JOptionPane.showMessageDialog(null, e.toString()); }
        }
    }//GEN-LAST:event_btneliminarActionPerformed

    private void btnlimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnlimpiarActionPerformed
        limpiar();
    }//GEN-LAST:event_btnlimpiarActionPerformed

    private void btnmodificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnmodificarActionPerformed
        if (idCategoriaSeleccionada == 0) {
            JOptionPane.showMessageDialog(null, "Seleccione una categoría."); return;
        }
        try {
            Resultado resultado = categoriaService.actualizar(idCategoriaSeleccionada, txtnombre.getText(), txtdescripcion.getText());
            JOptionPane.showMessageDialog(null, resultado.mensaje());
            if (resultado.exito()) { limpiar(); cargartabla(); }
        } catch (Exception e) { JOptionPane.showMessageDialog(null, e.toString()); }
    }//GEN-LAST:event_btnmodificarActionPerformed

    private void btnregistrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnregistrarActionPerformed
        if (txtnombre.getText().isEmpty() || txtdescripcion.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Complete todos los campos."); return;
        }
        try {
            Resultado resultado = categoriaService.registrar(txtnombre.getText(), txtdescripcion.getText());
            JOptionPane.showMessageDialog(null, resultado.mensaje());
            if (resultado.exito()) { limpiar(); cargartabla(); }
        } catch (Exception e) { JOptionPane.showMessageDialog(null, e.toString()); }
    }//GEN-LAST:event_btnregistrarActionPerformed

    private void btnregistrarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnregistrarMouseEntered
        btnregistrar.setForeground(new Color(204,0,0));
    }//GEN-LAST:event_btnregistrarMouseEntered

    private void btnregistrarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnregistrarMouseExited
        btnregistrar.setForeground(new Color(255,255,255));
    }//GEN-LAST:event_btnregistrarMouseExited

    private void btnlimpiarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnlimpiarMouseEntered
        btnlimpiar.setForeground(new Color(204,0,0));
    }//GEN-LAST:event_btnlimpiarMouseEntered

    private void btnlimpiarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnlimpiarMouseExited
        btnlimpiar.setForeground(new Color(255,255,255));
    }//GEN-LAST:event_btnlimpiarMouseExited

    private void btnmodificarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnmodificarMouseEntered
        btnmodificar.setForeground(new Color(204,0,0));
    }//GEN-LAST:event_btnmodificarMouseEntered

    private void btnmodificarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnmodificarMouseExited
        btnmodificar.setForeground(new Color(255,255,255));
    }//GEN-LAST:event_btnmodificarMouseExited

    private void btneliminarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btneliminarMouseEntered
        btneliminar.setForeground(new Color(204,0,0));
    }//GEN-LAST:event_btneliminarMouseEntered

    private void btneliminarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btneliminarMouseExited
        btneliminar.setForeground(new Color(255,255,255));
    }//GEN-LAST:event_btneliminarMouseExited

    private void btnvolverMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnvolverMouseEntered
        btnvolver.setForeground(new Color(204,0,0));
    }//GEN-LAST:event_btnvolverMouseEntered

    private void btnvolverMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnvolverMouseExited
        btnvolver.setForeground(new Color(255,255,255));
    }//GEN-LAST:event_btnvolverMouseExited

    private void txtnombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtnombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtnombreActionPerformed

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
            java.util.logging.Logger.getLogger(FrmCategoria.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmCategoria.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmCategoria.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmCategoria.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmCategoria().setVisible(true);
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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tablacategoria;
    private javax.swing.JTextField txtdescripcion;
    private javax.swing.JTextField txtidCategoria;
    private javax.swing.JTextField txtnombre;
    // End of variables declaration//GEN-END:variables
}
