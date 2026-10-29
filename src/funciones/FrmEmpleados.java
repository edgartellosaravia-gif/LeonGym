
package funciones;

import Diseño.diseño;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import fabrica.AppFactory;
import modelo.CargoEmpleado;
import servicio.EmpleadoService;
import servicio.Resultado;
import repositorio.EmpleadoRepository.EmpleadoFila;
import funciones.FrmSucursal;
import funciones.FrmClientes;
import java.awt.Color;
import static java.awt.image.ImageObserver.ALLBITS;

public class FrmEmpleados extends javax.swing.JFrame {

    private final EmpleadoService empleadoService = AppFactory.getInstance().empleadoService();

    FrmSucursal sucursal;
    FrmClientes frmClientes;
    int idEmpleado;
    String cargoEmpleado;
    int idSucursal;
    String nombreSucursal;
    int idEmpleadoSeleccionado = 0;

    public FrmEmpleados(FrmSucursal sucursal,int idSucursal,String nombreSucursal) {
        initComponents();
        setResizable(false);
        this.sucursal = sucursal;
        this.idSucursal = idSucursal;
        this.nombreSucursal = nombreSucursal;
        setResizable(false);
        setSize(1006, 520);
        setLocationRelativeTo(null);
        diseño.estilizarBotones(btnregistrar, btnmodificar, btnlimpiar, btneliminar,
                      btnasistencia, btnvolver, btnreporte);
        txtidempleado.setVisible(false);
        lblsucursal.setText(nombreSucursal);
        diseño.estilizarTabla(tablaempleado, jScrollPane1, ALLBITS);
        tablaempleado.getColumnModel().getColumn(0).setMinWidth(0);
        tablaempleado.getColumnModel().getColumn(0).setMaxWidth(0);
        tablaempleado.getColumnModel().getColumn(0).setWidth(0);
        cargarCargosSupervisor();
        cmbcargo.addActionListener(e -> actualizarEtiquetaSueldo());
        actualizarEtiquetaSueldo();
        cargartablaSupervisor();
    }
    
    public FrmEmpleados(FrmSucursal sucursal,int idSucursal,String nombreSucursal, int idEmpleado, String cargo, FrmClientes frmClientes) {
        initComponents();
        setResizable(false);
        this.sucursal = sucursal;
        this.idSucursal = idSucursal;
        this.nombreSucursal = nombreSucursal;
        this.idEmpleado = idEmpleado;
        this.cargoEmpleado = cargo;
        this.frmClientes = frmClientes;
        setResizable(false);
        setSize(1006, 520);
        setLocationRelativeTo(null);
        diseño.estilizarBotones(btnregistrar, btnmodificar, btnlimpiar, btneliminar,
                      btnasistencia, btnvolver, btnreporte);
        txtidempleado.setVisible(false);
        lblsucursal.setText(nombreSucursal);
        diseño.estilizarTabla(tablaempleado, jScrollPane1, ALLBITS);
        tablaempleado.getColumnModel().getColumn(0).setMinWidth(0);
        tablaempleado.getColumnModel().getColumn(0).setMaxWidth(0);
        tablaempleado.getColumnModel().getColumn(0).setWidth(0);
        cargarCargos();
        cmbcargo.addActionListener(e -> actualizarEtiquetaSueldo());
        actualizarEtiquetaSueldo();
        cargartabla();
    }
    

    public FrmEmpleados() {
        initComponents();
        setResizable(false);
        cargarCargos();
        cmbcargo.addActionListener(e -> actualizarEtiquetaSueldo());
        actualizarEtiquetaSueldo();
    }

    private void cargarCargos() {
        try {
            cmbcargo.removeAllItems();
            for (CargoEmpleado cargo : empleadoService.obtenerCargosDisponibles()) {
                cmbcargo.addItem(cargo.getValorBD());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"Error al cargar cargos: "+ e.getMessage());
        }
    }

    private void cargarCargosSupervisor() {
        try {
            cmbcargo.removeAllItems();
            for (CargoEmpleado cargo : empleadoService.obtenerCargosDisponiblesSupervisor()) {
                cmbcargo.addItem(cargo.getValorBD());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"Error al cargar cargos: "+ e.getMessage());
        }
    }


    /**
     * Actualiza la etiqueta "SUELDO (POR CLASE)" / "SUELDO MENSUAL" según el
     * cargo actualmente seleccionado en {@code cmbcargo}.
     * <p>
     * CORRECCIÓN DE BUG: en el código original esta etiqueta se calculaba
     * una sola vez al cargar el formulario comparando el ENUM completo de la
     * BD contra el literal "PROFESOR" (comparación que nunca era verdadera),
     * y {@code cmbcargo} no tenía listener alguno, por lo que la etiqueta
     * quedaba siempre en "SUELDO MENSUAL" sin importar el cargo elegido.
     * Ahora se reevalúa cada vez que cambia la selección, aplicando la regla
     * de negocio PROFESOR/MIXTO = pago por clase (ver {@link CargoEmpleado}).
     */
    private void actualizarEtiquetaSueldo() {
        Object seleccionado = cmbcargo.getSelectedItem();
        if (seleccionado == null) return;
        try {
            CargoEmpleado cargo = CargoEmpleado.desdeValorBD(seleccionado.toString());
            lblsueldo.setText(empleadoService.etiquetaSueldoPara(cargo));
        } catch (IllegalArgumentException ignored) {
            // Selección transitoria no reconocida (p.ej. combo vacío momentáneamente); se ignora.
        }
    }

    private void cargartabla() {
        DefaultTableModel modelo =(DefaultTableModel)tablaempleado.getModel();
        modelo.setRowCount(0);
        try {
            for (EmpleadoFila fila : empleadoService.listarPorSucursal(idSucursal)) {
                agregarFilaATabla(modelo, fila);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,e.toString());
        }
    }

    private void cargartablaSupervisor() {
        DefaultTableModel modelo =(DefaultTableModel)tablaempleado.getModel();
        modelo.setRowCount(0);
        try {
            for (EmpleadoFila fila : empleadoService.listarPorSucursalSupervisor(idSucursal)) {               
                agregarFilaATabla(modelo, fila);}
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,e.toString());
        }
    }

    private void agregarFilaATabla(DefaultTableModel modelo, EmpleadoFila fila) {
        modelo.addRow(new Object[] {
            fila.idEmpleado(), fila.nombre(), fila.apellido(), fila.dni(),
            fila.cargo().getValorBD(), fila.telefono(), fila.sueldo()
        });
    }

    private void buscarEmpleado() {
        try {
            String buscar = txtbuscar.getText().trim();
            DefaultTableModel modelo = (DefaultTableModel) tablaempleado.getModel();
            modelo.setRowCount(0);
            for (EmpleadoFila fila : empleadoService.buscarPorDni(idSucursal, buscar)) {
                agregarFilaATabla(modelo, fila);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,e.toString());
        }
    }

    private void limpiar() {

        txtidempleado.setText("");
        txtnombreem.setText("");
        txtapellidoem.setText("");
        txtdniem.setText("");
        txttelefono.setText("");
        txtsueldo.setText("");
        cmbcargo.setSelectedIndex(0);
        tablaempleado.clearSelection();
        idEmpleadoSeleccionado = 0;
        btnregistrar.setEnabled(true);
        txtcontrasenia.setText("*************");
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tablaempleado = new javax.swing.JTable();
        txtidempleado = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        lblsucursal = new javax.swing.JLabel();
        txtbuscar = new javax.swing.JTextField();
        txtnombreem = new javax.swing.JTextField();
        txtapellidoem = new javax.swing.JTextField();
        txtdniem = new javax.swing.JTextField();
        cmbcargo = new javax.swing.JComboBox<>();
        txttelefono = new javax.swing.JTextField();
        txtsueldo = new javax.swing.JTextField();
        btnregistrar = new javax.swing.JButton();
        btnmodificar = new javax.swing.JButton();
        btnlimpiar = new javax.swing.JButton();
        btneliminar = new javax.swing.JButton();
        btnvolver = new javax.swing.JButton();
        btnasistencia = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        lblsueldo = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtcontrasenia = new javax.swing.JPasswordField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        btnreporte = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tablaempleado.setBackground(new java.awt.Color(102, 102, 102));
        tablaempleado.setBorder(new javax.swing.border.MatteBorder(null));
        tablaempleado.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        tablaempleado.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "NOMBRE", "APELLIDO", "DNI", "CARGO", "TELEFONO", "SUELDO"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaempleado.setGridColor(new java.awt.Color(51, 51, 51));
        tablaempleado.setSelectionBackground(new java.awt.Color(153, 153, 153));
        tablaempleado.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaempleadoMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablaempleado);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(326, 102, 657, 275));
        getContentPane().add(txtidempleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(446, 40, 71, -1));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("SUCURSAL");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 81, -1, -1));

        lblsucursal.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblsucursal.setForeground(new java.awt.Color(255, 255, 255));
        lblsucursal.setText(" ");
        getContentPane().add(lblsucursal, new org.netbeans.lib.awtextra.AbsoluteConstraints(136, 81, 172, -1));

        txtbuscar.setBackground(new java.awt.Color(51, 51, 51));
        txtbuscar.setForeground(new java.awt.Color(255, 255, 255));
        txtbuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtbuscarKeyReleased(evt);
            }
        });
        getContentPane().add(txtbuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 70, 352, -1));

        txtnombreem.setBackground(new java.awt.Color(51, 51, 51));
        txtnombreem.setForeground(new java.awt.Color(255, 255, 255));
        getContentPane().add(txtnombreem, new org.netbeans.lib.awtextra.AbsoluteConstraints(136, 115, 172, -1));

        txtapellidoem.setBackground(new java.awt.Color(51, 51, 51));
        txtapellidoem.setForeground(new java.awt.Color(255, 255, 255));
        txtapellidoem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtapellidoemActionPerformed(evt);
            }
        });
        getContentPane().add(txtapellidoem, new org.netbeans.lib.awtextra.AbsoluteConstraints(136, 155, 172, -1));

        txtdniem.setBackground(new java.awt.Color(51, 51, 51));
        txtdniem.setForeground(new java.awt.Color(255, 255, 255));
        getContentPane().add(txtdniem, new org.netbeans.lib.awtextra.AbsoluteConstraints(136, 195, 172, -1));

        cmbcargo.setBackground(new java.awt.Color(51, 51, 51));
        cmbcargo.setForeground(new java.awt.Color(255, 255, 255));
        cmbcargo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbcargo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbcargoActionPerformed(evt);
            }
        });
        getContentPane().add(cmbcargo, new org.netbeans.lib.awtextra.AbsoluteConstraints(136, 235, 172, -1));

        txttelefono.setBackground(new java.awt.Color(51, 51, 51));
        txttelefono.setForeground(new java.awt.Color(255, 255, 255));
        getContentPane().add(txttelefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(136, 275, 172, -1));

        txtsueldo.setBackground(new java.awt.Color(51, 51, 51));
        txtsueldo.setForeground(new java.awt.Color(255, 255, 255));
        getContentPane().add(txtsueldo, new org.netbeans.lib.awtextra.AbsoluteConstraints(136, 315, 172, -1));

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
        getContentPane().add(btnregistrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(32, 395, 143, -1));

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
        getContentPane().add(btnmodificar, new org.netbeans.lib.awtextra.AbsoluteConstraints(181, 395, 143, -1));

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
        getContentPane().add(btnlimpiar, new org.netbeans.lib.awtextra.AbsoluteConstraints(32, 428, 143, -1));

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
        getContentPane().add(btneliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 395, 143, -1));

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
        getContentPane().add(btnvolver, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 428, 143, -1));

        btnasistencia.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        btnasistencia.setForeground(new java.awt.Color(255, 255, 255));
        btnasistencia.setText("ASISTENCIA");
        btnasistencia.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(204, 0, 0)));
        btnasistencia.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnasistenciaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnasistenciaMouseExited(evt);
            }
        });
        btnasistencia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnasistenciaActionPerformed(evt);
            }
        });
        getContentPane().add(btnasistencia, new org.netbeans.lib.awtextra.AbsoluteConstraints(181, 428, 143, -1));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("NOMBRE");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 118, 115, -1));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("APELLIDO");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 158, 115, -1));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("DNI");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 198, 115, -1));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("CARGO");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 238, 115, -1));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("CELULAR");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 278, 115, -1));

        lblsueldo.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        lblsueldo.setForeground(new java.awt.Color(255, 255, 255));
        lblsueldo.setText("SUELDO (MENSUAL)");
        getContentPane().add(lblsueldo, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 318, -1, -1));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("CONTRASEÑA");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 358, 115, -1));

        txtcontrasenia.setBackground(new java.awt.Color(51, 51, 51));
        txtcontrasenia.setForeground(new java.awt.Color(255, 255, 255));
        txtcontrasenia.setText("jPasswordField1");
        txtcontrasenia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtcontraseniaActionPerformed(evt);
            }
        });
        getContentPane().add(txtcontrasenia, new org.netbeans.lib.awtextra.AbsoluteConstraints(136, 355, 172, -1));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("BUSCAR EMPLEADO (DNI)");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(326, 68, 220, -1));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/LeonGymLogo.png"))); // NOI18N
        jLabel9.setText("EMPLEADOS");
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 6, 310, -1));

        btnreporte.setText("VER REPORTES");
        btnreporte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnreporteActionPerformed(evt);
            }
        });
        getContentPane().add(btnreporte, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 410, 140, -1));

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/fondo.png"))); // NOI18N
        getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1030, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tablaempleadoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaempleadoMouseClicked
        int fila =
            tablaempleado.getSelectedRow();
        if (fila >= 0) {
            try {
                int id = Integer.parseInt(tablaempleado.getValueAt(fila, 0).toString());
                EmpleadoFila empleado = empleadoService.obtenerPorId(id);
                if (empleado != null) {
                    txtidempleado.setText(String.valueOf(id));
                    idEmpleadoSeleccionado = id;
                    txtnombreem.setText(empleado.nombre());
                    txtapellidoem.setText(empleado.apellido());
                    txtdniem.setText(empleado.dni());
                    cmbcargo.setSelectedItem(empleado.cargo().getValorBD());
                    txttelefono.setText(empleado.telefono());
                    txtsueldo.setText(String.format(java.util.Locale.ROOT, "%.2f", empleado.sueldo()));
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,e.toString());
            }
            btnregistrar.setEnabled(false);
        }
    }//GEN-LAST:event_tablaempleadoMouseClicked

    private void btnregistrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnregistrarActionPerformed
        String contrasenia =
                String.valueOf(txtcontrasenia.getPassword()).trim();

        if (txtnombreem.getText().isEmpty()
                || txtapellidoem.getText().isEmpty()
                || txtdniem.getText().isEmpty()
                || cmbcargo.getSelectedItem() == null
                || txttelefono.getText().isEmpty()
                || txtsueldo.getText().isEmpty()
                || contrasenia.isEmpty()) {

            JOptionPane.showMessageDialog(null,
                    "Complete todos los campos");
            return;
        }

        try {
            CargoEmpleado cargo = CargoEmpleado.desdeValorBD(cmbcargo.getSelectedItem().toString());
            Resultado resultado = empleadoService.registrar(
                    idSucursal,
                    txtnombreem.getText(),
                    txtapellidoem.getText(),
                    txtdniem.getText(),
                    cargo,
                    txttelefono.getText(),
                    Float.parseFloat(txtsueldo.getText()),
                    contrasenia
            );
            JOptionPane.showMessageDialog(null, resultado.mensaje());
            if (resultado.exito()) { limpiar(); cargartabla(); }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.toString());
        }
    }//GEN-LAST:event_btnregistrarActionPerformed
    
    private void btnlimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnlimpiarActionPerformed
        limpiar();
    }//GEN-LAST:event_btnlimpiarActionPerformed

    private void btnmodificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnmodificarActionPerformed
        if (idEmpleadoSeleccionado == 0) {

            JOptionPane.showMessageDialog(null,"Seleccione un empleado de la tabla");
            return;
        }

        try {
            CargoEmpleado cargo = CargoEmpleado.desdeValorBD(cmbcargo.getSelectedItem().toString());
            String nuevaContrasenia = String.valueOf(txtcontrasenia.getPassword()).trim();
            Resultado resultado = empleadoService.actualizar(
                    idEmpleadoSeleccionado,
                    idSucursal,
                    txtnombreem.getText(),
                    txtapellidoem.getText(),
                    txtdniem.getText(),
                    cargo,
                    txttelefono.getText(),
                    Float.parseFloat(txtsueldo.getText()),
                    nuevaContrasenia
            );
            JOptionPane.showMessageDialog(null, resultado.mensaje());
            if (resultado.exito()) { limpiar(); cargartabla(); }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,e.toString());
        }       
    }//GEN-LAST:event_btnmodificarActionPerformed

    private void btneliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btneliminarActionPerformed
        if (idEmpleadoSeleccionado == 0) {
        JOptionPane.showMessageDialog(null,"Seleccione un empleado de la tabla");
        return;
    }

    int conf =JOptionPane.showConfirmDialog(null,"¿Eliminar empleado?","Confirmar",JOptionPane.YES_NO_OPTION);

    if (conf == JOptionPane.YES_OPTION) {

        try {
            Resultado resultado = empleadoService.eliminar(idEmpleadoSeleccionado);
            JOptionPane.showMessageDialog(null, resultado.mensaje());
            if (resultado.exito()) { limpiar(); cargartabla(); }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,e.toString());
        }
    }
    }//GEN-LAST:event_btneliminarActionPerformed

    private void btnvolverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnvolverActionPerformed
        String cargo = (this.cargoEmpleado != null) ? this.cargoEmpleado : "";

        String cargoUpper = java.text.Normalizer
            .normalize(cargo.toUpperCase().trim(), java.text.Normalizer.Form.NFD)
            .replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");

        if (cargoUpper.contains("SUPER")) {
            if (this.frmClientes != null) {
                this.frmClientes.setVisible(true);
            } else {
                new FrmClientes().setVisible(true);
            }
        } else {
            if (this.sucursal != null) {
                this.sucursal.setVisible(true);
            } else {
                new FrmSucursal().setVisible(true);
            }
        }
        this.dispose();       
    }//GEN-LAST:event_btnvolverActionPerformed

    private void btnasistenciaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnasistenciaActionPerformed
        Diseño.NavegacionUtil.navegar(btnasistencia, () -> {
            new FrmAsistencia(this, idSucursal).setVisible(true);
            this.setVisible(false);
        });
    }//GEN-LAST:event_btnasistenciaActionPerformed

    private void txtbuscarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtbuscarKeyReleased
        buscarEmpleado();
    }//GEN-LAST:event_txtbuscarKeyReleased

    private void txtapellidoemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtapellidoemActionPerformed

    }//GEN-LAST:event_txtapellidoemActionPerformed

    private void txtcontraseniaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtcontraseniaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtcontraseniaActionPerformed

    private void btnregistrarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnregistrarMouseEntered
        btnregistrar.setForeground(new Color(204,0,0));
    }//GEN-LAST:event_btnregistrarMouseEntered

    private void btnregistrarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnregistrarMouseExited
        btnregistrar.setForeground(new Color(255,255,255));
    }//GEN-LAST:event_btnregistrarMouseExited

    private void btnmodificarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnmodificarMouseEntered
        btnmodificar.setForeground(new Color(204,0,0));// TODO add your handling code here:
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

    private void btnasistenciaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnasistenciaMouseEntered
        btnasistencia.setForeground(new Color(204,0,0));
    }//GEN-LAST:event_btnasistenciaMouseEntered

    private void btnasistenciaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnasistenciaMouseExited
        btnasistencia.setForeground(new Color(255,255,255));
    }//GEN-LAST:event_btnasistenciaMouseExited

    private void btnvolverMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnvolverMouseEntered
        btnvolver.setForeground(new Color(204,0,0));
    }//GEN-LAST:event_btnvolverMouseEntered

    private void btnvolverMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnvolverMouseExited
        btnvolver.setForeground(new Color(255,255,255));
    }//GEN-LAST:event_btnvolverMouseExited

    private void btnreporteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnreporteActionPerformed
        new DlgReportes(this, idSucursal).setVisible(true);
    }//GEN-LAST:event_btnreporteActionPerformed

    private void cmbcargoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbcargoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbcargoActionPerformed

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
            java.util.logging.Logger.getLogger(FrmEmpleados.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmEmpleados.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmEmpleados.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmEmpleados.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmEmpleados().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnasistencia;
    private javax.swing.JButton btneliminar;
    private javax.swing.JButton btnlimpiar;
    private javax.swing.JButton btnmodificar;
    private javax.swing.JButton btnregistrar;
    private javax.swing.JButton btnreporte;
    private javax.swing.JButton btnvolver;
    private javax.swing.JComboBox<String> cmbcargo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblsucursal;
    private javax.swing.JLabel lblsueldo;
    private javax.swing.JTable tablaempleado;
    private javax.swing.JTextField txtapellidoem;
    private javax.swing.JTextField txtbuscar;
    private javax.swing.JPasswordField txtcontrasenia;
    private javax.swing.JTextField txtdniem;
    private javax.swing.JTextField txtidempleado;
    private javax.swing.JTextField txtnombreem;
    private javax.swing.JTextField txtsueldo;
    private javax.swing.JTextField txttelefono;
    // End of variables declaration//GEN-END:variables
}
