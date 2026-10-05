import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 * Simulador de tragamonedas inspirado en el Problem I de la maratón ICPC 2025.
 * Administra n ruedas que comparten la misma lista de símbolos de colores.
 * Hay jackpot cuando todas las ruedas muestran el mismo símbolo al mismo tiempo.
 *
 * Las posiciones se numeran desde 1. Si se indica un número fuera de rango,
 * se usa el límite más cercano automáticamente.
 * En modo invisible la lógica sigue funcionando sin mostrar ventanas ni mensajes.
 *
 * Ciclo 2: se añadieron intercambio de ruedas, bloqueo, giro por pasos y giro por arreglo.
 * Ciclo 3: se añadió el constructor que recibe n para crear la máquina completa de una vez.
 * Ciclo 4: hay varios tipos de rueda (normal, lefty, rebel, turbo) y de símbolo
 * (normal, ephemeral, shy). Se crean por nombre con addWheel(type, pos) y
 * addSymbol(type, pos, color). Además lock ahora devuelve cuántas ruedas quedan bloqueadas.
 *
 * @author BlancoS-GarzonR
 * @version 4.0
 */
public class SlotMachine {

    private static final int WHEEL_GAP    = 10;
    private static final int MARGIN       = 20;
    private static final int WHEEL_Y      = 55;
    private static final int CANVAS_H     = 210;
    private static final int MIN_CANVAS_W = 220;

    /**
     * Lista de 50 colores distintos que se asignan a los símbolos al crear la máquina con n ruedas.
     * Si hay más de 50 símbolos, el resto se genera como colores hexadecimales distintos.
     */
    private static final String[] CSS_PALETTE = {
        "red",     "blue",    "yellow",  "green",   "lime",
        "magenta", "cyan",    "orange",  "pink",    "gray",
        "purple",  "brown",   "navy",    "teal",    "maroon",
        "olive",   "coral",   "gold",    "violet",  "indigo",
        "silver",  "darkgray","lightgray","#FF6347","#FF4500",
        "#ADFF2F", "#7CFC00", "#00FA9A", "#00CED1", "#1E90FF",
        "#8A2BE2", "#DA70D6", "#FF69B4", "#DC143C", "#B8860B",
        "#556B2F", "#8B4513", "#2E8B57", "#4169E1", "#7B68EE",
        "#20B2AA", "#3CB371", "#FF8C00", "#C71585", "#191970",
        "#FF1493", "#00BFFF", "#32CD32", "#BA55D3", "#FA8072"
    };

    private final ArrayList<String> symbolColors;
    private final ArrayList<String> symbolTypes;
    private final ArrayList<Wheel>  wheels;
    private final SlotMachineView   view;
    private boolean visible;
    private boolean lastOk;

    /**
     * Crea una máquina vacía sin ruedas ni símbolos. Inicia en modo invisible.
     */
    public SlotMachine() {
        symbolColors = new ArrayList<String>();
        symbolTypes  = new ArrayList<String>();
        wheels       = new ArrayList<Wheel>();
        view         = new SlotMachineView(MIN_CANVAS_W, CANVAS_H);
        visible      = false;
        lastOk       = true;
    }

    /**
     * Crea una máquina con n ruedas y n símbolos de colores distintos,
     * cada rueda girada a una posición aleatoria al inicio.
     * La máquina queda en modo invisible.
     *
     * Este constructor es el punto de entrada para SlotMachineContest.
     * Los primeros 50 símbolos usan colores de una paleta; los siguientes usan
     * colores hexadecimales generados, distintos entre sí.
     *
     * @param n cantidad de ruedas y de símbolos (mínimo 1)
     */
    public SlotMachine(int n) {
        this();
        int count = Math.max(1, n);
        for (int i = 1; i <= count; i++) addWheel(i);
        for (int i = 0; i < count; i++) addSymbol(i + 1, symbolColorAt(i));
        java.util.Random rand = new java.util.Random();
        for (int i = 1; i <= count; i++) {
            int steps = rand.nextInt(count);
            if (steps > 0) spin(i, steps);
        }
    }

    // ------------------------------------------------------------------ ruedas

    /**
     * Agrega una rueda en la posición indicada (clamped a [1, n+1]).
     * La rueda hereda todos los símbolos existentes.
     *
     * @param pos posición de inserción (base 1)
     */
    public void addWheel(int pos) {
        addWheel(WheelFactory.NORMAL, pos);
    }

    /**
     * Agrega una rueda del tipo indicado. Recibe todos los símbolos que ya existen.
     * Si el tipo no existe, falla y no cambia nada.
     *
     * <p>Tipos: normal, lefty (copia a la rueda de su izquierda), rebel (no se bloquea,
     * ni se intercambia, ni se elimina) y turbo (gira el doble). No importan las mayúsculas.
     *
     * @param type tipo de rueda
     * @param pos  posición donde se inserta (empieza en 1)
     */
    public void addWheel(String type, int pos) {
        if (!WheelFactory.isValidType(type)) {
            handleError("El tipo de rueda '" + type + "' no existe.");
            lastOk = false;
            return;
        }
        int index = clamp(pos, 1, wheels.size() + 1) - 1;
        Wheel wheel = buildWheelWithSymbols(type, 0, WHEEL_Y);
        wheels.add(index, wheel);
        repositionWheels();
        if (visible) wheel.makeVisible();
        updateJackpotDisplay();
        lastOk = true;
    }

    /**
     * Elimina la rueda de la posición indicada.
     * Falla si no hay ruedas o si la rueda es rebel (no se deja eliminar).
     *
     * @param pos posición de la rueda a eliminar (base 1)
     */
    public void delWheel(int pos) {
        if (wheels.isEmpty()) { handleError("No hay ruedas para eliminar."); lastOk = false; return; }
        int index = clamp(pos, 1, wheels.size()) - 1;
        if (!wheels.get(index).canDelete()) {
            handleError("La rueda " + (index + 1) + " (" + wheels.get(index).getType() + ") no se deja eliminar.");
            lastOk = false;
            return;
        }
        Wheel removed = wheels.remove(index);
        removed.makeInvisible();
        repositionWheels();
        updateJackpotDisplay();
        lastOk = true;
    }

    /**
     * Intercambia dos ruedas de lugar.
     * Falla si no hay ruedas, si son la misma o si alguna es rebel (no se deja intercambiar).
     *
     * @param wheel1 posición de la primera rueda (base 1)
     * @param wheel2 posición de la segunda rueda (base 1)
     */
    public void swap(int wheel1, int wheel2) {
        if (wheels.isEmpty()) { handleError("No hay ruedas para intercambiar."); lastOk = false; return; }
        int i1 = clamp(wheel1, 1, wheels.size()) - 1;
        int i2 = clamp(wheel2, 1, wheels.size()) - 1;
        if (i1 == i2) { handleError("Las posiciones son la misma rueda."); lastOk = false; return; }
        if (!wheels.get(i1).canSwap() || !wheels.get(i2).canSwap()) {
            handleError("Una de las ruedas no se deja intercambiar.");
            lastOk = false;
            return;
        }
        Wheel tmp = wheels.get(i1);
        wheels.set(i1, wheels.get(i2));
        wheels.set(i2, tmp);
        repositionWheels();
        updateJackpotDisplay();
        lastOk = true;
    }

    /**
     * Bloquea la rueda: no gira hasta que se desbloquee.
     * Falla si no hay ruedas o si la rueda es rebel (no se deja bloquear).
     *
     * @param wheel posición de la rueda (base 1)
     * @return cuántas ruedas quedan bloqueadas (también cuando falla)
     */
    public int lock(int wheel) {
        if (wheels.isEmpty()) { handleError("No hay ruedas."); lastOk = false; return 0; }
        int index = clamp(wheel, 1, wheels.size()) - 1;
        if (!wheels.get(index).lock()) {
            handleError("La rueda " + (index + 1) + " (" + wheels.get(index).getType() + ") no se deja bloquear.");
            lastOk = false;
            return lockedCount();
        }
        lastOk = true;
        return lockedCount();
    }

    /**
     * Libera la rueda indicada para que pueda volver a girar.
     * Falla si no hay ruedas.
     *
     * @param wheel posición de la rueda (base 1)
     */
    public void unlock(int wheel) {
        if (wheels.isEmpty()) { handleError("No hay ruedas."); lastOk = false; return; }
        int index = clamp(wheel, 1, wheels.size()) - 1;
        wheels.get(index).unlock();
        lastOk = true;
    }


    // --------------------------------------------------------------- símbolos

    /**
     * Agrega un símbolo en la posición indicada de la secuencia compartida.
     * Falla si el color ya existe.
     *
     * @param pos   posición de inserción (base 1)
     * @param color color CSS del nuevo símbolo
     */
    public void addSymbol(int pos, String color) {
        addSymbol(SymbolFactory.NORMAL, pos, color);
    }

    /**
     * Agrega un símbolo del tipo indicado a todas las ruedas.
     * Falla si el color ya existe o si el tipo no existe.
     *
     * <p>Tipos: normal, ephemeral (se encoge en cada giro hasta ser un punto) y
     * shy (se esconde y se muestra cada vez que se selecciona). No importan las mayúsculas.
     *
     * @param type  tipo de símbolo
     * @param pos   posición de inserción (base 1)
     * @param color color CSS del nuevo símbolo
     */
    public void addSymbol(String type, int pos, String color) {
        if (!SymbolFactory.isValidType(type)) {
            handleError("El tipo de símbolo '" + type + "' no existe.");
            lastOk = false;
            return;
        }
        if (isColorUsed(color)) {
            handleError("El símbolo '" + color + "' ya existe.");
            lastOk = false;
            return;
        }
        int index = clamp(pos, 1, symbolColors.size() + 1) - 1;
        symbolColors.add(index, color);
        symbolTypes.add(index, type.trim().toLowerCase());
        for (Wheel w : wheels) w.addSymbol(index + 1, type, color);
        updateJackpotDisplay();
        lastOk = true;
    }

    /**
     * Elimina el símbolo con el color indicado de todas las ruedas.
     * Falla si el color no existe.
     *
     * @param symbol color CSS del símbolo a eliminar
     */
    public void delSymbol(String symbol) {
        if (!isColorUsed(symbol)) {
            handleError("El símbolo '" + symbol + "' no existe.");
            lastOk = false;
            return;
        }
        int at = symbolColors.indexOf(symbol);
        symbolColors.remove(at);
        symbolTypes.remove(at);
        for (Wheel w : wheels) w.delSymbol(symbol);
        updateJackpotDisplay();
        lastOk = true;
    }

    // ------------------------------------------------------------------- giro

    /**
     * Posiciona la rueda indicada en el símbolo con el color dado.
     * Falla si no hay ruedas o el símbolo no existe en esa rueda.
     *
     * @param wheel  posición de la rueda (base 1)
     * @param symbol color CSS del símbolo destino
     */
    public void placeSymbol(int wheel, String symbol) {
        if (wheels.isEmpty()) { handleError("No hay ruedas."); lastOk = false; return; }
        int index = clamp(wheel, 1, wheels.size()) - 1;
        if (!wheels.get(index).placeSymbol(symbol)) {
            handleError("El símbolo '" + symbol + "' no está en la rueda " + wheel + ".");
            lastOk = false;
            return;
        }
        updateJackpotDisplay();
        lastOk = true;
    }

    /**
     * Avanza la rueda indicada un símbolo de forma circular.
     * Falla si no hay ruedas, no hay símbolos, o la rueda está bloqueada.
     *
     * @param wheel posición de la rueda (base 1)
     */
    public void spin(int wheel) {
        if (!validateSpinPreconditions()) return;
        int index = clamp(wheel, 1, wheels.size()) - 1;
        Wheel w = wheels.get(index);
        if (w.symbolCount() == 0) {
            handleError("La rueda " + wheel + " no tiene símbolos.");
            lastOk = false;
            return;
        }
        if (w.isLocked()) {
            handleError("La rueda " + wheel + " está bloqueada.");
            lastOk = false;
            return;
        }
        w.spin();
        updateJackpotDisplay();
        lastOk = true;
    }

    /**
     * Gira la rueda indicada steps posiciones. Anima si está visible.
     * Falla si no hay ruedas, símbolos, o la rueda está bloqueada.
     *
     * @param wheel posición de la rueda (base 1)
     * @param steps pasos a avanzar
     */
    public void spin(int wheel, int steps) {
        if (!validateSpinPreconditions()) return;
        int index = clamp(wheel, 1, wheels.size()) - 1;
        Wheel w = wheels.get(index);
        if (w.isLocked()) {
            handleError("La rueda " + wheel + " está bloqueada.");
            lastOk = false;
            return;
        }
        w.spin(steps);
        updateJackpotDisplay();
        lastOk = true;
    }

    /**
     * Coloca cada rueda en el símbolo indicado por setSymbols.
     * El arreglo debe tener un color por rueda (izquierda a derecha).
     * Falla si el tamaño no coincide o algún color no existe.
     *
     * @param setSymbols colores CSS, uno por rueda
     */
    public void spin(String[] setSymbols) {
        if (wheels.isEmpty()) { handleError("No hay ruedas."); lastOk = false; return; }
        if (setSymbols.length != wheels.size()) {
            handleError("El arreglo debe tener " + wheels.size() + " elemento(s).");
            lastOk = false;
            return;
        }
        boolean allOk = true;
        for (int i = 0; i < wheels.size(); i++) {
            if (!wheels.get(i).placeSymbol(setSymbols[i])) {
                handleError("El símbolo '" + setSymbols[i] + "' no existe en la rueda " + (i + 1) + ".");
                allOk = false;
            }
        }
        updateJackpotDisplay();
        lastOk = allOk;
    }


    /**
     * Avanza todas las ruedas un símbolo de forma circular.
     * Falla si no hay ruedas o no hay símbolos.
     */
    public void spin() {
        if (!validateSpinPreconditions()) return;
        for (Wheel w : wheels) w.spin();
        updateJackpotDisplay();
        lastOk = true;
    }

    // ---------------------------------------------------------------- consultas

    /**
     * Retorna los colores de todos los símbolos en orden de posición (base 1 primero).
     *
     * @return arreglo de colores CSS; vacío si no hay símbolos
     */
    public String[] symbols() {
        return symbolColors.toArray(new String[0]);
    }

    /**
     * Tipo de cada símbolo, en el mismo orden que symbols().
     *
     * @return arreglo de tipos; vacío si no hay símbolos
     */
    public String[] symbolTypes() {
        return symbolTypes.toArray(new String[0]);
    }

    /**
     * Tipo de cada rueda, de izquierda a derecha.
     *
     * @return arreglo de tipos; vacío si no hay ruedas
     */
    public String[] wheelTypes() {
        String[] types = new String[wheels.size()];
        for (int i = 0; i < wheels.size(); i++) types[i] = wheels.get(i).getType();
        return types;
    }

    /**
     * Rueda de la posición indicada (empieza en 1). Solo para uso interno y de pruebas.
     *
     * @param pos posición de la rueda (base 1)
     * @return la rueda, o null si la posición no existe
     */
    Wheel wheelAt(int pos) {
        if (pos < 1 || pos > wheels.size()) return null;
        return wheels.get(pos - 1);
    }

    /**
     * Retorna la cantidad de símbolos distintos que se ven en este momento,
     * es decir, cuántos colores diferentes hay en la configuración visible.
     * Vale 1 cuando hay jackpot y 0 si la máquina no tiene ruedas con símbolos.
     *
     * @return número de colores distintos visibles
     */
    public int distinctSymbols() {
        java.util.HashSet<String> visibles = new java.util.HashSet<String>();
        for (Wheel w : wheels) {
            String color = w.getVisibleColor();
            if (color != null) visibles.add(color);
        }
        return visibles.size();
    }

    /**
     * Retorna el color visible de cada rueda, de izquierda a derecha.
     *
     * @return arreglo de colores CSS; null en posiciones de ruedas vacías
     */
    public String[] configuration() {
        String[] config = new String[wheels.size()];
        for (int i = 0; i < wheels.size(); i++) {
            config[i] = wheels.get(i).getVisibleColor();
        }
        return config;
    }

    /**
     * Indica si la máquina está en estado jackpot (todas las ruedas
     * muestran el mismo símbolo).
     *
     * @return true si hay jackpot; false en caso contrario
     */
    public boolean isJackpot() {
        if (wheels.isEmpty()) return false;
        String first = wheels.get(0).getVisibleColor();
        if (first == null) return false;
        for (Wheel w : wheels) {
            if (!first.equals(w.getVisibleColor())) return false;
        }
        return true;
    }

    // -------------------------------------------------------------- visibilidad

    /**
     * Muestra la ventana del canvas con todas las ruedas y símbolos.
     */
    public void makeVisible() {
        visible = true;
        resizeCanvas();
        Canvas.getCanvas().setVisible(true);
        view.makeVisible();
        for (Wheel w : wheels) w.makeVisible();
        updateJackpotDisplay();
        lastOk = true;
    }

    /**
     * Oculta la ventana del canvas. La lógica sigue funcionando en modo invisible.
     */
    public void makeInvisible() {
        for (Wheel w : wheels) w.makeInvisible();
        view.makeInvisible();
        visible = false;
        Canvas.setCanvasVisible(false);
        lastOk = true;
    }

    // ------------------------------------------------------------------ salida

    /**
     * Cierra la ventana del canvas y libera los recursos gráficos.
     * Si la ventana nunca se abrió, no hace nada visible.
     */
    public void exit() {
        Canvas.closeCanvas();
        lastOk = true;
    }

    // ------------------------------------------------------------------ estado

    /**
     * Indica si la última operación se realizó con éxito.
     *
     * @return true si la última operación fue exitosa
     */
    public boolean ok() {
        return lastOk;
    }

    // ---------------------------------------------------------------- privados

    /** Crea una rueda del tipo indicado y le agrega todos los símbolos, cada uno con su tipo. */
    private Wheel buildWheelWithSymbols(String type, int x, int y) {
        Wheel wheel = WheelFactory.create(type, x, y);
        for (int i = 0; i < symbolColors.size(); i++) {
            wheel.addSymbol(i + 1, symbolTypes.get(i), symbolColors.get(i));
        }
        return wheel;
    }

    /** Cuenta cuántas ruedas están bloqueadas. */
    private int lockedCount() {
        int count = 0;
        for (Wheel w : wheels) if (w.isLocked()) count++;
        return count;
    }

    /** Acomoda las ruedas, avisa a cada una quién queda a su izquierda y ajusta el tamaño de la ventana. */
    private void repositionWheels() {
        for (int i = 0; i < wheels.size(); i++) {
            wheels.get(i).moveTo(MARGIN + i * (Wheel.WIDTH + WHEEL_GAP), WHEEL_Y);
            wheels.get(i).setLeftNeighbor(i > 0 ? wheels.get(i - 1) : null);
        }
        resizeCanvas();
    }

    /** Ajusta el tamaño del canvas y la vista al número actual de ruedas. No actúa en modo invisible. */
    private void resizeCanvas() {
        if (!visible) return;
        int n = Math.max(1, wheels.size());
        int w = Math.max(MIN_CANVAS_W, MARGIN + n * (Wheel.WIDTH + WHEEL_GAP) + MARGIN);
        Canvas.getCanvas().resize(w, CANVAS_H);
        view.resize(w, CANVAS_H);
    }

    /** Muestra u oculta el banner de jackpot según el estado actual. */
    private void updateJackpotDisplay() {
        if (isJackpot()) { view.showJackpot(); } else { view.hideJackpot(); }
    }

    /**
     * Verifica que haya ruedas y símbolos antes de girar.
     * Establece lastOk = false y muestra error si falla.
     *
     * @return true si las precondiciones se cumplen
     */
    private boolean validateSpinPreconditions() {
        if (wheels.isEmpty())       { handleError("La máquina no tiene ruedas.");   lastOk = false; return false; }
        if (symbolColors.isEmpty()) { handleError("La máquina no tiene símbolos."); lastOk = false; return false; }
        return true;
    }

    /**
     * Retorna el color del símbolo número index (base 0): de la paleta
     * mientras haya, y después un color hexadecimal generado que no esté en uso.
     */
    private String symbolColorAt(int index) {
        if (index < CSS_PALETTE.length) return CSS_PALETTE[index];
        int k = index - CSS_PALETTE.length;
        String color;
        do {
            int rgb = (int) (((k + 1L) * 0x9E3779B1L) & 0xFFFFFFL);
            color = String.format("#%06X", rgb);
            k++;
        } while (isColorUsed(color));
        return color;
    }

    /** Retorna true si el color ya está en la secuencia de símbolos. */
    private boolean isColorUsed(String color) {
        return symbolColors.contains(color);
    }

    /** Muestra un diálogo de error solo si el simulador está visible. */
    private void handleError(String message) {
        if (visible) {
            JOptionPane.showMessageDialog(null, message, "Slot Machine — Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Limita val al rango [min, max]. */
    private int clamp(int val, int min, int max) {
        return Math.max(min, Math.min(val, max));
    }
}
