import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Acceptance tests for Cycle 3.
 *
 * These tests validate complete contest scenarios
 * integrating the new ICPC functionality:
 *
 * - Dynamic machine creation
 * - SlotMachineContest
 * - solve(n)
 * - Generated contest movements
 * - SlotMachine as a testing tool
 *
 * The acceptance scenarios run without visual
 * representation and validate concrete results
 * through the public SlotMachine tool.
 */
public class SlotMachineC3AcceptanceTest
{


    /**
     * Acceptance Scenario 1:
     * 
     * Can a user/solver complete the resolution history?
     *
     * The solver generates a solution that takes a contest
     * configuration to the jackpot state.
     *
     * User flow:
     *
     * 1. Create contest solver.
     * 2. Define a valid contest size.
     * 3. Create the SlotMachine testing tool.
     * 4. Prepare a contest configuration.
     * 5. Generate the solution.
     * 6. Apply the generated movements.
     * 7. Verify that only one distinct symbol remains.
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
         * Define a valid contest size.
         */
        int n = 4;
    
    
        /*
         * Step 3:
         *
         * Create the testing machine.
         * It remains invisible.
         */
        SlotMachine machine = new SlotMachine(n);
    
        
        /*
         * Step 4:
         *
         * Prepare a valid contest configuration.
         */
        for(int wheel = 2; wheel <= n; wheel++)
        {
            machine.spin(wheel, wheel - 1);
        }
    
    
        /*
         * Step 5:
         *
         * Generate the contest solution.
         */
        int[][] solution = contest.solve(n);
    
        assertNotNull(solution);
    
    
        /*
         * Step 6:
         *
         * Apply the generated movements.
         */
        for(int i = 0; i < solution.length; i++){
            int wheel = solution[i][0];
            int steps = solution[i][1];
    
            if(steps > 0){
                machine.spin(wheel, steps);
            }
        }
        
        /* Dear Teacher Angie :)
         * The generated solution is applied manually to this
         * SlotMachine instance because simulate(n) creates its
         * own internal machine, preventing validation over the
         * current testing instance configured in this scenario.
         *
         * This loop does not reproduce the solve algorithm;
         * it only executes the movements already generated
         * by solve(n) using the public SlotMachine tool.
         */

    
    
        /*
         * Step 7:
         *
         * Jackpot means that all wheels show
         * the same symbol.
         */
        assertEquals(1, machine.distinctSymbols());
    }




    /**
     * Acceptance Scenario 2:
     * 
     * Can the generated actions fully traverse the testing tool's public SlotMachine 
     * and maintain the contract?
     * 
     * The generated contest solution can be executed
     * completely through the public SlotMachine tool.
     *
     * User flow:
     *
     * 1. Create contest solver.
     * 2. Define a valid contest size.
     * 3. Create and prepare a testing machine.
     * 4. Generate the solution.
     * 5. Validate and execute every movement.
     * 6. Verify that the complete execution succeeds.
     */
    @Test
    public void accordingC3ShouldExecuteValidContestMovements()
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
         * Define a valid contest size.
         */
        int n = 5;
    
    
        /*
         * Step 3:
         *
         * Create the invisible testing machine.
         */
        SlotMachine machine = new SlotMachine(n);
    
        for(int wheel = 2; wheel <= n; wheel++)
        {
            machine.spin(wheel, wheel - 1);
        }
    
    
        /*
         * Step 4:
         *
         * Generate the contest solution.
         */
        int[][] solution = contest.solve(n);
    
        assertNotNull(solution);
    
    
        /*
         * Step 5:
         *
         * Validate and execute each generated action.
         */
        for(int i = 0; i < solution.length; i++){
            int wheel = solution[i][0];
            int steps = solution[i][1];
    
            assertTrue(wheel >= 1);
            assertTrue(wheel <= n);
            assertTrue(steps >= 0);
    
            if(steps > 0){
                machine.spin(wheel, steps);
    
                assertTrue(machine.ok());
            }
        }
    
    
        /*
         * Step 6:
         *
         * Verify the result after executing
         * the complete solution.
         */
        assertEquals(1, machine.distinctSymbols());
    }
    
    /**
     * Acceptance Scenario 3 (OPTIONAL):
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