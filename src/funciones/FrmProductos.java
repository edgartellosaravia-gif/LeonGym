/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package funciones;

import Diseño.diseño;
import fabrica.AppFactory;
import servicio.CategoriaService;
import servicio.ProveedorService;
import servicio.EquipoService;
import servicio.ProductoService;
import servicio.Resultado;
import repositorio.ProductoRepository.ProductoFila;
import java.util.LinkedHashMap;
import static java.awt.image.ImageObserver.ALLBITS;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class FrmProductos extends javax.swing.JFrame {

    // Catálogos de categoría/proveedor reutilizados de sus propios módulos
    // (misma tabla, mismo servicio) para no duplicar la consulta SQL aquí.
    private final CategoriaService categoriaService = AppFactory.getInstance().categoriaService();
    private final ProveedorService proveedorService = AppFactory.getInstance().proveedorService();
    // La resolución de inventario por sucursal es idéntica a la del módulo
    // Equipos (misma tabla INVENTARIO); se reutiliza en vez de duplicarla.
    private final EquipoService equipoService = AppFactory.getInstance().equipoService();
    private final ProductoService productoService = AppFactory.getInstance().productoService();

    // Caché en memoria de los combos: nombre mostrado -> id. Evita una
    // consulta a la BD por cada selección (antes se volvía a consultar en
    // cada cmbcategoriaActionPerformed/cmbproveedorActionPerformed).
    private LinkedHashMap<String, Integer> categoriasPorNombre = new LinkedHashMap<>();
    private LinkedHashMap<String, Integer> proveedoresPorNombre = new LinkedHashMap<>();

    private FrmInventario frmInventario;
    private int idSucursal;
    private int idProductoSeleccionado  = 0;
    private int idCategoriaSeleccionada = 0;
    private int idProveedorSeleccionado = 0;
    private int idInventario            = 0;

    public FrmProductos(FrmInventario frmInventario, int idSucursal) {
        initComponents();
        setResizable(false);
        setSize(910, 500);
        setLocationRelativeTo(null);
        this.frmInventario = frmInventario;
        this.idSucursal    = idSucursal;
        txtidproducto.setVisible(false);
        cargarCategoria();
        cargarProveedor();
        obtenerIdInventario();
        diseño.estilizarTabla(tablaproducto, jScrollPane1, ALLBITS);
        diseño.estilizarBotones(btncategoria, btnmproducto, btneproducto, btnproveedores, btnlimpiar, btnregistrar, btnvolver);
        cargartabla();
        tablaproducto.getColumnModel().getColumn(0).setMinWidth(0);
        tablaproducto.getColumnModel().getColumn(0).setMaxWidth(0);
        tablaproducto.getColumnModel().getColumn(0).setWidth(0);
    }

    public FrmProductos() { initComponents(); }

    private void obtenerIdInventario() {
        try {
            idInventario = equipoService.obtenerOCrearInventario(idSucursal);
        } catch (Exception e) { JOptionPane.showMessageDialog(null, e.toString()); }
    }
    
    public void recargarCombos() {
    cargarCategoria();
    cargarProveedor();
    }
    private void cargarCategoria() {
        try {
            categoriasPorNombre.clear();
            cmbcategoria.removeAllItems();
            cmbcategoria.addItem("Seleccione categoría");
            for (var fila : categoriaService.listarTodas()) {
                categoriasPorNombre.put(fila.nombre(), fila.idCategoria());
                cmbcategoria.addItem(fila.nombre());
            }
        } catch (Exception e) { JOptionPane.showMessageDialog(null, e.toString()); }
    }

    private void cargarProveedor() {
        try {
            proveedoresPorNombre.clear();
            cmbproveedor.removeAllItems();
            cmbproveedor.addItem("Seleccione proveedor");
            for (var fila : proveedorService.listarTodos()) {
                proveedoresPorNombre.put(fila.nombre(), fila.idProveedor());
                cmbproveedor.addItem(fila.nombre());
            }
        } catch (Exception e) { JOptionPane.showMessageDialog(null, e.toString()); }
    }

    private void cargartabla() {
        DefaultTableModel modelo = (DefaultTableModel) tablaproducto.getModel();
        modelo.setRowCount(0);
        try {
            for (ProductoFila fila : productoService.listarPorSucursal(idSucursal)) {
                modelo.addRow(new Object[]{
                    fila.idProducto(), fila.nombre(), fila.precio(), fila.stock(),
                    fila.categoria(), fila.proveedor(), fila.marca()
                });
            }
        } catch (Exception e) { JOptionPane.showMessageDialog(null, e.toString()); }
    }

    private void buscarProducto() {
        String buscar = txtbuscar.getText().trim();
        DefaultTableModel modelo = (DefaultTableModel) tablaproducto.getModel();
        modelo.setRowCount(0);
        try {
            for (ProductoFila fila : productoService.buscarPorSucursalYNombre(idSucursal, buscar)) {
                modelo.addRow(new Object[]{
                    fila.idProducto(), fila.nombre(), fila.precio(), fila.stock(),
                    fila.categoria(), fila.proveedor(), fila.marca()
                });
            }
        } catch (Exception e) { JOptionPane.showMessageDialog(null, e.toString()); }
    }

    private void limpiar() {
        txtidproducto.setText(""); txtnombre.setText("");
        txtprecio.setText(""); txtstock.setText(""); txtmarca.setText("");
        cmbcategoria.setSelectedIndex(0); cmbproveedor.setSelectedIndex(0);
        tablaproducto.clearSelection();
        idProductoSeleccionado = 0; idCategoriaSeleccionada = 0; idProveedorSeleccionado = 0;
        btnregistrar.setEnabled(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        txtidproducto = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtnombre = new javax.swing.JTextField();
        txtprecio = new javax.swing.JTextField();
        txtstock = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaproducto = new javax.swing.JTable();
        btnregistrar = new javax.swing.JButton();
        btnlimpiar = new javax.swing.JButton();
        btnmproducto = new javax.swing.JButton();
        btneproducto = new javax.swing.JButton();
        btnvolver = new javax.swing.JButton();
        txtbuscar = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtmarca = new javax.swing.JTextField();
        cmbcategoria = new javax.swing.JComboBox<>();
        cmbproveedor = new javax.swing.JComboBox<>();
        btncategoria = new javax.swing.JButton();
        btnproveedores = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/LeonGymLogo.png"))); // NOI18N
        jLabel1.setText("PRODUCTOS");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(39, 6, 266, -1));
        getContentPane().add(txtidproducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(556, 401, 266, -1));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("PRODUCTO");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(39, 72, 100, -1));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("PRECIO UNIT.");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(39, 112, 100, -1));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("STOCK");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(39, 152, 100, -1));

        txtnombre.setBackground(new java.awt.Color(51, 51, 51));
        txtnombre.setForeground(new java.awt.Color(255, 255, 255));
        txtnombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtnombreActionPerformed(evt);
            }
        });
        getContentPane().add(txtnombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(157, 69, 148, -1));

        txtprecio.setBackground(new java.awt.Color(51, 51, 51));
        txtprecio.setForeground(new java.awt.Color(255, 255, 255));
        txtprecio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtprecioActionPerformed(evt);
            }
        });
        getContentPane().add(txtprecio, new org.netbeans.lib.awtextra.AbsoluteConstraints(157, 109, 148, -1));

        txtstock.setBackground(new java.awt.Color(51, 51, 51));
        txtstock.setForeground(new java.awt.Color(255, 255, 255));
        txtstock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtstockActionPerformed(evt);
            }
        });
        getContentPane().add(txtstock, new org.netbeans.lib.awtextra.AbsoluteConstraints(157, 149, 148, -1));

        tablaproducto.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "PRODUCTO", "PRECIO UNIT.", "STOCK", "CATEGORIA", "PROVEEDOR", "MARCA"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaproducto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaproductoMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablaproducto);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(323, 87, 577, 267));

        btnregistrar.setText("REGISTRAR");
        btnregistrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnregistrarActionPerformed(evt);
            }
        });
        getContentPane().add(btnregistrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(39, 319, 130, -1));

        btnlimpiar.setText("LIMPIAR");
        btnlimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnlimpiarActionPerformed(evt);
            }
        });
        getContentPane().add(btnlimpiar, new org.netbeans.lib.awtextra.AbsoluteConstraints(181, 319, 130, -1));

        btnmproducto.setText("MODIFICAR");
        btnmproducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnmproductoActionPerformed(evt);
            }
        });
        getContentPane().add(btnmproducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 360, 130, -1));

        btneproducto.setText("ELIMINAR");
        btneproducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btneproductoActionPerformed(evt);
            }
        });
        getContentPane().add(btneproducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 360, 130, -1));

        btnvolver.setText("VOLVER");
        btnvolver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnvolverActionPerformed(evt);
            }
        });
        getContentPane().add(btnvolver, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 430, 124, -1));

        txtbuscar.setBackground(new java.awt.Color(51, 51, 51));
        txtbuscar.setForeground(new java.awt.Color(255, 255, 255));
        txtbuscar.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtbuscar.setText("Ingrese nombre del producto");
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
        getContentPane().add(txtbuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(481, 26, 419, -1));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("BUSCAR PRODUCTO:");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(351, 29, -1, -1));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("CATEGORIA");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(39, 192, 100, -1));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("PROVEEDOR");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(39, 229, 100, -1));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("MARCA");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(39, 272, 100, -1));

        txtmarca.setBackground(new java.awt.Color(51, 51, 51));
        txtmarca.setForeground(new java.awt.Color(255, 255, 255));
        getContentPane().add(txtmarca, new org.netbeans.lib.awtextra.AbsoluteConstraints(157, 269, 148, -1));

        cmbcategoria.setBackground(new java.awt.Color(51, 51, 51));
        cmbcategoria.setForeground(new java.awt.Color(255, 255, 255));
        cmbcategoria.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbcategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbcategoriaActionPerformed(evt);
            }
        });
        getContentPane().add(cmbcategoria, new org.netbeans.lib.awtextra.AbsoluteConstraints(157, 189, 148, -1));

        cmbproveedor.setBackground(new java.awt.Color(51, 51, 51));
        cmbproveedor.setForeground(new java.awt.Color(255, 255, 255));
        cmbproveedor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbproveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbproveedorActionPerformed(evt);
            }
        });
        getContentPane().add(cmbproveedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(157, 229, 148, -1));

        btncategoria.setText("CATEGORÍA");
        btncategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btncategoriaActionPerformed(evt);
            }
        });
        getContentPane().add(btncategoria, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 400, 130, -1));

        btnproveedores.setText("PROVEEDORES");
        btnproveedores.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnproveedoresActionPerformed(evt);
            }
        });
        getContentPane().add(btnproveedores, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 400, 130, -1));

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/fondo.png"))); // NOI18N
        jLabel9.setText("jLabel9");
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(-10, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtprecioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtprecioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtprecioActionPerformed

    private void btnregistrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnregistrarActionPerformed
        if (txtnombre.getText().isEmpty() || txtprecio.getText().isEmpty() ||
            txtstock.getText().isEmpty() || txtmarca.getText().isEmpty() ||
            cmbcategoria.getSelectedIndex() == 0 || cmbproveedor.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(null, "Complete todos los campos."); return;
        }
        if (idInventario <= 0) {
            JOptionPane.showMessageDialog(null, "No hay inventario para esta sucursal."); return;
        }
        try {
            int stock = Integer.parseInt(txtstock.getText());
            float precio = Float.parseFloat(txtprecio.getText());
            if (stock < 0) { JOptionPane.showMessageDialog(null, "El stock no puede ser negativo."); return; }
            if (precio <= 0) { JOptionPane.showMessageDialog(null, "El precio debe ser mayor a cero."); return; }
            Resultado resultado = productoService.registrar(idCategoriaSeleccionada, idProveedorSeleccionado,
                idInventario, txtnombre.getText(), stock, txtmarca.getText(), precio);
            JOptionPane.showMessageDialog(null, resultado.mensaje());
            if (resultado.exito()) { limpiar(); cargartabla(); }
        } catch (Exception e) { JOptionPane.showMessageDialog(null, e.toString()); }
    }//GEN-LAST:event_btnregistrarActionPerformed

    private void btnlimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnlimpiarActionPerformed
        limpiar();
    }//GEN-LAST:event_btnlimpiarActionPerformed

    private void btnvolverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnvolverActionPerformed
        this.setVisible(false);
        frmInventario.setVisible(true);
    }//GEN-LAST:event_btnvolverActionPerformed

    private void tablaproductoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaproductoMouseClicked
        int fila = tablaproducto.getSelectedRow();
        if (fila >= 0) {
            DefaultTableModel modelo = (DefaultTableModel) tablaproducto.getModel();
            idProductoSeleccionado = Integer.parseInt(modelo.getValueAt(fila, 0).toString());
            txtnombre.setText(modelo.getValueAt(fila, 1).toString());
            txtprecio.setText(modelo.getValueAt(fila, 2).toString());
            txtstock.setText(modelo.getValueAt(fila, 3).toString());
            txtmarca.setText(modelo.getValueAt(fila, 6).toString());
            String cat = modelo.getValueAt(fila, 4).toString();
            String prov = modelo.getValueAt(fila, 5).toString();
            for (int i = 0; i < cmbcategoria.getItemCount(); i++) {
                if (cmbcategoria.getItemAt(i).equals(cat)) { cmbcategoria.setSelectedIndex(i); break; }
            }
            for (int i = 0; i < cmbproveedor.getItemCount(); i++) {
                if (cmbproveedor.getItemAt(i).equals(prov)) { cmbproveedor.setSelectedIndex(i); break; }
            }
            btnregistrar.setEnabled(false);
        }
    }//GEN-LAST:event_tablaproductoMouseClicked

    private void txtstockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtstockActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtstockActionPerformed

    private void btneproductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btneproductoActionPerformed
        if (idProductoSeleccionado == 0) {
            JOptionPane.showMessageDialog(null, "Seleccione un producto."); return;
        }
        int conf = JOptionPane.showConfirmDialog(null, "¿Eliminar producto?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (conf == JOptionPane.YES_OPTION) {
            try {
                Resultado resultado = productoService.eliminar(idProductoSeleccionado);
                JOptionPane.showMessageDialog(null, resultado.mensaje());
                if (resultado.exito()) { limpiar(); cargartabla(); }
            } catch (Exception e) { JOptionPane.showMessageDialog(null, e.toString()); }
        }
    }//GEN-LAST:event_btneproductoActionPerformed

    private void btnmproductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnmproductoActionPerformed
        if (idProductoSeleccionado == 0) {
            JOptionPane.showMessageDialog(null, "Seleccione un producto."); return;
        }
        try {
            int stock = Integer.parseInt(txtstock.getText());
            float precio = Float.parseFloat(txtprecio.getText());
            if (stock < 0) { JOptionPane.showMessageDialog(null, "El stock no puede ser negativo."); return; }
            if (precio <= 0) { JOptionPane.showMessageDialog(null, "El precio debe ser mayor a cero."); return; }
            Resultado resultado = productoService.actualizar(idProductoSeleccionado, idCategoriaSeleccionada,
                idProveedorSeleccionado, txtnombre.getText(), stock, txtmarca.getText(), precio);
            JOptionPane.showMessageDialog(null, resultado.mensaje());
            if (resultado.exito()) { limpiar(); cargartabla(); }
        } catch (Exception e) { JOptionPane.showMessageDialog(null, e.toString()); }
    }//GEN-LAST:event_btnmproductoActionPerformed

    private void txtbuscarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtbuscarKeyReleased
        buscarProducto();
    }//GEN-LAST:event_txtbuscarKeyReleased

    private void txtbuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtbuscarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtbuscarActionPerformed

    private void btncategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncategoriaActionPerformed
        Diseño.NavegacionUtil.navegar(btncategoria, () -> {
            new FrmCategoria(this).setVisible(true);
            this.setVisible(false);
        });
    }//GEN-LAST:event_btncategoriaActionPerformed

    private void cmbproveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbproveedorActionPerformed
        if (cmbproveedor.getSelectedIndex() <= 0) return;
        Integer id = proveedoresPorNombre.get(cmbproveedor.getSelectedItem().toString());
        idProveedorSeleccionado = id != null ? id : 0;
    }//GEN-LAST:event_cmbproveedorActionPerformed

    private void cmbcategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbcategoriaActionPerformed
        if (cmbcategoria.getSelectedIndex() <= 0) return;
        Integer id = categoriasPorNombre.get(cmbcategoria.getSelectedItem().toString());
        idCategoriaSeleccionada = id != null ? id : 0;
    }//GEN-LAST:event_cmbcategoriaActionPerformed

    private void btnproveedoresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnproveedoresActionPerformed
        Diseño.NavegacionUtil.navegar(btnproveedores, () -> {
            new FrmProveedor(this).setVisible(true);
            this.setVisible(false);
        });
    }//GEN-LAST:event_btnproveedoresActionPerformed

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
            java.util.logging.Logger.getLogger(FrmProductos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmProductos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmProductos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmProductos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmProductos().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btncategoria;
    private javax.swing.JButton btneproducto;
    private javax.swing.JButton btnlimpiar;
    private javax.swing.JButton btnmproducto;
    private javax.swing.JButton btnproveedores;
    private javax.swing.JButton btnregistrar;
    private javax.swing.JButton btnvolver;
    private javax.swing.JComboBox<String> cmbcategoria;
    private javax.swing.JComboBox<String> cmbproveedor;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tablaproducto;
    private javax.swing.JTextField txtbuscar;
    private javax.swing.JTextField txtidproducto;
    private javax.swing.JTextField txtmarca;
    private javax.swing.JTextField txtnombre;
    private javax.swing.JTextField txtprecio;
    private javax.swing.JTextField txtstock;
    // End of variables declaration//GEN-END:variables
}
