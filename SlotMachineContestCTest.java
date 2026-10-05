/**
 * Pruebas de unidad compartidas del Ciclo 3 para SlotMachineContest.
 * Todas las pruebas se ejecutan en modo invisible (sin abrir ventanas).
 *
 * Nombre de cada prueba: accordingBlancoSGarzonRShould...
 *
 * @author BlancoS-GarzonR
 * @version 3.1
 */
public class SlotMachineContestCTest {

    /**
     * Verifica que solve con n=5 retorna una secuencia dentro del límite
     * de 10 000 acciones y con valores válidos (rueda entre 1 y n, pasos positivos).
     *
     * Autores: BlancoS-GarzonR
     */
    public void accordingBlancoSGarzonRShouldSolveReturnValidSequenceForN5() {
        int[][] actions = SlotMachineContest.solve(5);
        assert actions != null : "solve(5) no debe retornar null";
        assert actions.length <= 10000 : "solve(5) supera el límite de 10 000 acciones";
        for (int[] action : actions) {
            assert action.length == 2 : "Cada acción debe tener 2 valores";
            assert action[0] >= 1 && action[0] <= 5 : "Rueda fuera del rango [1,5]";
            assert action[1] > 0 : "Los pasos deben ser positivos";
        }
    }

    /**
     * Verifica que solve con n=3 retorna una secuencia dentro del límite
     * y con valores válidos (rueda entre 1 y n, pasos positivos).
     *
     * Autores: BlancoS-GarzonR
     */
    public void accordingBlancoSGarzonRShouldSolveReturnValidSequenceForN3() {
        int[][] actions = SlotMachineContest.solve(3);
        assert actions != null : "solve(3) no debe retornar null";
        assert actions.length <= 10000 : "solve(3) supera el límite de 10 000 acciones";
        for (int[] action : actions) {
            assert action.length == 2 : "Cada acción debe tener 2 valores";
            assert action[0] >= 1 && action[0] <= 3 : "Rueda fuera del rango [1,3]";
            assert action[1] > 0 : "Los pasos deben ser positivos";
        }
    }
}
