/**
 * Crea símbolos a partir del nombre de su tipo. Es el único lugar que conoce
 * las clases de símbolo: para agregar un tipo nuevo se crea su clase, una
 * constante y una línea en create.
 *
 * @author BlancoS-GarzonR
 * @version 1.0
 */
public final class SymbolFactory {

    /** Tipo de símbolo normal. */
    public static final String NORMAL    = "normal";
    /** Tipo de símbolo efímero. */
    public static final String EPHEMERAL = "ephemeral";
    /** Tipo de símbolo tímido. */
    public static final String SHY       = "shy";

    private static final String[] TYPES = { NORMAL, EPHEMERAL, SHY };

    private SymbolFactory() {
    }

    /**
     * Dice si el nombre es un tipo de símbolo conocido (no importan las mayúsculas).
     *
     * @param type nombre del tipo; puede ser null
     * @return true si el tipo existe
     */
    public static boolean isValidType(String type) {
        return normalize(type) != null;
    }

    /**
     * Nombres de los tipos de símbolo disponibles.
     *
     * @return los tipos
     */
    public static String[] types() {
        return TYPES.clone();
    }

    /**
     * Crea un símbolo del tipo y color indicados.
     *
     * @param type  nombre del tipo
     * @param color color del símbolo
     * @return el símbolo, o null si el tipo no existe
     */
    public static Symbol create(String type, String color) {
        String t = normalize(type);
        if (t == null) return null;
        if (t.equals(EPHEMERAL)) return new EphemeralSymbol(color);
        if (t.equals(SHY))       return new ShySymbol(color);
        return new NormalSymbol(color);
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
