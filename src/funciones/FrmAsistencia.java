package funciones;

import Diseño.diseño;
import PRINCIPAL.FrmInicio;
import fabrica.AppFactory;
import servicio.AsistenciaService;
import servicio.Resultado;
import repositorio.AsistenciaRepository.AsistenciaAdminFila;
import repositorio.AsistenciaRepository.AsistenciaEmpleadoFila;
import java.awt.Color;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class FrmAsistencia extends javax.swing.JFrame {

    private final AsistenciaService asistenciaService = AppFactory.getInstance().asistenciaService();
    private final servicio.ReporteService reporteService = AppFactory.getInstance().reporteService();
    // Modo empleado
    private int idEmpleado;
    private String nombreCompleto;
    private String cargo;

    // Modo administrador
    private FrmEmpleados frmEmpleados;
    private int idSucursal;
    private boolean modoAdmin = false;

    private DateTimeFormatter fmtFecha = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private DateTimeFormatter fmtHora  = DateTimeFormatter.ofPattern("HH:mm:ss");

    // ── CONSTRUCTOR MODO EMPLEADO (profesor/entrenador) ──────────────────────
    public FrmAsistencia(int idEmpleado, String nombreCompleto, String cargo) {
        this.idEmpleado     = idEmpleado;
        this.nombreCompleto = nombreCompleto;
        this.cargo          = cargo;
        this.modoAdmin      = false;
        initComponents();
        setSize(630, 445);
        setResizable(false);
        setLocationRelativeTo(null);
        diseño.estilizarTabla(tblAsistencia, jScrollPane1, 10);
        diseño.estilizarBotones(btnEntrada, btnSalida, btnActualizar, btmcerrarsesion, btnvolver, btnreporte);       
        setTitle("Asistencia — " + cargo);
        lblBienvenida.setText("Bienvenido/a, " + nombreCompleto + "  [" + cargo + "]");
        lblbuscar.setVisible(false);
        txtbuscar.setVisible(false);
        btnvolver.setVisible(false);
        btnEntrada.setVisible(true);
        btnSalida.setVisible(true);
        btnreporte.setContentAreaFilled(false);
        cargarTablaEmpleado();
    }

    // ── CONSTRUCTOR MODO ADMINISTRADOR ───────────────────────────────────────────────
    public FrmAsistencia(FrmEmpleados frmEmpleados, int idSucursal) {
        this.frmEmpleados = frmEmpleados;
        this.idSucursal   = idSucursal;
        this.modoAdmin    = true;
        initComponents();
        setSize(630, 445);
        setResizable(false);
        setLocationRelativeTo(null);
        btnEntrada.setContentAreaFilled(false);
        btnSalida.setContentAreaFilled(false);
        btnActualizar.setContentAreaFilled(false);
        btmcerrarsesion.setContentAreaFilled(false);
        btnvolver.setContentAreaFilled(false);
        btnreporte.setContentAreaFilled(false);
        setTitle("Asistencias - Sucursal");
        lblBienvenida.setText("Registro de asistencias de la sucursal");
        btnEntrada.setVisible(false);
        btnSalida.setVisible(false);
        btnvolver.setVisible(true);
        btnreporte.setVisible(false);
        diseño.estilizarTabla(tblAsistencia, jScrollPane1, ALLBITS);
        cargarTablaAdmin();
    }

    public FrmAsistencia() { initComponents(); }
    
    //--------------------------------------------------------------------------
    // ── CARGAR TABLA MODO EMPLEADO --------------------------------------------
    //--------------------------------------------------------------------------
    private void cargarTablaEmpleado() {
        DefaultTableModel modelo = (DefaultTableModel) tblAsistencia.getModel();
        modelo.setColumnIdentifiers(new String[]{"Fecha", "Hora Entrada", "Hora Salida"});
        modelo.setRowCount(0);
        try {
            for (AsistenciaEmpleadoFila fila : asistenciaService.listarPorEmpleado(idEmpleado)) {
                modelo.addRow(new Object[]{
                    fila.fecha(),
                    fila.horaEntrada(),
                    fila.horaSalida() != null ? fila.horaSalida() : "—"
                });
            }
        } catch (Exception e) { JOptionPane.showMessageDialog(null, "Error: " + e); }
    }
    
    //--------------------------------------------------------------------------
    // ── CARGAR TABLA MODO ADMIN ----------------------------------------------
    //--------------------------------------------------------------------------
    private void cargarTablaAdmin() {
        DefaultTableModel modelo = (DefaultTableModel) tblAsistencia.getModel();
        modelo.setColumnIdentifiers(new String[]{"Empleado", "Fecha", "Hora Entrada", "Hora Salida"});
        modelo.setRowCount(0);
        try {
            for (AsistenciaAdminFila fila : asistenciaService.listarPorSucursal(idSucursal)) {
                modelo.addRow(new Object[]{
                    fila.empleado(),
                    fila.fecha(),
                    fila.horaEntrada(),
                    fila.horaSalida() != null ? fila.horaSalida() : "—"
                });
            }
        } catch (Exception e) { JOptionPane.showMessageDialog(null, "Error: " + e); }
    }
    private void buscarEmpleado() {
        try {
            String buscar = txtbuscar.getText().trim();
            DefaultTableModel modelo = (DefaultTableModel) tblAsistencia.getModel();
            modelo.setRowCount(0);

            for (AsistenciaAdminFila fila : asistenciaService.buscarPorSucursalYDni(idSucursal, buscar)) {
                modelo.addRow(new Object[]{
                    fila.empleado(),
                    fila.fecha(),
                    fila.horaEntrada(),
                    fila.horaSalida() != null ? fila.horaSalida() : "—"
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.toString());
        }
    }
    // ── REGISTRAR ENTRADA ────────────────────────────────────────────────────
    private void registrarEntrada() {
        String fecha = LocalDate.now().format(fmtFecha);
        String hora  = LocalTime.now().format(fmtHora);
        Resultado resultado = asistenciaService.registrarEntrada(idEmpleado, fecha, hora);
        JOptionPane.showMessageDialog(null, resultado.mensaje());
        if (resultado.exito()) cargarTablaEmpleado();
    }

    // ── REGISTRAR SALIDA ─────────────────────────────────────────────────────
    private void registrarSalida() {
        String fecha = LocalDate.now().format(fmtFecha);
        String hora  = LocalTime.now().format(fmtHora);
        Resultado resultado = asistenciaService.registrarSalida(idEmpleado, fecha, hora);
        JOptionPane.showMessageDialog(null, resultado.mensaje());
        if (resultado.exito()) cargarTablaEmpleado();
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        btnActualizar = new javax.swing.JButton();
        btnEntrada = new javax.swing.JButton();
        btnSalida = new javax.swing.JButton();
        lblBienvenida = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblAsistencia = new javax.swing.JTable();
        btnvolver = new javax.swing.JButton();
        btmcerrarsesion = new javax.swing.JButton();
        txtbuscar = new javax.swing.JTextField();
        lblbuscar = new javax.swing.JLabel();
        btnreporte = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

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
        jScrollPane2.setViewportView(jTable1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnActualizar.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        btnActualizar.setForeground(new java.awt.Color(255, 255, 255));
        btnActualizar.setText("ACTUALIZAR");
        btnActualizar.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(204, 0, 0)));
        btnActualizar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnActualizarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnActualizarMouseExited(evt);
            }
        });
        btnActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarActionPerformed(evt);
            }
        });
        getContentPane().add(btnActualizar, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 300, 180, -1));

        btnEntrada.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        btnEntrada.setForeground(new java.awt.Color(255, 255, 255));
        btnEntrada.setText("ENTRADA");
        btnEntrada.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(204, 0, 0)));
        btnEntrada.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnEntradaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnEntradaMouseExited(evt);
            }
        });
        btnEntrada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEntradaActionPerformed(evt);
            }
        });
        getContentPane().add(btnEntrada, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 300, 180, -1));

        btnSalida.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        btnSalida.setForeground(new java.awt.Color(255, 255, 255));
        btnSalida.setText("SALIDA");
        btnSalida.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(204, 0, 0)));
        btnSalida.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSalidaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSalidaMouseExited(evt);
            }
        });
        btnSalida.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalidaActionPerformed(evt);
            }
        });
        getContentPane().add(btnSalida, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 300, 180, -1));

        lblBienvenida.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblBienvenida.setForeground(new java.awt.Color(255, 255, 255));
        lblBienvenida.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblBienvenida.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/LeonGymLogo.png"))); // NOI18N
        lblBienvenida.setText("BIENVENIDO");
        getContentPane().add(lblBienvenida, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 630, -1));

        tblAsistencia.setBackground(new java.awt.Color(102, 102, 102));
        tblAsistencia.setBorder(new javax.swing.border.MatteBorder(null));
        tblAsistencia.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        tblAsistencia.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblAsistencia.setGridColor(new java.awt.Color(51, 51, 51));
        tblAsistencia.setSelectionBackground(new java.awt.Color(153, 153, 153));
        jScrollPane3.setViewportView(tblAsistencia);

        jScrollPane1.setViewportView(jScrollPane3);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 610, 180));

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
        getContentPane().add(btnvolver, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 340, 180, -1));

        btmcerrarsesion.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        btmcerrarsesion.setForeground(new java.awt.Color(255, 255, 255));
        btmcerrarsesion.setText("CERRAR SESIÓN");
        btmcerrarsesion.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(204, 0, 0)));
        btmcerrarsesion.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btmcerrarsesionMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btmcerrarsesionMouseExited(evt);
            }
        });
        btmcerrarsesion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btmcerrarsesionActionPerformed(evt);
            }
        });
        getContentPane().add(btmcerrarsesion, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 340, 180, -1));

        txtbuscar.setBackground(new java.awt.Color(51, 51, 51));
        txtbuscar.setForeground(new java.awt.Color(255, 255, 255));
        txtbuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtbuscarActionPerformed(evt);
            }
        });
        txtbuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtbuscarKeyReleased(evt);
            }
        });
        getContentPane().add(txtbuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 70, 334, -1));

        lblbuscar.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        lblbuscar.setForeground(new java.awt.Color(255, 255, 255));
        lblbuscar.setText("BUSCAR EMPLEADO");
        getContentPane().add(lblbuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 70, 160, -1));

        btnreporte.setText("ENVIAR REPORTE");
        btnreporte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnreporteActionPerformed(evt);
            }
        });
        getContentPane().add(btnreporte, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 340, 180, -1));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/fondo.png"))); // NOI18N
        jLabel1.setText("jLabel1");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalidaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalidaActionPerformed
        registrarSalida();
    }//GEN-LAST:event_btnSalidaActionPerformed

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
        if(modoAdmin){cargarTablaAdmin();}
        else
        cargarTablaEmpleado();
    }//GEN-LAST:event_btnActualizarActionPerformed

    private void btnvolverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnvolverActionPerformed
        this.setVisible(false);
        frmEmpleados.setVisible(true);
    }//GEN-LAST:event_btnvolverActionPerformed

    private void btnEntradaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEntradaActionPerformed
        registrarEntrada();
    }//GEN-LAST:event_btnEntradaActionPerformed

    private void btmcerrarsesionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btmcerrarsesionActionPerformed
        this.dispose();
        new FrmInicio().setVisible(true);
    }//GEN-LAST:event_btmcerrarsesionActionPerformed

    private void txtbuscarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtbuscarKeyReleased
        buscarEmpleado();
    }//GEN-LAST:event_txtbuscarKeyReleased

    private void btnEntradaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEntradaMouseEntered
        btnEntrada.setForeground(new Color(204,0,0));
    }//GEN-LAST:event_btnEntradaMouseEntered

    private void btnEntradaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEntradaMouseExited
        btnEntrada.setForeground(new Color(255,255,255));
    }//GEN-LAST:event_btnEntradaMouseExited

    private void btmcerrarsesionMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btmcerrarsesionMouseEntered
        btmcerrarsesion.setForeground(new Color(204,0,0));
    }//GEN-LAST:event_btmcerrarsesionMouseEntered

    private void btmcerrarsesionMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btmcerrarsesionMouseExited
        btmcerrarsesion.setForeground(new Color(255,255,255));
    }//GEN-LAST:event_btmcerrarsesionMouseExited

    private void btnSalidaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSalidaMouseEntered
        btnSalida.setForeground(new Color(204,0,0));
    }//GEN-LAST:event_btnSalidaMouseEntered

    private void btnSalidaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSalidaMouseExited
        btnSalida.setForeground(new Color(255,255,255));
    }//GEN-LAST:event_btnSalidaMouseExited

    private void btnActualizarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnActualizarMouseEntered
        btnActualizar.setForeground(new Color(204,0,0));
    }//GEN-LAST:event_btnActualizarMouseEntered

    private void btnActualizarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnActualizarMouseExited
        btnActualizar.setForeground(new Color(255,255,255));
    }//GEN-LAST:event_btnActualizarMouseExited

    private void btnvolverMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnvolverMouseEntered
        btnvolver.setForeground(new Color(204,0,0));
    }//GEN-LAST:event_btnvolverMouseEntered

    private void btnvolverMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnvolverMouseExited
        btnvolver.setForeground(new Color(255,255,255));
    }//GEN-LAST:event_btnvolverMouseExited

    private void btnreporteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnreporteActionPerformed
        Diseño.ReporteDialogUtil.mostrarDialogoReporte(this, reporteService, idEmpleado);
    }//GEN-LAST:event_btnreporteActionPerformed

    private void txtbuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtbuscarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtbuscarActionPerformed

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
            java.util.logging.Logger.getLogger(FrmAsistencia.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmAsistencia.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmAsistencia.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmAsistencia.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmAsistencia().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btmcerrarsesion;
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnEntrada;
    private javax.swing.JButton btnSalida;
    private javax.swing.JButton btnreporte;
    private javax.swing.JButton btnvolver;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblBienvenida;
    private javax.swing.JLabel lblbuscar;
    private javax.swing.JTable tblAsistencia;
    private javax.swing.JTextField txtbuscar;
    // End of variables declaration//GEN-END:variables
}
