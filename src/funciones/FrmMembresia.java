package funciones;

import Diseño.diseño;
import PRINCIPAL.FrmInicio;
import fabrica.AppFactory;
import servicio.MembresiaService;
import servicio.MembresiaClienteService;
import servicio.ClaseService;
import servicio.Resultado;
import repositorio.MembresiaRepository.PlanActivoFila;
import repositorio.MembresiaRepository.ClaseActivaFila;
import repositorio.MembresiaRepository.MembresiaFila;
import java.util.*;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import java.awt.*;
import static java.awt.image.ImageObserver.ALLBITS;
import java.time.LocalDate;
import javax.swing.table.DefaultTableModel;

public class FrmMembresia extends javax.swing.JFrame {

    private final MembresiaService membresiaService = AppFactory.getInstance().membresiaService();
    // Reutiliza el mismo catálogo de profesores/mixtos que ya usa el
    // módulo de Clases (misma regla de negocio: solo PROFESOR o MIXTO).
    private final ClaseService claseService = AppFactory.getInstance().claseService();
    private LinkedHashMap<String, Integer> entrenadoresPorNombre = new LinkedHashMap<>();
    private static final String SIN_ENTRENADOR = "Sin asignar (opcional)";
    // Reutiliza el mismo recálculo VENCIDA/POR_VENCER ya centralizado para
    // el portal de cliente y el panel de alertas de FrmClientes (DRY).
    private final MembresiaClienteService membresiaClienteService = AppFactory.getInstance().membresiaClienteService();

    private FrmClientes frmClientes;
    private int idClienteSeleccionado;
    private int idSucursal;
    private int idEmpleado;
    private int idMembresiaSeleccionada = 0;
    private boolean membresiaSinPlanSeleccionada = false;
    private LinkedHashMap<Integer, JCheckBox> mapaClases = new LinkedHashMap<>();
    private LinkedHashMap<Integer, Double> mapaPreciosClase = new LinkedHashMap<>();

    private LinkedHashMap<Integer, Object[]> mapaPlanes = new LinkedHashMap<>();

    public FrmMembresia(int idClienteSeleccionado, String nombreClienteSeleccionado, FrmClientes frmClientes) {
        initComponents();
        setResizable(false);
        this.frmClientes           = frmClientes;
        this.idClienteSeleccionado = idClienteSeleccionado;
        this.idSucursal            = frmClientes.getIdSucursal();
        this.idEmpleado            = frmClientes.getIdEmpleado();
        setTitle("Membresías - " + frmClientes.getNombreSucursal());
        lblnombrecl.setText(nombreClienteSeleccionado);
        diseño.estilizarPanel(panelClases);
        txtidmembresia.setVisible(false);
        cargarCombosAnioMes();
        btnfechaActionPerformed(null);
        setSize(1540, 550);
        setLocationRelativeTo(null);
        cargarPlanes();
        //cmbplan.addActionListener(e -> actualizarPrecio());
        cargarClases();
        cargarEntrenadores();
        cmbplan.addActionListener(e -> actualizarVisibilidadEntrenador());
        actualizarVisibilidadEntrenador();

        diseño.estilizarTabla(tablamembresia, jScrollPane1, ALLBITS);
        cargartabla();
        diseño.estilizarBotones(btnmodificar, btnrenovar, btnsinplan, btncerrarsesion, btnregistrar, btnvolver, btnfecha);
        btnmodificar.setEnabled(false);
        btnrenovar.setEnabled(false);
        btncerrarsesion.setVisible(false);
        tablamembresia.getColumnModel().getColumn(0).setMinWidth(0);
        tablamembresia.getColumnModel().getColumn(0).setMaxWidth(0);
        tablamembresia.getColumnModel().getColumn(0).setWidth(0);
    }

    public FrmMembresia() { initComponents(); }

    // ── COMBOS AÑO/MES/DIA ──────────────────────────────────────────────────
    private void cargarCombosAnioMes() {
        int anioActual = LocalDate.now().getYear();
        for (int i = anioActual; i <= anioActual + 5; i++) cmbanio.addItem(String.valueOf(i));
        String[] meses = {"Enero","Febrero","Marzo","Abril","Mayo","Junio",
                          "Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre"};
        for (String m : meses) cmbmes.addItem(m);
        cmbanio.addActionListener(e -> cargarDias());
        cmbmes.addActionListener(e -> cargarDias());
        cargarDias();
    }

    private void cargarDias() {
        int anio = Integer.parseInt(cmbanio.getSelectedItem().toString());
        int mes  = cmbmes.getSelectedIndex() + 1;
        int diasEnMes = LocalDate.of(anio, mes, 1).lengthOfMonth();
        int diaActual = cmbdia.getSelectedItem() != null ?
                        Integer.parseInt(cmbdia.getSelectedItem().toString()) : 1;
        cmbdia.removeAllItems();
        for (int i = 1; i <= diasEnMes; i++) cmbdia.addItem(String.valueOf(i));
        if (diaActual <= diasEnMes) cmbdia.setSelectedItem(String.valueOf(diaActual));
    }

    private LocalDate getFechaInicio() {
        int anio = Integer.parseInt(cmbanio.getSelectedItem().toString());
        int mes  = cmbmes.getSelectedIndex() + 1;
        int dia  = Integer.parseInt(cmbdia.getSelectedItem().toString());
        return LocalDate.of(anio, mes, dia);
    }

    // ── ENTRENADOR ASIGNADO (opcional) ──────────────────────────────────────
    private void cargarEntrenadores() {
        try {
            entrenadoresPorNombre = claseService.obtenerProfesoresHabilitados(idSucursal);
            cmbentrenador.removeAllItems();
            cmbentrenador.addItem(SIN_ENTRENADOR);
            for (String nombre : entrenadoresPorNombre.keySet()) cmbentrenador.addItem(nombre);
        } catch (Exception e) { JOptionPane.showMessageDialog(null, e.toString()); }
    }

    /** null si no se asignó entrenador (selección válida, no es un error). */
    private Integer getIdEntrenadorSeleccionado() {
        if (cmbentrenador.getSelectedIndex() <= 0) return null;
        return entrenadoresPorNombre.get(cmbentrenador.getSelectedItem().toString());
    }
    private void seleccionarEntrenadorEnCombo(String nombreEntrenador) {
        if (nombreEntrenador == null || nombreEntrenador.isBlank()) {
            cmbentrenador.setSelectedIndex(0);
            return;
        }
        for (int i = 0; i < cmbentrenador.getItemCount(); i++) {
            if (cmbentrenador.getItemAt(i).startsWith(nombreEntrenador + "-")) {
                cmbentrenador.setSelectedIndex(i);
                return;
            }
        }
        cmbentrenador.setSelectedIndex(0);
    }

    /**
     * El combo de entrenador solo tiene sentido para planes de tipo
     * "PERSONALIZADO" (entrenamiento con un profesor/mixto dedicado); para
     * cualquier otro tipo de plan permanece oculto. Se llama después de
     * declarar getPlanSeleccionado() más abajo, pero como es un método de
     * instancia el orden de declaración no importa.
     */
    private void actualizarVisibilidadEntrenador() {
        Object[] plan = getPlanSeleccionado();
        boolean esPersonalizado = plan != null && "PERSONALIZADO".equalsIgnoreCase(String.valueOf(plan[3]));
        lblentrenador.setVisible(esPersonalizado);
        cmbentrenador.setVisible(esPersonalizado);
        if (!esPersonalizado) cmbentrenador.setSelectedIndex(0);
    }
    // ── CARGAR PLANES ────────────────────────────────────────────────────────
    private void cargarPlanes() {
        cmbplan.removeAllItems();
        mapaPlanes.clear();
        try {
            for (PlanActivoFila fila : membresiaService.obtenerPlanesActivos(idSucursal)) {
                String etiqueta = fila.nombre() + " - " +
                                    fila.duracionMeses() + " mes(es) - S/ " +
                                    String.format("%.2f", fila.precio())+ " - " +
                                    fila.tipoServicio();
                mapaPlanes.put(fila.idPlan(), new Object[]{fila.duracionMeses(), fila.precio(), etiqueta, fila.tipoServicio()});
                cmbplan.addItem(etiqueta);
            }
        } catch (Exception e) { JOptionPane.showMessageDialog(null, e.toString()); }
        actualizarPrecio();
    }

    private Object[] getPlanSeleccionado() {
        int idx = cmbplan.getSelectedIndex();
        if (idx < 0) return null;
        int i = 0;
        for (Map.Entry<Integer, Object[]> entrada : mapaPlanes.entrySet()) {
            if (i == idx) return new Object[]{entrada.getKey(),
                                              entrada.getValue()[0],
                                              entrada.getValue()[1],
                                              entrada.getValue()[3]};
            i++;
        }
        return null;
    }

    // ── CARGAR CLASES ────────────────────────────────────────────────────────
    private void cargarClases() {
        panelClases.removeAll();
        mapaClases.clear();
        mapaPreciosClase.clear();
        panelClases.setLayout(new GridLayout(0, 1, 4, 4));
        try {
            List<ClaseActivaFila> clases = membresiaService.obtenerClasesActivas(idSucursal);
            boolean hayClases = !clases.isEmpty();
            for (ClaseActivaFila fila : clases) {
                int idClase     = fila.idClase();
                double precioCL = fila.precio();
                int cupoCL      = fila.cupo();
                String etiqueta =
                        " | Clase: " + fila.nombre() +
                        " | Horario: " + fila.horario() +
                        " | Precio: S/ " + String.format("%.2f", precioCL) +
                        " | Cupos: " + cupoCL;
                JCheckBox chk = new JCheckBox(etiqueta);
                chk.setBackground(panelClases.getBackground());
                diseño.estilizarCheckbox(chk);
                if (cupoCL <= 0) {
                    chk.setText(etiqueta + "  [LLENO]");
                    chk.setEnabled(false);
                    chk.setForeground(Color.RED);
                }
                chk.addActionListener(e -> actualizarPrecio());
                mapaClases.put(idClase, chk);
                mapaPreciosClase.put(idClase, precioCL);
                panelClases.add(chk);
            }
            if (!hayClases) panelClases.add(new JLabel("No hay clases activas disponibles."));
        } catch (Exception e) { JOptionPane.showMessageDialog(null, e.toString()); }
        panelClases.revalidate();
        panelClases.repaint();
    }

    // ── CARGAR TABLA ─────────────────────────────────────────────────────────
    private void cargartabla() {
        // Primero actualizar estados vencidos automáticamente (misma operación
        // reutilizada del portal de cliente / panel de alertas).
        membresiaClienteService.actualizarEstadosVencidos();

        DefaultTableModel modelo = (DefaultTableModel) tablamembresia.getModel();
        modelo.setRowCount(0);
        try {
            for (MembresiaFila fila : membresiaService.listarPorCliente(idClienteSeleccionado)) {
                modelo.addRow(new Object[]{
                    fila.idMembresia(),
                    fila.nombrePlan() == null ? "Sin plan" : fila.nombrePlan(),
                    fila.fechaInicio(),
                    fila.fechaFin(),
                    fila.clases() != null ? fila.clases() : "Sin clases",
                    "S/ " + String.format("%.2f", fila.total()),
                    fila.estado(),
                    fila.entrenador() // dato adicional oculto (no es una columna declarada en el diseño); se lee por índice, no se muestra
                });
            }
        } catch (Exception e) { JOptionPane.showMessageDialog(null, e.toString()); }
    }

    // ── ACTUALIZAR PRECIO ────────────────────────────────────────────────────
    private void actualizarPrecio() {
        double total = 0.0;
        Object[] plan = getPlanSeleccionado();
        if (plan != null) total += (double) plan[2];
        for (Map.Entry<Integer, JCheckBox> entrada : mapaClases.entrySet()) {
            if (entrada.getValue().isSelected()) total += mapaPreciosClase.get(entrada.getKey());
        }
        lblprecio.setText("S/ " + String.format("%.2f", total));
    }

    // ── CLASES SELECCIONADAS ─────────────────────────────────────────────────
    private ArrayList<Integer> getIdsClasesSeleccionadas() {
        ArrayList<Integer> ids = new ArrayList<>();
        for (Map.Entry<Integer, JCheckBox> entrada : mapaClases.entrySet())
            if (entrada.getValue().isSelected()) ids.add(entrada.getKey());
        return ids;
    }

    private double getTotalClases(ArrayList<Integer> ids) {
        double total = 0.0;
        for (int id : ids) total += mapaPreciosClase.get(id);
        return total;
    }

    /** Mismo mapa de precios de clase, convertido a Float para los métodos de UTPsports/servicio (igual que el .floatValue() del original). */
    private Map<Integer, Float> mapaPreciosClaseComoFloat() {
        Map<Integer, Float> resultado = new LinkedHashMap<>();
        for (Map.Entry<Integer, Double> entrada : mapaPreciosClase.entrySet()) {
            resultado.put(entrada.getKey(), entrada.getValue().floatValue());
        }
        return resultado;
    }

    // ── LIMPIAR ──────────────────────────────────────────────────────────────
    private void limpiar() {
        cmbplan.setSelectedIndex(0);
        if (cmbentrenador != null) cmbentrenador.setSelectedIndex(0);
        for (JCheckBox chk : mapaClases.values()) chk.setSelected(false);
        idMembresiaSeleccionada = 0;
        membresiaSinPlanSeleccionada = false;
        btnmodificar.setEnabled(false);
        btnrenovar.setEnabled(false);
        btnregistrar.setEnabled(true);
        actualizarPrecio();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        cmbplan = new javax.swing.JComboBox<>();
        btnvolver = new javax.swing.JButton();
        btnregistrar = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        lblcliente = new javax.swing.JLabel();
        btnrenovar = new javax.swing.JButton();
        lblprecio = new javax.swing.JLabel();
        cmbdia = new javax.swing.JComboBox<>();
        cmbmes = new javax.swing.JComboBox<>();
        cmbanio = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        btnfecha = new javax.swing.JButton();
        txtidmembresia = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablamembresia = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        panelClases = new javax.swing.JPanel();
        btnmodificar = new javax.swing.JButton();
        lblnombrecl = new javax.swing.JLabel();
        btncerrarsesion = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        btnsinplan = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        lblentrenador = new javax.swing.JLabel();
        cmbentrenador = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();

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
        jScrollPane1.setViewportView(jTable1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("CLIENTE");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 70, 90, -1));

        cmbplan.setBackground(new java.awt.Color(51, 51, 51));
        cmbplan.setForeground(new java.awt.Color(255, 255, 255));
        cmbplan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbplan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbplanActionPerformed(evt);
            }
        });
        getContentPane().add(cmbplan, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 130, 409, -1));

        btnvolver.setText("VOLVER");
        btnvolver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnvolverActionPerformed(evt);
            }
        });
        getContentPane().add(btnvolver, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 480, 190, -1));

        btnregistrar.setText("REGISTRAR MEMBRESIA");
        btnregistrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnregistrarActionPerformed(evt);
            }
        });
        getContentPane().add(btnregistrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 440, 190, -1));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("PRECIO");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 270, 310, -1));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("PLAN");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 130, 310, -1));
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(607, 0, 197, -1));
        getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(1299, 133, 458, -1));
        getContentPane().add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(1757, 218, -1, -1));
        getContentPane().add(lblcliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(405, 0, 196, -1));

        btnrenovar.setText("RENOVAR MEMBRESIA");
        btnrenovar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnrenovarActionPerformed(evt);
            }
        });
        getContentPane().add(btnrenovar, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 440, 180, -1));

        lblprecio.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblprecio.setForeground(new java.awt.Color(255, 255, 255));
        lblprecio.setText("   ");
        getContentPane().add(lblprecio, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 270, 410, -1));

        cmbdia.setBackground(new java.awt.Color(51, 51, 51));
        cmbdia.setForeground(new java.awt.Color(255, 255, 255));
        cmbdia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbdiaActionPerformed(evt);
            }
        });
        getContentPane().add(cmbdia, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 220, 90, -1));

        cmbmes.setBackground(new java.awt.Color(51, 51, 51));
        cmbmes.setForeground(new java.awt.Color(255, 255, 255));
        cmbmes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbmesActionPerformed(evt);
            }
        });
        getContentPane().add(cmbmes, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 220, 80, -1));

        cmbanio.setBackground(new java.awt.Color(51, 51, 51));
        cmbanio.setForeground(new java.awt.Color(255, 255, 255));
        cmbanio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbanioActionPerformed(evt);
            }
        });
        getContentPane().add(cmbanio, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 220, 80, -1));

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("DIA:");
        getContentPane().add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 220, 43, -1));

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("MES:");
        getContentPane().add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 220, 43, -1));

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("AÑO:");
        getContentPane().add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 220, 43, -1));

        btnfecha.setText("FECHA DE REGISTRO");
        btnfecha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnfechaActionPerformed(evt);
            }
        });
        getContentPane().add(btnfecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 220, 310, -1));
        getContentPane().add(txtidmembresia, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 70, 71, -1));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 3, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("AGREGAR PLAN");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 100, -1, -1));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 3, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("AGREGAR CLASE");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 310, -1, -1));

        tablamembresia.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "PLAN", "INICIO", "CESE", "CLASES", "PRECIO TOTAL", "ESTADO"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablamembresia.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablamembresiaMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tablamembresia);

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 100, 730, 350));

        panelClases.setBackground(new java.awt.Color(51, 51, 51));
        panelClases.setForeground(new java.awt.Color(255, 255, 255));
        panelClases.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        javax.swing.GroupLayout panelClasesLayout = new javax.swing.GroupLayout(panelClases);
        panelClases.setLayout(panelClasesLayout);
        panelClasesLayout.setHorizontalGroup(
            panelClasesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 836, Short.MAX_VALUE)
        );
        panelClasesLayout.setVerticalGroup(
            panelClasesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 148, Short.MAX_VALUE)
        );

        jScrollPane3.setViewportView(panelClases);

        getContentPane().add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 340, 738, 80));

        btnmodificar.setText("MODIFICAR MEMBRESIA");
        btnmodificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnmodificarActionPerformed(evt);
            }
        });
        getContentPane().add(btnmodificar, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 440, 180, -1));

        lblnombrecl.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblnombrecl.setForeground(new java.awt.Color(255, 255, 255));
        lblnombrecl.setText("                ");
        getContentPane().add(lblnombrecl, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 70, 160, -1));

        btncerrarsesion.setText("CERRAR SESIÓN");
        btncerrarsesion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btncerrarsesionActionPerformed(evt);
            }
        });
        getContentPane().add(btncerrarsesion, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 480, 350, -1));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/LeonGymLogo.png"))); // NOI18N
        jLabel2.setText("PERSONALIZA TU MEMBRESÍA");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, 740, 60));

        btnsinplan.setText("REGISTRAR SIN PLAN");
        btnsinplan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnsinplanActionPerformed(evt);
            }
        });
        getContentPane().add(btnsinplan, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 440, 160, -1));

        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/fondo.png"))); // NOI18N
        getContentPane().add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(1010, 0, -1, -1));

        lblentrenador.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        lblentrenador.setForeground(new java.awt.Color(255, 255, 255));
        lblentrenador.setText("ENTRENADOR");
        getContentPane().add(lblentrenador, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 180, 320, -1));

        cmbentrenador.setBackground(new java.awt.Color(51, 51, 51));
        cmbentrenador.setForeground(new java.awt.Color(255, 255, 255));
        cmbentrenador.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        getContentPane().add(cmbentrenador, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 180, 410, -1));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/fondo.png"))); // NOI18N
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1020, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmbplanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbplanActionPerformed
    actualizarPrecio();      
    }//GEN-LAST:event_cmbplanActionPerformed

    private void btnrenovarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnrenovarActionPerformed
    if (idMembresiaSeleccionada == 0) {
        JOptionPane.showMessageDialog(null, "Seleccione una membresía.");
        return;
    }

    Object[] plan = getPlanSeleccionado();

    if (plan == null) {
        JOptionPane.showMessageDialog(null, "Seleccione un plan.");
        return;
    }

    int idPlan = (int) plan[0];
    int duracionMeses = (int) plan[1];
    double precioPlan = (double) plan[2];

    ArrayList<Integer> ids = getIdsClasesSeleccionadas();
    Map<Integer, Float> precios = mapaPreciosClaseComoFloat();

    Resultado resultado = membresiaService.renovar(idMembresiaSeleccionada, idClienteSeleccionado,
            idEmpleado, idSucursal, idPlan, duracionMeses, (float) precioPlan, ids, precios,
            getIdEntrenadorSeleccionado());

    JOptionPane.showMessageDialog(null, resultado.mensaje());
    if (resultado.exito()) {
        limpiar();
        cargartabla();
        cargarClases();
    }
    }//GEN-LAST:event_btnrenovarActionPerformed

    private void btnvolverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnvolverActionPerformed
        this.setVisible(false);
        frmClientes.setVisible(true);
    }//GEN-LAST:event_btnvolverActionPerformed

    private void cmbdiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbdiaActionPerformed

    }//GEN-LAST:event_cmbdiaActionPerformed

    private void cmbmesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbmesActionPerformed

    }//GEN-LAST:event_cmbmesActionPerformed

    private void cmbanioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbanioActionPerformed

    }//GEN-LAST:event_cmbanioActionPerformed

    private void btnregistrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnregistrarActionPerformed
        Object[] plan = getPlanSeleccionado();
        if (plan == null) { JOptionPane.showMessageDialog(null, "Seleccione un plan."); return; }
        int idPlan          = (int) plan[0];
        double precioPlan   = (double) plan[2];
        ArrayList<Integer> ids = getIdsClasesSeleccionadas();
        float totalVenta    = (float) (precioPlan + getTotalClases(ids));
        LocalDate fechaInicio = getFechaInicio();
        LocalDate fechaFin    = fechaInicio.plusMonths((int) plan[1]);

        Resultado resultado = membresiaService.registrar(idClienteSeleccionado, idEmpleado, idSucursal,
                idPlan, (float) precioPlan, ids, mapaPreciosClaseComoFloat(), totalVenta, fechaInicio, fechaFin,
                getIdEntrenadorSeleccionado());

        JOptionPane.showMessageDialog(null, resultado.mensaje());
        if (resultado.exito()) { limpiar(); cargartabla(); cargarClases(); }
    }//GEN-LAST:event_btnregistrarActionPerformed

    private void btnfechaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnfechaActionPerformed
        LocalDate hoy = LocalDate.now();
        cmbanio.setSelectedItem(String.valueOf(hoy.getYear()));
        cmbmes.setSelectedIndex(hoy.getMonthValue() - 1);
        cargarDias();
        cmbdia.setSelectedItem(String.valueOf(hoy.getDayOfMonth()));
    }//GEN-LAST:event_btnfechaActionPerformed

    private void btnmodificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnmodificarActionPerformed
        if (idMembresiaSeleccionada == 0) {
            JOptionPane.showMessageDialog(null, "Seleccione una membresía."); return;
        }

        ArrayList<Integer> idsNuevas = getIdsClasesSeleccionadas();
        LocalDate fechaInicio = getFechaInicio();
        Resultado resultado;

        if (membresiaSinPlanSeleccionada) {
            // No se le pide (ni se le asigna) un plan: se preserva tal cual
            // se vendió originalmente, solo con clases sueltas.
            float totalVenta = (float) getTotalClases(idsNuevas);
            resultado = membresiaService.modificarSinPlan(idMembresiaSeleccionada, fechaInicio, fechaInicio,
                    idsNuevas, mapaPreciosClaseComoFloat(), totalVenta, getIdEntrenadorSeleccionado());
        } else {
            Object[] plan = getPlanSeleccionado();
            if (plan == null) { JOptionPane.showMessageDialog(null, "Seleccione un plan."); return; }
            int idPlan          = (int) plan[0];
            int duracionMeses   = (int) plan[1];
            double precioPlan   = (double) plan[2];
            float totalVenta    = (float) (precioPlan + getTotalClases(idsNuevas));
            LocalDate fechaFin  = fechaInicio.plusMonths(duracionMeses);
            resultado = membresiaService.modificar(idMembresiaSeleccionada, idPlan, fechaInicio, fechaFin,
                    idsNuevas, mapaPreciosClaseComoFloat(), totalVenta, getIdEntrenadorSeleccionado());
        }

        JOptionPane.showMessageDialog(null, resultado.mensaje());
        if (resultado.exito()) { limpiar(); cargartabla(); cargarClases(); }
    }//GEN-LAST:event_btnmodificarActionPerformed

    private void tablamembresiaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablamembresiaMouseClicked
        int fila = tablamembresia.getSelectedRow();
        if (fila < 0) return;
        DefaultTableModel modelo = (DefaultTableModel) tablamembresia.getModel();
        idMembresiaSeleccionada = Integer.parseInt(modelo.getValueAt(fila, 0).toString());
        String estado      = modelo.getValueAt(fila, 6).toString();
        String nombrePlan  = modelo.getValueAt(fila, 1).toString();
        String fechaInicio = modelo.getValueAt(fila, 2).toString();

        // "Sin plan" es el texto que arma cargartabla() cuando la membresía
        // no tiene id_plan (se vendió solo con clases sueltas). Antes, si
        // no se encontraba ese texto entre los planes reales, el combo
        // simplemente se quedaba con lo que tuviera seleccionado de antes
        // -- y al modificar, esa selección vieja (de otra membresía) se
        // guardaba como si fuera el plan de ESTA membresía, convirtiéndola
        // en una membresía con plan sin que nadie lo pidiera.
        membresiaSinPlanSeleccionada = nombrePlan.equals("Sin plan");
        if (membresiaSinPlanSeleccionada) {
            cmbplan.setSelectedIndex(-1); // sin selección visible: cmbplan no tiene un ítem "ninguno"
        }

        LocalDate fi = LocalDate.parse(fechaInicio);
        cmbanio.setSelectedItem(String.valueOf(fi.getYear()));
        cmbmes.setSelectedIndex(fi.getMonthValue() - 1);
        cargarDias();
        cmbdia.setSelectedItem(String.valueOf(fi.getDayOfMonth()));

        if (!membresiaSinPlanSeleccionada) {
            for (Map.Entry<Integer, Object[]> entrada : mapaPlanes.entrySet()) {
                if (entrada.getValue()[2].toString().startsWith(nombrePlan)) {
                    int idx = new ArrayList<>(mapaPlanes.keySet()).indexOf(entrada.getKey());
                    cmbplan.setSelectedIndex(idx);
                    break;
                }
            }
        }

        for (JCheckBox chk : mapaClases.values()) chk.setSelected(false);

        try {
            for (int idCL : membresiaService.obtenerIdsClaseDeMembresia(idMembresiaSeleccionada)) {
                if (mapaClases.containsKey(idCL)) mapaClases.get(idCL).setSelected(true);
            }
        } catch (Exception e) { JOptionPane.showMessageDialog(null, e.toString()); }

        String entrenadorActual = (String) modelo.getValueAt(fila, 7);
        seleccionarEntrenadorEnCombo(entrenadorActual);

        actualizarPrecio();
        btnmodificar.setEnabled(true);
        btnrenovar.setEnabled(estado.equals("VENCIDA") || estado.equals("POR_VENCER"));
        btnregistrar.setEnabled(false);
    }//GEN-LAST:event_tablamembresiaMouseClicked

    private void btncerrarsesionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncerrarsesionActionPerformed
        this.dispose();
        new FrmInicio().setVisible(true);
    }//GEN-LAST:event_btncerrarsesionActionPerformed

    private void btnsinplanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnsinplanActionPerformed
        ArrayList<Integer> ids = getIdsClasesSeleccionadas();
            if (ids.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Seleccione al menos una clase."); return;
            }

            String fecha = getFechaInicio().toString();

            Resultado resultado = membresiaService.registrarSinPlan(idClienteSeleccionado, idEmpleado, idSucursal,
                    fecha, ids, mapaPreciosClaseComoFloat(), getIdEntrenadorSeleccionado());

            JOptionPane.showMessageDialog(null, resultado.mensaje());
            if (resultado.exito()) { limpiar(); cargartabla(); cargarClases(); }
    }//GEN-LAST:event_btnsinplanActionPerformed

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
            java.util.logging.Logger.getLogger(FrmMembresia.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmMembresia.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmMembresia.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmMembresia.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmMembresia().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btncerrarsesion;
    private javax.swing.JButton btnfecha;
    private javax.swing.JButton btnmodificar;
    private javax.swing.JButton btnregistrar;
    private javax.swing.JButton btnrenovar;
    private javax.swing.JButton btnsinplan;
    private javax.swing.JButton btnvolver;
    private javax.swing.JComboBox<String> cmbanio;
    private javax.swing.JComboBox<String> cmbdia;
    private javax.swing.JComboBox<String> cmbentrenador;
    private javax.swing.JComboBox<String> cmbmes;
    private javax.swing.JComboBox<String> cmbplan;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblcliente;
    private javax.swing.JLabel lblentrenador;
    private javax.swing.JLabel lblnombrecl;
    private javax.swing.JLabel lblprecio;
    private javax.swing.JPanel panelClases;
    private javax.swing.JTable tablamembresia;
    private javax.swing.JTextField txtidmembresia;
    // End of variables declaration//GEN-END:variables
}
