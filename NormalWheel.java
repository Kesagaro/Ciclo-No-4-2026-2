/**
 * Rueda normal: la del ciclo 3. Se puede bloquear, intercambiar y eliminar,
 * y avanza los pasos que se le piden. Marco azul.
 *
 * @author BlancoS-GarzonR
 * @version 1.0
 */
public class NormalWheel extends Wheel {

    /**
     * Crea una rueda normal vacía e invisible.
     *
     * @param x posición horizontal del marco
     * @param y posición vertical del marco
     */
    public NormalWheel(int x, int y) {
        super(x, y);
    }

    @Override
    public String getType() {
        return WheelFactory.NORMAL;
    }
}
