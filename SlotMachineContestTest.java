import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import org.junit.After;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

/**
 * Pruebas unitarias (JUnit 4) del Ciclo 3 para SlotMachineContest,
 * SlotMachine(n), distinctSymbols() y el Canvas extendido.
 * Se ejecutan desde BlueJ con <i>Run Tests</i>.
 *
 * <p>Para comprobar que solve realmente resuelve el problema, las pruebas resuelven una
 * máquina conocida con solveOn, comprueban el jackpot y repiten la lista de acciones sobre
 * una réplica con la misma configuración inicial (ver solveAndVerify).
 *
 * <p>La prueba que necesita mostrar la ventana usa Assume y se omite si no hay pantalla.
 *
 * @author BlancoS-GarzonR
 * @version 5.0
 */
public class SlotMachineContestTest {

    private static final int ACTION_LIMIT = 10000;

    @Before
    public void hideWindowBefore() {
        Canvas.setCanvasVisible(false);
    }

    @After
    public void hideWindowAfter() {
        Canvas.setCanvasVisible(false);
    }

    /**
     * SlotMachine(n) crea n ruedas y n símbolos distintos (también más allá de la paleta de 50),
     * con n menor que 1 crea una sola rueda, empieza en posiciones aleatorias y todas las ruedas
     * comparten el mismo orden circular de símbolos.
     */
    @Test
    public void shouldCreateMachinesWithNWheelsAndNDistinctSymbols() {
        int[] sizes = {1, 2, 5, 10, 50, 120};
        for (int n : sizes) {
            SlotMachine sm = new SlotMachine(n);
            List<String> symbols = Arrays.asList(sm.symbols());
            assertEquals("ruedas con n=" + n, n, sm.configuration().length);
            assertEquals("símbolos con n=" + n, n, symbols.size());
            assertEquals("nombres distintos con n=" + n, n, new HashSet<String>(symbols).size());
            HashSet<Integer> rgbs = new HashSet<Integer>();
            for (String s : symbols) {
                assertNotEquals("el símbolo " + s + " se pintaría negro", java.awt.Color.black, Canvas.colorOf(s));
                rgbs.add(Integer.valueOf(Canvas.colorOf(s).getRGB()));
            }
            assertEquals("colores RGB distintos con n=" + n, n, rgbs.size());
            for (String color : sm.configuration()) {
                assertTrue("el color " + color + " no es un símbolo de la máquina", symbols.contains(color));
            }
        }
        for (int n : new int[]{0, -4}) {
            SlotMachine sm = new SlotMachine(n);
            assertEquals("ruedas con n=" + n, 1, sm.configuration().length);
            assertTrue("una sola rueda es jackpot", sm.isJackpot());
        }

        HashSet<String> configuraciones = new HashSet<String>();
        HashSet<String> coloresRueda1 = new HashSet<String>();
        for (int i = 0; i < 300; i++) {
            String[] config = new SlotMachine(6).configuration();
            configuraciones.add(Arrays.toString(config));
            coloresRueda1.add(config[0]);
        }
        assertTrue("la configuración inicial debe ser aleatoria", configuraciones.size() > 1);
        assertEquals("la rueda 1 debería mostrar los 6 símbolos en 300 máquinas", 6, coloresRueda1.size());

        int n = 7;
        SlotMachine sm = new SlotMachine(n);
        List<String> orden = Arrays.asList(sm.symbols());
        int[] posicion = new int[n];
        for (int w = 0; w < n; w++) posicion[w] = orden.indexOf(sm.configuration()[w]);
        for (int paso = 1; paso <= 2 * n; paso++) {
            for (int w = 1; w <= n; w++) sm.spin(w, 1);
            for (int w = 0; w < n; w++) {
                assertEquals("rueda " + (w + 1) + " tras " + paso + " pasos",orden.get((posicion[w] + paso) % n), sm.configuration()[w]);
            }
        }
    }

    /** distinctSymbols cuenta los colores distintos visibles (no los definidos) y vale 1 solo con jackpot. */
    @Test
    public void shouldDistinctSymbolsCountVisibleColors() {
        SlotMachine sm = new SlotMachine(4);
        sm.spin(new String[]{"red", "red", "blue", "blue"});
        assertEquals("dos grupos", 2, sm.distinctSymbols());
        sm.spin(new String[]{"red", "blue", "yellow", "green"});
        assertEquals("todas distintas", 4, sm.distinctSymbols());
        sm.spin(new String[]{"yellow", "yellow", "yellow", "yellow"});
        assertEquals("todas iguales", 1, sm.distinctSymbols());
        sm.spin(1, 1);
        assertEquals("girar una rueda separa una de las cuatro", 2, sm.distinctSymbols());
        assertEquals("sin ruedas no hay nada visible", 0, new SlotMachine().distinctSymbols());

        Random azar = new Random(12345);
        for (int n = 1; n <= 12; n++) {
            for (int intento = 0; intento < 50; intento++) {
                SlotMachine m = new SlotMachine(n);
                for (int giros = 0; giros < 5; giros++) {
                    int esperado = new HashSet<String>(Arrays.asList(m.configuration())).size();
                    assertEquals("n=" + n + " " + Arrays.toString(m.configuration()), esperado, m.distinctSymbols());
                    assertEquals("jackpot <=> 1 distinto", esperado == 1, m.isJackpot());
                    m.spin(1 + azar.nextInt(n), azar.nextInt(n + 1));
                }
            }
        }
    }

    /** colorOf reconoce los nombres CSS (sin importar mayúsculas), los hexadecimales y da negro a lo inválido. */
    @Test
    public void shouldCanvasRecognizeCssColorNames() {
        assertEquals(new java.awt.Color(255, 127, 80), Canvas.colorOf("coral"));
        assertEquals(new java.awt.Color(165, 42, 42), Canvas.colorOf("brown"));
        assertEquals(new java.awt.Color(0, 128, 0), Canvas.colorOf("green"));
        assertEquals(new java.awt.Color(0, 255, 0), Canvas.colorOf("lime"));
        assertEquals(new java.awt.Color(250, 128, 114), Canvas.colorOf("salmon"));
        assertEquals("mayúsculas", Canvas.colorOf("salmon"), Canvas.colorOf("SaLmOn"));
        assertEquals("hexadecimal", Canvas.colorOf("salmon"), Canvas.colorOf("#FA8072"));
        assertNotEquals("lightgray y silver son distintos", Canvas.colorOf("silver"), Canvas.colorOf("lightgray"));
        assertEquals("nombre inventado", java.awt.Color.black, Canvas.colorOf("noExisteEsteColor"));
        assertEquals("hexadecimal inválido", java.awt.Color.black, Canvas.colorOf("#GGGGGG"));
        assertEquals("null", java.awt.Color.black, Canvas.colorOf(null));
    }

    /** solve devuelve acciones bien formadas (rueda en [1,n], pasos en [1,n-1]) y un arreglo vacío si n es menor que 2. */
    @Test(timeout = 60000)
    public void shouldSolveReturnWellFormedActions() {
        for (int n = 2; n <= 15; n++) {
            for (int intento = 0; intento < 100; intento++) {
                int[][] actions = SlotMachineContest.solve(n);
                assertNotNull("solve(" + n + ") no debe retornar null", actions);
                assertTrue("solve(" + n + ") usó " + actions.length + " acciones", actions.length <= ACTION_LIMIT);
                for (int[] action : actions) {
                    assertEquals("cada acción es {rueda, pasos}", 2, action.length);
                    assertTrue("rueda " + action[0] + " fuera de [1," + n + "]", action[0] >= 1 && action[0] <= n);
                    assertTrue("pasos " + action[1] + " fuera de [1," + (n - 1) + "]", action[1] >= 1 && action[1] <= n - 1);
                }
            }
        }
        for (int n : new int[]{1, 0, -3, Integer.MIN_VALUE}) {
            assertEquals("solve(" + n + ") no necesita acciones", 0, SlotMachineContest.solve(n).length);
        }
    }

    /** Casos conocidos: máquina ya en jackpot (no cambia), dos ruedas (una acción) y un ejemplo resuelto a mano. */
    @Test
    public void shouldSolveKnownCases() {
        for (int n = 2; n <= 10; n++) {
            String[] iguales = new String[n];
            Arrays.fill(iguales, new SlotMachine(n).symbols()[n - 1]);
            SlotMachine yaGanadora = machineWith(iguales);
            assertEquals("ya en jackpot, n=" + n, 0, SlotMachineContest.solveOn(yaGanadora, n).length);
            assertArrayEquals("no debe cambiar, n=" + n, iguales, yaGanadora.configuration());
        }

        SlotMachine dos = machineWith("red", "blue");
        int[][] unaAccion = SlotMachineContest.solveOn(dos, 2);
        assertEquals(1, unaAccion.length);
        assertArrayEquals(new int[]{1, 1}, unaAccion[0]);
        assertTrue(dos.isJackpot());

        SlotMachine sm = machineWith("red", "red", "blue", "yellow");
        int[][] actions = SlotMachineContest.solveOn(sm, 4);
        assertEquals("16 giros de barrido + 2 de alineación", 18, actions.length);
        for (int i = 0; i < 4 * 4; i++) {
            assertArrayEquals("giro de barrido " + i, new int[]{i / 4 + 1, 1}, actions[i]);
        }
        assertArrayEquals("rueda 3 se adelanta 3 pasos", new int[]{3, 3}, actions[16]);
        assertArrayEquals("rueda 4 se adelanta 2 pasos", new int[]{4, 2}, actions[17]);
        assertArrayEquals(new String[]{"red", "red", "red", "red"}, sm.configuration());
    }

    /** Todas las configuraciones posibles de 2 a 6 ruedas (50 068 máquinas) terminan en jackpot con una lista fiel. */
    @Test(timeout = 240000)
    public void shouldSolveEveryPossibleConfigurationUpToSixWheels() {
        for (int n = 2; n <= 6; n++) {
            String[] symbols = new SlotMachine(n).symbols();
            int total = (int) Math.round(Math.pow(n, n));
            for (int codigo = 0; codigo < total; codigo++) {
                String[] config = new String[n];
                int resto = codigo;
                for (int w = 0; w < n; w++) {
                    config[w] = symbols[resto % n];
                    resto /= n;
                }
                solveAndVerify(machineWith(config), n, "configuración " + Arrays.toString(config));
            }
        }
    }

    /** Máquinas aleatorias (n de 2 a 25) y configuraciones simétricas o regulares (n de 2 a 20) llegan a jackpot. */
    @Test(timeout = 240000)
    public void shouldSolveRandomAndSymmetricConfigurations() {
        for (int n = 2; n <= 25; n++) {
            for (int intento = 0; intento < 100; intento++) {
                solveAndVerify(new SlotMachine(n), n, "máquina aleatoria n=" + n);
            }
        }
        for (int n = 2; n <= 20; n++) {
            String[] symbols = new SlotMachine(n).symbols();
            for (int paso = 0; paso < n; paso++) {
                String[] progresion = new String[n];
                for (int w = 0; w < n; w++) progresion[w] = symbols[(w * paso) % n];
                solveAndVerify(machineWith(progresion), n, "progresión de paso " + paso + " con n=" + n);
            }
            for (int periodo = 1; periodo < n; periodo++) {
                String[] repetida = new String[n];
                for (int w = 0; w < n; w++) repetida[w] = symbols[(w % periodo) * (n / periodo)];
                solveAndVerify(machineWith(repetida), n, "patrón de periodo " + periodo + " con n=" + n);
            }
        }
    }

    /** Con 50 ruedas (el máximo del problema) y con más de 50: jackpot sin superar 10 000 acciones, y como mucho dos barridos. */
    @Test(timeout = 240000)
    public void shouldSolveLargeMachinesWithinTheActionLimit() {
        for (int n : new int[]{25, 50}) {
            int maximo = 2 * (n * n + n);
            for (int intento = 0; intento < 20; intento++) {
                int[][] actions = solveAndVerify(new SlotMachine(n), n, "n=" + n);
                assertTrue("n=" + n + ": " + actions.length + " acciones, máximo esperado " + maximo, actions.length <= maximo);
            }
        }
        solveAndVerify(machineWith(new SlotMachine(50).symbols()), 50, "50 símbolos distintos");
        for (int n : new int[]{51, 70}) {
            solveAndVerify(new SlotMachine(n), n, "más de 50 ruedas, n=" + n);
        }
    }

    /** Una máquina invisible nunca crea la ventana: ni solve, ni simulate con n inválido, ni exit, ni girar o bloquear. */
    @Test(timeout = 60000)
    public void shouldNotCreateAWindowWhileTheMachineIsInvisible() {
        resetCanvas();
        for (int n = 2; n <= 8; n++) SlotMachineContest.solve(n);
        assertFalse("solve creó la ventana", canvasCreated());

        SlotMachineContest.solve(1);
        SlotMachineContest.simulate(1);
        SlotMachineContest.simulate(0);
        SlotMachineContest.simulate(-2);
        assertFalse("n menor que 2 creó la ventana", canvasCreated());

        SlotMachine sm = new SlotMachine(5);
        sm.spin(2, 3);
        sm.spin();
        sm.swap(1, 5);
        sm.lock(3);
        sm.unlock(3);
        assertFalse("girar una máquina invisible creó la ventana", canvasCreated());

        sm.exit();
        assertTrue(sm.ok());
        assertFalse("exit creó una ventana solo para cerrarla", canvasCreated());
    }

    /**
     * Ventana real (requiere pantalla): makeVisible, makeInvisible y makeVisible otra vez la muestran,
     * ocultan y vuelven a mostrar; getCanvas no la fuerza; exit la cierra; y simulate la muestra
     * mientras corre y la oculta al terminar.
     */
    @Test(timeout = 120000)
    public void shouldShowAndHideTheWindowInMakeVisibleAndSimulate() throws InterruptedException {
        Assume.assumeFalse("requiere pantalla", java.awt.GraphicsEnvironment.isHeadless());
        SlotMachine sm = new SlotMachine(3);
        assertFalse("recién creada es invisible", windowVisible());
        sm.makeVisible();
        assertTrue("makeVisible", windowVisible());
        sm.makeInvisible();
        assertFalse("makeInvisible", windowVisible());
        Canvas.getCanvas();
        assertFalse("getCanvas no debe forzar la visibilidad", windowVisible());
        sm.makeVisible();
        assertTrue("makeVisible otra vez", windowVisible());
        sm.exit();
        assertFalse("exit cierra la ventana", windowVisible());

        final boolean[] vista = {false};
        Thread observador = new Thread(new Runnable() {
            public void run() {
                long limite = System.currentTimeMillis() + 100000;
                while (System.currentTimeMillis() < limite) {
                    if (windowVisible()) {
                        vista[0] = true;
                        return;
                    }
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        return;
                    }
                }
            }
        });
        observador.start();
        SlotMachineContest.simulate(3);
        observador.join(2000);
        assertTrue("la ventana debió estar visible durante simulate", vista[0]);
        assertFalse("simulate debe ocultar la máquina al terminar", windowVisible());
    }

    // ================================================================== ayudas

    /** Crea una máquina con tantas ruedas como colores y la deja en esa configuración. */
    private static SlotMachine machineWith(String... configuration) {
        SlotMachine sm = new SlotMachine(configuration.length);
        sm.spin(configuration);
        assertTrue("configuración inválida " + Arrays.toString(configuration), sm.ok());
        assertArrayEquals(configuration, sm.configuration());
        return sm;
    }

    /**
     * Resuelve la máquina con solveOn y comprueba: forma de las acciones, jackpot final,
     * que no hubo jackpot antes de la última acción, y que repetir las acciones sobre una réplica
     * con la misma configuración inicial da el mismo estado final.
     */
    private static int[][] solveAndVerify(SlotMachine sm, int n, String contexto) {
        String[] inicial = sm.configuration();
        int[][] actions = SlotMachineContest.solveOn(sm, n);
        assertNotNull(contexto, actions);
        assertTrue(contexto + ": jackpot final (inicial " + Arrays.toString(inicial) + ")", sm.isJackpot());
        assertTrue(contexto + ": usó " + actions.length + " acciones", actions.length <= ACTION_LIMIT);

        SlotMachine replica = machineWith(inicial);
        for (int i = 0; i < actions.length; i++) {
            int[] action = actions[i];
            assertEquals(contexto + ": acción " + i + " debe tener 2 valores", 2, action.length);
            assertTrue(contexto + ": rueda inválida " + action[0], action[0] >= 1 && action[0] <= n);
            assertTrue(contexto + ": pasos inválidos " + action[1], action[1] >= 1 && action[1] <= n - 1);
            replica.spin(action[0], action[1]);
            if (i < actions.length - 1) {
                assertFalse(contexto + ": ya había jackpot antes de la última acción (" + i + " de " + actions.length + ")",
                            replica.isJackpot());
            }
        }
        assertTrue(contexto + ": la réplica debe llegar a jackpot", replica.isJackpot());
        assertArrayEquals(contexto + ": la réplica debe quedar igual", sm.configuration(), replica.configuration());
        return actions;
    }

    /** Indica si ya se creó la ventana del simulador (el singleton de Canvas), sin crearla. */
    private static boolean canvasCreated() {
        try {
            java.lang.reflect.Field campoCanvas = Canvas.class.getDeclaredField("canvasSingleton");
            campoCanvas.setAccessible(true);
            return campoCanvas.get(null) != null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /** Cierra la ventana del simulador y olvida el singleton, para que la prueba parta sin ventana. */
    private static void resetCanvas() {
        try {
            Canvas.closeCanvas();
            java.lang.reflect.Field campoCanvas = Canvas.class.getDeclaredField("canvasSingleton");
            campoCanvas.setAccessible(true);
            campoCanvas.set(null, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /** Indica si la ventana del simulador existe y está visible (sin crearla). */
    private static boolean windowVisible() {
        try {
            java.lang.reflect.Field campoCanvas = Canvas.class.getDeclaredField("canvasSingleton");
            campoCanvas.setAccessible(true);
            Object canvas = campoCanvas.get(null);
            if (canvas == null) return false;
            java.lang.reflect.Field campoFrame = Canvas.class.getDeclaredField("frame");
            campoFrame.setAccessible(true);
            return ((javax.swing.JFrame) campoFrame.get(canvas)).isVisible();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
