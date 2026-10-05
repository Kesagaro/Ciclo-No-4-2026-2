/**
 * Un símbolo de la tragamonedas, identificado por su color.
 *
 * <p>Ciclo 4: es la base de los tipos de símbolo (NormalSymbol, EphemeralSymbol,
 * ShySymbol). Cada tipo decide cómo se dibuja y qué hace cuando una rueda
 * lo selecciona. Se crean con SymbolFactory.
 *
 * @author BlancoS-GarzonR
 * @version 2.0
 */
public abstract class Symbol {

    /** Tamaño del símbolo en píxeles. */
    protected static final int DIAMETER = 56;
    /** Posición horizontal inicial (la del ciclo 3). */
    protected static final int INIT_X   = 20;
    /** Posición vertical inicial (la del ciclo 3). */
    protected static final int INIT_Y   = 15;

    private final String color;
    /** Esquina superior izquierda del símbolo. */
    protected int xPos;
    /** Esquina superior izquierda del símbolo. */
    protected int yPos;

    /**
     * Crea un símbolo invisible del color indicado.
     *
     * @param color nombre del color (por ejemplo "red") o hexadecimal ("#ff6600")
     */
    protected Symbol(String color) {
        this.color = color;
        xPos = INIT_X;
        yPos = INIT_Y;
    }

    /**
     * Color del símbolo.
     *
     * @return el color
     */
    public String getColor() {
        return color;
    }

    /**
     * Nombre del tipo de símbolo ("normal", "ephemeral" o "shy").
     *
     * @return el nombre del tipo
     */
    public abstract String getType();

    /**
     * Tamaño actual de la figura en píxeles.
     *
     * @return el tamaño
     */
    public int getSize() {
        return DIAMETER;
    }

    /**
     * Dice si el símbolo se dibuja cuando la rueda está visible.
     * Solo el símbolo tímido (ShySymbol) puede responder false.
     *
     * @return true si se dibuja
     */
    public boolean isShown() {
        return true;
    }

    /**
     * Mueve el símbolo a una posición (sirve visible o invisible).
     *
     * @param x posición horizontal
     * @param y posición vertical
     */
    public abstract void moveTo(int x, int y);

    /** Muestra el símbolo. */
    public abstract void makeVisible();

    /** Oculta el símbolo. */
    public abstract void makeInvisible();

    /**
     * Se llama cuando un giro deja este símbolo seleccionado.
     * Por defecto no hace nada; cada tipo lo cambia si lo necesita.
     */
    public void onSpin() {
    }

    /**
     * Se llama cuando placeSymbol deja este símbolo seleccionado.
     * Por defecto no hace nada.
     */
    public void onPlaced() {
    }
}
