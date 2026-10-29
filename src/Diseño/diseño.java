package Diseño;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.TextArea;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JCheckBox;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.border.MatteBorder;

public class diseño {

    //==========================================
    // ESTILO DE TABLAS
    //==========================================
    public static void estilizarTabla(JTable tabla, JScrollPane scroll, int maxFilas) {

        // Fuente y colores
        tabla.setFont(new Font("Segoe UI", Font.BOLD | Font.ITALIC, 12));
        tabla.setBackground(new Color(102,102,102));
        tabla.setForeground(new Color(204,204,204));

        tabla.setSelectionBackground(new Color(51,51,51));
        tabla.setSelectionForeground(new Color(255,255,255));

        tabla.setGridColor(new Color(51,51,51));
        tabla.setBorder(new MatteBorder(0,0,0,0,Color.BLACK));

        tabla.setRowHeight(25);
        tabla.setShowGrid(true);
        tabla.setFillsViewportHeight(true);

        // Área vacía del JScrollPane
        scroll.getViewport().setBackground(new Color(102,102,102));

        //=========================
        // ENCABEZADO
        //=========================

        DefaultTableCellRenderer encabezado = new DefaultTableCellRenderer();

        encabezado.setOpaque(true);
        encabezado.setBackground(new Color(0,0,0));
        encabezado.setForeground(new Color(204,204,204));
        encabezado.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
        encabezado.setFont(new Font("Segoe UI", Font.BOLD | Font.ITALIC, 13));
        encabezado.setBorder(new MatteBorder(0,0,2,0,new Color(204,0,0)));

        tabla.getTableHeader().setDefaultRenderer(encabezado);
        tabla.getTableHeader().setReorderingAllowed(false);
        tabla.getTableHeader().setResizingAllowed(false);

        //=========================
        // AJUSTAR ALTURA
        //=========================

        int filas = tabla.getRowCount();
        int filasVisibles = Math.max(1, Math.min(filas, maxFilas));

        int alto = tabla.getTableHeader().getPreferredSize().height
                + (filasVisibles * tabla.getRowHeight());

        scroll.setBounds(
                scroll.getX(),
                scroll.getY(),
                scroll.getWidth(),
                alto
        );

        scroll.revalidate();
        scroll.repaint();
    }

    //==========================================
    // ESTILO BOTÓN
    //==========================================
    public static void estilizarBoton(JButton boton) {

        boton.setFont(new Font("Segoe UI", Font.BOLD | Font.ITALIC, 14));
        boton.setForeground(Color.WHITE);

        boton.setBorder(new MatteBorder(2,2,2,2,new Color(204,0,0)));

        boton.setContentAreaFilled(false);
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        Rectangle original = boton.getBounds();

        boton.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setFont(new Font("Segoe UI", Font.BOLD | Font.ITALIC, 16));
                boton.setForeground(new Color(204,0,0));
                boton.setBorder(new MatteBorder(3,3,3,3,new Color(204,0,0)));

                boton.setBounds(
                        original.x - 5,
                        original.y - 5,
                        original.width + 10,
                        original.height + 10
                );
            }

            @Override
            public void mouseExited(MouseEvent e) {

                boton.setForeground(Color.WHITE);
                boton.setBorder(new MatteBorder(1,1,1,1,new Color(204,0,0)));

                boton.setBounds(original);
            }

        });

 
        java.awt.event.ActionListener[] originales = boton.getActionListeners();
        for (java.awt.event.ActionListener al : originales) {
            boton.removeActionListener(al);
        }
        boton.addActionListener(evt -> {
            boton.setEnabled(false);
            try {
                for (java.awt.event.ActionListener al : originales) {
                    al.actionPerformed(evt);
                }
            } finally {
                boton.setEnabled(true);
            }
        });

    }

    //==========================================
    // VARIOS BOTONES
    //==========================================
    public static void estilizarBotones(JButton... botones) {

        for (JButton boton : botones) {
            estilizarBoton(boton);
        }

    }
    public static void estilizarTextPane(JTextPane textPane) {
        textPane.setBackground(new Color(51, 51, 51));
        textPane.setForeground(Color.WHITE);
        textPane.setCaretColor(Color.WHITE);
        textPane.setSelectionColor(new Color(153, 153, 153));
        textPane.setSelectedTextColor(Color.WHITE);
        textPane.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80)));
        textPane.setFont(new Font("Segoe UI", Font.PLAIN, 13));
    }
    public static void estilizarTextArea(JTextArea textArea) {
        textArea.setBackground(new Color(51, 51, 51));
        textArea.setForeground(Color.WHITE);
        textArea.setCaretColor(Color.WHITE);
        textArea.setSelectionColor(new Color(153, 153, 153));
        textArea.setSelectedTextColor(Color.WHITE);
        textArea.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80)));
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
    }
    public static void estilizarAreaText(TextArea textArea) {
        textArea.setBackground(new Color(51, 51, 51));
        textArea.setForeground(new Color(255,255,255));
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
    }
   public static void estilizarPanel(JPanel panel) {
        panel.setBackground(new Color(51, 51, 51));
        panel.setFont(new Font("Segoe UI", Font.BOLD | Font.ITALIC, 12));
        panel.setForeground(new Color(255,255,255));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(80, 80, 80), 1),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
    }
   public static void estilizarCheckbox(JCheckBox jcb) {
       jcb.setForeground(new Color(255,255,255));

   }

    }
