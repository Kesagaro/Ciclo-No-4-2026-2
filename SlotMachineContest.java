import java.util.ArrayList;

/**
 * Resuelve el problema de la tragamonedas de la maratón ICPC 2025.
 *
 * Usa SlotMachine solo como herramienta de prueba y como simulador visual.
 * No contiene la lógica interna del simulador.
 *
 * Para resolver (solve): solo puede crear la máquina, girar ruedas y
 * consultar cuántos símbolos distintos hay visibles.
 *
 * Para simular (simulate): además puede hacer visible o invisible la máquina.
 *
 * Algoritmo: la única información disponible es distinctSymbols(). Si se gira
 * una rueda una vuelta completa, el conteo vale m en las posiciones cuyo símbolo
 * ya tiene alguna otra rueda y m+1 en las demás. Eso revela, para cada rueda,
 * qué símbolos están ocupados vistos desde su propia posición (un "perfil").
 * Como todas las ruedas comparten el mismo orden de símbolos, los perfiles de
 * dos ruedas son el mismo conjunto desplazado; el desplazamiento es la
 * diferencia de posición entre las ruedas. Con esas diferencias se lleva cada
 * rueda al símbolo de la rueda 1.
 *
 * Si el conjunto de símbolos ocupados es simétrico (por ejemplo, cuando todas
 * las ruedas muestran símbolos distintos) el desplazamiento no es único. En ese
 * caso se mueve la rueda 1 hasta que el conteo cambie, lo que rompe la
 * simetría, y se mide de nuevo.
 *
 * Los métodos públicos son estáticos, como indica el diseño del enunciado:
 * SlotMachineContest.solve(n) y SlotMachineContest.simulate(n).
 *
 * @author BlancoS-GarzonR
 * @version 3.2
 */
public class SlotMachineContest {

    /** Límite de acciones que impone el problema de la maratón. */
    private static final int MAX_ACTIONS = 10000;
    /** Tiempo que se deja visible el banner de jackpot al final de simulate. */
    private static final int JACKPOT_PAUSE_MS = 2000;

    /**
     * Resuelve el problema para una máquina de n ruedas y n símbolos,
     * creada con posiciones iniciales aleatorias. La máquina es invisible.
     *
     * Solo usa: crear la máquina, girar ruedas y consultar distintos.
     *
     * @param n cantidad de ruedas y símbolos (mínimo 2)
     * @return lista de acciones {rueda, pasos} aplicadas hasta lograr el jackpot;
     *         vacía si n &lt; 2 (no hay nada que resolver) o si la máquina ya empezó en jackpot
     */
    public static int[][] solve(int n) {
        if (n < 2) return new int[0][];
        return solveOn(new SlotMachine(n), n);
    }

    /**
     * Simula visualmente la solución del problema.
     * Crea su propia máquina con n ruedas, la hace visible y aplica
     * el mismo algoritmo que solve, mostrando la animación de cada giro.
     * Deja el banner de jackpot a la vista un momento y oculta la máquina.
     * Si n &lt; 2 no hay nada que simular y no abre ninguna ventana.
     *
     * Solo usa: crear la máquina, girar ruedas, consultar distintos,
     * hacer visible y hacer invisible.
     *
     * @param n cantidad de ruedas y símbolos (mínimo 2)
     */
    public static void simulate(int n) {
        if (n < 2) return;
        SlotMachine sm = new SlotMachine(n);
        sm.makeVisible();
        align(sm, n, new ArrayList<int[]>());
        pause(JACKPOT_PAUSE_MS);
        sm.makeInvisible();
    }

    /**
     * Aplica el algoritmo sobre una máquina ya creada. Es de paquete para que las
     * pruebas de unidad puedan comprobar el jackpot sobre la misma máquina.
     *
     * @param sm máquina de n ruedas y n símbolos
     * @param n  cantidad de ruedas y símbolos
     * @return lista de acciones {rueda, pasos} aplicadas
     */
    static int[][] solveOn(SlotMachine sm, int n) {
        ArrayList<int[]> actions = new ArrayList<int[]>();
        align(sm, n, actions);
        return actions.toArray(new int[0][]);
    }

    // -------- algoritmo --------

    /** Lleva todas las ruedas al mismo símbolo, anotando cada acción en actions. */
    private static void align(SlotMachine sm, int n, ArrayList<int[]> actions) {
        if (n < 2 || sm.distinctSymbols() == 1) return;
        while (actions.size() + n * n + n <= MAX_ACTIONS) {
            boolean[][] profiles = scanProfiles(sm, n, actions);
            if (profiles == null) return;
            int[] offsets = relativeOffsets(profiles, n);
            if (offsets != null) {
                applyOffsets(sm, n, offsets, actions);
                return;
            }
            if (breakSymmetry(sm, n, actions)) return;
        }
    }

    /**
     * Gira cada rueda una vuelta completa y construye su perfil: profiles[w][x]
     * es verdadero si el símbolo que está x posiciones adelante de la rueda w+1
     * lo muestra alguna rueda. Deja la máquina como estaba.
     *
     * @return los perfiles, o null si durante el barrido se logró el jackpot
     */
    private static boolean[][] scanProfiles(SlotMachine sm, int n, ArrayList<int[]> actions) {
        boolean[][] profiles = new boolean[n][n];
        for (int w = 0; w < n; w++) {
            int[] counts = new int[n];
            counts[0] = sm.distinctSymbols();
            for (int x = 1; x < n; x++) {
                spin(sm, w + 1, 1, actions);
                counts[x] = sm.distinctSymbols();
                if (counts[x] == 1) return null;
            }
            spin(sm, w + 1, 1, actions);
            int min = counts[0];
            for (int c : counts) min = Math.min(min, c);
            profiles[w][0] = true;
            for (int x = 1; x < n; x++) profiles[w][x] = counts[x] == min;
        }
        return profiles;
    }

    /**
     * Calcula, para cada rueda, cuántas posiciones está adelante de la rueda 1.
     *
     * @return arreglo con la diferencia de cada rueda respecto a la rueda 1,
     *         o null si alguna diferencia no es única (conjunto simétrico)
     */
    private static int[] relativeOffsets(boolean[][] profiles, int n) {
        int[] offsets = new int[n];
        for (int w = 1; w < n; w++) {
            int found = -1;
            for (int r = 0; r < n; r++) {
                if (!sameShifted(profiles[w], profiles[0], r, n)) continue;
                if (found >= 0) return null;
                found = r;
            }
            if (found < 0) return null;
            offsets[w] = found;
        }
        return offsets;
    }

    /** Indica si a[x] == b[(x + r) % n] para todo x. */
    private static boolean sameShifted(boolean[] a, boolean[] b, int r, int n) {
        for (int x = 0; x < n; x++) {
            if (a[x] != b[(x + r) % n]) return false;
        }
        return true;
    }

    /** Gira cada rueda de la 2 a la n hasta el símbolo de la rueda 1. */
    private static void applyOffsets(SlotMachine sm, int n, int[] offsets, ArrayList<int[]> actions) {
        for (int w = 2; w <= n; w++) {
            int steps = (n - offsets[w - 1]) % n;
            if (steps > 0) spin(sm, w, steps, actions);
        }
    }

    /**
     * Gira la rueda 1 hasta que cambie el conteo de distintos, lo que modifica
     * el conjunto de símbolos ocupados y rompe su simetría.
     *
     * @return true si con ese giro se logró el jackpot
     */
    private static boolean breakSymmetry(SlotMachine sm, int n, ArrayList<int[]> actions) {
        int before = sm.distinctSymbols();
        for (int step = 1; step < n; step++) {
            spin(sm, 1, 1, actions);
            int now = sm.distinctSymbols();
            if (now == 1) return true;
            if (now != before) return false;
        }
        return false;
    }

    // -------- auxiliares --------

    /** Gira una rueda y anota la acción. */
    private static void spin(SlotMachine sm, int wheel, int steps, ArrayList<int[]> actions) {
        sm.spin(wheel, steps);
        actions.add(new int[]{wheel, steps});
    }

    /** Detiene la ejecución el número de milisegundos indicado. */
    private static void pause(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
