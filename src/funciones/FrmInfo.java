/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package funciones;

import Diseño.diseño;
import fabrica.AppFactory;
import servicio.EmpresaService;
import servicio.Resultado;
import repositorio.EmpresaRepository.EmpresaDatos;
import PRINCIPAL.PRINCIPAL;
import java.awt.Color;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
public class FrmInfo extends javax.swing.JFrame {

    private final EmpresaService empresaService = AppFactory.getInstance().empresaService();
    PRINCIPAL inicio;
    public FrmInfo(PRINCIPAL inicio) {
        this();
        this.inicio = inicio;
        cargarEmpresa();
        txtidempresa.setVisible(false);
    }
    public FrmInfo() {
        initComponents();
        setResizable(false);
        setSize(632, 490);
        setLocationRelativeTo(null);
        diseño.estilizarBotones(btnmod, btnvolver, btnactualizar);
        txtnombre.setVisible(false);
        txtruc.setVisible(false);
        txtdireccion.setVisible(false);
        txttelefono.setVisible(false);
        txtcorreo.setVisible(false);
    }
    int idEmpresa=0;
    private void cargarEmpresa() {
        try {
            EmpresaDatos datos = empresaService.obtener();
            if (datos != null) {
                idEmpresa = datos.idEmpresa();
                lblnombre.setText(datos.nombre());
                lblruc.setText(datos.ruc());
                lbldireccion.setText(datos.direccion());
                lbltelefono.setText(datos.telefono());
                lblcorreo.setText(datos.email());
                txtnombre.setText(datos.nombre());
                txtruc.setText(datos.ruc());
                txtdireccion.setText(datos.direccion());
                txttelefono.setText(datos.telefono());
                txtcorreo.setText(datos.email());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.toString());      
        }

    }
    private void ocultarTextos(){
        txtnombre.setVisible(false);
        txtruc.setVisible(false);
        txtdireccion.setVisible(false);
        txttelefono.setVisible(false);
        txtcorreo.setVisible(false);
    };

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        btnmod = new javax.swing.JButton();
        btnvolver = new javax.swing.JButton();
        nombre = new javax.swing.JLabel();
        ruc = new javax.swing.JLabel();
        direccion = new javax.swing.JLabel();
        telefono = new javax.swing.JLabel();
        correo = new javax.swing.JLabel();
        txttelefono = new javax.swing.JTextField();
        txtdireccion = new javax.swing.JTextField();
        txtruc = new javax.swing.JTextField();
        txtnombre = new javax.swing.JTextField();
        txtcorreo = new javax.swing.JTextField();
        lblnombre = new javax.swing.JLabel();
        lblruc = new javax.swing.JLabel();
        lbldireccion = new javax.swing.JLabel();
        lbltelefono = new javax.swing.JLabel();
        lblcorreo = new javax.swing.JLabel();
        txtidempresa = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        btnactualizar = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/LeonGymLogo.png"))); // NOI18N
        jLabel1.setText("LEÓN GYM");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(3, 25, 620, -1));

        btnmod.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        btnmod.setForeground(new java.awt.Color(255, 255, 255));
        btnmod.setText("MODIFICAR INFORMACIÓN");
        btnmod.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(204, 0, 0)));
        btnmod.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnmodMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnmodMouseExited(evt);
            }
        });
        btnmod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnmodActionPerformed(evt);
            }
        });
        getContentPane().add(btnmod, new org.netbeans.lib.awtextra.AbsoluteConstraints(107, 399, -1, -1));

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
        getContentPane().add(btnvolver, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 430, 190, -1));

        nombre.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        nombre.setForeground(new java.awt.Color(255, 255, 255));
        nombre.setText("NOMBRE");
        getContentPane().add(nombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(107, 102, 68, -1));

        ruc.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        ruc.setForeground(new java.awt.Color(255, 255, 255));
        ruc.setText("RUC");
        getContentPane().add(ruc, new org.netbeans.lib.awtextra.AbsoluteConstraints(107, 161, 68, -1));

        direccion.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        direccion.setForeground(new java.awt.Color(255, 255, 255));
        direccion.setText("DIRECCION");
        getContentPane().add(direccion, new org.netbeans.lib.awtextra.AbsoluteConstraints(107, 220, 160, -1));

        telefono.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        telefono.setForeground(new java.awt.Color(255, 255, 255));
        telefono.setText("TELÉFONO");
        getContentPane().add(telefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(107, 279, 160, -1));

        correo.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        correo.setForeground(new java.awt.Color(255, 255, 255));
        correo.setText("CORREO");
        getContentPane().add(correo, new org.netbeans.lib.awtextra.AbsoluteConstraints(107, 341, 68, -1));

        txttelefono.setBackground(new java.awt.Color(51, 51, 51));
        txttelefono.setForeground(new java.awt.Color(255, 255, 255));
        getContentPane().add(txttelefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(277, 301, 245, -1));

        txtdireccion.setBackground(new java.awt.Color(51, 51, 51));
        txtdireccion.setForeground(new java.awt.Color(255, 255, 255));
        getContentPane().add(txtdireccion, new org.netbeans.lib.awtextra.AbsoluteConstraints(277, 242, 245, -1));

        txtruc.setBackground(new java.awt.Color(51, 51, 51));
        txtruc.setForeground(new java.awt.Color(255, 255, 255));
        getContentPane().add(txtruc, new org.netbeans.lib.awtextra.AbsoluteConstraints(277, 183, 245, -1));

        txtnombre.setBackground(new java.awt.Color(51, 51, 51));
        txtnombre.setForeground(new java.awt.Color(255, 255, 255));
        getContentPane().add(txtnombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(277, 124, 245, -1));

        txtcorreo.setBackground(new java.awt.Color(51, 51, 51));
        txtcorreo.setForeground(new java.awt.Color(255, 255, 255));
        getContentPane().add(txtcorreo, new org.netbeans.lib.awtextra.AbsoluteConstraints(277, 359, 245, -1));

        lblnombre.setForeground(new java.awt.Color(255, 255, 255));
        lblnombre.setText("  ");
        getContentPane().add(lblnombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(277, 102, 245, -1));

        lblruc.setForeground(new java.awt.Color(255, 255, 255));
        lblruc.setText(" ");
        getContentPane().add(lblruc, new org.netbeans.lib.awtextra.AbsoluteConstraints(277, 161, 245, -1));

        lbldireccion.setForeground(new java.awt.Color(255, 255, 255));
        lbldireccion.setText(" ");
        getContentPane().add(lbldireccion, new org.netbeans.lib.awtextra.AbsoluteConstraints(277, 220, 245, -1));

        lbltelefono.setForeground(new java.awt.Color(255, 255, 255));
        lbltelefono.setText(" ");
        getContentPane().add(lbltelefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(277, 279, 245, -1));

        lblcorreo.setForeground(new java.awt.Color(255, 255, 255));
        lblcorreo.setText(" ");
        getContentPane().add(lblcorreo, new org.netbeans.lib.awtextra.AbsoluteConstraints(277, 341, 245, -1));
        getContentPane().add(txtidempresa, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 90, 71, -1));

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("---------------------------------");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(107, 127, 164, -1));

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("---------------------------------");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(107, 186, 164, -1));

        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("---------------------------------");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(107, 245, 164, -1));

        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("---------------------------------");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(107, 304, 164, -1));

        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("---------------------------------");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(107, 362, 164, -1));

        btnactualizar.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        btnactualizar.setForeground(new java.awt.Color(255, 255, 255));
        btnactualizar.setText("ACTUALIZAR");
        btnactualizar.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(204, 0, 0)));
        btnactualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnactualizarActionPerformed(evt);
            }
        });
        getContentPane().add(btnactualizar, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 400, 190, -1));

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/fondo.png"))); // NOI18N
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 740, 800));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnmodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnmodActionPerformed

        String nombre=txtnombre.getText();
        String ruc=txtruc.getText();
        String direccion=txtdireccion.getText();
        String telefono=txttelefono.getText();
        String email=txtcorreo.getText();
        String contra ="utpsports";
        
        JPasswordField pwd = new JPasswordField();
        int conf = JOptionPane.showConfirmDialog(null, pwd, "Ingrese contrasenia", JOptionPane.OK_CANCEL_OPTION);
        if (conf == JOptionPane.OK_OPTION) {
            String contrasenia = new String(pwd.getPassword());
            if (!contrasenia.equals(contra)) {
                JOptionPane.showMessageDialog(null, "Contraseña incorrecta");
                return;
            }
            try {
                Resultado resultado = empresaService.actualizar(idEmpresa, nombre, ruc, direccion, telefono, email);
                if (resultado.exito()) {
                    txtnombre.setVisible(true);
                    txtruc.setVisible(true);
                    txtdireccion.setVisible(true);
                    txttelefono.setVisible(true);
                    txtcorreo.setVisible(true);
                }
                JOptionPane.showMessageDialog(null, resultado.mensaje());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.toString());
            }
        }

        cargarEmpresa();
    }//GEN-LAST:event_btnmodActionPerformed
        
    private void btnvolverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnvolverActionPerformed
        if(inicio!=null){
        inicio.setVisible(true);} 
        this.setVisible(false);
    }//GEN-LAST:event_btnvolverActionPerformed

    private void btnmodMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnmodMouseEntered
        btnmod.setForeground(new Color(204,0,0));
    }//GEN-LAST:event_btnmodMouseEntered

    private void btnmodMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnmodMouseExited
        btnmod.setForeground(new Color(255,255,255)); 
    }//GEN-LAST:event_btnmodMouseExited

    private void btnvolverMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnvolverMouseEntered
        btnvolver.setForeground(new Color(204,0,0));
    }//GEN-LAST:event_btnvolverMouseEntered

    private void btnvolverMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnvolverMouseExited
        btnmod.setForeground(new Color(255,255,255));
    }//GEN-LAST:event_btnvolverMouseExited

    private void btnactualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnactualizarActionPerformed
        ocultarTextos();
    }//GEN-LAST:event_btnactualizarActionPerformed

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
            java.util.logging.Logger.getLogger(FrmInfo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmInfo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmInfo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmInfo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmInfo().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnactualizar;
    private javax.swing.JButton btnmod;
    private javax.swing.JButton btnvolver;
    private javax.swing.JLabel correo;
    private javax.swing.JLabel direccion;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel lblcorreo;
    private javax.swing.JLabel lbldireccion;
    private javax.swing.JLabel lblnombre;
    private javax.swing.JLabel lblruc;
    private javax.swing.JLabel lbltelefono;
    private javax.swing.JLabel nombre;
    private javax.swing.JLabel ruc;
    private javax.swing.JLabel telefono;
    private javax.swing.JTextField txtcorreo;
    private javax.swing.JTextField txtdireccion;
    private javax.swing.JTextField txtidempresa;
    private javax.swing.JTextField txtnombre;
    private javax.swing.JTextField txtruc;
    private javax.swing.JTextField txttelefono;
    // End of variables declaration//GEN-END:variables
}
