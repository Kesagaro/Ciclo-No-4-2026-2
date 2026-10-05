/**
 * Pruebas de unidad compartidas del Ciclo 2 para {@code SlotMachine}.
 * Todas las pruebas corren en modo invisible.
 *
 * <p>Naming: {@code accordingBlancoSGarzonRShould...}
 *
 * @author BlancoS-GarzonR
 * @version 2.0
 */
public class SlotMachineCC2Test {

    /**
     * Verifica que swap intercambia correctamente la configuracion visible
     * de dos ruedas distintas.
     *
     * Autores: BlancoS-GarzonR
     */
    public void accordingBlancoSGarzonRShouldSwapWheelsCorrectly() {
        SlotMachine sm = new SlotMachine();
        sm.addWheel(1);
        sm.addWheel(2);
        sm.addSymbol(1, "red");
        sm.addSymbol(2, "blue");
        sm.placeSymbol(1, "red");
        sm.placeSymbol(2, "blue");
        sm.swap(1, 2);
        assert sm.ok() : "swap debe ser exitoso";
        String[] cfg = sm.configuration();
        assert cfg[0].equals("blue") : "Rueda 1 debe mostrar blue tras swap";
        assert cfg[1].equals("red")  : "Rueda 2 debe mostrar red tras swap";
    }

    /**
     * Verifica que una rueda bloqueada no puede girar con spin(wheel).
     *
     * Autores: BlancoS-GarzonR
     */
    public void accordingBlancoSGarzonRShouldNotSpinLockedWheel() {
        SlotMachine sm = new SlotMachine();
        sm.addWheel(1);
        sm.addSymbol(1, "red");
        sm.addSymbol(2, "blue");
        sm.placeSymbol(1, "red");
        sm.lock(1);
        String colorAntes = sm.configuration()[0];
        sm.spin(1);
        assert !sm.ok() : "spin en rueda bloqueada debe dejar ok() en false";
        assert sm.configuration()[0].equals(colorAntes) : "El simbolo no debe cambiar";
    }
}
