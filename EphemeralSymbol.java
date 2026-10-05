/**
 * Símbolo efímero: se encoge cada vez que un giro lo deja seleccionado,
 * hasta quedar como un punto. Se dibuja como un disco de color dentro de un
 * aro negro, así se distingue de un símbolo normal aunque todavía no se haya encogido.
 *
 * <p>Nunca baja del tamaño mínimo (el punto) y no recupera su tamaño.
 *
 * @author BlancoS-GarzonR
 * @version 1.0
 */
public class EphemeralSymbol extends Symbol {

    /** Tamaño inicial del disco. */
    public static final int START_SIZE  = 48;
    /** Tamaño final del disco: el punto. */
    public static final int MIN_SIZE    = 4;
    /** Píxeles que pierde el disco en cada giro. */
    public static final int SHRINK_STEP = 8;

    private final Circle ring;          // aro negro fijo
    private final Circle disc;          // disco de color que se encoge
    private int size;
    private int discX;
    private int discY;

    /**
     * Crea un símbolo efímero invisible con el tamaño inicial.
     *
     * @param color color del disco
     */
    public EphemeralSymbol(String color) {
        super(color);
        ring = new Circle();
        ring.changeSize(DIAMETER);
        ring.changeColor("black");
        disc = new Circle();
        size = START_SIZE;
        disc.changeSize(size);
        disc.changeColor(color);
        discX = INIT_X;
        discY = INIT_Y;
        centerDisc();
    }

    @Override
    public String getType() {
        return SymbolFactory.EPHEMERAL;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public void moveTo(int x, int y) {
        ring.moveHorizontal(x - xPos);
        ring.moveVertical(y - yPos);
        xPos = x;
        yPos = y;
        centerDisc();
    }

    @Override
    public void makeVisible() {
        ring.makeVisible();     // primero el aro, para que el disco quede encima
        disc.makeVisible();
    }

    @Override
    public void makeInvisible() {
        disc.makeInvisible();
        ring.makeInvisible();
    }

    /** Encoge el disco un paso, sin bajar del tamaño mínimo. */
    @Override
    public void onSpin() {
        int next = Math.max(MIN_SIZE, size - SHRINK_STEP);
        if (next == size) return;
        size = next;
        disc.changeSize(size);
        centerDisc();
    }

    /** Deja el disco centrado dentro del aro. */
    private void centerDisc() {
        int targetX = xPos + (DIAMETER - size) / 2;
        int targetY = yPos + (DIAMETER - size) / 2;
        disc.moveHorizontal(targetX - discX);
        disc.moveVertical(targetY - discY);
        discX = targetX;
        discY = targetY;
    }
}
