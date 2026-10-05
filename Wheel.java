import java.util.ArrayList;

/**
 * Una rueda de la tragamonedas: guarda sus símbolos en orden circular y
 * muestra uno a la vez dentro de un marco rectangular.
 *
 * <p>Ciclo 2: se agregó bloquear la rueda y girar varios pasos.
 *
 * <p>Ciclo 4: es la base de los tipos de rueda (NormalWheel, LeftyWheel,
 * RebelWheel, TurboWheel). Cada tipo cambia solo lo que lo hace distinto,
 * por ejemplo si se deja bloquear o cuántos pasos avanza. Se crean con WheelFactory.
 *
 * @author BlancoS-GarzonR
 * @version 3.0
 */
public abstract class Wheel {

    /** Ancho del marco en píxeles. */
    public static final int WIDTH  = 80;
    /** Alto del marco en píxeles. */
    public static final int HEIGHT = 120;

    private static final int RECT_INIT_X  = 70;
    private static final int RECT_INIT_Y  = 15;
    private static final int SYMBOL_PAD_X = 12;
    private static final int SYMBOL_PAD_Y = 32;

    private final ArrayList<Symbol> symbols;
    private int currentIndex;           // posición del símbolo que se está mostrando
    private final Rectangle frame;
    private boolean isVisible;
    private boolean locked;
    private Wheel left;                 // rueda que tiene a su izquierda (null si es la primera)
    private int xPos;
    private int yPos;

    /**
     * Crea una rueda vacía e invisible.
     *
     * @param x posición horizontal del marco
     * @param y posición vertical del marco
     */
    protected Wheel(int x, int y) {
        symbols = new ArrayList<Symbol>();
        currentIndex = 0;
        isVisible = false;
        locked = false;
        frame = new Rectangle();
        frame.changeSize(HEIGHT, WIDTH);
        frame.changeColor(frameColor());
        xPos = RECT_INIT_X;
        yPos = RECT_INIT_Y;
        moveFrameTo(x, y);
    }

    // -------- lo que cambia en cada tipo de rueda --------

    /**
     * Nombre del tipo de rueda ("normal", "lefty", "rebel" o "turbo").
     *
     * @return el nombre del tipo
     */
    public abstract String getType();

    /**
     * Color del marco; así se distingue cada tipo a simple vista.
     *
     * @return color del marco
     */
    protected String frameColor() {
        return "blue";
    }

    /**
     * Dice si la rueda se deja bloquear.
     *
     * @return true si se puede bloquear
     */
    public boolean canLock() {
        return true;
    }

    /**
     * Dice si la rueda se deja intercambiar con otra.
     *
     * @return true si se puede intercambiar
     */
    public boolean canSwap() {
        return true;
    }

    /**
     * Dice si la rueda se deja eliminar.
     *
     * @return true si se puede eliminar
     */
    public boolean canDelete() {
        return true;
    }

    /**
     * Cuántos pasos avanza de verdad cuando se le piden "steps".
     * Normalmente son los mismos; la rueda turbo avanza el doble.
     *
     * @param steps pasos pedidos
     * @return pasos que avanza
     */
    protected int effectiveSteps(int steps) {
        return steps;
    }

    /**
     * Se llama al girar. La rueda lefty lo usa para copiar a su vecina.
     *
     * @return true si ya quedó lista y no debe avanzar por su cuenta
     */
    protected boolean followLeft() {
        return false;
    }

    /**
     * Rueda que está a su izquierda.
     *
     * @return la vecina, o null si es la primera
     */
    protected Wheel leftNeighbor() {
        return left;
    }

    /**
     * La máquina avisa quién queda a la izquierda cada vez que cambia el orden de las ruedas.
     *
     * @param neighbor la rueda de la izquierda, o null si no hay
     */
    void setLeftNeighbor(Wheel neighbor) {
        left = neighbor;
    }

    // -------- giro --------

    /**
     * Gira un paso. No hace nada si la rueda está vacía o bloqueada.
     */
    public void spin() {
        if (symbols.isEmpty() || locked) return;
        if (followLeft()) return;               // lefty: se coloca como su vecina y termina
        advance(effectiveSteps(1), false);
    }

    /**
     * Gira varios pasos y los anima si la rueda está visible.
     * No hace nada si la rueda está vacía o bloqueada.
     *
     * @param steps pasos pedidos
     */
    public void spin(int steps) {
        if (symbols.isEmpty() || locked) return;
        if (followLeft()) return;
        advance(effectiveSteps(steps), true);
    }

    /** Avanza símbolo por símbolo y avisa a cada uno que fue seleccionado. */
    private void advance(int count, boolean animate) {
        for (int i = 0; i < count; i++) {
            hideCurrentSymbol();
            currentIndex = (currentIndex + 1) % symbols.size();
            symbols.get(currentIndex).onSpin();
            showCurrentSymbol();
            if (animate && isVisible) Canvas.getCanvas().wait(150);
        }
    }

    /**
     * Bloquea la rueda y pone el marco rojo, si el tipo lo permite.
     *
     * @return true si quedó bloqueada, false si el tipo no se deja
     */
    public boolean lock() {
        if (!canLock()) return false;
        locked = true;
        frame.changeColor("red");
        return true;
    }

    /**
     * Desbloquea la rueda y devuelve el marco al color de su tipo.
     */
    public void unlock() {
        locked = false;
        frame.changeColor(frameColor());
    }

    /**
     * Dice si la rueda está bloqueada.
     *
     * @return true si está bloqueada
     */
    public boolean isLocked() {
        return locked;
    }

    /**
     * Pone la rueda en el símbolo del color indicado.
     *
     * @param color color del símbolo
     * @return true si lo encontró, false si no existe en la rueda
     */
    public boolean placeSymbol(String color) {
        int index = indexOfColor(color);
        if (index < 0) return false;
        hideCurrentSymbol();
        currentIndex = index;
        symbols.get(currentIndex).onPlaced();
        showCurrentSymbol();
        return true;
    }

    /**
     * Color del símbolo que muestra la rueda.
     *
     * @return el color, o null si la rueda está vacía
     */
    public String getVisibleColor() {
        if (symbols.isEmpty()) return null;
        return symbols.get(currentIndex).getColor();
    }

    /**
     * Símbolo que muestra la rueda.
     *
     * @return el símbolo, o null si la rueda está vacía
     */
    public Symbol getCurrentSymbol() {
        if (symbols.isEmpty()) return null;
        return symbols.get(currentIndex);
    }

    /**
     * Colores de todos los símbolos, en orden.
     *
     * @return los colores; vacío si no hay símbolos
     */
    public String[] getSymbolColors() {
        String[] colors = new String[symbols.size()];
        for (int i = 0; i < symbols.size(); i++) {
            colors[i] = symbols.get(i).getColor();
        }
        return colors;
    }

    /**
     * Agrega un símbolo normal. La posición empieza en 1 y se ajusta si se sale del rango.
     *
     * @param pos   posición donde se inserta
     * @param color color del símbolo
     */
    public void addSymbol(int pos, String color) {
        addSymbol(pos, SymbolFactory.NORMAL, color);
    }

    /**
     * Agrega un símbolo del tipo indicado. La posición empieza en 1 y se ajusta si se sale del rango.
     *
     * @param pos   posición donde se inserta
     * @param type  tipo de símbolo; si no existe se crea uno normal
     * @param color color del símbolo
     */
    public void addSymbol(int pos, String type, String color) {
        int index = clamp(pos, 1, symbols.size() + 1) - 1;
        Symbol s = SymbolFactory.create(type, color);
        if (s == null) s = new NormalSymbol(color);
        s.moveTo(xPos + SYMBOL_PAD_X, yPos + SYMBOL_PAD_Y);
        symbols.add(index, s);
        adjustIndexAfterInsertion(index);
        if (isVisible) showCurrentSymbol();
    }

    /**
     * Elimina el símbolo del color indicado.
     *
     * @param color color del símbolo
     * @return true si lo eliminó, false si no existe
     */
    public boolean delSymbol(String color) {
        int index = indexOfColor(color);
        if (index < 0) return false;
        if (index == currentIndex) hideCurrentSymbol();
        symbols.remove(index);
        adjustIndexAfterRemoval(index);
        if (isVisible && !symbols.isEmpty()) showCurrentSymbol();
        return true;
    }

    /**
     * Cantidad de símbolos de la rueda.
     *
     * @return número de símbolos
     */
    public int symbolCount() {
        return symbols.size();
    }

    /**
     * Mueve la rueda (y su símbolo visible) a otra posición.
     *
     * @param x nueva posición horizontal
     * @param y nueva posición vertical
     */
    public void moveTo(int x, int y) {
        moveFrameTo(x, y);
        if (!symbols.isEmpty()) {
            symbols.get(currentIndex).moveTo(xPos + SYMBOL_PAD_X, yPos + SYMBOL_PAD_Y);
        }
    }

    /** Muestra el marco y el símbolo actual. */
    public void makeVisible() {
        frame.makeVisible();
        isVisible = true;
        showCurrentSymbol();
    }

    /** Oculta el marco y el símbolo actual. */
    public void makeInvisible() {
        hideCurrentSymbol();
        frame.makeInvisible();
        isVisible = false;
    }

    // -------- métodos privados --------

    /** Mueve el marco a una posición absoluta. */
    private void moveFrameTo(int x, int y) {
        frame.moveHorizontal(x - xPos);
        frame.moveVertical(y - yPos);
        xPos = x;
        yPos = y;
    }

    /** Posición del símbolo con ese color, o -1 si no existe. */
    private int indexOfColor(String color) {
        for (int i = 0; i < symbols.size(); i++) {
            if (symbols.get(i).getColor().equals(color)) return i;
        }
        return -1;
    }

    /** Dibuja el símbolo actual dentro del marco. */
    private void showCurrentSymbol() {
        if (symbols.isEmpty() || !isVisible) return;
        Symbol s = symbols.get(currentIndex);
        s.moveTo(xPos + SYMBOL_PAD_X, yPos + SYMBOL_PAD_Y);
        s.makeVisible();
    }

    /** Oculta el símbolo actual. */
    private void hideCurrentSymbol() {
        if (!symbols.isEmpty()) {
            symbols.get(currentIndex).makeInvisible();
        }
    }

    /** Corrige la posición actual después de insertar un símbolo. */
    private void adjustIndexAfterInsertion(int insertedIndex) {
        if (symbols.size() == 1) {
            currentIndex = 0;
        } else if (currentIndex >= insertedIndex) {
            currentIndex++;
        }
    }

    /** Corrige la posición actual después de eliminar un símbolo. */
    private void adjustIndexAfterRemoval(int removedIndex) {
        if (symbols.isEmpty()) {
            currentIndex = 0;
        } else if (currentIndex > removedIndex) {
            currentIndex--;
        } else if (currentIndex >= symbols.size()) {
            currentIndex = symbols.size() - 1;
        }
    }

    /** Ajusta un valor para que quede entre min y max. */
    private int clamp(int val, int min, int max) {
        return Math.max(min, Math.min(val, max));
    }
}
