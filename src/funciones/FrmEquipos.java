/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package funciones;


import Diseño.diseño;
import fabrica.AppFactory;
import servicio.EquipoService;
import servicio.Resultado;
import repositorio.EquipoRepository.EquipoFila;
import java.awt.Color;
import static java.awt.image.ImageObserver.ALLBITS;
import java.time.LocalDate;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class FrmEquipos extends javax.swing.JFrame {

    private final EquipoService equipoService = AppFactory.getInstance().equipoService();
    private FrmInventario frmInventario;
    private int idSucursal;
    private int idInventario;
    private int idEquipoSeleccionado = 0;

    public FrmEquipos(FrmInventario frmInventario, int idSucursal) {
        initComponents();
        setResizable(false);
        this.frmInventario = frmInventario;
        this.idSucursal    = idSucursal;
        txtidequipo.setVisible(false);
        setSize(1200, 400);
        setResizable(false);
        setLocationRelativeTo(null);
        diseño.estilizarBotones(btnregistrar, btnmodificar, btnlimpiar, btneleminar, btnvolver);
        obtenerIdInventario();
        cargarEstados();
        cargarCombosAnioMes();
        btnfechaActionPerformed(null);
        diseño.estilizarTabla(tablaequipo, jScrollPane1, ALLBITS);
        cargartabla();
        tablaequipo.getColumnModel().getColumn(0).setMinWidth(0);
        tablaequipo.getColumnModel().getColumn(0).setMaxWidth(0);
        tablaequipo.getColumnModel().getColumn(0).setWidth(0);
    }

    public FrmEquipos() { initComponents(); }

    private void obtenerIdInventario() {
        try {
            idInventario = equipoService.obtenerOCrearInventario(idSucursal);
        } catch (Exception e) { JOptionPane.showMessageDialog(null, e.toString()); }
    }

    private void cargarEstados() {
        cmbestado.removeAllItems();
        cmbestado.addItem("OPERATIVO");
        cmbestado.addItem("MANTENIMIENTO");
        cmbestado.addItem("DAÑADO");
    }

    // ── COMBOS FECHA ─────────────────────────────────────────────────────────
    private void cargarCombosAnioMes() {
        int anioActual = LocalDate.now().getYear();
        for (int i = anioActual - 10; i <= anioActual; i++) cmbanio.addItem(String.valueOf(i));
        String[] meses = {"Enero","Febrero","Marzo","Abril","Mayo","Junio",
                          "Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre"};
        for (String m : meses) cmbmes.addItem(m);
        cmbanio.addActionListener(e -> cargarDias());
        cmbmes.addActionListener(e -> cargarDias());
        cargarDias();
    }

    private void cargarDias() {
        if (cmbanio.getSelectedItem() == null || cmbmes.getSelectedItem() == null) return;
        int anio = Integer.parseInt(cmbanio.getSelectedItem().toString());
        int mes  = cmbmes.getSelectedIndex() + 1;
        int diasEnMes = LocalDate.of(anio, mes, 1).lengthOfMonth();
        int diaActual = cmbdia.getSelectedItem() != null ?
                        Integer.parseInt(cmbdia.getSelectedItem().toString()) : 1;
        cmbdia.removeAllItems();
        for (int i = 1; i <= diasEnMes; i++) cmbdia.addItem(String.valueOf(i));
        if (diaActual <= diasEnMes) cmbdia.setSelectedItem(String.valueOf(diaActual));
    }

    private String getFecha() {
        int anio = Integer.parseInt(cmbanio.getSelectedItem().toString());
        int mes  = cmbmes.getSelectedIndex() + 1;
        int dia  = Integer.parseInt(cmbdia.getSelectedItem().toString());
        return String.format("%04d-%02d-%02d", anio, mes, dia);
    }

    private void btnfechaActionPerformed(java.awt.event.ActionEvent evt) {
        LocalDate hoy = LocalDate.now();
        cmbanio.setSelectedItem(String.valueOf(hoy.getYear()));
        cmbmes.setSelectedIndex(hoy.getMonthValue() - 1);
        cargarDias();
        cmbdia.setSelectedItem(String.valueOf(hoy.getDayOfMonth()));
    }

    private void cargartabla() {
        DefaultTableModel modelo = (DefaultTableModel) tablaequipo.getModel();
        modelo.setRowCount(0);
        try {
            for (EquipoFila fila : equipoService.listarPorSucursal(idSucursal)) {
                modelo.addRow(new Object[]{
                    fila.idEquipo(), fila.nombre(), fila.marca(), fila.stock(), fila.estado(), fila.fechaAdquisicion()
                });
            }
        } catch (Exception e) { JOptionPane.showMessageDialog(null, e.toString()); }
    }

    private void limpiar() {
        txtidequipo.setText(""); txtnombre.setText("");
        txtmarca.setText(""); txtstock.setText("");
        cmbestado.setSelectedIndex(0);
        btnfechaActionPerformed(null);
        tablaequipo.clearSelection();
        idEquipoSeleccionado = 0;
        btnregistrar.setEnabled(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        txtidequipo = new javax.swing.JTextField();
        cmbanio = new javax.swing.JComboBox<>();
        cmbmes = new javax.swing.JComboBox<>();
        cmbdia = new javax.swing.JComboBox<>();
        cmbestado = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaequipo = new javax.swing.JTable();
        btnregistrar = new javax.swing.JButton();
        txtnombre = new javax.swing.JTextField();
        txtmarca = new javax.swing.JTextField();
        txtstock = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        btnmodificar = new javax.swing.JButton();
        btneleminar = new javax.swing.JButton();
        btnlimpiar = new javax.swing.JButton();
        btnvolver = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();

        jButton1.setText("jButton1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        getContentPane().add(txtidequipo, new org.netbeans.lib.awtextra.AbsoluteConstraints(337, 304, 71, -1));

        cmbanio.setBackground(new java.awt.Color(51, 51, 51));
        cmbanio.setForeground(new java.awt.Color(255, 255, 255));
        getContentPane().add(cmbanio, new org.netbeans.lib.awtextra.AbsoluteConstraints(152, 191, -1, -1));

        cmbmes.setBackground(new java.awt.Color(51, 51, 51));
        cmbmes.setForeground(new java.awt.Color(255, 255, 255));
        getContentPane().add(cmbmes, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 190, -1, -1));

        cmbdia.setBackground(new java.awt.Color(51, 51, 51));
        cmbdia.setForeground(new java.awt.Color(255, 255, 255));
        getContentPane().add(cmbdia, new org.netbeans.lib.awtextra.AbsoluteConstraints(338, 191, -1, -1));

        cmbestado.setBackground(new java.awt.Color(51, 51, 51));
        cmbestado.setForeground(new java.awt.Color(255, 255, 255));
        getContentPane().add(cmbestado, new org.netbeans.lib.awtextra.AbsoluteConstraints(152, 231, 262, -1));

        tablaequipo.setBackground(new java.awt.Color(102, 102, 102));
        tablaequipo.setBorder(new javax.swing.border.MatteBorder(null));
        tablaequipo.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        tablaequipo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID", "NOMBRE", "MARCA", "CANTIDAD", "ESTADO", "FECHA ADQUISICIÓN"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaequipo.setGridColor(new java.awt.Color(51, 51, 51));
        tablaequipo.setSelectionBackground(new java.awt.Color(153, 153, 153));
        tablaequipo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaequipoMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablaequipo);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 20, 771, 270));

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
        getContentPane().add(btnregistrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 270, 83, -1));

        txtnombre.setBackground(new java.awt.Color(51, 51, 51));
        txtnombre.setForeground(new java.awt.Color(255, 255, 255));
        getContentPane().add(txtnombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(152, 67, 262, -1));

        txtmarca.setBackground(new java.awt.Color(51, 51, 51));
        txtmarca.setForeground(new java.awt.Color(255, 255, 255));
        getContentPane().add(txtmarca, new org.netbeans.lib.awtextra.AbsoluteConstraints(152, 111, 262, -1));

        txtstock.setBackground(new java.awt.Color(51, 51, 51));
        txtstock.setForeground(new java.awt.Color(255, 255, 255));
        getContentPane().add(txtstock, new org.netbeans.lib.awtextra.AbsoluteConstraints(152, 151, 262, -1));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("NOMBRE");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 70, 140, -1));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("MARCA");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 114, 140, -1));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("STOCK");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 154, 140, -1));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("FECHA DE ADQUISICIÓN");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 194, -1, -1));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("ESTADO");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 234, 140, -1));

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
        getContentPane().add(btnmodificar, new org.netbeans.lib.awtextra.AbsoluteConstraints(107, 270, -1, -1));

        btneleminar.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        btneleminar.setForeground(new java.awt.Color(255, 255, 255));
        btneleminar.setText("ELIMINAR");
        btneleminar.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(204, 0, 0)));
        btneleminar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btneleminarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btneleminarMouseExited(evt);
            }
        });
        btneleminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btneleminarActionPerformed(evt);
            }
        });
        getContentPane().add(btneleminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(219, 270, 83, -1));

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
        getContentPane().add(btnlimpiar, new org.netbeans.lib.awtextra.AbsoluteConstraints(331, 270, 83, -1));

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
        getContentPane().add(btnvolver, new org.netbeans.lib.awtextra.AbsoluteConstraints(107, 304, 195, -1));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/LeonGymLogo.png"))); // NOI18N
        jLabel6.setText("EQUIPOS");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 17, 404, 32));

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/fondo.png"))); // NOI18N
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/fondo.png"))); // NOI18N
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(1020, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tablaequipoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaequipoMouseClicked
        int fila = tablaequipo.getSelectedRow();
        if (fila >= 0) {
            DefaultTableModel modelo = (DefaultTableModel) tablaequipo.getModel();
            idEquipoSeleccionado = Integer.parseInt(modelo.getValueAt(fila, 0).toString());
            txtnombre.setText(modelo.getValueAt(fila, 1).toString());
            txtmarca.setText(modelo.getValueAt(fila, 2).toString());
            txtstock.setText(modelo.getValueAt(fila, 3).toString());
            cmbestado.setSelectedItem(modelo.getValueAt(fila, 4).toString());

            String fecha = modelo.getValueAt(fila, 5).toString();
            LocalDate ld = LocalDate.parse(fecha);
            cmbanio.setSelectedItem(String.valueOf(ld.getYear()));
            cmbmes.setSelectedIndex(ld.getMonthValue() - 1);
            cargarDias();
            cmbdia.setSelectedItem(String.valueOf(ld.getDayOfMonth()));

            btnregistrar.setEnabled(false);
        }
    }//GEN-LAST:event_tablaequipoMouseClicked

    private void btnvolverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnvolverActionPerformed
        this.setVisible(false);
        frmInventario.setVisible(true);
    }//GEN-LAST:event_btnvolverActionPerformed

    private void btnlimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnlimpiarActionPerformed
        limpiar();
    }//GEN-LAST:event_btnlimpiarActionPerformed

    private void btneleminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btneleminarActionPerformed
        if (idEquipoSeleccionado == 0) {
            JOptionPane.showMessageDialog(null, "Seleccione un equipo."); return;
        }
        int conf = JOptionPane.showConfirmDialog(null, "¿Eliminar equipo?",
            "Confirmar", JOptionPane.YES_NO_OPTION);
        if (conf == JOptionPane.YES_OPTION) {
            try {
                Resultado resultado = equipoService.eliminar(idEquipoSeleccionado);
                JOptionPane.showMessageDialog(null, resultado.mensaje());
                if (resultado.exito()) { limpiar(); cargartabla(); }
            } catch (Exception e) { JOptionPane.showMessageDialog(null, e.toString()); }
        }
    }//GEN-LAST:event_btneleminarActionPerformed

    private void btnmodificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnmodificarActionPerformed
        if (idEquipoSeleccionado == 0) {
            JOptionPane.showMessageDialog(null, "Seleccione un equipo."); return;
        }
        try {
            int stock = Integer.parseInt(txtstock.getText());
            if (stock < 0) { JOptionPane.showMessageDialog(null, "El stock no puede ser negativo."); return; }
            Resultado resultado = equipoService.actualizar(idEquipoSeleccionado, txtnombre.getText(),
                stock, txtmarca.getText(), cmbestado.getSelectedItem().toString());
            JOptionPane.showMessageDialog(null, resultado.mensaje());
            if (resultado.exito()) { limpiar(); cargartabla(); }
        } catch (Exception e) { JOptionPane.showMessageDialog(null, e.toString()); }
    }//GEN-LAST:event_btnmodificarActionPerformed

    private void btnregistrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnregistrarActionPerformed
        if (txtnombre.getText().isEmpty() || txtmarca.getText().isEmpty() ||
            txtstock.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Complete todos los campos."); return;
        }
        if (idInventario <= 0) {
            JOptionPane.showMessageDialog(null, "No hay inventario para esta sucursal."); return;
        }
        try {
            int stock = Integer.parseInt(txtstock.getText());
            if (stock < 0) { JOptionPane.showMessageDialog(null, "El stock no puede ser negativo."); return; }
            Resultado resultado = equipoService.registrar(idInventario, txtnombre.getText(),
                stock, txtmarca.getText(),
                cmbestado.getSelectedItem().toString(), getFecha());
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

    private void btnmodificarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnmodificarMouseEntered
        btnmodificar.setForeground(new Color(204,0,0));
    }//GEN-LAST:event_btnmodificarMouseEntered

    private void btnmodificarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnmodificarMouseExited
        btnmodificar.setForeground(new Color(255,255,255));
    }//GEN-LAST:event_btnmodificarMouseExited

    private void btneleminarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btneleminarMouseEntered
        btneleminar.setForeground(new Color(204,0,0));
    }//GEN-LAST:event_btneleminarMouseEntered

    private void btneleminarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btneleminarMouseExited
        btneleminar.setForeground(new Color(255,255,255));
    }//GEN-LAST:event_btneleminarMouseExited

    private void btnlimpiarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnlimpiarMouseEntered
        btnlimpiar.setForeground(new Color(204,0,0));
    }//GEN-LAST:event_btnlimpiarMouseEntered

    private void btnlimpiarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnlimpiarMouseExited
        btnlimpiar.setForeground(new Color(255,255,255));
    }//GEN-LAST:event_btnlimpiarMouseExited

    private void btnvolverMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnvolverMouseEntered
        btnvolver.setForeground(new Color(204,0,0));
    }//GEN-LAST:event_btnvolverMouseEntered

    private void btnvolverMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnvolverMouseExited
        btnvolver.setForeground(new Color(255,255,255));
    }//GEN-LAST:event_btnvolverMouseExited

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
            java.util.logging.Logger.getLogger(FrmEquipos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmEquipos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmEquipos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmEquipos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmEquipos().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btneleminar;
    private javax.swing.JButton btnlimpiar;
    private javax.swing.JButton btnmodificar;
    private javax.swing.JButton btnregistrar;
    private javax.swing.JButton btnvolver;
    private javax.swing.JComboBox<String> cmbanio;
    private javax.swing.JComboBox<String> cmbdia;
    private javax.swing.JComboBox<String> cmbestado;
    private javax.swing.JComboBox<String> cmbmes;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tablaequipo;
    private javax.swing.JTextField txtidequipo;
    private javax.swing.JTextField txtmarca;
    private javax.swing.JTextField txtnombre;
    private javax.swing.JTextField txtstock;
    // End of variables declaration//GEN-END:variables
}
