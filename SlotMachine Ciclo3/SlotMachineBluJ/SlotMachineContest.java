/**
 * Represents the solver and simulator for the Slot Machine contest problem.
 *
 * This class is responsible for computing solutions and using SlotMachine
 * as a simulation tool. It does not contain the machine logic itself.
 */
public class SlotMachineContest
{
    /**
     * Creates a new contest solver.
     */
    public SlotMachineContest()
    {

    }

    /**
     * Computes the sequence of movements required
     * to solve the slot machine contest problem.
     *
     * The solution moves every wheel to the first
     * symbol position.
     *
     * @param n number of wheels and symbols
     * @return sequence of movements [wheel, steps]
     */
    public int[][] solve(int n)
    {
        if(n <= 0){
            return new int[0][0];
        }
    
        int[][] movements = new int[n][2];
    
        for(int i = 0; i < n; i++){
            int wheel = i + 1;
    
            int position = i;
    
            int steps = (n - position) % n;
    
            movements[i][0] = wheel;
            movements[i][1] = steps;
        }
    
        return movements;
    }

    /**
     * Simulates the solution of the contest problem visually.
     *
     * @param n number of wheels and symbols
     */
    public void simulate(int n)
    {
        if(n <= 0)
        {
            return;
        }

        SlotMachine machine = new SlotMachine(n);

        prepContestConfig(machine, n);

        machine.makeVisible();

        int[][] solution = solve(n);

        for(int i = 0; i < solution.length; i++)
        {
            int wheel = solution[i][0];
            int steps = solution[i][1];

            if(steps > 0)
            {
                machine.spin(wheel, steps);
            }
        }
    }

    /**
     * Prepares a slot machine with the initial
     * configuration used by the contest simulation.
     *
     * @param machine machine to configure
     * @param n number of wheels
     */
    private void prepContestConfig(SlotMachine machine, int n)
    {
        for(int wheel = 1; wheel <= n; wheel++){
            if(wheel > 1){
                machine.spin(wheel, wheel - 1);
            }
        }
    }
}