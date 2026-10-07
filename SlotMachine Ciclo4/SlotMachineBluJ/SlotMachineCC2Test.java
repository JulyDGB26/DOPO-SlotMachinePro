import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for Cycle 2 methods of SlotMachine.
 * All tests run in invisible mode.
 *
 * @author Julian Gomez Boada
 * 
 */
public class SlotMachineCC2Test
{
    private SlotMachine machine;

    @BeforeEach
    public void setUp()
    {
        machine = new SlotMachine();
    }

    @AfterEach
    public void tearDown()
    {
        machine = null;
    }


    
    /**
     * swap on a valid two-wheel machine exchanges the visible configuration.
     */
    @Test
    public void shouldSwapTwoWheelsCorrectly()
    {
        machine.addWheel(1);
        machine.addWheel(2);

        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");

        machine.swap(1, 2);

        String[] config = machine.configuration();

        assertTrue(machine.ok());
        assertEquals("blue", config[0]);
        assertEquals("red",  config[1]);
    }

    /**
     * swap on a machine with fewer than two wheels fails.
     */
    @Test
    public void shouldFailSwapWithOnlyOneWheel()
    {
        machine.addWheel(1);
        machine.addSymbol(1, "red");

        machine.swap(1, 1);

        assertFalse(machine.ok());
    }

    /**
     * swap with equal positions is a valid no-op.
     */
    @Test
    public void shouldSwapSamePositionAsNoOp()
    {
        machine.addWheel(1);
        machine.addWheel(2);

        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");

        machine.swap(1, 1);

        assertTrue(machine.ok());
        assertEquals("red",  machine.configuration()[0]);
        assertEquals("blue", machine.configuration()[1]);
    }

    /**
     * swap with out-of-range positions clamps and still swaps.
     */
    @Test
    public void shouldSwapWithClampedPositions()
    {
        machine.addWheel(1);
        machine.addWheel(2);

        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");

        machine.swap(-5, 100);

        assertTrue(machine.ok());
        assertEquals("blue", machine.configuration()[0]);
        assertEquals("red",  machine.configuration()[1]);
    }



    /**
     * A locked wheel does not advance when the machine spins.
     */
    @Test
    public void accordingCcGbShouldNotSpinLockedWheel()
    {
        machine.addWheel(1);
        machine.addSymbol(1, "red");
        machine.addSymbol(1, "blue");

        machine.lock(1);
        machine.spin(1);

        assertEquals("red", machine.configuration()[0]);
        assertFalse(machine.ok());
    }

    /**
     * lock on an invalid position fails.
     */
    @Test
    public void shouldFailLockOnInvalidPosition()
    {
        machine.lock(5);

        assertFalse(machine.ok());
    }

    /**
     * lock on a valid position succeeds.
     */
    @Test
    public void shouldLockValidWheel()
    {
        machine.addWheel(1);

        machine.lock(1);

        assertTrue(machine.ok());
    }



    /**
     * A wheel that was locked resumes spinning after unlock.
     */
    @Test
    public void accordingCcGbShouldSpinAfterUnlock()
    {
        machine.addWheel(1);
        machine.addSymbol(1, "red");
        machine.addSymbol(1, "blue");

        machine.lock(1);
        machine.unlock(1);
        machine.spin(1);

        assertEquals("blue", machine.configuration()[0]);
        assertTrue(machine.ok());
    }

    /**
     * unlock on an invalid position fails.
     */
    @Test
    public void shouldFailUnlockOnInvalidPosition()
    {
        machine.unlock(3);

        assertFalse(machine.ok());
    }



    /**
     * Spinning a wheel 3 steps lands on the correct symbol.
     */
    @Test
    public void shouldSpinWheelCorrectNumberOfSteps()
    {
        machine.addWheel(1);
        machine.addSymbol(1, "red");
        machine.addSymbol(1, "blue");
        machine.addSymbol(1, "green");
        machine.addSymbol(1, "yellow");

        machine.spin(1, 3);

        assertEquals("yellow", machine.configuration()[0]);
        assertTrue(machine.ok());
    }

    /**
     * Spinning a wheel by its total symbol count returns to the initial symbol.
     */
    @Test
    public void shouldReturnToStartAfterFullRotation()
    {
        machine.addWheel(1);
        machine.addSymbol(1, "red");
        machine.addSymbol(1, "blue");
        machine.addSymbol(1, "green");

        machine.spin(1, 3);

        assertEquals("red", machine.configuration()[0]);
        assertTrue(machine.ok());
    }

    /**
     * Spinning with steps = 0 fails.
     */
    @Test
    public void shouldFailSpinWithZeroSteps()
    {
        machine.addWheel(1);
        machine.addSymbol(1, "red");

        machine.spin(1, 0);

        assertFalse(machine.ok());
    }

    /**
     * Spinning an invalid wheel position fails.
     */
    @Test
    public void shouldFailSpinStepsOnInvalidWheel()
    {
        machine.spin(5, 2);

        assertFalse(machine.ok());
    }



    /**
     * spin with a valid symbol array sets the visible configuration.
     */
    @Test
    public void shouldSetConfigurationWithValidSymbols()
    {
        machine.addWheel(1);
        machine.addWheel(2);

        machine.addSymbol(1, "red");
        machine.addSymbol(1, "blue");
        machine.addSymbol(2, "green");
        machine.addSymbol(2, "yellow");

        machine.spin(new String[]{"blue", "yellow"});

        assertTrue(machine.ok());
        assertEquals("blue",   machine.configuration()[0]);
        assertEquals("yellow", machine.configuration()[1]);
    }

    /**
     * spin with a non-existing symbol in a wheel fails.
     */
    @Test
    public void shouldFailSpinSetWithNonExistingSymbol()
    {
        machine.addWheel(1);
        machine.addSymbol(1, "red");

        machine.spin(new String[]{"purple"});

        assertFalse(machine.ok());
        assertEquals("red", machine.configuration()[0]);
    }

    /**
     * spin with an array shorter than the wheel count applies to the first N wheels.
     */
    @Test
    public void shouldApplyPartialConfigurationArray()
    {
        machine.addWheel(1);
        machine.addWheel(2);

        machine.addSymbol(1, "red");
        machine.addSymbol(1, "blue");
        machine.addSymbol(2, "green");
        machine.addSymbol(2, "yellow");

        machine.spin(new String[]{"blue"});

        assertTrue(machine.ok());
        assertEquals("blue",  machine.configuration()[0]);
        assertEquals("green", machine.configuration()[1]);
    }


    /**
     * spin() on all wheels succeeds when at least one unlocked wheel rotates.
     */
    @Test
    public void shouldSpinAllWithSomeLockedWheels()
    {
        machine.addWheel(1);
        machine.addWheel(2);

        machine.addSymbol(1, "red");
        machine.addSymbol(1, "blue");
        machine.addSymbol(2, "green");
        machine.addSymbol(2, "yellow");

        machine.lock(1);
        machine.spin();

        assertTrue(machine.ok());
        assertEquals("red",    machine.configuration()[0]);
        assertEquals("yellow", machine.configuration()[1]);
    }
    
    
    
    
    // WIKI MOODLE SlotMachineCC2Test
    /**
     * Verifies that swapping wheels does not create or remove wheels.
     */
    @Test
    public void accordingAsGaShouldKeepTheSameNumberOfWheelsAfterASwap()
    {
        machine.addWheel(1);
        machine.addWheel(2);
    
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
    
        machine.swap(1, 2);
    
        assertTrue(machine.ok());
        assertEquals(2, machine.configuration().length);
    }
    
    /**
     * Verifies that deleting a symbol that does not exist
     * fails without removing existing symbols.
     */
    @Test
    public void accordingCxLxShouldNotDeleteMissingSymbol()
    {
        machine.addWheel(1);
        machine.addSymbol(1, "red");
    
        machine.delSymbol("blue");
    
        assertFalse(machine.ok());
        assertEquals(1, machine.symbols().length);
    }
    
    /**
     * Verifies that spin(String[]) does not partially modify
     * the machine when one requested symbol does not exist.
     */
    @Test
    public void accordingMrSeShouldRejectSpinSetSymbolsWhenColorMissing()
    {
        machine.addWheel(1);
        machine.addWheel(2);
    
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "green");
    
        String[] before = machine.configuration();
    
        machine.spin(new String[]{"red", "purple"});
    
        assertFalse(machine.ok());
        assertArrayEquals(before, machine.configuration());
    }
    
    /**
     * Verifies that adding and deleting wheels
     * maintains the expected wheel count.
     */
    @Test
    public void accordingDrRmShouldKeepCorrectWheelCountAfterAddAndDelete()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addWheel(3);
    
        machine.delWheel(2);
    
        assertEquals(2, machine.configuration().length);
    }
    
    /**
     * Verifies that distinctSymbols counts different visible colors.
     */
    @Test
    public void accordingCcIcshouldCalculateDistinctSymbolsCorrectly()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addWheel(3);
    
        machine.addSymbol(1,"red");
        machine.addSymbol(2,"red");
        machine.addSymbol(3,"green");
    
        machine.placeSymbol(1,"red");
        machine.placeSymbol(2,"red");
        machine.placeSymbol(3,"green");
    
        assertEquals(2, machine.distinctSymbols());
        assertTrue(machine.ok());
    }
}