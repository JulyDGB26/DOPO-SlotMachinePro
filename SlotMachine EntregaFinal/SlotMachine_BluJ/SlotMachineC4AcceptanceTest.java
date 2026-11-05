import static org.junit.Assert.*;
import org.junit.Test;


/**
 * Acceptance tests for Cycle 4.
 *
 * These tests validate complete user scenarios
 * integrating the specialized wheels and symbols
 * developed during the fourth development cycle:
 *
 * - normal, lefty and rebel wheels
 * - normal, ephemeral, shy and pulsing symbols
 * - specialized wheel spinning behavior
 * - restrictions on rebel wheels
 * - preservation of logical configurations
 * - jackpot evaluation with specialized elements
 *
 * The tests also verify that the new functionalities
 * remain compatible with operations developed
 * during previous cycles.
 *
 * @author Julian Gomez Boada
 * @version Cycle 4 - MC5
 */
public class SlotMachineC4AcceptanceTest
{

    /**
     * Acceptance Scenario 1:
     *
     * A user completes a game using specialized
     * wheels and symbols.
     *
     * The user creates a machine with normal,
     * lefty and rebel wheels, adds different
     * symbol types and performs controlled spins.
     *
     * The lefty wheel must copy the configuration
     * of its left neighbor, while the rebel wheel
     * must preserve its ability to spin.
     *
     * Finally, the user reaches a jackpot
     * configuration using specialized symbols.
     *
     * Covered requirements:
     *
     * - Manage different wheel types
     * - Manage different symbol types
     * - Spin specialized wheels
     * - Copy the left wheel configuration
     * - Preserve specialized symbol behavior
     * - Consult machine configuration
     * - Evaluate jackpot
     */
    @Test
    public void accordingC4ShouldCompleteGameWithSpecializedElements()
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
         * Create three specialized wheels.
         *
         * Wheel 1 -> normal
         * Wheel 2 -> lefty
         * Wheel 3 -> rebel
         */
        machine.addWheel("normal", 1);
        machine.addWheel("lefty", 2);
        machine.addWheel("rebel", 3);

        assertEquals(3, machine.wheels());
        assertTrue(machine.ok());



        /*
         * Step 3:
         *
         * Add different symbol types.
         *
         * Wheel 1:
         * normal red -> ephemeral blue
         *
         * Wheel 2:
         * normal red -> pulsing blue
         *
         * Wheel 3:
         * normal red -> shy blue
         *
         * All four symbol types are represented.
         */
        machine.addSymbol("normal", "red", 1);
        machine.addSymbol("ephemeral", "blue", 1);

        machine.addSymbol("normal", "red", 2);
        machine.addSymbol("pulsing", "blue", 2);

        machine.addSymbol("normal", "red", 3);
        machine.addSymbol("shy", "blue", 3);

        assertTrue(machine.ok());



        /*
         * Step 4:
         *
         * Establish a known initial configuration.
         *
         * Expected:
         *
         * Wheel 1 -> red
         * Wheel 2 -> red
         * Wheel 3 -> red
         */
        machine.spin(new String[]{"red", "red", "red"});

        assertArrayEquals(new String[]{"red", "red", "red"}, machine.configuration());

        assertTrue(machine.ok());



        /*
         * Step 5:
         *
         * Verify that the initial configuration
         * is a jackpot.
         */
        assertTrue(machine.isJackpot());



        /*
         * Step 6:
         *
         * The user rotates the normal wheel.
         *
         * Wheel 1:
         *
         * red -> blue
         *
         * The ephemeral symbol is selected
         * as a consequence of the spin.
         */
        machine.spin(1);

        assertTrue(machine.ok());

        assertEquals("blue", machine.configuration()[0]);



        /*
         * Step 7:
         *
         * The user rotates the lefty wheel.
         *
         * Its left neighbor currently displays blue.
         *
         * Expected:
         *
         * The lefty wheel copies blue,
         * selecting its pulsing symbol.
         */
        machine.spin(2);

        assertTrue(machine.ok());

        assertEquals("blue", machine.configuration()[1]);



        /*
         * Step 8:
         *
         * The user rotates the rebel wheel.
         *
         * The rebel wheel can rotate normally,
         * although it cannot be locked,
         * exchanged or deleted.
         *
         * Expected:
         *
         * red -> blue
         *
         * Its shy symbol is selected.
         */
        machine.spin(3);

        assertTrue(machine.ok());

        assertEquals("blue", machine.configuration()[2]);



        /*
         * Step 9:
         *
         * Verify the complete configuration.
         *
         * Expected:
         *
         * Wheel 1 -> blue (ephemeral)
         * Wheel 2 -> blue (pulsing)
         * Wheel 3 -> blue (shy)
         */
        assertArrayEquals(new String[]{"blue", "blue", "blue"}, machine.configuration());



        /*
         * Step 10:
         *
         * Verify jackpot.
         *
         * Specialized visual behavior must
         * not modify the logical identity
         * of the selected symbols.
         */
        assertTrue(machine.isJackpot());



        /*
         * Step 11:
         *
         * Verify that all three wheels
         * remain part of the machine.
         */
        assertEquals(3, machine.wheels());



        /*
         * Step 12:
         *
         * Verify that the machine remains
         * in a successful operational state.
         */
        assertTrue(machine.ok());

    }



    /**
     * Acceptance Scenario 2:
     *
     * A user attempts restricted operations
     * and continues playing afterward.
     *
     * The user creates a normal wheel and a
     * rebel wheel with specialized symbols.
     *
     * The rebel wheel must reject locking,
     * swapping and deletion without changing
     * the logical configuration.
     *
     * The user then locks a normal wheel,
     * verifies that it cannot rotate,
     * unlocks it and continues the game.
     *
     * Covered requirements:
     *
     * - Reject locking of rebel wheels
     * - Reject swapping of rebel wheels
     * - Reject deletion of rebel wheels
     * - Preserve configuration after rejection
     * - Lock and unlock normal wheels
     * - Spin specialized symbols
     * - Preserve machine integrity
     */
    @Test
    public void accordingC4ShouldPreserveMachineIntegrityAfterRestrictions()
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
         *
         * Wheel 1 -> normal
         * Wheel 2 -> rebel
         */
        machine.addWheel("normal", 1);
        machine.addWheel("rebel", 2);

        assertEquals(2, machine.wheels());
        assertTrue(machine.ok());



        /*
         * Step 3:
         *
         * Add normal and specialized symbols.
         *
         * Wheel 1:
         * normal red -> pulsing blue
         *
         * Wheel 2:
         * normal red -> ephemeral blue
         */
        machine.addSymbol("normal", "red", 1);
        machine.addSymbol("pulsing", "blue", 1);

        machine.addSymbol("normal", "red", 2);
        machine.addSymbol("ephemeral", "blue", 2);

        assertTrue(machine.ok());



        /*
         * Step 4:
         *
         * Establish the initial configuration.
         *
         * Expected:
         *
         * Wheel 1 -> red
         * Wheel 2 -> red
         */
        machine.spin(new String[]{"red", "red"});

        assertArrayEquals(new String[]{"red", "red"}, machine.configuration());

        assertTrue(machine.ok());



        /*
         * Step 5:
         *
         * Attempt to lock the rebel wheel.
         *
         * Expected:
         *
         * The operation is rejected.
         */
        machine.lock(2);

        assertFalse(machine.ok());

        assertArrayEquals(new String[]{"red", "red"}, machine.configuration());



        /*
         * Step 6:
         *
         * Attempt to exchange the normal
         * wheel with the rebel wheel.
         *
         * Expected:
         *
         * The operation is rejected.
         */
        machine.swap(1, 2);

        assertFalse(machine.ok());

        assertArrayEquals(new String[]{"red", "red"}, machine.configuration());



        /*
         * Step 7:
         *
         * Attempt to delete the rebel wheel.
         *
         * Expected:
         *
         * The operation is rejected.
         * Both wheels remain in the machine.
         */
        machine.delWheel(2);

        assertFalse(machine.ok());

        assertEquals(2, machine.wheels());

        assertArrayEquals(new String[]{"red", "red"}, machine.configuration());



        /*
         * Step 8:
         *
         * Lock the normal wheel.
         *
         * Expected:
         *
         * The operation succeeds.
         */
        machine.lock(1);

        assertTrue(machine.ok());



        /*
         * Step 9:
         *
         * Attempt to rotate the locked wheel.
         *
         * Expected:
         *
         * The wheel does not rotate.
         */
        machine.spin(1);

        assertFalse(machine.ok());

        assertEquals("red", machine.configuration()[0]);



        /*
         * Step 10:
         *
         * Unlock the normal wheel.
         *
         * Expected:
         *
         * The operation succeeds.
         */
        machine.unlock(1);

        assertTrue(machine.ok());



        /*
         * Step 11:
         *
         * Rotate the normal wheel.
         *
         * Expected:
         *
         * red -> blue
         *
         * The pulsing symbol is selected.
         */
        machine.spin(1);

        assertTrue(machine.ok());

        assertEquals("blue", machine.configuration()[0]);



        /*
         * Step 12:
         *
         * Rotate the rebel wheel.
         *
         * The rebel wheel must still
         * be able to spin normally.
         *
         * Expected:
         *
         * red -> blue
         */
        machine.spin(2);

        assertTrue(machine.ok());

        assertEquals("blue", machine.configuration()[1]);



        /*
         * Step 13:
         *
         * Verify the final configuration.
         *
         * Both wheels display blue,
         * despite having different
         * specialized symbol types.
         */
        assertArrayEquals(new String[]{"blue", "blue"}, machine.configuration());



        /*
         * Step 14:
         *
         * Verify that the machine reaches
         * a jackpot after recovering from
         * the rejected operations.
         */
        assertTrue(machine.isJackpot());



        /*
         * Step 15:
         *
         * Verify that no wheel was removed
         * and the machine remains operational.
         */
        assertEquals(2, machine.wheels());

        assertTrue(machine.ok());

    }

}