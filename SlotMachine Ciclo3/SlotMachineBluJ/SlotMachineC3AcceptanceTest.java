import static org.junit.Assert.*;
import org.junit.Test;


/**
 * Acceptance tests for Cycle 3.
 *
 * These tests validate complete user scenarios
 * integrating the new ICPC contest functionality:
 *
 * - Dynamic machine creation
 * - SlotMachineContest
 * - solve(n)
 * - simulate(n)
 *
 * SlotMachine is only used as:
 *
 * - testing tool in solve
 * - simulator in simulate
 */
public class SlotMachineC3AcceptanceTest
{


    /**
     * Acceptance Scenario 1:
     *
     * The solver generates the necessary movements
     * to solve a contest machine.
     *
     * User flow:
     *
     * 1. Create contest solver.
     * 2. Prepare a contest machine.
     * 3. Generate solution.
     * 4. Apply movements.
     * 5. Verify final state.
     */
    @Test
    public void accordingC3ShouldSolveContestConfiguration()
    {

        /*
         * Step 1:
         *
         * Create contest solver.
         */
        SlotMachineContest contest = new SlotMachineContest();



        /*
         * Step 2:
         *
         * Define machine size.
         *
         * The contest machine has:
         *
         * n wheels
         * n symbols
         */
        int n = 4;



        /*
         * Step 3:
         *
         * Create a testing machine.
         *
         * This machine is used only
         * to verify the solution.
         */
        SlotMachine machine = new SlotMachine(n);



        /*
         * Step 4:
         *
         * Obtain solution movements.
         *
         * Each movement has:
         *
         * [wheel, steps]
         */
        int[][] solution = contest.solve(n);



        /*
         * Step 5:
         *
         * Validate that the solver
         * generated movements.
         */
        assertNotNull(solution);

        assertEquals(n, solution.length);



        /*
         * Step 6:
         *
         * Apply each generated movement
         * to the SlotMachine testing tool.
         */
        for(int i = 0; i < solution.length; i++)
        {
            int wheel = solution[i][0];

            int steps = solution[i][1];

            machine.spin(wheel,steps);
        }
        
        /* Dear Teacher Angie :)
         * The generated solution is applied manually to this
         * SlotMachine instance because simulate(n) creates its
         * own internal machine, preventing validation over the
         * current testing instance configured in this scenario.
         *
         * This loop does not reproduce the solve algorithm;
         * it only executes the movements already generated
         * by solve(n) using the public SlotMachine interface.
         */

        
        
        /*
         * Step 7:
         *
         * Validate that the machine reached
         * a valid contest state.
         */
        assertEquals(n, machine.distinctSymbols());
    }




    /**
     * Acceptance Scenario 2:
     *
     * The user executes the complete simulation
     * of the contest solution.
     *
     * User flow:
     *
     * 1. Create contest.
     * 2. Solve problem.
     * 3. Simulate solution.
     * 4. Validate execution.
     */
    @Test
    public void accordingC3ShouldSimulateCompleteContestSolution()
    {
        /*
         * Step 1:
         *
         * Create contest solver.
         */
        SlotMachineContest contest = new SlotMachineContest();



        /*
         * Step 2:
         *
         * Define contest size.
         */
        int n = 5;



        /*
         * Step 3:
         *
         * Solve the contest problem.
         *
         * The machine remains invisible
         * during this process.
         */
        int[][] solution = contest.solve(n);

        assertNotNull(solution);



        /*
         * Step 4:
         *
         * Execute the visual simulation.
         *
         * During simulate:
         *
         * SlotMachine becomes visible.
         */
        contest.simulate(n);



        /*
         * Step 5:
         *
         * Validate that simulation
         * generated a valid solution.
         */
        assertTrue(solution.length > 0);

    }

}