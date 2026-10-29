package Diseño;

import javax.swing.JComponent;


public final class NavegacionUtil {

    private NavegacionUtil() {
    }

    
    public static void navegar(JComponent control, Runnable accionDeNavegacion) {
        if (control != null) control.setEnabled(false);
        try {
            accionDeNavegacion.run();
        } finally {
            if (control != null) control.setEnabled(true);
        }
    }
    public static void evitarDobleClic(JComponent control, Runnable accion) {
        navegar(control, accion);
    }
}
