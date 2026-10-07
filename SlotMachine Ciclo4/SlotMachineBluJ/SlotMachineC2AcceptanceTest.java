import static org.junit.Assert.*;
import org.junit.Test;


/**
 * Acceptance tests for Cycle 2.
 *
 * These tests validate complete user scenarios
 * integrating the new functionalities developed
 * during the second development cycle:
 *
 * - swap wheels
 * - lock and unlock wheels
 * - spin wheels by steps
 * - configure machine state
 *
 * @author Julian Gomez Boada
 */
public class SlotMachineC2AcceptanceTest
{

    /**
     * Acceptance Scenario 1:
     *
     * A user manages wheels during a game.
     *
     * The user creates a machine, blocks one wheel,
     * verifies that it cannot rotate, releases it,
     * rotates again and finally exchanges wheels.
     *
     * Covered requirements:
     *
     * - Fix and release a wheel
     * - Rotate wheels
     * - Exchange wheels
     * - Consult configuration
     */
    @Test
    public void accordingC2ShouldManageWheelOperationsDuringGame()
    {
        /*
         * Step 1:
         *
         * Create an empty slot machine.
         */
        SlotMachine machine = new SlotMachine();



        /*
         * Step 2:
         *
         * Create two wheels.
         */
        machine.addWheel(1);
        machine.addWheel(2);



        /*
         * Step 3:
         *
         * Add symbols to each wheel.
         *
         * Wheel 1:
         * red -> blue
         *
         * Wheel 2:
         * green -> yellow
         */
        machine.addSymbol(1,"red");
        machine.addSymbol(1,"blue");

        machine.addSymbol(2,"green");
        machine.addSymbol(2,"yellow");



        /*
         * Step 4:
         *
         * Save initial configuration.
         */
        String firstWheelBefore = machine.configuration()[0];



        /*
         * Step 5:
         *
         * User locks the first wheel.
         */
        machine.lock(1);



        /*
         * Step 6:
         *
         * User tries to rotate the machine.
         *
         * Expected:
         * The locked wheel does not change.
         */
        machine.spin();

        assertEquals(firstWheelBefore, machine.configuration()[0]);



        /*
         * Step 7:
         *
         * User unlocks the wheel.
         */
        machine.unlock(1);



        /*
         * Step 8:
         *
         * User rotates the first wheel.
         *
         * Expected:
         * The wheel changes its visible symbol.
         */
        machine.spin(1);

        assertNotEquals(firstWheelBefore, machine.configuration()[0]);



        /*
         * Step 9:
         *
         * User exchanges both wheels.
         */
        machine.swap(1,2);



        /*
         * Step 10:
         *
         * Validate that the exchange was successful.
         */
        assertEquals("yellow", machine.configuration()[0]);

        assertEquals("blue", machine.configuration()[1]);

        assertTrue(machine.ok());

    }



    /**
     * Acceptance Scenario 2:
     *
     * A user configures the machine manually,
     * performs a controlled rotation and verifies
     * the final state.
     *
     * Covered requirements:
     *
     * - Leave machine in a given configuration
     * - Rotate wheel a number of steps
     * - Consult configuration
     */
    @Test
    public void accordingC2ShouldConfigureAndRotateMachine()
    {

        /*
         * Step 1:
         *
         * Create machine.
         */
        SlotMachine machine = new SlotMachine();



        /*
         * Step 2:
         *
         * Create two wheels.
         */
        machine.addWheel(1);
        machine.addWheel(2);



        /*
         * Step 3:
         *
         * Add symbols.
         */
        machine.addSymbol(1,"red");
        machine.addSymbol(1,"blue");
        machine.addSymbol(1,"green");


        machine.addSymbol(2,"yellow");
        machine.addSymbol(2,"red");
        machine.addSymbol(2,"blue");



        /*
         * Step 4:
         *
         * User chooses a desired configuration.
         *
         * Expected:
         *
         * Wheel 1 -> blue
         * Wheel 2 -> blue
         */
        machine.spin(new String[]{"blue", "blue"});

        assertEquals("blue", machine.configuration()[0]);

        assertEquals("blue", machine.configuration()[1]);



        /*
         * Step 5:
         *
         * User rotates wheel 1
         * three positions.
         *
         * Wheel 1:
         *
         * blue -> green -> red -> blue
         *
         */
        machine.spin(1,3);



        /*
         * Step 6:
         *
         * Verify that the complete rotation
         * returned the wheel to its original position.
         */
        assertEquals("blue", machine.configuration()[0]);



        /*
         * Step 7:
         *
         * Verify that the machine remains valid.
         */
        assertTrue(machine.ok());

    }

}