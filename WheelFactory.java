/**
 * Crea ruedas a partir del nombre de su tipo. Es el único lugar que conoce
 * las clases de rueda: para agregar un tipo nuevo se crea su clase, una
 * constante y una línea en create.
 *
 * @author BlancoS-GarzonR
 * @version 1.0
 */
public final class WheelFactory {

    /** Tipo de rueda normal. */
    public static final String NORMAL = "normal";
    /** Tipo de rueda zurda. */
    public static final String LEFTY  = "lefty";
    /** Tipo de rueda rebelde. */
    public static final String REBEL  = "rebel";
    /** Tipo de rueda turbo (propuesto por el equipo). */
    public static final String TURBO  = "turbo";

    private static final String[] TYPES = { NORMAL, LEFTY, REBEL, TURBO };

    private WheelFactory() {
    }

    /**
     * Dice si el nombre es un tipo de rueda conocido (no importan las mayúsculas).
     *
     * @param type nombre del tipo; puede ser null
     * @return true si el tipo existe
     */
    public static boolean isValidType(String type) {
        return normalize(type) != null;
    }

    /**
     * Nombres de los tipos de rueda disponibles.
     *
     * @return los tipos
     */
    public static String[] types() {
        return TYPES.clone();
    }

    /**
     * Crea una rueda vacía del tipo indicado.
     *
     * @param type nombre del tipo
     * @param x    posición horizontal del marco
     * @param y    posición vertical del marco
     * @return la rueda, o null si el tipo no existe
     */
    public static Wheel create(String type, int x, int y) {
        String t = normalize(type);
        if (t == null) return null;
        if (t.equals(LEFTY)) return new LeftyWheel(x, y);
        if (t.equals(REBEL)) return new RebelWheel(x, y);
        if (t.equals(TURBO)) return new TurboWheel(x, y);
        return new NormalWheel(x, y);
    }

    /** Nombre del tipo en minúsculas y sin espacios, o null si no existe. */
    private static String normalize(String type) {
        if (type == null) return null;
        String t = type.trim().toLowerCase();
        for (String known : TYPES) {
            if (known.equals(t)) return known;
        }
        return null;
    }
}
