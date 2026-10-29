/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package funciones;

import Diseño.diseño;
import fabrica.AppFactory;
import servicio.VentaService;
import repositorio.VentaRepository.DashboardVenta;
import repositorio.VentaRepository.VentaFila;
import static java.awt.image.ImageObserver.ALLBITS;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class FrmVenta extends javax.swing.JFrame {
private final VentaService ventaService = AppFactory.getInstance().ventaService();
private FrmSucursal frmSucursal;
private int idSucursal;
private String nombreSucursal;

public FrmVenta(FrmSucursal frmSucursal, int idSucursal, String nombreSucursal) {
        initComponents();
        setResizable(false);
        setSize(1110, 515);
        setLocationRelativeTo(null);
        this.frmSucursal    = frmSucursal;
        this.idSucursal     = idSucursal;
        this.nombreSucursal = nombreSucursal;
        setTitle("Reportes - " + nombreSucursal);
        lblsucursal.setText(nombreSucursal);
        cargarCombosAnioMes();
        ocultarCombosAnioMes();
        cargarDashboard();
        diseño.estilizarBotones(btnactualizar, btnfactura, btnvolver, btnbuscarfecha);
        diseño.estilizarTabla(tablaventas, jScrollPane1, ALLBITS);
        cargarHistorialVentas();
        tablaventas.getColumnModel().getColumn(0).setMinWidth(0);
        tablaventas.getColumnModel().getColumn(0).setMaxWidth(0);
        tablaventas.getColumnModel().getColumn(0).setWidth(0);
    }

    public FrmVenta() { initComponents(); }

    // ── COMBOS FECHA ─────────────────────────────────────────────────────────
    private void cargarCombosAnioMes() {
        int anioActual = java.time.LocalDate.now().getYear();
        for (int i = anioActual - 3; i <= anioActual; i++) cmbanio.addItem(String.valueOf(i));
        String[] meses = {"Enero","Febrero","Marzo","Abril","Mayo","Junio",
                          "Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre"};
        for (String m : meses) {
            cmbmes.addItem(m);
        }
    }

    private void ocultarCombosAnioMes() {
        cmbanio.setVisible(false);
        cmbmes.setVisible(false);
        btnbuscarfecha.setVisible(false);
    }

    private void mostrarCombosAnioMes() {
        cmbanio.setVisible(true);
        cmbmes.setVisible(true);
        btnbuscarfecha.setVisible(true);
        // Cargar fecha actual por defecto
        java.time.LocalDate hoy = java.time.LocalDate.now();
        cmbanio.setSelectedItem(String.valueOf(hoy.getYear()));
        cmbmes.setSelectedIndex(hoy.getMonthValue() - 1);
    }

    // ── FILTRO FECHA ──────────────────────────────────────────────────────────
    private String getFiltroFecha() {
        switch (cmbperiodo.getSelectedIndex()) {
            case 0: return "DATE(fechaV) = CURDATE()";
            case 1: return "fechaV >= DATE_SUB(CURDATE(), INTERVAL 7 DAY)";
            case 2: return "MONTH(fechaV) = MONTH(CURDATE()) AND YEAR(fechaV) = YEAR(CURDATE())";
            case 3: // POR MES
                int anio = Integer.parseInt(cmbanio.getSelectedItem().toString());
                int mes  = cmbmes.getSelectedIndex() + 1;
                return String.format("MONTH(fechaV) = %d AND YEAR(fechaV) = %d", mes, anio);
            default: return "1=1";
        }
    }
    // ── DASHBOARD ────────────────────────────────────────────────────────────
    private void cargarDashboard() {
        String filtroFecha = getFiltroFecha();
        try {
            DashboardVenta d = ventaService.obtenerDashboard(idSucursal, filtroFecha);
            lbltotalmes.setText("S/ " + String.format("%.2f", d.totalVendido()));
            lblmembresias.setText(d.membresiasActivas());
            lbldeudas.setText("S/ " + String.format("%.2f", d.totalPorCobrar()));
            lblplan.setText(d.planMasVendido());
            lblproducto.setText(d.productoMasVendido());
        } catch (Exception e) { JOptionPane.showMessageDialog(null, e.toString()); }
    }

    // ── HISTORIAL VENTAS ─────────────────────────────────────────────────────
    private void cargarHistorialVentas() {
        String filtroFecha = getFiltroFecha();
        DefaultTableModel modelo = (DefaultTableModel) tablaventas.getModel();
        modelo.setRowCount(0);
        try {
            for (VentaFila fila : ventaService.listarHistorial(idSucursal, filtroFecha)) {
                modelo.addRow(new Object[]{
                    fila.idVenta(),
                    fila.fecha(),
                    fila.cliente(),
                    fila.empleado(),
                    "S/ " + String.format("%.2f", fila.total()),
                    fila.tipo(),
                    fila.estadoPago()
                });
            }
        } catch (Exception e) { JOptionPane.showMessageDialog(null, e.toString()); }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblsucursal = new javax.swing.JLabel();
        lbltotalmes = new javax.swing.JLabel();
        lblmembresias = new javax.swing.JLabel();
        lbldeudas = new javax.swing.JLabel();
        lblplan = new javax.swing.JLabel();
        lblproducto = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaventas = new javax.swing.JTable();
        btnfactura = new javax.swing.JButton();
        btnactualizar = new javax.swing.JButton();
        btnvolver = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        cmbperiodo = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        cmbanio = new javax.swing.JComboBox<>();
        cmbmes = new javax.swing.JComboBox<>();
        btnbuscarfecha = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblsucursal.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblsucursal.setForeground(new java.awt.Color(255, 255, 255));
        lblsucursal.setText("                 ");
        getContentPane().add(lblsucursal, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 40, 250, -1));

        lbltotalmes.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lbltotalmes.setForeground(new java.awt.Color(255, 255, 255));
        lbltotalmes.setText("venta");
        getContentPane().add(lbltotalmes, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 176, 145, -1));

        lblmembresias.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblmembresias.setForeground(new java.awt.Color(255, 255, 255));
        lblmembresias.setText("membresias");
        getContentPane().add(lblmembresias, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 238, 158, -1));

        lbldeudas.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lbldeudas.setForeground(new java.awt.Color(255, 255, 255));
        lbldeudas.setText("0");
        getContentPane().add(lbldeudas, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 294, 153, -1));

        lblplan.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblplan.setForeground(new java.awt.Color(255, 255, 255));
        lblplan.setText("plan ");
        getContentPane().add(lblplan, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 350, 157, -1));

        lblproducto.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblproducto.setForeground(new java.awt.Color(255, 255, 255));
        lblproducto.setText("producto");
        getContentPane().add(lblproducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 406, 157, -1));

        tablaventas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "FECHA", "CLIENTE", "EMPLEADO", "TOTAL", "TIPO", "ESTADO"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tablaventas);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 60, 709, 365));

        btnfactura.setText("FACTURAS");
        btnfactura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnfacturaActionPerformed(evt);
            }
        });
        getContentPane().add(btnfactura, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 440, -1, -1));

        btnactualizar.setText("ACTUALIZAR");
        btnactualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnactualizarActionPerformed(evt);
            }
        });
        getContentPane().add(btnactualizar, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 441, -1, -1));

        btnvolver.setText("VOLVER");
        btnvolver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnvolverActionPerformed(evt);
            }
        });
        getContentPane().add(btnvolver, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 440, -1, -1));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("SUCURSAL           :");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, -1, -1));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("VENTA MEMBRESIAS");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 210, 145, -1));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("POR COBRAR");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 266, 145, -1));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("PLAN MÁS VENDIDO ");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 322, 202, -1));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("PRODUCTO MÁS VENDIDO");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 378, -1, -1));

        cmbperiodo.setBackground(new java.awt.Color(51, 51, 51));
        cmbperiodo.setForeground(new java.awt.Color(255, 255, 255));
        cmbperiodo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "DIARIO", "SEMANAL", "ESTE MES", "MENSUAL" }));
        cmbperiodo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbperiodoActionPerformed(evt);
            }
        });
        getContentPane().add(cmbperiodo, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 73, 203, -1));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("VENTA");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 148, 158, -1));

        cmbanio.setBackground(new java.awt.Color(51, 51, 51));
        cmbanio.setForeground(new java.awt.Color(255, 255, 255));
        cmbanio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbanioActionPerformed(evt);
            }
        });
        getContentPane().add(cmbanio, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, 100, -1));

        cmbmes.setBackground(new java.awt.Color(51, 51, 51));
        cmbmes.setForeground(new java.awt.Color(255, 255, 255));
        getContentPane().add(cmbmes, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 110, 100, -1));

        btnbuscarfecha.setText("FILTRAR MES");
        btnbuscarfecha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnbuscarfechaActionPerformed(evt);
            }
        });
        getContentPane().add(btnbuscarfecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 110, -1, 22));

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/fondo.png"))); // NOI18N
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(-10, 0, 1020, -1));

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/fondo.png"))); // NOI18N
        jLabel8.setToolTipText("");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(1010, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnfacturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnfacturaActionPerformed
        Diseño.NavegacionUtil.navegar(btnfactura, () -> {
            new FrmFactura(this, idSucursal).setVisible(true);
            this.setVisible(false);
        });
    }//GEN-LAST:event_btnfacturaActionPerformed

    private void btnactualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnactualizarActionPerformed
        cargarDashboard();
        cargarHistorialVentas();
    }//GEN-LAST:event_btnactualizarActionPerformed

    private void btnvolverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnvolverActionPerformed
        this.setVisible(false);
        frmSucursal.setVisible(true);
    }//GEN-LAST:event_btnvolverActionPerformed

    private void cmbperiodoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbperiodoActionPerformed
        if (cmbperiodo.getSelectedIndex() == 3) mostrarCombosAnioMes();
        else {
            ocultarCombosAnioMes();
            cargarDashboard();
            cargarHistorialVentas();
        }
    }//GEN-LAST:event_cmbperiodoActionPerformed

    private void btnbuscarfechaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnbuscarfechaActionPerformed
            cargarDashboard();
            cargarHistorialVentas();
    }//GEN-LAST:event_btnbuscarfechaActionPerformed

    private void cmbanioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbanioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbanioActionPerformed

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
            java.util.logging.Logger.getLogger(FrmVenta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmVenta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmVenta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmVenta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmVenta().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnactualizar;
    private javax.swing.JButton btnbuscarfecha;
    private javax.swing.JButton btnfactura;
    private javax.swing.JButton btnvolver;
    private javax.swing.JComboBox<String> cmbanio;
    private javax.swing.JComboBox<String> cmbmes;
    private javax.swing.JComboBox<String> cmbperiodo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbldeudas;
    private javax.swing.JLabel lblmembresias;
    private javax.swing.JLabel lblplan;
    private javax.swing.JLabel lblproducto;
    private javax.swing.JLabel lblsucursal;
    private javax.swing.JLabel lbltotalmes;
    private javax.swing.JTable tablaventas;
    // End of variables declaration//GEN-END:variables
}
