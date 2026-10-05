/**
 * Símbolo normal: un círculo del color indicado, sin comportamiento especial.
 * Es el único tipo que usan SlotMachine(n), solve y simulate.
 *
 * @author BlancoS-GarzonR
 * @version 1.0
 */
public class NormalSymbol extends Symbol {

    private final Circle circle;

    /**
     * Crea un símbolo normal invisible.
     *
     * @param color nombre del color o hexadecimal
     */
    public NormalSymbol(String color) {
        super(color);
        circle = new Circle();
        circle.changeSize(DIAMETER);
        circle.changeColor(color);
    }

    @Override
    public String getType() {
        return SymbolFactory.NORMAL;
    }

    @Override
    public void moveTo(int x, int y) {
        circle.moveHorizontal(x - xPos);
        circle.moveVertical(y - yPos);
        xPos = x;
        yPos = y;
    }

    @Override
    public void makeVisible() {
        circle.makeVisible();
    }

    @Override
    public void makeInvisible() {
        circle.makeInvisible();
    }
}
