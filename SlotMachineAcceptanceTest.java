import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Aquí pasamos a código las dos pruebas de aceptación que vamos a mostrar en la
 * presentación (están explicadas en el README). Son los mismos pasos, pero sin
 * abrir la ventana: en lugar de mirar la pantalla, revisamos lo que responde la
 * máquina (configuration, ok, lock, isJackpot) y cómo están los símbolos.
 *
 * <p>Cada prueba parte de una historia de usuario y solo pasa si todos
 * sus pasos dan lo que esperamos.
 *
 * @author BlancoS-GarzonR
 * @version 1.0
 */
public class SlotMachineAcceptanceTest {

    /**
     * Historia 1: como jugador quiero ruedas de distintos tipos para variar el juego:
     * una que copie a la de su izquierda (lefty), una que no se deje bloquear,
     * intercambiar ni eliminar (rebel) y una que gire el doble (turbo).
     */
    @Test
    public void shouldAcceptWheelTypes() {
        SlotMachine sm = new SlotMachine();
        sm.addWheel("normal", 1);
        sm.addWheel("lefty", 2);
        sm.addWheel("rebel", 3);
        sm.addWheel("turbo", 4);
        sm.addSymbol(1, "red");
        sm.addSymbol(2, "yellow");
        sm.addSymbol(3, "cyan");
        sm.addSymbol(4, "pink");

        // 1. Se crean las cuatro ruedas y todas empiezan en rojo, o sea, jackpot.
        assertArrayEquals(new String[] {"normal", "lefty", "rebel", "turbo"}, sm.wheelTypes());
        assertArrayEquals(new String[] {"red", "red", "red", "red"}, sm.configuration());
        assertTrue(sm.isJackpot());

        // 2. Ponemos símbolos distintos y se acaba el jackpot.
        sm.placeSymbol(1, "cyan");
        sm.placeSymbol(2, "red");
        sm.placeSymbol(3, "yellow");
        assertArrayEquals(new String[] {"cyan", "red", "yellow", "red"}, sm.configuration());
        assertFalse(sm.isJackpot());

        // 3. La lefty queda igual que la rueda 1: la copió.
        sm.spin(2);
        assertTrue(sm.ok());
        assertArrayEquals(new String[] {"cyan", "cyan", "yellow", "red"}, sm.configuration());

        // 4. Si gira solo la rueda 1, la lefty se queda quieta.
        sm.spin(1);
        assertArrayEquals(new String[] {"pink", "cyan", "yellow", "red"}, sm.configuration());

        // 5. Giramos todas: la 1 avanza, la lefty la copia, la rebel avanza uno y la turbo dos.
        sm.spin();
        assertArrayEquals(new String[] {"red", "red", "cyan", "cyan"}, sm.configuration());

        // 6. La rebel no deja bloquearla, intercambiarla ni eliminarla, y todo queda igual.
        assertEquals(0, sm.lock(3));
        assertFalse(sm.ok());
        sm.swap(1, 3);
        assertFalse(sm.ok());
        sm.delWheel(3);
        assertFalse(sm.ok());
        assertArrayEquals(new String[] {"normal", "lefty", "rebel", "turbo"}, sm.wheelTypes());
        assertArrayEquals(new String[] {"red", "red", "cyan", "cyan"}, sm.configuration());

        // 7. Una rueda normal sí se bloquea: lock devuelve 1 y ya no gira.
        assertEquals(1, sm.lock(1));
        assertTrue(sm.ok());
        sm.spin(1);
        assertFalse(sm.ok());
        assertArrayEquals(new String[] {"red", "red", "cyan", "cyan"}, sm.configuration());

        // 8. Al desbloquearla, gira otra vez.
        sm.unlock(1);
        assertTrue(sm.ok());
        sm.spin(1);
        assertTrue(sm.ok());
        assertEquals("yellow", sm.configuration()[0]);
    }

    /**
     * Historia 2: como jugador quiero símbolos de distintos tipos para que el juego se
     * vea distinto: uno que se encoja en cada giro hasta ser un punto (ephemeral) y uno
     * que se esconda y se muestre cada vez que se selecciona (shy).
     */
    @Test
    public void shouldAcceptSymbolTypes() {
        SlotMachine sm = new SlotMachine();
        sm.addWheel(1);
        sm.addWheel(2);
        sm.addSymbol("ephemeral", 1, "red");
        sm.addSymbol("shy", 2, "yellow");
        sm.addSymbol(3, "cyan");

        // 1. Cada símbolo tiene su tipo y las dos ruedas empiezan en rojo (efímero de 48 px): jackpot.
        assertArrayEquals(new String[] {"ephemeral", "shy", "normal"}, sm.symbolTypes());
        assertArrayEquals(new String[] {"red", "red"}, sm.configuration());
        assertEquals(48, sizeOnWheel(sm, 1));
        assertEquals(48, sizeOnWheel(sm, 2));
        assertTrue(sm.isJackpot());

        // 2. Parte A, shy: al seleccionarlo se esconde, pero su color sigue contando.
        sm.placeSymbol(1, "yellow");
        assertFalse(shownOnWheel(sm, 1));
        assertArrayEquals(new String[] {"yellow", "red"}, sm.configuration());

        // 3. Si lo seleccionamos otra vez, se vuelve a mostrar.
        sm.placeSymbol(1, "yellow");
        assertTrue(shownOnWheel(sm, 1));
        sm.placeSymbol(1, "red");
        assertEquals(48, sizeOnWheel(sm, 1));

        // 4. Parte B, ephemeral: cada vuelta de la rueda 2 lo deja más pequeño.
        sm.spin(2, 3);
        assertEquals(40, sizeOnWheel(sm, 2));
        sm.spin(2, 3);
        assertEquals(32, sizeOnWheel(sm, 2));
        sm.spin(2, 3);
        assertEquals(24, sizeOnWheel(sm, 2));

        // 5. Con más vueltas queda en un punto de 4 px y ya no cambia.
        sm.spin(2, 18);
        assertEquals(EphemeralSymbol.MIN_SIZE, sizeOnWheel(sm, 2));
        sm.spin(2, 3);
        assertEquals(EphemeralSymbol.MIN_SIZE, sizeOnWheel(sm, 2));
        assertArrayEquals(new String[] {"red", "red"}, sm.configuration());

        // 6. Parte C: hay jackpot por el color, aunque un símbolo shy esté escondido.
        sm.placeSymbol(1, "yellow");
        sm.placeSymbol(2, "yellow");
        assertArrayEquals(new String[] {"yellow", "yellow"}, sm.configuration());
        assertEquals(1, sm.distinctSymbols());
        assertTrue(sm.isJackpot());
        assertFalse(shownOnWheel(sm, 1));

        // 7. Al seleccionar de nuevo el de la rueda 2 cambia si se ve o no, y el jackpot sigue.
        boolean before = shownOnWheel(sm, 2);
        sm.placeSymbol(2, "yellow");
        assertEquals(!before, shownOnWheel(sm, 2));
        assertTrue(sm.isJackpot());
    }

    // ------------------------------------------------------------------ auxiliares

    /** Tamaño del símbolo que muestra la rueda (la primera es la 1). */
    private static int sizeOnWheel(SlotMachine sm, int wheel) {
        return sm.wheelAt(wheel).getCurrentSymbol().getSize();
    }

    /** Dice si el símbolo que muestra la rueda se está dibujando. */
    private static boolean shownOnWheel(SlotMachine sm, int wheel) {
        return sm.wheelAt(wheel).getCurrentSymbol().isShown();
    }
}
