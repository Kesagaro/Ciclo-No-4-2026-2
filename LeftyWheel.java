/**
 * Rueda zurda: si tiene otra rueda a su izquierda, al girar se coloca en el
 * mismo símbolo que esa rueda (la copia). Si es la primera, gira como una normal.
 * Marco verde.
 *
 * <p>Con spin() (todas las ruedas) se gira de izquierda a derecha, así que la zurda
 * copia a su vecina ya girada. Si está bloqueada, no gira ni copia.
 *
 * @author BlancoS-GarzonR
 * @version 1.0
 */
public class LeftyWheel extends Wheel {

    /**
     * Crea una rueda zurda vacía e invisible.
     *
     * @param x posición horizontal del marco
     * @param y posición vertical del marco
     */
    public LeftyWheel(int x, int y) {
        super(x, y);
    }

    @Override
    public String getType() {
        return WheelFactory.LEFTY;
    }

    @Override
    protected String frameColor() {
        return "green";
    }

    /** Copia el símbolo de la vecina de la izquierda, si la hay. */
    @Override
    protected boolean followLeft() {
        Wheel neighbor = leftNeighbor();
        if (neighbor == null) return false;
        String color = neighbor.getVisibleColor();
        if (color == null) return false;
        return placeSymbol(color);
    }
}
