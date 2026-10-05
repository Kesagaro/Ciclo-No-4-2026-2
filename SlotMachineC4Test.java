import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.awt.GraphicsEnvironment;
import java.util.HashSet;
import org.junit.Assume;
import org.junit.Test;

/**
 * Nuestras pruebas del Ciclo 4 (JUnit 4). Revisan que funcionen los tipos de rueda
 * (normal, lefty, rebel, turbo) y de símbolo (normal, ephemeral, shy).
 *
 * <p>Todas usan la máquina invisible, menos la última, que abre la ventana y se
 * salta sola si no hay pantalla. Cada prueba revisa un comportamiento completo,
 * y sus nombres empiezan con should.
 *
 * @author BlancoS-GarzonR
 * @version 1.0
 */
public class SlotMachineC4Test {

    // ---------------------------------------------------------- creación por tipo

    /** addWheel(type, pos) crea cada tipo, no distingue mayúsculas y rechaza los tipos que no existen. */
    @Test
    public void shouldCreateEveryWheelTypeAndRejectUnknownOnes() {
        SlotMachine sm = new SlotMachine();
        sm.addWheel("normal", 1);
        sm.addWheel("LEFTY", 2);
        sm.addWheel(" rebel ", 3);
        sm.addWheel("turbo", 4);
        assertTrue(sm.ok());
        assertArrayEquals(new String[] {"normal", "lefty", "rebel", "turbo"}, sm.wheelTypes());

        sm.addWheel("turbo", 1);                       // la posición también se respeta
        assertEquals("turbo", sm.wheelTypes()[0]);

        int before = sm.wheelTypes().length;
        for (String bad : new String[] {"gigante", "", null}) {
            sm.addWheel(bad, 1);
            assertFalse("tipo '" + bad + "' no debe crearse", sm.ok());
            assertEquals(before, sm.wheelTypes().length);
        }

        sm.addWheel(2);                                // la versión sin tipo sigue creando normales
        assertTrue(sm.ok());
        assertEquals("normal", sm.wheelTypes()[1]);
    }

    /** addSymbol(type, pos, color) crea cada tipo, rechaza tipos o colores inválidos, y delSymbol también borra el tipo. */
    @Test
    public void shouldCreateEverySymbolTypeAndRejectInvalidOnes() {
        SlotMachine sm = new SlotMachine();
        sm.addWheel(1);
        sm.addSymbol("normal", 1, "red");
        sm.addSymbol("Ephemeral", 2, "blue");
        sm.addSymbol("shy", 3, "green");
        sm.addSymbol(4, "yellow");                     // versión sin tipo = normal
        assertTrue(sm.ok());
        assertArrayEquals(new String[] {"red", "blue", "green", "yellow"}, sm.symbols());
        assertArrayEquals(new String[] {"normal", "ephemeral", "shy", "normal"}, sm.symbolTypes());

        sm.addSymbol("magico", 1, "pink");
        assertFalse(sm.ok());
        sm.addSymbol("shy", 1, "red");                 // color repetido
        assertFalse(sm.ok());
        assertEquals(4, sm.symbols().length);
        assertEquals(4, sm.symbolTypes().length);

        sm.delSymbol("blue");
        assertTrue(sm.ok());
        assertArrayEquals(new String[] {"normal", "shy", "normal"}, sm.symbolTypes());
    }

    /** Una rueda que se crea después recibe los símbolos con su tipo, no como normales. */
    @Test
    public void shouldNewWheelsInheritSymbolTypes() {
        SlotMachine sm = new SlotMachine();
        sm.addWheel(1);
        sm.addSymbol("shy", 1, "red");
        sm.addSymbol("ephemeral", 2, "blue");
        sm.addWheel("rebel", 2);

        Wheel late = sm.wheelAt(2);
        assertEquals(2, late.symbolCount());
        sm.placeSymbol(2, "red");
        assertEquals("shy", late.getCurrentSymbol().getType());
        sm.placeSymbol(2, "blue");
        assertEquals("ephemeral", late.getCurrentSymbol().getType());
    }

    // ------------------------------------------------------------------- lefty

    /** La lefty copia a la rueda de su izquierda en spin(w), spin(w,k) y spin(); si no tiene vecina, gira normal. */
    @Test
    public void shouldLeftyCopyTheWheelOnItsLeft() {
        SlotMachine sm = machine(new String[] {"normal", "lefty"}, "red", "blue", "yellow");
        sm.placeSymbol(1, "blue");
        sm.placeSymbol(2, "red");

        sm.spin(2);                                    // la zurda se coloca en lo que muestra la 1
        assertArrayEquals(new String[] {"blue", "blue"}, sm.configuration());
        assertTrue(sm.isJackpot());

        sm.spin(1);                                    // la 1 avanza; la 2 no se mueve sola
        assertArrayEquals(new String[] {"yellow", "blue"}, sm.configuration());
        sm.spin(2, 3);                                 // con pasos también copia, no avanza 3
        assertArrayEquals(new String[] {"yellow", "yellow"}, sm.configuration());

        sm.spin();                                     // todas: primero gira la 1, luego la 2 la copia
        assertArrayEquals(new String[] {"red", "red"}, sm.configuration());

        sm.lock(2);                                    // bloqueada no copia
        sm.spin(1);
        sm.spin(2);
        assertFalse(sm.ok());
        assertArrayEquals(new String[] {"blue", "red"}, sm.configuration());
    }

    /** La lefty siempre copia a la vecina que tiene en ese momento, aunque se eliminen ruedas. */
    @Test
    public void shouldLeftyFollowTheCurrentLeftNeighbour() {
        SlotMachine sm = machine(new String[] {"lefty", "normal", "lefty"}, "red", "blue", "yellow");
        sm.placeSymbol(1, "red");
        sm.placeSymbol(2, "blue");
        sm.placeSymbol(3, "yellow");

        sm.spin(1);                                    // la zurda de la posición 1 no tiene vecina
        assertEquals("blue", sm.configuration()[0]);
        sm.spin(3);                                    // la 3 copia a la 2
        assertEquals("blue", sm.configuration()[2]);

        sm.delWheel(2);                                // ahora la 2 (zurda) queda junto a la zurda 1
        sm.placeSymbol(1, "red");
        sm.spin(2);
        assertEquals("red", sm.configuration()[1]);
        assertEquals(2, sm.wheelTypes().length);

        sm.delWheel(1);                                // queda una sola zurda: gira normal
        sm.placeSymbol(1, "red");
        sm.spin(1);
        assertEquals("blue", sm.configuration()[0]);
    }

    // ------------------------------------------------------------------- rebel

    /** La rebel rechaza lock, swap y delWheel sin cambiar nada, pero sí gira. */
    @Test
    public void shouldRebelRefuseLockSwapAndDelete() {
        SlotMachine sm = machine(new String[] {"normal", "rebel", "normal"}, "red", "blue", "yellow");
        sm.placeSymbol(1, "red");
        sm.placeSymbol(2, "blue");
        sm.placeSymbol(3, "yellow");
        String[] before = sm.configuration();

        assertEquals(0, sm.lock(2));
        assertFalse(sm.ok());
        assertFalse(sm.wheelAt(2).isLocked());

        sm.swap(1, 2);
        assertFalse(sm.ok());
        sm.swap(2, 3);
        assertFalse(sm.ok());
        sm.delWheel(2);
        assertFalse(sm.ok());
        assertArrayEquals(new String[] {"normal", "rebel", "normal"}, sm.wheelTypes());
        assertArrayEquals(before, sm.configuration());

        sm.swap(1, 3);                                 // las normales sí se dejan
        assertTrue(sm.ok());
        sm.spin(2);                                    // y la rebelde gira
        assertTrue(sm.ok());
        assertEquals("yellow", sm.configuration()[1]);
        sm.unlock(2);
        assertTrue(sm.ok());
    }

    /** lock devuelve cuántas ruedas quedan bloqueadas, incluso cuando falla. */
    @Test
    public void shouldLockReturnTheNumberOfLockedWheels() {
        SlotMachine empty = new SlotMachine();
        assertEquals(0, empty.lock(1));
        assertFalse(empty.ok());

        SlotMachine sm = machine(new String[] {"normal", "normal", "rebel", "turbo"}, "red", "blue");
        assertEquals(1, sm.lock(1));
        assertEquals(2, sm.lock(2));
        assertEquals(2, sm.lock(2));                   // repetir no suma
        assertTrue(sm.ok());
        assertEquals(2, sm.lock(3));                   // la rebelde falla; siguen 2
        assertFalse(sm.ok());
        assertEquals(3, sm.lock(4));
        sm.unlock(1);
        assertEquals(2, sm.lock(7));                   // fuera de rango: usa la última (turbo, ya bloqueada)
        assertTrue(sm.wheelAt(2).isLocked());
        assertFalse(sm.wheelAt(1).isLocked());
    }

    // ------------------------------------------------------------------- turbo

    /** La turbo avanza el doble en spin(w), spin(w,k) y spin(). */
    @Test
    public void shouldTurboAdvanceTwiceAsFar() {
        SlotMachine sm = machine(new String[] {"turbo", "normal"}, "red", "blue", "yellow", "green", "lime");
        sm.spin(1);
        assertEquals("yellow", sm.configuration()[0]);   // 0 + 2
        sm.spin(1, 2);
        assertEquals("blue", sm.configuration()[0]);     // 2 + 4 = 6 -> 1
        sm.spin();
        assertEquals("green", sm.configuration()[0]);    // 1 + 2
        assertEquals("blue", sm.configuration()[1]);     // la normal avanza uno
        sm.lock(1);
        sm.spin(1);
        assertFalse(sm.ok());
        assertEquals("green", sm.configuration()[0]);
    }

    // ----------------------------------------------------------------- símbolos

    /** El ephemeral se encoge hasta ser un punto, solo cuando un giro lo deja seleccionado, y no vuelve a crecer. */
    @Test
    public void shouldEphemeralShrinkToADotAndStayThere() {
        EphemeralSymbol e = new EphemeralSymbol("red");
        int[] expected = {48, 40, 32, 24, 16, 8, 4, 4, 4};
        for (int size : expected) {
            assertEquals(size, e.getSize());
            e.onSpin();
        }
        e.onPlaced();                                  // placeSymbol no lo encoge ni lo recupera
        assertEquals(EphemeralSymbol.MIN_SIZE, e.getSize());
        assertTrue(e.isShown());

        SlotMachine sm = new SlotMachine();
        sm.addWheel(1);
        sm.addSymbol("ephemeral", 1, "red");
        sm.addSymbol(2, "blue");
        Symbol red = sm.wheelAt(1).getCurrentSymbol();
        assertEquals(48, red.getSize());
        sm.spin(1);                                    // llega blue: red no cambia
        assertEquals(48, red.getSize());
        sm.spin(1);                                    // vuelve red: se encoge
        assertEquals(40, red.getSize());
        sm.spin(1, 4);                                 // dos vueltas más: dos encogimientos
        assertEquals(24, red.getSize());
        sm.placeSymbol(1, "red");
        assertEquals(24, red.getSize());
    }

    /** El shy se esconde y se muestra cada vez que lo seleccionan (giro o placeSymbol), y su color sigue contando. */
    @Test
    public void shouldShyToggleOnEverySelectionButKeepItsColor() {
        ShySymbol s = new ShySymbol("red");
        assertTrue(s.isShown());
        s.onSpin();
        assertFalse(s.isShown());
        s.onPlaced();
        assertTrue(s.isShown());
        s.onSpin();
        s.onSpin();
        assertTrue(s.isShown());

        SlotMachine sm = new SlotMachine();
        sm.addWheel(1);
        sm.addWheel(2);
        sm.addSymbol("shy", 1, "red");
        sm.addSymbol(2, "blue");
        sm.spin();                                     // las dos pasan a blue (normal)
        sm.spin();                                     // vuelven a red: las dos shy se esconden
        assertFalse(sm.wheelAt(1).getCurrentSymbol().isShown());
        assertFalse(sm.wheelAt(2).getCurrentSymbol().isShown());
        assertArrayEquals(new String[] {"red", "red"}, sm.configuration());
        assertEquals(1, sm.distinctSymbols());
        assertTrue(sm.isJackpot());                    // escondido o no, el color cuenta

        sm.placeSymbol(1, "red");                      // seleccionarlo otra vez lo muestra
        assertTrue(sm.wheelAt(1).getCurrentSymbol().isShown());
    }

    // -------------------------------------------------------------- transversales

    /** Con tipos mezclados, el jackpot y los errores funcionan igual que antes. */
    @Test
    public void shouldKeepJackpotAndErrorRulesWithMixedTypes() {
        SlotMachine sm = machine(new String[] {"normal", "lefty", "lefty", "rebel"}, "red", "blue", "yellow");
        assertTrue(sm.isJackpot());                    // todas empiezan en la primera posición
        sm.placeSymbol(4, "yellow");
        assertFalse(sm.isJackpot());
        sm.spin();                                     // la 1 avanza y las zurdas la copian en cadena
        assertArrayEquals(new String[] {"blue", "blue", "blue", "red"}, sm.configuration());
        assertEquals(2, sm.distinctSymbols());         // solo la rebelde queda aparte
        sm.spin(4, 1);                                 // la rebelde alcanza a las demás
        assertTrue(sm.isJackpot());

        sm.spin(new String[] {"red", "blue", "yellow", "red"});
        assertTrue(sm.ok());
        assertEquals(3, sm.distinctSymbols());
        sm.placeSymbol(2, "naranja");
        assertFalse(sm.ok());
    }

    /** Cada tipo se distingue a simple vista: un color de marco por rueda y una figura por símbolo. */
    @Test
    public void shouldEveryTypeBeVisuallyDistinguishable() {
        HashSet<String> frames = new HashSet<String>();
        for (String type : WheelFactory.types()) {
            Wheel w = WheelFactory.create(type, 0, 0);
            assertNotNull(w);
            assertEquals(type, w.getType());
            assertNotEquals("el rojo es del bloqueo", "red", w.frameColor());
            assertTrue("marco repetido en " + type, frames.add(w.frameColor()));
        }
        assertEquals(WheelFactory.types().length, frames.size());

        HashSet<Class<?>> shapes = new HashSet<Class<?>>();
        for (String type : SymbolFactory.types()) {
            Symbol s = SymbolFactory.create(type, "red");
            assertEquals(type, s.getType());
            shapes.add(s.getClass());
        }
        assertEquals(SymbolFactory.types().length, shapes.size());
        assertNull(WheelFactory.create("otro", 0, 0));
        assertNull(SymbolFactory.create(null, "red"));
        assertTrue(WheelFactory.isValidType("Rebel"));
        assertFalse(SymbolFactory.isValidType("turbo"));
    }

    /** Con la ventana abierta, una máquina con todos los tipos gira, se oculta y se cierra sin errores. */
    @Test(timeout = 60000)
    public void shouldDrawAndSpinEveryTypeWithTheWindowOpen() {
        Assume.assumeFalse("sin pantalla", GraphicsEnvironment.isHeadless());
        SlotMachine sm = machine(new String[] {"normal", "lefty", "rebel", "turbo"}, "red", "blue", "yellow");
        sm.addSymbol("shy", 4, "cyan");
        sm.addSymbol("ephemeral", 5, "pink");
        try {
            sm.makeVisible();
            sm.spin(1, 1);
            sm.spin();
            sm.lock(1);
            assertTrue(sm.ok());
            sm.makeInvisible();
            sm.lock(3);                                // rebel: se rechaza (invisible, sin diálogo de error)
            assertFalse(sm.ok());
            assertEquals(4, sm.wheelTypes().length);
        } finally {
            sm.exit();
        }
    }

    // ------------------------------------------------------------------ auxiliares

    /** Arma una máquina con ruedas de esos tipos y símbolos normales de esos colores. */
    private static SlotMachine machine(String[] wheelTypes, String... colors) {
        SlotMachine sm = new SlotMachine();
        for (int i = 0; i < wheelTypes.length; i++) sm.addWheel(wheelTypes[i], i + 1);
        for (int i = 0; i < colors.length; i++) sm.addSymbol(i + 1, colors[i]);
        assertTrue(sm.ok());
        return sm;
    }
}
