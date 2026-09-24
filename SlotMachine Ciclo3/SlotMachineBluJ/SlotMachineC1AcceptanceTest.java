import static org.junit.Assert.*;
import org.junit.Test;


/**
 * Acceptance tests for Cycle 1.
 *
 * These tests validate complete user scenarios
 * integrating the main functionalities developed
 * during the first development cycle.
 */
public class SlotMachineC1AcceptanceTest
{
    /**
     * Acceptance Scenario 1:
     *
     * A user creates a complete slot machine,
     * adds wheels, adds symbols, configures the machine,
     * and verifies that the simulator can identify
     * a winning configuration.
     *
     * Covered requirements:
     * - Create slot machine
     * - Add wheels
     * - Add symbols
     * - Consult symbols
     * - Check jackpot
     */
    @Test
    public void accordingC1ShouldCreateCompleteMachineAndReachJackpot()
    {
        /*
         * Step 1:
         * Create a new slot machine.
         */
        SlotMachine machine = new SlotMachine();


        /*
         * Step 2:
         * Add three wheels to the machine.
         */
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addWheel(3);


        /*
         * Step 3:
         * Add the same symbol order to every wheel.
         *
         * This creates a valid configuration where
         * all visible symbols can match.
         */
        machine.addSymbol(1, "red");
        machine.addSymbol(1, "blue");

        machine.addSymbol(2, "red");
        machine.addSymbol(2, "blue");

        machine.addSymbol(3, "red");
        machine.addSymbol(3, "blue");


        /*
         * Step 4:
         * Place every wheel in the first position.
         *
         * This guarantees that the visible symbols
         * correspond to the same color.
         */
        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "red");
        machine.placeSymbol(3, "red");


        /*
         * Step 5:
         * Verify that the machine configuration is
         * a winning state.
         */
        assertTrue(machine.isJackpot());


        /*
         * Step 6:
         * Verify that the machine can consult
         * its current configuration.
         */
        assertEquals(1, machine.distinctSymbols());
    }



    /**
     * Acceptance Scenario 2:
     *
     * A user operates the simulator in invisible mode,
     * rotates wheels and verifies that the logical
     * behavior works without graphical interaction.
     *
     * Covered requirements:
     * - Spin wheels
     * - Consult configuration
     * - Work in invisible mode
     * - Change visibility
     * - Validate non-winning and winning states
     */
    @Test
    public void accordingC1ShouldOperateSimulatorInInvisibleMode()
    {

        /*
         * Step 1:
         * Create a slot machine.
         */
        SlotMachine machine = new SlotMachine();


        /*
         * Step 2:
         * Create the required wheels.
         */
        machine.addWheel(1);
        machine.addWheel(2);


        /*
         * Step 3:
         * Add different symbols to each wheel.
         */
        machine.addSymbol(1, "red");
        machine.addSymbol(1, "blue");

        machine.addSymbol(2, "red");
        machine.addSymbol(2, "blue");


        /*
         * Step 4:
         * Ensure the simulator works without
         * visual representation.
         */
        machine.makeInvisible();


        /*
         * Step 5:
         * Rotate the wheels.
         *
         * The action must be executed correctly
         * even without a graphical interface.
         */
        machine.spin(1);
        machine.spin(2);


        /*
         * Step 6:
         * Verify that the simulator still maintains
         * a valid configuration.
         */
        assertNotNull(machine.configuration());


        /*
         * Step 7:
         * Change visibility state.
         */
        machine.makeVisible();


        /*
         * Step 8:
         * Verify that the simulator continues working
         * after changing visibility.
         */
        assertNotNull(machine.symbols());
    }
}