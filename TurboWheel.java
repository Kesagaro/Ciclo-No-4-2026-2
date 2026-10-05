/**
 * Rueda turbo (tipo propuesto por el equipo): avanza el doble de lo que se
 * le pide. spin(rueda) avanza 2 símbolos y spin(rueda, k) avanza 2k. Marco magenta.
 *
 * @author BlancoS-GarzonR
 * @version 1.0
 */
public class TurboWheel extends Wheel {

    /** Por cuánto se multiplican los pasos pedidos. */
    public static final int FACTOR = 2;

    /**
     * Crea una rueda turbo vacía e invisible.
     *
     * @param x posición horizontal del marco
     * @param y posición vertical del marco
     */
    public TurboWheel(int x, int y) {
        super(x, y);
    }

    @Override
    public String getType() {
        return WheelFactory.TURBO;
    }

    @Override
    protected String frameColor() {
        return "magenta";
    }

    @Override
    protected int effectiveSteps(int steps) {
        return steps * FACTOR;
    }
}
