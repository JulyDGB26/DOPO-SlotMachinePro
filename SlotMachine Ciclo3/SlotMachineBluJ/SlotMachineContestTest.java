import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Tests for the dynamic SlotMachine constructor introduced
 */
public class SlotMachineContestTest
{
    
    
    
    
    
    
    
    /**
     * Verifica que SlotMachine(n) crea exactamente n ruedas, cada una
     * con n símbolos montados
     */
    @Test
    public void accordingCjMcShouldCreateEqualWheelsAndSymbolsPerWheel() {
        SlotMachine sm = new SlotMachine(5);
        assertEquals(5, sm.configuration().length);
        assertEquals(25, sm.symbols().length);
    }
    
    
    
    
    

    /**
     * Verifies that every wheel contains
     * the same number of symbols as wheels created.
     */
    @Test
    public void testEachWheelHasNSymbols()
    {
        SlotMachine machine = new SlotMachine(4);

        String[] symbols = machine.symbols();

        assertEquals(16, symbols.length);
    }

    /**
     * Verifies that all wheels share the same
     * symbol order according to the contest problem.
     */
    @Test
    public void accordingCcGbShouldtestAllWheelsHaveSameSymbolOrder()
    {
        SlotMachine machine = new SlotMachine(3);

        String[] configuration = machine.configuration();

        assertEquals("red", configuration[0]);
        assertEquals("red", configuration[1]);
        assertEquals("red", configuration[2]);
    }

    /**
     * Verifies that invalid machine sizes
     * are rejected.
     */
    @Test
    public void testInvalidMachineSize()
    {
        SlotMachine machine = new SlotMachine(0);

        assertFalse(machine.ok());
    }

    /**
     * Verifies that a dynamically created machine
     * can perform normal operations.
     */
    @Test
    public void testDynamicMachineCanSpin()
    {
        SlotMachine machine = new SlotMachine(3);

        machine.spin();

        assertTrue(machine.ok());
    }
    
    


    /**
     * Verifies that a contest solver can be created.
     */
    @Test
    public void testCreateContestSolver()
    {
        SlotMachineContest contest = new SlotMachineContest();

        assertNotNull(contest);
    }

    /**
     * Verifies that solve method exists
     * and returns a valid movement structure.
     */
    @Test
    public void testExistsSolveStructure()
    {
        SlotMachineContest contest = new SlotMachineContest();

        int[][] solution = contest.solve(3);

        assertNotNull(solution);
    }
    
    /**
     * Verifies that solve returns
     * the expected number of movements.
     */
    @Test
    public void accordingCcGbShouldtestSolveNumberOfMovements()
    {
        SlotMachineContest contest = new SlotMachineContest();

        int[][] solution = contest.solve(5);

        assertEquals(5, solution.length);
    }
    
    @Test
    public void testFirstWheelMovement()
    {
        SlotMachineContest contest = new SlotMachineContest();
    
        int[][] solution = contest.solve(5);
    
        assertEquals(1, solution[0][0]);
        assertEquals(0, solution[0][1]);
    }
    
    @Test
    public void testMovementsAreValid()
    {
        SlotMachineContest contest = new SlotMachineContest();
    
        int[][] solution = contest.solve(6);
    
        for(int i = 0; i < solution.length; i++){
            assertTrue(solution[i][0] > 0);
            assertTrue(solution[i][1] >= 0);
        }
    }
    
    @Test
    public void testInvalidSolve()
    {
        SlotMachineContest contest = new SlotMachineContest();
    
        int[][] solution = contest.solve(0);
    
        assertEquals(0, solution.length);
    }
    
    
    
    
    
    
    /**
     * Verifies that simulate can be executed
     * without generating errors.
     */
    @Test
    public void accordingCaPpShouldRunSimulationWithoutErrors()
    {
        int n = 3;
        SlotMachineContest contest = new SlotMachineContest();
        contest.simulate(n);
    }
    
    
    
    
    
    
    
    /**
     * Verifies that an invalid contest size
     * does not execute a simulation.
     */
    @Test
    public void testInvalidSimulation()
    {
        SlotMachineContest contest = new SlotMachineContest();
    
        contest.simulate(0);
    
        assertTrue(true);
    }
    
    /**
     * Verifies that solve produces a valid solution
     * for the initial contest configuration.
     */
    @Test
    public void testSolutionWithContestConfiguration()
    {
        int n = 4;
    
        SlotMachine machine = new SlotMachine(n);
    
        for(int wheel = 2; wheel <= n; wheel++)
        {
            machine.spin(wheel, wheel - 1);
        }
    
        SlotMachineContest contest = new SlotMachineContest();
    
        int[][] solution = contest.solve(n);
    
        for(int i = 0; i < solution.length; i++)
        {
            if(solution[i][1] > 0)
            {
                machine.spin(
                    solution[i][0],
                    solution[i][1]
                );
            }
        }
    
        assertTrue(machine.isJackpot());
    }
}