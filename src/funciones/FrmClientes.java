package funciones;

import Diseño.diseño;
import PRINCIPAL.FrmInicio;
import fabrica.AppFactory;
import servicio.ClienteService;
import servicio.MembresiaClienteService;
import servicio.AsistenciaService;
import servicio.AutenticacionService;
import repositorio.ClienteRepository.ClienteFila;
import repositorio.MembresiaClienteRepository.AlertaFila;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import funciones.FrmMembresia;
import funciones.FrmPagos;
import funciones.FrmSucursal;
import java.awt.Color;
import static java.awt.image.ImageObserver.ALLBITS;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import servicio.AvisoService;
import servicio.Resultado;

public class FrmClientes extends javax.swing.JFrame {

    private final ClienteService clienteService = AppFactory.getInstance().clienteService();
    private final MembresiaClienteService membresiaClienteService = AppFactory.getInstance().membresiaClienteService();
    private final AsistenciaService asistenciaService = AppFactory.getInstance().asistenciaService();
    private final AutenticacionService autenticacionService = AppFactory.getInstance().autenticacionService();
    private final servicio.ReporteService reporteService = AppFactory.getInstance().reporteService();
    private final AvisoService avisoService = AppFactory.getInstance().avisoService();

    FrmSucursal sucursal;
    int idEmpleado;
    int idSucursal;
    String nombreSucursal;
    String cargoEmpleado;
    int idClienteSeleccionado = 0;
    private javax.swing.Timer timer;

    public FrmClientes(FrmSucursal sucursal, int idSucursal, int idEmpleado, String nombreSucursal, String cargo) {
        initComponents();
        setResizable(false);
        setLocationRelativeTo(null);
        diseño.estilizarBotones(btnregistrar, btnmodificar, btnlimpiar, btneliminar, btncerrarsesion, 
                      btnactualizar, btnvolver, btnmembresia, btnpago, btnreporte);
        this.sucursal     = sucursal;
        this.idSucursal   = idSucursal;
        this.idEmpleado   = idEmpleado;
        this.nombreSucursal = nombreSucursal;
        this.cargoEmpleado = cargo;

        diseño.estilizarTabla(tabla, jScrollPane1, ALLBITS);
        diseño.estilizarTabla(tablacaduca, jScrollPane1, ALLBITS);

        txtidcliente.setVisible(false);
        lblsede.setText(nombreSucursal);
        cargartabla();
        cargarTablaMembresias();
        String cargoUpper = java.text.Normalizer
        .normalize(cargo.toUpperCase().trim(), java.text.Normalizer.Form.NFD)
        .replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        if (cargoUpper.contains("RECEP")||cargoUpper.contains("SUPER")) {
            btnvolver.setText("SALIR");
        }
        if (cargoUpper.contains("SUPER")) {
            setSize(1190, 596);
        }else{
            setSize(1190, 555);
        }

        if (tablacaduca.getRowCount() > 0) {
            int conDeudaYVencida = 0;
            for (int i = 0; i < tablacaduca.getRowCount(); i++) {
                String estado = tablacaduca.getValueAt(i, 4).toString();
                String deuda  = tablacaduca.getValueAt(i, 5).toString().replace("S/ ", "");
                if (estado.equals("VENCIDA") && Double.parseDouble(deuda) > 0) conDeudaYVencida++;
            }
            if (conDeudaYVencida > 0) {
                JOptionPane.showMessageDialog(null,
                    "Hay " + conDeudaYVencida + " cliente(s) con membresía VENCIDA y deuda pendiente.",
                    "Alerta de Membresías",
                    JOptionPane.WARNING_MESSAGE);
            }
        }

        timer = new javax.swing.Timer(60000, e -> cargarTablaMembresias());
        timer.start();

        btnmembresia.setEnabled(false);
        btnpago.setEnabled(false);
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                btnmembresia.setEnabled(tabla.getSelectedRow() >= 0);
                btnpago.setEnabled(tabla.getSelectedRow() >= 0);
                cargarTablaMembresias();
            }
        });
        cargarcmb();
        tabla.getColumnModel().getColumn(0).setMinWidth(0);
        tabla.getColumnModel().getColumn(0).setMaxWidth(0);
        tabla.getColumnModel().getColumn(0).setWidth(0);
    }

    public FrmClientes() { initComponents(); }
    int idSucursalSeleccionada = idSucursal;
    private void cargarcmb(){
        cmbaccion.removeAllItems();
        cmbaccion.addItem("VER GESTIÓN DE:");
        cmbaccion.addItem("EMPLEADOS");
        cmbaccion.addItem("INVENTARIO");
        cmbaccion.addItem("REPORTES");
        cmbaccion.addItem("SERVICIOS");
    }
    private void cargartabla() {
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        modelo.setRowCount(0);
        try {
            for (ClienteFila fila : clienteService.listarPorSucursal(idSucursal)) {
                modelo.addRow(new Object[]{
                    fila.idCliente(), fila.nombre(), fila.apellido(), fila.dni(), fila.telefono(), fila.email()
                });
            }
        } catch (Exception e) { JOptionPane.showMessageDialog(null, e.toString()); }
    }

    private void cargarTablaMembresias() {
        // Recalcula VENCIDA/POR_VENCER (misma operación reutilizada del portal de cliente).
        membresiaClienteService.actualizarEstadosVencidos();

        DefaultTableModel modelo = (DefaultTableModel) tablacaduca.getModel();
        modelo.setRowCount(0);
        try {
            for (AlertaFila fila : membresiaClienteService.listarAlertasPorSucursal(idSucursal)) {
                modelo.addRow(new Object[]{
                    fila.nombreCliente(),
                    fila.dni(),
                    fila.fechaInicio(),
                    fila.fechaFin(),
                    fila.estado(),
                    "S/ " + String.format("%.2f", fila.deuda()),
                    fila.alerta()
                });
            }
        } catch (Exception e) { JOptionPane.showMessageDialog(null, e.toString()); }
    }

    private void buscarCliente() {
        try {
            String buscar = txtbuscar.getText().trim();
            DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
            modelo.setRowCount(0);
            for (ClienteFila fila : clienteService.buscarPorSucursalYNombre(idSucursal, buscar)) {
                modelo.addRow(new Object[]{
                    fila.idCliente(), fila.nombre(), fila.apellido(), fila.dni(), fila.telefono(), fila.email()
                });
            }
        } catch (Exception e) { JOptionPane.showMessageDialog(null, e.toString()); }
    }

    private void limpiar() {
        txtidcliente.setText(""); txtnombre.setText(""); txtapellido.setText("");
        txtdni.setText(""); txttelefono.setText(""); txtemail.setText("");
        tabla.clearSelection(); idClienteSeleccionado = 0;
        btnmembresia.setEnabled(false); btnpago.setEnabled(false);
        btnregistrar.setEnabled(true);
    }

    public int getIdSucursal()      { return idSucursal; }
    public String getNombreSucursal() { return nombreSucursal; }
    public int getIdEmpleado()      { return idEmpleado; }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();
        btnregistrar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtnombre = new javax.swing.JTextField();
        txtdni = new javax.swing.JTextField();
        txttelefono = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        btneliminar = new javax.swing.JButton();
        btnmodificar = new javax.swing.JButton();
        btnlimpiar = new javax.swing.JButton();
        btnmembresia = new javax.swing.JButton();
        btnpago = new javax.swing.JButton();
        txtidcliente = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablacaduca = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        btnvolver = new javax.swing.JButton();
        txtbuscar = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtemail = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtapellido = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        lblsede = new javax.swing.JLabel();
        btncerrarsesion = new javax.swing.JButton();
        btnactualizar = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        btnreporte = new javax.swing.JButton();
        cmbaccion = new javax.swing.JComboBox<>();
        btnAviso = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();

        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tabla.setBackground(new java.awt.Color(102, 102, 102));
        tabla.setBorder(new javax.swing.border.MatteBorder(null));
        tabla.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        tabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID", "NOMBRE", "APELLIDO", "DNI", "TELEFONO", "EMAIL"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabla.setGridColor(new java.awt.Color(51, 51, 51));
        tabla.setSelectionBackground(new java.awt.Color(153, 153, 153));
        tabla.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabla);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(358, 88, 810, 221));

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
        getContentPane().add(btnregistrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 310, 150, -1));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/LeonGymLogo.png"))); // NOI18N
        jLabel1.setText("CLIENTES  \"UTP-SPORTS\"");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 25, 350, -1));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("NOMBRE:");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 128, 71, -1));

        txtnombre.setBackground(new java.awt.Color(51, 51, 51));
        txtnombre.setForeground(new java.awt.Color(255, 255, 255));
        txtnombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtnombreActionPerformed(evt);
            }
        });
        getContentPane().add(txtnombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(95, 125, 245, -1));

        txtdni.setBackground(new java.awt.Color(51, 51, 51));
        txtdni.setForeground(new java.awt.Color(255, 255, 255));
        getContentPane().add(txtdni, new org.netbeans.lib.awtextra.AbsoluteConstraints(95, 193, 245, -1));

        txttelefono.setBackground(new java.awt.Color(51, 51, 51));
        txttelefono.setForeground(new java.awt.Color(255, 255, 255));
        getContentPane().add(txttelefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(95, 227, 245, -1));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("DNI:");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 196, 71, -1));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("TELEFONO:");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 230, 71, -1));

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
        getContentPane().add(btneliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 340, 150, -1));

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
        getContentPane().add(btnmodificar, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 310, 160, -1));

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
        getContentPane().add(btnlimpiar, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 340, 160, -1));

        btnmembresia.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        btnmembresia.setForeground(new java.awt.Color(255, 255, 255));
        btnmembresia.setText("VER MEMBRESIA");
        btnmembresia.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(204, 0, 0)));
        btnmembresia.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnmembresiaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnmembresiaMouseExited(evt);
            }
        });
        btnmembresia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnmembresiaActionPerformed(evt);
            }
        });
        getContentPane().add(btnmembresia, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 380, 150, -1));

        btnpago.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        btnpago.setForeground(new java.awt.Color(255, 255, 255));
        btnpago.setText("VER PAGOS");
        btnpago.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(204, 0, 0)));
        btnpago.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnpagoMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnpagoMouseExited(evt);
            }
        });
        btnpago.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnpagoActionPerformed(evt);
            }
        });
        getContentPane().add(btnpago, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 380, 160, -1));
        getContentPane().add(txtidcliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 6, 281, 13));

        tablacaduca.setBackground(new java.awt.Color(102, 102, 102));
        tablacaduca.setBorder(new javax.swing.border.MatteBorder(null));
        tablacaduca.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        tablacaduca.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "NOMBRE", "DNI", "FECHA DE INICIO", "CADUCIDAD", "ESTADO", "DEUDA", "ALERTA"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablacaduca.setGridColor(new java.awt.Color(51, 51, 51));
        tablacaduca.setSelectionBackground(new java.awt.Color(153, 153, 153));
        jScrollPane2.setViewportView(tablacaduca);

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(358, 357, 810, 140));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("FIN DE MEMBRESIA (<15 DIAS)");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 330, -1, -1));

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
        getContentPane().add(btnvolver, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 470, 160, -1));

        txtbuscar.setBackground(new java.awt.Color(51, 51, 51));
        txtbuscar.setForeground(new java.awt.Color(255, 255, 255));
        txtbuscar.setHorizontalAlignment(javax.swing.JTextField.CENTER);
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
        getContentPane().add(txtbuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 50, 311, 25));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("BUSCAR CLIENTE (DNI) :");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 50, 180, -1));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("EMAIL:");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 264, 71, -1));

        txtemail.setBackground(new java.awt.Color(51, 51, 51));
        txtemail.setForeground(new java.awt.Color(255, 255, 255));
        getContentPane().add(txtemail, new org.netbeans.lib.awtextra.AbsoluteConstraints(95, 261, 245, -1));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("APELLIDO:");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 162, 71, -1));

        txtapellido.setBackground(new java.awt.Color(51, 51, 51));
        txtapellido.setForeground(new java.awt.Color(255, 255, 255));
        getContentPane().add(txtapellido, new org.netbeans.lib.awtextra.AbsoluteConstraints(95, 159, 245, -1));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("SEDE");
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 103, 71, -1));

        lblsede.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblsede.setForeground(new java.awt.Color(255, 255, 255));
        lblsede.setText("    ");
        getContentPane().add(lblsede, new org.netbeans.lib.awtextra.AbsoluteConstraints(95, 103, 245, -1));

        btncerrarsesion.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        btncerrarsesion.setForeground(new java.awt.Color(255, 255, 255));
        btncerrarsesion.setText("CERRAR SESIÓN");
        btncerrarsesion.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(204, 0, 0)));
        btncerrarsesion.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btncerrarsesionMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btncerrarsesionMouseExited(evt);
            }
        });
        btncerrarsesion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btncerrarsesionActionPerformed(evt);
            }
        });
        getContentPane().add(btncerrarsesion, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 470, 150, -1));

        btnactualizar.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        btnactualizar.setForeground(new java.awt.Color(255, 255, 255));
        btnactualizar.setText("ACTUALIZAR");
        btnactualizar.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(204, 0, 0)));
        btnactualizar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnactualizarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnactualizarMouseExited(evt);
            }
        });
        btnactualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnactualizarActionPerformed(evt);
            }
        });
        getContentPane().add(btnactualizar, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 410, 150, -1));

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/fondo.png"))); // NOI18N
        jLabel11.setText("jLabel11");
        getContentPane().add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(1020, 0, -1, -1));

        btnreporte.setText("ENVIAR REPORTE");
        btnreporte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnreporteActionPerformed(evt);
            }
        });
        getContentPane().add(btnreporte, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 410, 160, -1));

        cmbaccion.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbaccion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbaccionActionPerformed(evt);
            }
        });
        getContentPane().add(cmbaccion, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 530, 170, -1));

        btnAviso.setText("ENVIAR AVISO");
        btnAviso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAvisoActionPerformed(evt);
            }
        });
        getContentPane().add(btnAviso, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 530, -1, -1));

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/fondo.png"))); // NOI18N
        jLabel10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel10MouseEntered(evt);
            }
        });
        getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnregistrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnregistrarActionPerformed

        if (txtnombre.getText().isEmpty() || txtapellido.getText().isEmpty() ||
            txtdni.getText().isEmpty() || txttelefono.getText().isEmpty() ||
            txtemail.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Complete todos los campos");
            return;
        }
        try {
            Resultado resultado = clienteService.registrar(idSucursal, txtnombre.getText(), txtapellido.getText(),
                txtdni.getText(), txttelefono.getText(), txtemail.getText());
            JOptionPane.showMessageDialog(null, resultado.mensaje());
            if (resultado.exito()) { limpiar(); cargartabla(); }
        } catch (Exception e) { JOptionPane.showMessageDialog(null, e.toString()); }
    
    }//GEN-LAST:event_btnregistrarActionPerformed

    private void txtnombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtnombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtnombreActionPerformed

    private void btneliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btneliminarActionPerformed
        if (idClienteSeleccionado == 0) {
            JOptionPane.showMessageDialog(null, "Seleccione un cliente de la tabla");
            return;
        }
        int conf = JOptionPane.showConfirmDialog(null, "¿Eliminar cliente?",
            "Confirmar", JOptionPane.YES_NO_OPTION);
        if (conf == JOptionPane.YES_OPTION) {
            try {
                Resultado resultado = clienteService.eliminar(idClienteSeleccionado);
                JOptionPane.showMessageDialog(null, resultado.mensaje());
                if (resultado.exito()) { limpiar(); cargartabla(); }
            } catch (Exception e) { JOptionPane.showMessageDialog(null, e.toString()); }
        }
    }//GEN-LAST:event_btneliminarActionPerformed

    private void tablaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaMouseClicked

        int fila = tabla.getSelectedRow();
        if (fila >= 0) {
            try {
                int id = Integer.parseInt(tabla.getValueAt(fila, 0).toString());
                ClienteFila cliente = clienteService.obtenerPorId(id);
                if (cliente != null) {
                    txtidcliente.setText(String.valueOf(id));
                    idClienteSeleccionado = id;
                    txtnombre.setText(cliente.nombre());
                    txtapellido.setText(cliente.apellido());
                    txtdni.setText(cliente.dni());
                    txttelefono.setText(cliente.telefono());
                    txtemail.setText(cliente.email());
                }
            } catch (Exception e) { JOptionPane.showMessageDialog(null, e.toString()); }
            btnregistrar.setEnabled(false);
        
    }

    }//GEN-LAST:event_tablaMouseClicked

    private void btnlimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnlimpiarActionPerformed
        limpiar();
    }//GEN-LAST:event_btnlimpiarActionPerformed

    private void btnmodificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnmodificarActionPerformed

        if (idClienteSeleccionado == 0) {
            JOptionPane.showMessageDialog(null, "Seleccione un cliente de la tabla");
            return;}
        try {
            Resultado resultado = clienteService.actualizar(idClienteSeleccionado, txtnombre.getText(), txtapellido.getText(),
                txtdni.getText(), txttelefono.getText(), txtemail.getText());
            JOptionPane.showMessageDialog(null, resultado.mensaje());
            if (resultado.exito()) { limpiar(); cargartabla(); }
        } catch (Exception e) { JOptionPane.showMessageDialog(null, e.toString()); }
        
    }//GEN-LAST:event_btnmodificarActionPerformed

    private void btnmembresiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnmembresiaActionPerformed
        int fila = tabla.getSelectedRow();
        if (fila == -1) { JOptionPane.showMessageDialog(null, "Seleccione un cliente"); return; }
        int idCliente = Integer.parseInt(tabla.getValueAt(fila, 0).toString());
        String nombreCliente = tabla.getValueAt(fila, 1) + " " + tabla.getValueAt(fila, 2);
        Diseño.NavegacionUtil.navegar(btnmembresia, () ->
            new FrmMembresia(idCliente, nombreCliente, this).setVisible(true));
        this.setVisible(false); 
    }//GEN-LAST:event_btnmembresiaActionPerformed

    private void btnvolverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnvolverActionPerformed
        timer.stop();
        autenticacionService.cerrarConexionCompartida();

        String cargoUpper = java.text.Normalizer
            .normalize(cargoEmpleado.toUpperCase().trim(), java.text.Normalizer.Form.NFD)
            .replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");

        if (cargoUpper.contains("RECEP")||cargoUpper.contains("SUPER")) {
            int conf = JOptionPane.showConfirmDialog(null,
                "¿Deseas registrar tu salida y cerrar el programa?",
                "Registrar Salida",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
            if (conf == JOptionPane.YES_OPTION) {
                String fechaHoy  = java.time.LocalDate.now().toString();
                String horaAhora = java.time.LocalTime.now()
                    .format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss"));
                asistenciaService.registrarSalida(idEmpleado, fechaHoy, horaAhora);
                JOptionPane.showMessageDialog(null, "Salida registrada. ¡Hasta luego!");
                System.exit(0);
            }
        } else {
            this.setVisible(false);
            if (sucursal != null) sucursal.setVisible(true);
            else this.dispose();
        }
    }//GEN-LAST:event_btnvolverActionPerformed

    private void txtbuscarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtbuscarKeyReleased
        buscarCliente();
    }//GEN-LAST:event_txtbuscarKeyReleased

    private void btnpagoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnpagoActionPerformed
        int fila = tabla.getSelectedRow();
        if (fila == -1) { JOptionPane.showMessageDialog(null, "Seleccione un cliente"); return; }
        int idCliente = Integer.parseInt(tabla.getValueAt(fila, 0).toString());
        String nombreCliente = tabla.getValueAt(fila, 1) + " " + tabla.getValueAt(fila, 2);
        Diseño.NavegacionUtil.navegar(btnpago, () -> {
            new FrmPagos(idCliente, nombreCliente, this, idEmpleado, true).setVisible(true);
            this.setVisible(false);
        });
    }//GEN-LAST:event_btnpagoActionPerformed

    private void txtbuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtbuscarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtbuscarActionPerformed

    private void btncerrarsesionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncerrarsesionActionPerformed
        this.dispose();
        new FrmInicio().setVisible(true);
    }//GEN-LAST:event_btncerrarsesionActionPerformed

    private void btnactualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnactualizarActionPerformed
        cargartabla();
        cargarTablaMembresias();
    }//GEN-LAST:event_btnactualizarActionPerformed

    private void jLabel10MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel10MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel10MouseEntered

    private void btnregistrarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnregistrarMouseEntered
        btnregistrar.setForeground(new Color(204,0,0));
    }//GEN-LAST:event_btnregistrarMouseEntered

    private void btnregistrarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnregistrarMouseExited
        btnregistrar.setForeground(new Color(255,255,255));
    }//GEN-LAST:event_btnregistrarMouseExited

    private void btncerrarsesionMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btncerrarsesionMouseEntered
        btncerrarsesion.setForeground(new Color(204,0,0));
    }//GEN-LAST:event_btncerrarsesionMouseEntered

    private void btncerrarsesionMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btncerrarsesionMouseExited
        btncerrarsesion.setForeground(new Color(255,255,255));
    }//GEN-LAST:event_btncerrarsesionMouseExited

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

    private void btnlimpiarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnlimpiarMouseEntered
        btnlimpiar.setForeground(new Color(204,0,0));
    }//GEN-LAST:event_btnlimpiarMouseEntered

    private void btnlimpiarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnlimpiarMouseExited
        btnlimpiar.setForeground(new Color(255,255,255));
    }//GEN-LAST:event_btnlimpiarMouseExited

    private void btnmembresiaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnmembresiaMouseEntered
        btnmembresia.setForeground(new Color(204,0,0));
    }//GEN-LAST:event_btnmembresiaMouseEntered

    private void btnmembresiaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnmembresiaMouseExited
        btnmembresia.setForeground(new Color(255,255,255));
    }//GEN-LAST:event_btnmembresiaMouseExited

    private void btnpagoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnpagoMouseEntered
        btnpago.setForeground(new Color(204,0,0));
    }//GEN-LAST:event_btnpagoMouseEntered

    private void btnpagoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnpagoMouseExited
        btnpago.setForeground(new Color(255,255,255));
    }//GEN-LAST:event_btnpagoMouseExited

    private void btnactualizarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnactualizarMouseEntered
        btnactualizar.setForeground(new Color(204,0,0));
    }//GEN-LAST:event_btnactualizarMouseEntered

    private void btnactualizarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnactualizarMouseExited
        btnactualizar.setForeground(new Color(255,255,255));
    }//GEN-LAST:event_btnactualizarMouseExited

    private void btnvolverMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnvolverMouseEntered
        btnvolver.setForeground(new Color(204,0,0));
    }//GEN-LAST:event_btnvolverMouseEntered

    private void btnvolverMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnvolverMouseExited
        btnvolver.setForeground(new Color(255,255,255));
    }//GEN-LAST:event_btnvolverMouseExited

    private void btnreporteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnreporteActionPerformed
        Diseño.ReporteDialogUtil.mostrarDialogoReporte(this, reporteService, idEmpleado);
    }//GEN-LAST:event_btnreporteActionPerformed

    private void cmbaccionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbaccionActionPerformed
         
   if (cmbaccion.getSelectedIndex() <= 0) return;

    String seleccion = cmbaccion.getSelectedItem().toString();
    Diseño.NavegacionUtil.navegar(cmbaccion, () -> {
    switch (seleccion) {

        case "EMPLEADOS":
            System.out.println(""+ idSucursalSeleccionada);
            new FrmEmpleados(this.sucursal, idSucursal,nombreSucursal, this.idEmpleado, cargoEmpleado, this).setVisible(true);
            //this.sucursal, idSucursalSeleccionada,nombreSucursal, this.idEmpleado, cargoEmpleado, this
            this.setVisible(false);
            break;
        case "INVENTARIO":
            System.out.println(""+ idSucursalSeleccionada);
            new FrmInventario(this.sucursal, idSucursalSeleccionada, nombreSucursal).setVisible(true);
            this.setVisible(false);
            break;
       /* case "REPORTES":
            new FrmVenta(this.sucursal, idSucursalSeleccionada,
                tabla.getValueAt(tabla.getSelectedRow(), 2).toString()).setVisible(true);
            this.setVisible(false);
            break;
        case "SERVICIOS":
            System.out.println(""+ idSucursalSeleccionada);
            new FrmServicio(this.sucursal, idSucursalSeleccionada,
            tabla.getValueAt(tabla.getSelectedRow(), 2).toString()).setVisible(true);
            this.setVisible(false);
            break;*/
    }
    });

    }//GEN-LAST:event_cmbaccionActionPerformed

    private void btnAvisoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAvisoActionPerformed
        JTextArea txtAviso = new JTextArea(8, 32);
        txtAviso.setLineWrap(true);
        txtAviso.setWrapStyleWord(true);

        try{
            avisoService.obtenerUltimoSucursal(this.idSucursal).ifPresent(a -> txtAviso.setText(a.mensaje()));
        }catch(Exception e){}
        int opcion= JOptionPane.showConfirmDialog(this, new JScrollPane(txtAviso), "Publicar anuncio para los clientes", 
                    JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (opcion != JOptionPane.OK_OPTION) return;
        
        String mensaje = txtAviso.getText().trim();
        if (mensaje.isEmpty()){
            JOptionPane.showMessageDialog(this, "El anuncio no puede estar vacio");
            return;
        }
        
        Resultado resultado = avisoService.publicarSupervisor(this.idSucursal, mensaje);
        JOptionPane.showMessageDialog(this, resultado.mensaje());
        ////////////
        //Resultado resultado = avisoService.publicar(idEmpleado, mensaje);
        //JOptionPane.showMessageDialog(this, resultado.mensaje());
    }//GEN-LAST:event_btnAvisoActionPerformed

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
            java.util.logging.Logger.getLogger(FrmClientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmClientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmClientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmClientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmClientes().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAviso;
    private javax.swing.JButton btnactualizar;
    private javax.swing.JButton btncerrarsesion;
    private javax.swing.JButton btneliminar;
    private javax.swing.JButton btnlimpiar;
    private javax.swing.JButton btnmembresia;
    private javax.swing.JButton btnmodificar;
    private javax.swing.JButton btnpago;
    private javax.swing.JButton btnregistrar;
    private javax.swing.JButton btnreporte;
    private javax.swing.JButton btnvolver;
    private javax.swing.JComboBox<String> cmbaccion;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
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
    private javax.swing.JLabel lblsede;
    private javax.swing.JTable tabla;
    private javax.swing.JTable tablacaduca;
    private javax.swing.JTextField txtapellido;
    private javax.swing.JTextField txtbuscar;
    private javax.swing.JTextField txtdni;
    private javax.swing.JTextField txtemail;
    private javax.swing.JTextField txtidcliente;
    private javax.swing.JTextField txtnombre;
    private javax.swing.JTextField txttelefono;
    // End of variables declaration//GEN-END:variables
}
