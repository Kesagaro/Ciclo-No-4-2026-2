/**
 * Rueda rebelde: no se deja bloquear, ni intercambiar, ni eliminar.
 * Gira como una normal. Marco naranja.
 *
 * @author BlancoS-GarzonR
 * @version 1.0
 */
public class RebelWheel extends Wheel {

    /**
     * Crea una rueda rebelde vacía e invisible.
     *
     * @param x posición horizontal del marco
     * @param y posición vertical del marco
     */
    public RebelWheel(int x, int y) {
        super(x, y);
    }

    @Override
    public String getType() {
        return WheelFactory.REBEL;
    }

    @Override
    protected String frameColor() {
        return "orange";
    }

    @Override
    public boolean canLock() {
        return false;
    }

    @Override
    public boolean canSwap() {
        return false;
    }

    @Override
    public boolean canDelete() {
        return false;
    }
}
