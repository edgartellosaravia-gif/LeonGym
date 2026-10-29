package PRINCIPAL;

import Diseño.diseño;
import fabrica.AppFactory;
import servicio.AutenticacionService;
import servicio.MembresiaClienteService;
import repositorio.AutenticacionRepository.EmpleadoLogin;
import repositorio.AutenticacionRepository.ClienteLogin;

import java.util.Optional;

import javax.swing.JOptionPane;
import java.awt.Color;

public class FrmInicio extends javax.swing.JFrame {
    private final AutenticacionService autenticacionService = AppFactory.getInstance().autenticacionService();
    // Recalcula VENCIDA/POR_VENCER en cada login (antes solo se recalculaba
    // al abrir ciertas pantallas específicas; con esto, cualquier inicio de
    // sesión -- de cualquier rol -- mantiene los estados al día).
    private final MembresiaClienteService membresiaClienteService = AppFactory.getInstance().membresiaClienteService();
    int idSucursal=0;
    int idEmpleado=0;
    public FrmInicio() {
        initComponents();
        btninicio.setContentAreaFilled(false);
        setSize(500, 380);
        getContentPane().setBackground(new Color(36,39,38));
        setResizable(false);
        diseño.estilizarBoton(btninicio);
        setLocationRelativeTo(null);
        lblemp.setVisible(false);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtdni = new javax.swing.JTextField();
        btninicio = new javax.swing.JButton();
        txtcontrasenia = new javax.swing.JPasswordField();
        lblemp = new javax.swing.JLabel();
        lblempleado = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(0, 0, 0));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("USUARIO");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 140, 95, -1));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("CONTRASEÑA");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 180, 110, 20));

        txtdni.setBackground(new java.awt.Color(51, 51, 51));
        txtdni.setForeground(new java.awt.Color(255, 255, 255));
        getContentPane().add(txtdni, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 140, 200, -1));

        btninicio.setFont(new java.awt.Font("Segoe UI", 3, 18)); // NOI18N
        btninicio.setForeground(new java.awt.Color(255, 255, 255));
        btninicio.setText("INGRESAR");
        btninicio.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(204, 0, 0)));
        btninicio.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btninicioMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btninicioMouseExited(evt);
            }
        });
        btninicio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btninicioActionPerformed(evt);
            }
        });
        getContentPane().add(btninicio, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 230, 110, -1));

        txtcontrasenia.setBackground(new java.awt.Color(51, 51, 51));
        txtcontrasenia.setForeground(new java.awt.Color(255, 255, 255));
        txtcontrasenia.setText("jPasswordField1");
        getContentPane().add(txtcontrasenia, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 180, 200, -1));

        lblemp.setText("EMPLEADO");
        getContentPane().add(lblemp, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 250, 37, -1));

        lblempleado.setText("   ");
        getContentPane().add(lblempleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(175, 1240, 153, -1));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 3, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/LeonGymLogo.png"))); // NOI18N
        jLabel4.setText("INICIO DE SESIÓN");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 42, 430, -1));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/fondo.png"))); // NOI18N
        jLabel1.setText("jLabel1");
        jLabel1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(-190, 0, 730, 400));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btninicioMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btninicioMouseEntered
        btninicio.setForeground(new Color(204,0,0));
    }//GEN-LAST:event_btninicioMouseEntered

    private void btninicioMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btninicioMouseExited
        btninicio.setForeground(new Color(255,255,255));
    }//GEN-LAST:event_btninicioMouseExited

    private void btninicioActionPerformed(java.awt.event.ActionEvent evt) {
    String dni        = txtdni.getText().trim();
    String contrasenia = String.valueOf(txtcontrasenia.getPassword()).trim();

    if (dni.isEmpty() || contrasenia.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Ingrese su DNI y contraseña."); return;
    }

    btninicio.setEnabled(false);
    btninicio.setText("Conectando...");

    new javax.swing.SwingWorker<Void, Void>() {
        String nombre, apellido, cargo, nombreSede = "";
        int idEmpleado, idSucursal, idCliente;
        boolean esEmpleado = false, esCliente = false;

        @Override
        protected Void doInBackground() throws Exception {
            // Login empleado
            Optional<EmpleadoLogin> login = autenticacionService.autenticarEmpleado(dni, contrasenia);
            if (login.isPresent()) {
                EmpleadoLogin datos = login.get();
                esEmpleado  = true;
                idEmpleado  = datos.idEmpleado();
                idSucursal  = datos.idSucursal();
                nombre      = datos.nombre();
                apellido    = datos.apellido();
                cargo       = datos.cargo();
                nombreSede  = datos.nombreSede();
                return null;
            }
            // Login cliente
            Optional<ClienteLogin> loginCliente = autenticacionService.autenticarCliente(dni);
            if (loginCliente.isPresent()) {
                ClienteLogin datosCliente = loginCliente.get();
                esCliente  = true;
                idCliente  = datosCliente.idCliente();
                idSucursal = datosCliente.idSucursal();
                nombre     = datosCliente.nombre();
                apellido   = datosCliente.apellido();
            }
            return null;
        }

        @Override
        protected void done() {
            btninicio.setEnabled(true);
            btninicio.setText("INGRESAR");
            try {
                get();
                if (esEmpleado || esCliente) {
                    membresiaClienteService.actualizarEstadosVencidos();
                }
                if (esEmpleado) {
                    JOptionPane.showMessageDialog(null, "Bienvenido " + nombre + " " + apellido + " - " + cargo);
                    String cu = java.text.Normalizer.normalize(cargo.toUpperCase().trim(),
                        java.text.Normalizer.Form.NFD).replaceAll("[\\p{InCombiningDiacriticalMarks}]","");
                    if (cu.contains("ADMIN")) {
                        btninicio.setEnabled(false);
                        new PRINCIPAL(idEmpleado, idSucursal, nombre, cargo).setVisible(true);
                        dispose();
                    } else if (cu.contains("RECEP") || cu.contains("SUPER")) {
                        autenticacionService.registrarIngresoAsistencia(idEmpleado,
                            java.time.LocalDate.now().toString(),
                            java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")));
                        btninicio.setEnabled(false);
                        new funciones.FrmClientes(null, idSucursal, idEmpleado, nombreSede, cargo).setVisible(true);
                        dispose();
                    } else if (cu.contains("PROFE") || cu.contains("ENTREN") || cu.contains("MIX")) {
                        btninicio.setEnabled(false);
                        new funciones.FrmAsistencia(idEmpleado, nombre+" "+apellido, cargo).setVisible(true);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(null, "Cargo ["+cargo+"] sin acceso configurado.");
                    }
                } else if (esCliente) {
                    JOptionPane.showMessageDialog(null, "Bienvenido cliente: "+nombre+" "+apellido);
                    btninicio.setEnabled(false);
                    new funciones.FrmMembresiaCliente(idCliente, nombre+" "+apellido, idSucursal, 0).setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "DNI o contraseña incorrectos.");
                }
            } catch (Exception e) { JOptionPane.showMessageDialog(null, e.toString()); }
        }
    }.execute();
    }

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info :
                    javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    javax.swing.UIManager.put("TabbedPane.selected", java.awt.Color.BLACK);
                    break;
                }
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(FrmInicio.class.getName())
                    .log(java.util.logging.Level.SEVERE, null, ex);
        }
        java.awt.EventQueue.invokeLater(() -> new FrmInicio().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btninicio;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel lblemp;
    private javax.swing.JLabel lblempleado;
    private javax.swing.JPasswordField txtcontrasenia;
    private javax.swing.JTextField txtdni;
    // End of variables declaration//GEN-END:variables
}
