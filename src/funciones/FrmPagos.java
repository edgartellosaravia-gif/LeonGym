/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package funciones;

import Diseño.diseño;
import fabrica.AppFactory;
import servicio.PagoClienteService;
import servicio.CategoriaService;
import servicio.ProductoService;
import servicio.PagoService;
import servicio.Resultado;
import repositorio.PagoClienteRepository.MembresiaActiva;
import repositorio.PagoClienteRepository.DeudaFila;
import repositorio.PagoClienteRepository.PagoFila;
import repositorio.PagoClienteRepository.CompraFila;
import repositorio.ProductoRepository.ProductoDisponibleFila;
import repositorio.PagoRepository.ItemCarrito;
import static java.awt.image.ImageObserver.ALLBITS;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class FrmPagos extends javax.swing.JFrame {

    // Estas 4 lecturas son exactamente las mismas que el "estado de cuenta"
    // del portal de cliente (FrmPagoClientes); se reutiliza el mismo
    // servicio en vez de duplicar las consultas SQL aquí.
    private final PagoClienteService pagoClienteService = AppFactory.getInstance().pagoClienteService();
    private final CategoriaService categoriaService = AppFactory.getInstance().categoriaService();
    private final ProductoService productoService = AppFactory.getInstance().productoService();
    private final PagoService pagoService = AppFactory.getInstance().pagoService();

    private FrmClientes frmClientes;
    private int idCliente;
    private int idEmpleado;
    private int idSucursal;
    private String nombreCliente;
    private boolean modoEmpleado;
    private LinkedHashMap<Integer, Object[]> carrito = new LinkedHashMap<>();
    // Caché en memoria del combo de categorías: nombre -> id (evita una
    // consulta a la BD por cada selección, igual que en FrmProductos).
    private LinkedHashMap<String, Integer> categoriasPorNombre = new LinkedHashMap<>();

    // ── CONSTRUCTOR MODO EMPLEADO ────────────────────────────────────────────
    public FrmPagos(int idCliente, String nombreCliente, FrmClientes frmClientes,
                    int idEmpleado, boolean modoEmpleado) {
        initComponents();
        setResizable(false);
        this.idCliente     = idCliente;
        this.nombreCliente = nombreCliente;
        this.frmClientes   = frmClientes;
        this.modoEmpleado  = modoEmpleado;
        if (frmClientes != null) {
            this.idSucursal = frmClientes.getIdSucursal();
            this.idEmpleado = frmClientes.getIdEmpleado();
        }
        setTitle("Pagos y Compras - " + nombreCliente);
        lblcliente.setText(nombreCliente);
        setSize(730, 760);
        setLocationRelativeTo(null);
        //jlabel7.setContentAreaFilled(false);
        configurarModo();
        cargarMembresia();
        cargarCategorias();
        cargarProductos(0);
        cargarDeudas();
        cargarHistorialPagos();
        cargarHistorialCompras();
        diseño.estilizarBotones(btnAgregarCarrito, btnQuitarCarrito, btnRegistrarVenta, btnactualizar, btnRegistrarPago, btnvolver);
        diseño.estilizarTabla(tabladeudas, jScrollPane1, ALLBITS);
        diseño.estilizarTabla(tablaproductos, jScrollPane1, ALLBITS);
        diseño.estilizarTabla(tablacarrito, jScrollPane1, ALLBITS);
        diseño.estilizarTabla(tablapagos, jScrollPane1, ALLBITS);
        diseño.estilizarTabla(tablacompras, jScrollPane1, 10);
        tabladeudas.getColumnModel().getColumn(0).setMinWidth(0);
        tabladeudas.getColumnModel().getColumn(0).setMaxWidth(0);
        tabladeudas.getColumnModel().getColumn(0).setWidth(0);
        tablaproductos.getColumnModel().getColumn(0).setMinWidth(0);
        tablaproductos.getColumnModel().getColumn(0).setMaxWidth(0);
        tablaproductos.getColumnModel().getColumn(0).setWidth(0);
        tablacarrito.getColumnModel().getColumn(0).setMinWidth(0);
        tablacarrito.getColumnModel().getColumn(0).setMaxWidth(0);
        tablacarrito.getColumnModel().getColumn(0).setWidth(0);
    }

    public FrmPagos() { initComponents(); }

    private void configurarModo() {
        btnAgregarCarrito.setEnabled(modoEmpleado);
        btnQuitarCarrito.setEnabled(modoEmpleado);
        btnRegistrarVenta.setEnabled(modoEmpleado);
        btnRegistrarPago.setEnabled(modoEmpleado);
        cmbcategoria.setEnabled(modoEmpleado);
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

    private void cargarCategorias() {
        cmbcategoria.removeAllItems();
        cmbcategoria.addItem("Todas las categorías");
        try {
            categoriasPorNombre.clear();
            for (var fila : categoriaService.listarTodas()) {
                categoriasPorNombre.put(fila.nombre(), fila.idCategoria());
                cmbcategoria.addItem(fila.nombre());
            }
        } catch (Exception e) { JOptionPane.showMessageDialog(null, e.toString()); }
    }

    private void cargarProductos(int idCategoria) {
        DefaultTableModel modelo = (DefaultTableModel) tablaproductos.getModel();
        modelo.setRowCount(0);
        try {
            for (ProductoDisponibleFila fila : productoService.listarDisponiblesParaVenta(idSucursal, idCategoria)) {
                modelo.addRow(new Object[]{
                    fila.idProducto(),
                    fila.nombre(),
                    fila.marca(),
                    "S/ " + String.format("%.2f", fila.precio()),
                    fila.stock(),
                    fila.categoria()
                });
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

    private void actualizarTablaCarrito() {
        DefaultTableModel modelo = (DefaultTableModel) tablacarrito.getModel();
        modelo.setRowCount(0);
        double total = 0.0;
        for (Map.Entry<Integer, Object[]> entrada : carrito.entrySet()) {
            Object[] datos  = entrada.getValue();
            String nombre   = (String) datos[0];
            double precio   = (double) datos[1];
            int cantidad    = (int) datos[2];
            double subtotal = precio * cantidad;
            total += subtotal;
            modelo.addRow(new Object[]{
                entrada.getKey(), nombre,
                "S/ " + String.format("%.2f", precio),
                cantidad,
                "S/ " + String.format("%.2f", subtotal)
            });
        }
        lbltotal.setText("Total: S/ " + String.format("%.2f", total));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        lblcliente = new javax.swing.JLabel();
        TabbedPane = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        lblplan = new javax.swing.JLabel();
        lblfechainicio = new javax.swing.JLabel();
        lblfechafin = new javax.swing.JLabel();
        lblestado = new javax.swing.JLabel();
        lblclases = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        cmbcategoria = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaproductos = new javax.swing.JTable();
        btnAgregarCarrito = new javax.swing.JButton();
        btnQuitarCarrito = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablacarrito = new javax.swing.JTable();
        lbltotal = new javax.swing.JLabel();
        btnRegistrarVenta = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tabladeudas = new javax.swing.JTable();
        lbltotaldeuda = new javax.swing.JLabel();
        btnRegistrarPago = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tablapagos = new javax.swing.JTable();
        jLabel15 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tablacompras = new javax.swing.JTable();
        jLabel17 = new javax.swing.JLabel();
        btnvolver = new javax.swing.JButton();
        btnactualizar = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();

        jLabel2.setText("jLabel2");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/LeonGymLogo.png"))); // NOI18N
        jLabel1.setText("CLIENTE:");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, 150, -1));

        lblcliente.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblcliente.setForeground(new java.awt.Color(255, 255, 255));
        lblcliente.setText("     ");
        getContentPane().add(lblcliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 30, 180, -1));

        TabbedPane.setBackground(new java.awt.Color(51, 51, 51));
        TabbedPane.setBorder(new javax.swing.border.MatteBorder(null));
        TabbedPane.setForeground(new java.awt.Color(255, 255, 255));
        TabbedPane.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("PLAN         :");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 43, 70, -1));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("INICIO       :");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 75, 70, -1));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("ESTADO    :");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 101, 70, -1));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("CLASES      :");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 133, 70, -1));

        jLabel7.setBackground(new java.awt.Color(51, 51, 51));
        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("MEMBRESÍA ACTIVA");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 6, 650, -1));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("FIN            :");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(214, 75, 70, -1));

        jLabel9.setBackground(new java.awt.Color(51, 51, 51));
        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("COMPRAR PRODUCTOS");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 168, 650, -1));

        lblplan.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblplan.setForeground(new java.awt.Color(255, 255, 255));
        lblplan.setText("          ");
        jPanel1.add(lblplan, new org.netbeans.lib.awtextra.AbsoluteConstraints(82, 40, 126, -1));

        lblfechainicio.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblfechainicio.setForeground(new java.awt.Color(255, 255, 255));
        lblfechainicio.setText("                    ");
        jPanel1.add(lblfechainicio, new org.netbeans.lib.awtextra.AbsoluteConstraints(82, 72, 126, -1));

        lblfechafin.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblfechafin.setForeground(new java.awt.Color(255, 255, 255));
        lblfechafin.setText("           ");
        jPanel1.add(lblfechafin, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 72, 120, -1));

        lblestado.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblestado.setForeground(new java.awt.Color(255, 255, 255));
        lblestado.setText("           ");
        jPanel1.add(lblestado, new org.netbeans.lib.awtextra.AbsoluteConstraints(82, 98, 126, -1));

        lblclases.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblclases.setForeground(new java.awt.Color(255, 255, 255));
        lblclases.setText("               ");
        jPanel1.add(lblclases, new org.netbeans.lib.awtextra.AbsoluteConstraints(82, 130, 126, -1));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("CATEGORÍA:");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 205, 114, -1));

        cmbcategoria.setBackground(new java.awt.Color(51, 51, 51));
        cmbcategoria.setForeground(new java.awt.Color(255, 255, 255));
        cmbcategoria.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbcategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbcategoriaActionPerformed(evt);
            }
        });
        jPanel1.add(cmbcategoria, new org.netbeans.lib.awtextra.AbsoluteConstraints(126, 202, 198, -1));

        tablaproductos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID", "PRODUCTO", "MARCA", "PRECIO", "STOCK", "CATEGORÍA"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tablaproductos);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 230, 638, 111));

        btnAgregarCarrito.setText("AGREGAR (+)");
        btnAgregarCarrito.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarCarritoActionPerformed(evt);
            }
        });
        jPanel1.add(btnAgregarCarrito, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 353, -1, -1));

        btnQuitarCarrito.setText("QUITAR (-)");
        btnQuitarCarrito.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuitarCarritoActionPerformed(evt);
            }
        });
        jPanel1.add(btnQuitarCarrito, new org.netbeans.lib.awtextra.AbsoluteConstraints(305, 353, 102, -1));

        jLabel11.setBackground(new java.awt.Color(51, 51, 51));
        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("CARRITO");
        jPanel1.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 394, 644, -1));

        tablacarrito.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID", "PRODUCTO", "PRECIO", "CANTIDAD", "SUBTOTAL"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tablacarrito);

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 428, 644, 111));

        lbltotal.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lbltotal.setForeground(new java.awt.Color(255, 255, 255));
        lbltotal.setText("TOTAL: S/ 0.00");
        jPanel1.add(lbltotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 554, 160, -1));

        btnRegistrarVenta.setText("REGISTRAR VENTA");
        btnRegistrarVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarVentaActionPerformed(evt);
            }
        });
        jPanel1.add(btnRegistrarVenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 551, -1, -1));

        jLabel16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/fondo.png"))); // NOI18N
        jPanel1.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        TabbedPane.addTab("COMPRAS", jPanel1);

        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("DEUDAS PENDIENTES");
        jPanel2.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 6, 638, -1));

        tabladeudas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID", "FECHA Y HORA", "DESCRIPCIÓN", "MONTO", "MÉTODO", "ESTADO"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(tabladeudas);

        jPanel2.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 28, 641, 108));

        lbltotaldeuda.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        lbltotaldeuda.setForeground(new java.awt.Color(255, 255, 255));
        lbltotaldeuda.setText("DEUDA TOTAL:  S/ 0.00");
        jPanel2.add(lbltotaldeuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 157, -1, -1));

        btnRegistrarPago.setText("REGISTRAR PAGO");
        btnRegistrarPago.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarPagoActionPerformed(evt);
            }
        });
        jPanel2.add(btnRegistrarPago, new org.netbeans.lib.awtextra.AbsoluteConstraints(494, 154, -1, -1));

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("HISTORIAL DE PAGOS");
        jPanel2.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 195, 638, -1));

        tablapagos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "FECHA Y HORA", "DESCRIPCIÓN", "MONTO", "MÉTODO"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(tablapagos);

        jPanel2.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 223, 638, 112));

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("HISTORIAL DE COMPRAS");
        jPanel2.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 353, 638, -1));

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
        jScrollPane5.setViewportView(tablacompras);

        jPanel2.add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 387, 638, 117));

        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/fondo.png"))); // NOI18N
        jLabel17.setText("jLabel17");
        jPanel2.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 650, -1));

        TabbedPane.addTab("PAGOS", jPanel2);

        getContentPane().add(TabbedPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 70, 650, 620));

        btnvolver.setText("VOLVER");
        btnvolver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnvolverActionPerformed(evt);
            }
        });
        getContentPane().add(btnvolver, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 690, -1, -1));

        btnactualizar.setText("ACTUALIZAR");
        btnactualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnactualizarActionPerformed(evt);
            }
        });
        getContentPane().add(btnactualizar, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 690, 117, -1));

        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/fondo.png"))); // NOI18N
        getContentPane().add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 730));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnQuitarCarritoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuitarCarritoActionPerformed
        int fila = tablacarrito.getSelectedRow();
        if (fila < 0) { JOptionPane.showMessageDialog(null, "Seleccione un producto del carrito."); return; }
        DefaultTableModel modelo = (DefaultTableModel) tablacarrito.getModel();
        int idProducto = Integer.parseInt(modelo.getValueAt(fila, 0).toString());
        int cantidad   = (int) carrito.get(idProducto)[2];
        if (cantidad <= 1) carrito.remove(idProducto);
        else carrito.get(idProducto)[2] = cantidad - 1;
        actualizarTablaCarrito();
    }//GEN-LAST:event_btnQuitarCarritoActionPerformed

    private void btnRegistrarPagoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarPagoActionPerformed
        int fila = tabladeudas.getSelectedRow();
            if (fila < 0) { JOptionPane.showMessageDialog(null, "Seleccione una deuda."); return; }
            DefaultTableModel modelo = (DefaultTableModel) tabladeudas.getModel();
            int idPago    = Integer.parseInt(modelo.getValueAt(fila, 0).toString());
            double monto  = Double.parseDouble(modelo.getValueAt(fila, 3).toString().replace("S/ ", ""));

            String metodo = (String) JOptionPane.showInputDialog(null,
                "Método de pago:", "Registrar Pago", JOptionPane.QUESTION_MESSAGE, null,
                new String[]{"EFECTIVO","TARJETA","TRANSFERENCIA","YAPE"}, "EFECTIVO");
            if (metodo == null) return;

            Resultado resultado = pagoService.registrarPago(idPago, (float) monto, metodo);
            JOptionPane.showMessageDialog(null, resultado.mensaje());
            if (resultado.exito()) {
                cargarDeudas();
                cargarHistorialPagos();
            }
    }//GEN-LAST:event_btnRegistrarPagoActionPerformed

    private void btnvolverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnvolverActionPerformed
        this.setVisible(false);
        frmClientes.setVisible(true);
    }//GEN-LAST:event_btnvolverActionPerformed

    private void cmbcategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbcategoriaActionPerformed
        if (cmbcategoria.getSelectedIndex() <= 0) { cargarProductos(0); return; }
        Integer id = categoriasPorNombre.get(cmbcategoria.getSelectedItem().toString());
        cargarProductos(id != null ? id : 0);
    }//GEN-LAST:event_cmbcategoriaActionPerformed

    private void btnRegistrarVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarVentaActionPerformed
        if (carrito.isEmpty()) {
            JOptionPane.showMessageDialog(null, "El carrito está vacío."); return;
        }
        double total = 0.0;
        for (Object[] datos : carrito.values()) total += (double) datos[1] * (int) datos[2];

        String metodo = (String) JOptionPane.showInputDialog(null,
            "Método de pago:", "Pago", JOptionPane.QUESTION_MESSAGE, null,
            new String[]{"EFECTIVO","TARJETA","TRANSFERENCIA","YAPE"}, "EFECTIVO");
        if (metodo == null) return;

        int confirmacion = JOptionPane.showConfirmDialog(null,
            "¿El cliente paga ahora o queda pendiente?",
            "Estado del pago", JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null);
        String estadoPago = (confirmacion == JOptionPane.YES_OPTION) ? "PAGADO" : "PENDIENTE";

        Map<Integer, ItemCarrito> items = new LinkedHashMap<>();
        for (Map.Entry<Integer, Object[]> entrada : carrito.entrySet()) {
            Object[] datos = entrada.getValue();
            items.put(entrada.getKey(), new ItemCarrito((double) datos[1], (int) datos[2]));
        }

        Resultado resultado = pagoService.registrarVentaProductos(
                idCliente, idEmpleado, idSucursal, items, total, metodo, estadoPago);

        JOptionPane.showMessageDialog(null, resultado.mensaje());
        if (resultado.exito()) {
            carrito.clear();
            actualizarTablaCarrito();
            cargarProductos(0);
            cmbcategoria.setSelectedIndex(0);
            cargarDeudas();
            cargarHistorialPagos();
            cargarHistorialCompras();
        }
    }//GEN-LAST:event_btnRegistrarVentaActionPerformed

    private void btnAgregarCarritoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarCarritoActionPerformed
        int fila = tablaproductos.getSelectedRow();
        if (fila < 0) { JOptionPane.showMessageDialog(null, "Seleccione un producto."); return; }
        DefaultTableModel modelo = (DefaultTableModel) tablaproductos.getModel();
        int idProducto  = Integer.parseInt(modelo.getValueAt(fila, 0).toString());
        String nombre   = modelo.getValueAt(fila, 1).toString();
        double precio   = Double.parseDouble(modelo.getValueAt(fila, 3).toString().replace("S/ ", ""));
        int stockDisp   = Integer.parseInt(modelo.getValueAt(fila, 4).toString());
        if (carrito.containsKey(idProducto)) {
            int cantActual = (int) carrito.get(idProducto)[2];
            if (cantActual >= stockDisp) {
                JOptionPane.showMessageDialog(null, "No hay más stock disponible."); return;
            }
            carrito.get(idProducto)[2] = cantActual + 1;
        } else {
            carrito.put(idProducto, new Object[]{nombre, precio, 1});
        }
        actualizarTablaCarrito();
    }//GEN-LAST:event_btnAgregarCarritoActionPerformed

    private void btnactualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnactualizarActionPerformed
        cargarDeudas();
        cargarHistorialPagos();
        cargarHistorialCompras();
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
            java.util.logging.Logger.getLogger(FrmPagos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmPagos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmPagos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmPagos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmPagos().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane TabbedPane;
    private javax.swing.JButton btnAgregarCarrito;
    private javax.swing.JButton btnQuitarCarrito;
    private javax.swing.JButton btnRegistrarPago;
    private javax.swing.JButton btnRegistrarVenta;
    private javax.swing.JButton btnactualizar;
    private javax.swing.JButton btnvolver;
    private javax.swing.JComboBox<String> cmbcategoria;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JLabel lblclases;
    private javax.swing.JLabel lblcliente;
    private javax.swing.JLabel lblestado;
    private javax.swing.JLabel lblfechafin;
    private javax.swing.JLabel lblfechainicio;
    private javax.swing.JLabel lblplan;
    private javax.swing.JLabel lbltotal;
    private javax.swing.JLabel lbltotaldeuda;
    private javax.swing.JTable tablacarrito;
    private javax.swing.JTable tablacompras;
    private javax.swing.JTable tabladeudas;
    private javax.swing.JTable tablapagos;
    private javax.swing.JTable tablaproductos;
    // End of variables declaration//GEN-END:variables
}
