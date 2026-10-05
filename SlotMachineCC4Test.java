import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Pruebas compartidas del Ciclo 4 para SlotMachine.
 * Solo usan los métodos del diagrama del enunciado y los nombres de tipo que
 * ahí aparecen ("normal", "lefty", "rebel", "ephemeral", "shy"), para que otro
 * grupo pueda correrlas con su proyecto. Todas usan la máquina invisible.
 *
 * <p>Los nombres empiezan con accordingBlancoSGarzonRShould.
 *
 * @author BlancoS-GarzonR
 * @version 1.0
 */
public class SlotMachineCC4Test {

    /**
     * Una rueda lefty con otra a su izquierda queda mostrando el mismo
     * símbolo que ella después de girar.
     *
     * Autores: BlancoS-GarzonR
     */
    @Test
    public void accordingBlancoSGarzonRShouldLeftyWheelCopyItsLeftNeighbour() {
        SlotMachine sm = new SlotMachine();
        sm.addWheel("normal", 1);
        sm.addWheel("lefty", 2);
        sm.addSymbol(1, "red");
        sm.addSymbol(2, "blue");
        sm.addSymbol(3, "yellow");
        sm.placeSymbol(1, "blue");
        sm.placeSymbol(2, "red");
        sm.spin(2);
        assertTrue("spin de la lefty debe ser exitoso", sm.ok());
        assertArrayEquals(new String[] {"blue", "blue"}, sm.configuration());
        assertTrue("con ambas ruedas iguales hay jackpot", sm.isJackpot());
    }

    /**
     * Una rueda rebel no se deja bloquear, intercambiar ni eliminar, y la
     * máquina queda igual después de cada intento.
     *
     * Autores: BlancoS-GarzonR
     */
    @Test
    public void accordingBlancoSGarzonRShouldRebelWheelRefuseLockSwapAndDelete() {
        SlotMachine sm = new SlotMachine();
        sm.addWheel("normal", 1);
        sm.addWheel("rebel", 2);
        sm.addSymbol(1, "red");
        sm.addSymbol(2, "blue");
        sm.placeSymbol(1, "red");
        sm.placeSymbol(2, "blue");
        String[] before = sm.configuration();

        sm.lock(2);
        assertFalse("lock sobre rebel debe fallar", sm.ok());
        sm.swap(1, 2);
        assertFalse("swap con rebel debe fallar", sm.ok());
        sm.delWheel(2);
        assertFalse("delWheel sobre rebel debe fallar", sm.ok());

        assertArrayEquals("la configuración no debe cambiar", before, sm.configuration());
        sm.spin(2);
        assertTrue("la rebel sí gira", sm.ok());
        assertEquals("red", sm.configuration()[1]);
    }

    /**
     * addWheel y addSymbol con un tipo que no existe fallan y no cambian la máquina.
     *
     * Autores: BlancoS-GarzonR
     */
    @Test
    public void accordingBlancoSGarzonRShouldRejectUnknownTypes() {
        SlotMachine sm = new SlotMachine();
        sm.addWheel("normal", 1);
        sm.addSymbol("normal", 1, "red");
        sm.addSymbol("ephemeral", 2, "blue");
        sm.addSymbol("shy", 3, "green");
        assertTrue(sm.ok());

        sm.addWheel("inexistente", 2);
        assertFalse("tipo de rueda desconocido debe fallar", sm.ok());
        assertEquals(1, sm.configuration().length);

        sm.addSymbol("inexistente", 1, "pink");
        assertFalse("tipo de símbolo desconocido debe fallar", sm.ok());
        assertEquals(3, sm.symbols().length);
    }
}
