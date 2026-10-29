/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package funciones;

import Diseño.diseño;
import fabrica.AppFactory;
import servicio.PagoClienteService;
import repositorio.PagoClienteRepository.CompraFila;
import repositorio.PagoClienteRepository.DeudaFila;
import repositorio.PagoClienteRepository.MembresiaActiva;
import repositorio.PagoClienteRepository.PagoFila;
import java.util.Optional;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author luisc
 */
public class FrmPagoClientes extends javax.swing.JFrame {

    private final PagoClienteService pagoClienteService = AppFactory.getInstance().pagoClienteService();
    private FrmMembresiaCliente frmMembresiaCliente;
    private int idCliente;
    private int idEmpleado;
    private int idSucursal;
    private String nombreCliente;
    private boolean modoEmpleado;

    public FrmPagoClientes(int idCliente, String nombreCliente, FrmMembresiaCliente frmMembresiaCliente) {
        initComponents();
        setResizable(false);
        setSize(810, 490);
        setLocationRelativeTo(null);
        this.idCliente    = idCliente;
        this.nombreCliente = nombreCliente;
        this.modoEmpleado  = false;
        this.frmMembresiaCliente = frmMembresiaCliente;
        setTitle("Mis Pagos - " + nombreCliente);
        lblcliente.setText(nombreCliente);
        diseño.estilizarBoton(btnvolver);
        diseño.estilizarTabla(tablacompras, jScrollPane1, ALLBITS);
        diseño.estilizarTabla(tablapagos, jScrollPane1, ALLBITS);
        diseño.estilizarTabla(tabladeudas, jScrollPane1, ALLBITS);
        
        cargarMembresia();

        cargarDeudas();
        cargarHistorialPagos();
        cargarHistorialCompras();             
        tabladeudas.getColumnModel().getColumn(0).setMinWidth(0);
        tabladeudas.getColumnModel().getColumn(0).setMaxWidth(0);
        tabladeudas.getColumnModel().getColumn(0).setWidth(0);
    }
    public FrmPagoClientes() {
        initComponents();
    }
        private void cargarMembresia() {
        try {
            Optional<MembresiaActiva> membresia = pagoClienteService.obtenerMembresiaActiva(idCliente);
            if (membresia.isPresent()) {
                MembresiaActiva m = membresia.get();
                lblplan.setText(m.nombrePlan());
                lblfechainicio.setText(m.fechaInicio());
                lblfechafin.setText(m.fechaFin());
                lblestado.setText(m.estado());
                lblclases.setText(m.clases() != null ? m.clases() : "Sin clases");
            } else {
                lblplan.setText("Sin membresía activa");
                lblfechainicio.setText("-"); lblfechafin.setText("-");
                lblestado.setText("-"); lblclases.setText("-");
            }
        } catch (Exception e) { JOptionPane.showMessageDialog(null, e.toString()); }
    }

    
    private void cargarDeudas() {
        DefaultTableModel modelo = (DefaultTableModel) tabladeudas.getModel();
        modelo.setRowCount(0);
        double totalDeuda = 0.0;
        try {
            for (DeudaFila fila : pagoClienteService.listarDeudas(idCliente)) {
                totalDeuda += fila.monto();
                modelo.addRow(new Object[]{
                    fila.idPago(),
                    fila.fecha(),
                    fila.descripcion(),
                    "S/ " + String.format("%.2f", fila.monto()),
                    fila.metodo(),
                    fila.estado()
                });
            }
        } catch (Exception e) { JOptionPane.showMessageDialog(null, e.toString()); }
        lbltotaldeuda.setText("Total deuda: S/ " + String.format("%.2f", totalDeuda));
    }
    
    private void cargarHistorialPagos() {
        DefaultTableModel modelo = (DefaultTableModel) tablapagos.getModel();
        modelo.setRowCount(0);
        try {
            for (PagoFila fila : pagoClienteService.listarHistorialPagos(idCliente)) {
                modelo.addRow(new Object[]{
                    fila.fecha(),
                    fila.descripcion(),
                    "S/ " + String.format("%.2f", fila.monto()),
                    fila.metodo(),
                    fila.estado()
                });
            }
        } catch (Exception e) { JOptionPane.showMessageDialog(null, e.toString()); }
    }

    private void cargarHistorialCompras() {
        DefaultTableModel modelo = (DefaultTableModel) tablacompras.getModel();
        modelo.setRowCount(0);
        try {
            for (CompraFila fila : pagoClienteService.listarHistorialCompras(idCliente)) {
                modelo.addRow(new Object[]{
                    fila.fecha(),
                    fila.producto(),
                    fila.cantidad(),
                    "S/ " + String.format("%.2f", fila.precioUnitario()),
                    "S/ " + String.format("%.2f", fila.subtotal())
                });
            }
        } catch (Exception e) { JOptionPane.showMessageDialog(null, e.toString()); }
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tabladeudas = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablapagos = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        tablacompras = new javax.swing.JTable();
        lblcliente = new javax.swing.JLabel();
        lblplan = new javax.swing.JLabel();
        lbltotaldeuda = new javax.swing.JLabel();
        btnvolver = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lblfechafin = new javax.swing.JLabel();
        lblfechainicio = new javax.swing.JLabel();
        lblestado = new javax.swing.JLabel();
        lblclases = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tabladeudas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID", "FECHA Y HORA", "DESCRIPCIÓN", "MONTO", "ESTADO"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tabladeudas);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(317, 89, 460, 85));

        tablapagos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "FECHA Y HORA", "DESCRIPCIÓN", "METODO", "MONTO"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tablapagos);

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(317, 226, 460, 85));

        tablacompras.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "FECHA Y HORA", "PRODUCTO", "CANTIDAD", "PRECIO", "SUBTOTAL"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(tablacompras);

        getContentPane().add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(317, 351, 460, 85));

        lblcliente.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblcliente.setForeground(new java.awt.Color(255, 255, 255));
        lblcliente.setText("cliente");
        getContentPane().add(lblcliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(164, 110, 128, -1));

        lblplan.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblplan.setForeground(new java.awt.Color(255, 255, 255));
        lblplan.setText("jLabel1");
        getContentPane().add(lblplan, new org.netbeans.lib.awtextra.AbsoluteConstraints(164, 144, 128, -1));

        lbltotaldeuda.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lbltotaldeuda.setForeground(new java.awt.Color(255, 255, 255));
        lbltotaldeuda.setText("deuda");
        getContentPane().add(lbltotaldeuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(164, 331, 127, -1));

        btnvolver.setText("VOLVER");
        btnvolver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnvolverActionPerformed(evt);
            }
        });
        getContentPane().add(btnvolver, new org.netbeans.lib.awtextra.AbsoluteConstraints(104, 375, -1, -1));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("INICIO");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(28, 226, 83, -1));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("FIN");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(28, 263, 83, -1));

        lblfechafin.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblfechafin.setForeground(new java.awt.Color(255, 255, 255));
        lblfechafin.setText("jLabel3");
        getContentPane().add(lblfechafin, new org.netbeans.lib.awtextra.AbsoluteConstraints(164, 263, 127, -1));

        lblfechainicio.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblfechainicio.setForeground(new java.awt.Color(255, 255, 255));
        lblfechainicio.setText("jLabel3");
        getContentPane().add(lblfechainicio, new org.netbeans.lib.awtextra.AbsoluteConstraints(164, 226, 127, -1));

        lblestado.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblestado.setForeground(new java.awt.Color(255, 255, 255));
        lblestado.setText("jLabel3");
        getContentPane().add(lblestado, new org.netbeans.lib.awtextra.AbsoluteConstraints(164, 178, 128, -1));

        lblclases.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblclases.setForeground(new java.awt.Color(255, 255, 255));
        lblclases.setText("jLabel3");
        getContentPane().add(lblclases, new org.netbeans.lib.awtextra.AbsoluteConstraints(164, 297, 127, -1));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("ESTADO");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(28, 178, 103, -1));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("CLASES");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(28, 297, 83, -1));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("PLAN");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(28, 144, 103, -1));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("DEUDA TOTAL");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(28, 331, -1, -1));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("CLIENTE");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(28, 110, 103, -1));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/LeonGymLogo.png"))); // NOI18N
        jLabel8.setText("ESTADO DE LA CUENTA");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(28, 35, 283, -1));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("DEUDAS");
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(317, 67, 460, -1));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("PAGOS");
        getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(317, 192, 460, -1));

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("COMPRAS");
        getContentPane().add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(317, 323, 460, -1));

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/fondo.png"))); // NOI18N
        getContentPane().add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnvolverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnvolverActionPerformed
        this.setVisible(false);
        frmMembresiaCliente.setVisible(true);
    }//GEN-LAST:event_btnvolverActionPerformed

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
            java.util.logging.Logger.getLogger(FrmPagoClientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmPagoClientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmPagoClientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmPagoClientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmPagoClientes().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnvolver;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblclases;
    private javax.swing.JLabel lblcliente;
    private javax.swing.JLabel lblestado;
    private javax.swing.JLabel lblfechafin;
    private javax.swing.JLabel lblfechainicio;
    private javax.swing.JLabel lblplan;
    private javax.swing.JLabel lbltotaldeuda;
    private javax.swing.JTable tablacompras;
    private javax.swing.JTable tabladeudas;
    private javax.swing.JTable tablapagos;
    // End of variables declaration//GEN-END:variables
}
