/**
 * Pruebas unitarias del Ciclo 2 para {@code SlotMachine}.
 * Todas las pruebas corren en modo invisible.
 *
 * <p>Convenci&#243;n de nombres:
 * <ul>
 *   <li>{@code shouldXxx} &mdash; comportamiento esperado (happy path)</li>
 *   <li>{@code shouldNotXxx} &mdash; comportamiento ante precondiciones inv&#225;lidas</li>
 * </ul>
 *
 * @author BlancoS-GarzonR
 * @version 2.0
 */
public class SlotMachineC2Test {

    // ------------------------------------------------------------------ swap

    /** swap intercambia la configuracion visible de dos ruedas. */
    public void shouldSwapWheels() {
        SlotMachine sm = new SlotMachine();
        sm.addWheel(1);
        sm.addWheel(2);
        sm.addSymbol(1, "red");
        sm.addSymbol(2, "blue");
        sm.placeSymbol(1, "red");
        sm.placeSymbol(2, "blue");
        sm.swap(1, 2);
        String[] cfg = sm.configuration();
        assert cfg[0].equals("blue") : "Tras swap, rueda 1 debe mostrar blue";
        assert cfg[1].equals("red")  : "Tras swap, rueda 2 debe mostrar red";
        assert sm.ok();
    }

    /** swap falla si la maquina no tiene ruedas. */
    public void shouldNotSwapWithNoWheels() {
        SlotMachine sm = new SlotMachine();
        sm.swap(1, 2);
        assert !sm.ok() : "swap sin ruedas debe dejar ok() en false";
    }

    /** swap con las mismas posiciones falla. */
    public void shouldNotSwapSamePosition() {
        SlotMachine sm = new SlotMachine();
        sm.addWheel(1);
        sm.swap(1, 1);
        assert !sm.ok() : "swap de una rueda consigo misma debe fallar";
    }

    // ------------------------------------------------------------------ lock / unlock

    /** lock impide que la rueda gire. */
    public void shouldNotSpinLockedWheel() {
        SlotMachine sm = new SlotMachine();
        sm.addWheel(1);
        sm.addSymbol(1, "red");
        sm.addSymbol(2, "blue");
        sm.placeSymbol(1, "red");
        sm.lock(1);
        String before = sm.configuration()[0];
        sm.spin(1);
        assert !sm.ok() : "spin en rueda bloqueada debe dejar ok() en false";
        assert sm.configuration()[0].equals(before) : "El simbolo visible no debe cambiar";
    }

    /** unlock restaura la capacidad de girar. */
    public void shouldSpinAfterUnlock() {
        SlotMachine sm = new SlotMachine();
        sm.addWheel(1);
        sm.addSymbol(1, "red");
        sm.addSymbol(2, "blue");
        sm.placeSymbol(1, "red");
        sm.lock(1);
        sm.unlock(1);
        sm.spin(1);
        assert sm.ok() : "spin tras unlock debe ser exitoso";
        assert sm.configuration()[0].equals("blue") : "Debe mostrar el siguiente simbolo";
    }

    /** lock sobre maquina vacia falla. */
    public void shouldNotLockWithNoWheels() {
        SlotMachine sm = new SlotMachine();
        sm.lock(1);
        assert !sm.ok() : "lock sin ruedas debe dejar ok() en false";
    }

    /** unlock sobre maquina vacia falla. */
    public void shouldNotUnlockWithNoWheels() {
        SlotMachine sm = new SlotMachine();
        sm.unlock(1);
        assert !sm.ok() : "unlock sin ruedas debe dejar ok() en false";
    }

    // ------------------------------------------------------------------ spin(steps)

    /** spin(wheel, steps) avanza la rueda el numero exacto de pasos. */
    public void shouldSpinNSteps() {
        SlotMachine sm = new SlotMachine();
        sm.addWheel(1);
        sm.addSymbol(1, "red");
        sm.addSymbol(2, "blue");
        sm.addSymbol(3, "green");
        sm.placeSymbol(1, "red");
        sm.spin(1, 2);
        assert sm.ok() : "spin(steps) debe ser exitoso";
        assert sm.configuration()[0].equals("green") : "Tras 2 pasos desde red: green";
    }

    /** spin(wheel, steps) en rueda bloqueada falla. */
    public void shouldNotSpinStepsOnLockedWheel() {
        SlotMachine sm = new SlotMachine();
        sm.addWheel(1);
        sm.addSymbol(1, "red");
        sm.addSymbol(2, "blue");
        sm.lock(1);
        sm.spin(1, 3);
        assert !sm.ok() : "spin(steps) en rueda bloqueada debe dejar ok() en false";
    }

    /** spin(wheel, steps) sin ruedas falla. */
    public void shouldNotSpinStepsWithNoWheels() {
        SlotMachine sm = new SlotMachine();
        sm.spin(1, 3);
        assert !sm.ok() : "spin(steps) sin ruedas debe dejar ok() en false";
    }

    // ------------------------------------------------------------------ spin(String[])

    /** spin(String[]) configura la maquina con los colores indicados. */
    public void shouldSetConfigurationWithArray() {
        SlotMachine sm = new SlotMachine();
        sm.addWheel(1);
        sm.addWheel(2);
        sm.addSymbol(1, "red");
        sm.addSymbol(2, "blue");
        sm.spin(new String[]{"blue", "red"});
        assert sm.ok() : "spin(String[]) debe ser exitoso";
        String[] cfg = sm.configuration();
        assert cfg[0].equals("blue") : "Rueda 1 debe mostrar blue";
        assert cfg[1].equals("red")  : "Rueda 2 debe mostrar red";
    }

    /** spin(String[]) con arreglo de tamano incorrecto falla. */
    public void shouldNotSetConfigWithWrongArraySize() {
        SlotMachine sm = new SlotMachine();
        sm.addWheel(1);
        sm.addWheel(2);
        sm.addSymbol(1, "red");
        sm.spin(new String[]{"red"});
        assert !sm.ok() : "spin(String[]) con tamano incorrecto debe fallar";
    }

    /** spin(String[]) con color inexistente falla. */
    public void shouldNotSetConfigWithUnknownColor() {
        SlotMachine sm = new SlotMachine();
        sm.addWheel(1);
        sm.addSymbol(1, "red");
        sm.spin(new String[]{"pink"});
        assert !sm.ok() : "spin(String[]) con color inexistente debe fallar";
    }

    /** spin(String[]) que produce jackpot es detectado correctamente. */
    public void shouldDetectJackpotAfterSetConfig() {
        SlotMachine sm = new SlotMachine();
        sm.addWheel(1);
        sm.addWheel(2);
        sm.addSymbol(1, "red");
        sm.addSymbol(2, "blue");
        sm.spin(new String[]{"red", "red"});
        assert sm.ok() : "spin(String[]) debe ser exitoso";
        assert sm.isJackpot() : "Configuracion all-red debe ser jackpot";
    }
}
