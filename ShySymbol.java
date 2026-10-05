/**
 * Símbolo tímido: cada vez que una rueda lo selecciona (al girar o con
 * placeSymbol) cambia entre visible e invisible. Se dibuja como un triángulo.
 *
 * <p>Que esté escondido solo afecta al dibujo: su color sigue contando para
 * configuration(), distinctSymbols() e isJackpot().
 *
 * @author BlancoS-GarzonR
 * @version 1.0
 */
public class ShySymbol extends Symbol {

    private static final int HEIGHT = 50;
    private static final int APEX_Y = 3;

    private final Triangle triangle;
    private boolean hidden;             // true cuando el símbolo se escondió
    private boolean requestedVisible;   // true cuando la rueda pidió mostrarlo
    private int triX;
    private int triY;

    /**
     * Crea un símbolo tímido, invisible y todavía no escondido.
     *
     * @param color color del triángulo
     */
    public ShySymbol(String color) {
        super(color);
        triangle = new Triangle();
        triangle.changeSize(HEIGHT, DIAMETER);
        triangle.changeColor(color);
        hidden = false;
        requestedVisible = false;
        triX = 140;
        triY = 15;
        place();
    }

    @Override
    public String getType() {
        return SymbolFactory.SHY;
    }

    @Override
    public boolean isShown() {
        return !hidden;
    }

    @Override
    public void moveTo(int x, int y) {
        xPos = x;
        yPos = y;
        place();
    }

    @Override
    public void makeVisible() {
        requestedVisible = true;
        if (!hidden) triangle.makeVisible();
    }

    @Override
    public void makeInvisible() {
        requestedVisible = false;
        triangle.makeInvisible();
    }

    /** Un giro lo seleccionó: cambia entre mostrado y escondido. */
    @Override
    public void onSpin() {
        toggle();
    }

    /** placeSymbol lo seleccionó: cambia entre mostrado y escondido. */
    @Override
    public void onPlaced() {
        toggle();
    }

    /** Cambia el estado y actualiza el dibujo si la rueda lo estaba mostrando. */
    private void toggle() {
        hidden = !hidden;
        if (requestedVisible) {
            if (hidden) triangle.makeInvisible(); else triangle.makeVisible();
        }
    }

    /** Coloca la punta del triángulo en el centro superior del símbolo. */
    private void place() {
        int targetX = xPos + DIAMETER / 2;
        int targetY = yPos + APEX_Y;
        triangle.moveHorizontal(targetX - triX);
        triangle.moveVertical(targetY - triY);
        triX = targetX;
        triY = targetY;
    }
}
