package modelo;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;


public enum CargoEmpleado {

    ENTRENADOR("ENTRENADOR"),
    PROFESOR("PROFESOR"),
    RECEPCION("RECEPCIÓN"),
    ADMINISTRACION("ADMINISTRACIÓN"),
    ADMIN("SUPERVISOR"),
    MIXTO("MIXTO");

    private final String valorBD;

    CargoEmpleado(String valorBD) {
        this.valorBD = valorBD;
    }

    /** Valor exacto tal cual está almacenado/definido en el ENUM de la BD. */
    public String getValorBD() {
        return valorBD;
    }

    /**
     * Cargos habilitados para dictar clases y para ser listados en el combo
     * "profesor" del módulo de Clases.
     * REGLA DE NEGOCIO: un empleado puede dictar una clase si su cargo es
     * PROFESOR o MIXTO.
     */
    private static final Set<CargoEmpleado> HABILITADOS_PARA_CLASE =
            EnumSet.of(PROFESOR, MIXTO);

    /** true si este cargo puede ser asignado como dictante de una clase. */
    public boolean puedeDictarClases() {
        return HABILITADOS_PARA_CLASE.contains(this);
    }

    /**
     * true si el sueldo de este cargo se calcula por clase dictada en lugar
     * de sueldo mensual fijo. Aplica a los mismos cargos habilitados para
     * dictar clases (PROFESOR o MIXTO).
     */
    public boolean seRemuneraPorClase() {
        return puedeDictarClases();
    }

    /** Valores de BD de los cargos habilitados para clase, listos para un IN (?,?) parametrizado. */
    public static String[] valoresHabilitadosParaClase() {
        return HABILITADOS_PARA_CLASE.stream()
                .map(CargoEmpleado::getValorBD)
                .toArray(String[]::new);
    }

    
    public static CargoEmpleado desdeValorBD(String valor) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("El cargo del empleado no puede estar vacío");
        }
        String normalizado = valor.trim();
        return Arrays.stream(values())
                .filter(c -> c.valorBD.equalsIgnoreCase(normalizado))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Cargo de empleado desconocido: " + valor));
    }

    @Override
    public String toString() {
        return valorBD;
    }
}
