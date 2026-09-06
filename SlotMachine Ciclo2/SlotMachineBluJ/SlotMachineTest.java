import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * The test class SlotMachineTest.
 *
 * @author  (Julian Gomez - Julian Carranza)
 * 
 */
public class SlotMachineTest
{
    // MiniCycle1
    private SlotMachine machine;
    
    /**
     * Default constructor for test class SlotMachineTest
     */
    public SlotMachineTest()
    {
    }

    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @BeforeEach
    public void setUp()
    {
        machine = new SlotMachine();
    }

    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    @AfterEach
    public void tearDown()
    {
        machine = null;
    }


    /**
     * Verifies that a slot machine is created correctly.
     */
    @Test
    public void testCreateSlotMachine()
    {
        assertEquals(0, machine.wheels());
        assertTrue(machine.ok());
    }


    /**
     * Verifies that a wheel can be added.
     */
    @Test
    public void testAddFirstWheel()
    {
        machine.addWheel(1);

        assertEquals(1, machine.wheels());
        assertTrue(machine.ok());
    }


    /**
     * Verifies insertion at the first position.
     */
    @Test
    public void testAddWheelInFirstPosition()
    {
        machine.addWheel(1);
        machine.addWheel(1);

        assertEquals(2, machine.wheels());
        assertTrue(machine.ok());
    }


    /**
     * Verifies that positions below one are adjusted to one.
     */
    @Test
    public void testAddWheelPositionBelowMinimum()
    {
        machine.addWheel(-5);

        assertEquals(1, machine.wheels());
        assertTrue(machine.ok());
    }


    /**
     * Verifies that positions above the maximum are adjusted correctly.
     */
    @Test
    public void testAddWheelPositionAboveMaximum()
    {
        machine.addWheel(100);

        assertEquals(1, machine.wheels());
        assertTrue(machine.ok());
    }


    /**
     * Verifies that an existing wheel can be deleted.
     */
    @Test
    public void testDeleteWheel()
    {
        machine.addWheel(1);
        machine.addWheel(2);

        machine.delWheel(1);

        assertEquals(1, machine.wheels());
        assertTrue(machine.ok());
    }


    /**
     * Verifies deletion with a position below one.
     */
    @Test
    public void testDeleteWheelPositionBelowMinimum()
    {
        machine.addWheel(1);
        machine.addWheel(2);

        machine.delWheel(-10);

        assertEquals(1, machine.wheels());
        assertTrue(machine.ok());
    }


    /**
     * Verifies deletion with a position above the maximum.
     */
    @Test
    public void testDeleteWheelPositionAboveMaximum()
    {
        machine.addWheel(1);
        machine.addWheel(2);

        machine.delWheel(100);

        assertEquals(1, machine.wheels());
        assertTrue(machine.ok());
    }


    /**
     * Verifies that deleting from an empty machine fails.
     */
    @Test
    public void testDeleteWheelFromEmptyMachine()
    {
        machine.delWheel(1);

        assertEquals(0, machine.wheels());
        assertFalse(machine.ok());
    }
    
    
    
    
    
    
    
    // MiniCycle2

    /**
     * Verifies that a symbol can be added to a wheel.
     */
    @Test
    public void testAddSymbol()
    {
        machine.addWheel(1);

        machine.addSymbol(1,"red");

        assertTrue(machine.ok());
        assertEquals(1, machine.symbols().length);
        assertEquals("red", machine.symbols()[0]);
    }



    /**
     * Verifies that repeated symbols are rejected.
     */
    @Test
    public void testAddRepeatedSymbol()
    {
        machine.addWheel(1);

        machine.addSymbol(1,"red");
        machine.addSymbol(1,"red");

        assertFalse(machine.ok());
        assertEquals(1, machine.symbols().length);
    }



    /**
     * Verifies deleting an existing symbol.
     */
    @Test
    public void testDeleteSymbol()
    {
        machine.addWheel(1);

        machine.addSymbol(1,"red");
        machine.addSymbol(1,"blue");

        machine.delSymbol("red");

        assertTrue(machine.ok());
        assertEquals(1, machine.symbols().length);
        assertEquals("blue", machine.symbols()[0]);
    }



    /**
     * Verifies deleting a symbol that does not exist.
     */
    @Test
    public void testDeleteNonExistingSymbol()
    {
        machine.addWheel(1);

        machine.addSymbol(1,"red");

        machine.delSymbol("green");

        assertFalse(machine.ok());
        assertEquals(1, machine.symbols().length);
    }



    /**
     * Verifies that symbols are returned wheel by wheel.
     */
    @Test
    public void testSymbolsOrder()
    {
        machine.addWheel(1);
        machine.addWheel(2);

        machine.addSymbol(1,"red");
        machine.addSymbol(1,"blue");

        machine.addSymbol(2,"green");
        machine.addSymbol(2,"yellow");


        String[] symbols = machine.symbols();


        assertEquals(4, symbols.length);

        assertEquals("red", symbols[0]);
        assertEquals("blue", symbols[1]);
        assertEquals("green", symbols[2]);
        assertEquals("yellow", symbols[3]);
    }



    /**
     * Verifies adding symbols to a non existing wheel.
     */
    @Test
    public void testAddSymbolToInvalidWheel()
    {
        machine.addSymbol(1,"red");

        assertFalse(machine.ok());
        assertEquals(0, machine.symbols().length);
    }
    
    
    
    
    
    
    
    // MiniCycle 3
    /**
     * Verifies that a single wheel can spin.
     */
    @Test
    public void testSpinSingleWheel()
    {
        machine.addWheel(1);

        machine.addSymbol(1,"red");
        machine.addSymbol(1,"blue");

        String before = machine.configuration()[0];

        machine.spin(1);

        String after = machine.configuration()[0];


        assertTrue(machine.ok());
        assertNotEquals(before, after);
    }


    /**
     * Verifies that a complete machine spin rotates all wheels.
     */
    @Test
    public void testSpinAllWheels()
    {
        machine.addWheel(1);
        machine.addWheel(2);


        machine.addSymbol(1,"red");
        machine.addSymbol(1,"blue");


        machine.addSymbol(2,"green");
        machine.addSymbol(2,"yellow");


        String firstBefore = machine.configuration()[0];
        String secondBefore = machine.configuration()[1];


        machine.spin();


        String firstAfter = machine.configuration()[0];
        String secondAfter = machine.configuration()[1];


        assertTrue(machine.ok());

        assertNotEquals(firstBefore, firstAfter);
        assertNotEquals(secondBefore, secondAfter);
    }


    /**
     * Verifies that an empty wheel cannot spin.
     */
    @Test
    public void testSpinEmptyWheel()
    {
        machine.addWheel(1);


        machine.spin(1);


        assertFalse(machine.ok());
    }


    /**
     * Verifies that configuration returns visible symbols.
     */
    @Test
    public void testConfiguration()
    {
        machine.addWheel(1);
        machine.addWheel(2);


        machine.addSymbol(1,"red");
        machine.addSymbol(1,"blue");


        machine.addSymbol(2,"green");
        machine.addSymbol(2,"yellow");


        String[] configuration = machine.configuration();


        assertEquals(2, configuration.length);

        assertEquals("red", configuration[0]);
        assertEquals("green", configuration[1]);
    }


    /**
     * Verifies that an invalid wheel cannot spin.
     */
    @Test
    public void testSpinInvalidWheel()
    {
        machine.spin(1);


        assertFalse(machine.ok());
    }


    /**
     * Verifies that a wheel returns to its initial symbol after
     * complete rotation.
     */
    @Test
    public void testCompleteRotation()
    {
        machine.addWheel(1);


        machine.addSymbol(1,"red");
        machine.addSymbol(1,"blue");


        machine.spin(1);
        machine.spin(1);


        String visible = machine.configuration()[0];


        assertEquals("red", visible);
        assertTrue(machine.ok());
    }
    
    
    
    
    
    // MiniCycle 4
        @Test
    public void testDistinctSymbols()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addWheel(3);


        machine.addSymbol(1,"red");
        machine.addSymbol(2,"blue");
        machine.addSymbol(3,"red");


        int result = machine.distinctSymbols();


        assertEquals(2, result);
    }


    /**
     * Verifies a winning jackpot configuration.
     */
    @Test
    public void testAllSameSymbolsJackpot()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addWheel(3);


        machine.addSymbol(1,"red");
        machine.addSymbol(2,"red");
        machine.addSymbol(3,"red");


        assertTrue(machine.isJackpot());
    }


    /**
     * Verifies that different visible symbols are not jackpot.
     */
    @Test
    public void testDifferentSymbolsNoJackpot()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addWheel(3);


        machine.addSymbol(1,"red");
        machine.addSymbol(2,"blue");
        machine.addSymbol(3,"green");


        assertFalse(machine.isJackpot());
    }


    /**
     * Verifies jackpot behavior with only one wheel.
     */
    @Test
    public void testSingleWheelJackpot()
    {
        machine.addWheel(1);


        machine.addSymbol(1,"red");


        assertTrue(machine.isJackpot());
    }
    

    /**
     * Verifies that an empty machine is not jackpot.
     */
    @Test
    public void testEmptyMachineNoJackpot()
    {
        assertFalse(machine.isJackpot());
    }


    /**
     * Verifies that jackpot and distinct symbols use
     * the current configuration after spinning.
     */
    @Test
    public void testConfigurationAfterSpin()
    {
        machine.addWheel(1);
        machine.addWheel(2);


        machine.addSymbol(1,"red");
        machine.addSymbol(1,"blue");


        machine.addSymbol(2,"red");
        machine.addSymbol(2,"green");


        machine.spin();


        String[] configuration = machine.configuration();


        assertEquals(2, configuration.length);

        assertEquals("blue", configuration[0]);
        assertEquals("green", configuration[1]);

        assertEquals(2, machine.distinctSymbols());
        assertFalse(machine.isJackpot());
    }
    
    
    
    
    
    // MiniCycle 5
        /**
     * Verifies that the slot machine can be created
     * with its visual representation.
     */
    @Test
    public void testSlotMachineVisualCreation()
    {
        assertNotNull(machine);

        assertEquals(0, machine.wheels());

        assertTrue(machine.ok());
    }



    /**
     * Verifies that the machine can show
     * its visual elements.
     */
    @Test
    public void testMakeVisible()
    {
        machine.addWheel(1);


        machine.makeVisible();


        assertTrue(machine.ok());

        assertEquals(1, machine.wheels());
    }



    /**
     * Verifies that the machine can hide
     * its visual elements.
     */
    @Test
    public void testMakeInvisible()
    {
        machine.addWheel(1);


        machine.makeVisible();

        machine.makeInvisible();


        assertTrue(machine.ok());

        assertEquals(1, machine.wheels());
    }



    /**
     * Verifies that wheels maintain their logic
     * after visual integration.
     */
    @Test
    public void testWheelVisualIntegration()
    {
        machine.addWheel(1);


        machine.addSymbol(1,"red");
        machine.addSymbol(1,"blue");


        machine.makeVisible();


        machine.spin(1);


        String[] configuration = machine.configuration();


        assertEquals(1, configuration.length);

        assertEquals("blue", configuration[0]);

        assertTrue(machine.ok());
    }



    /**
     * Complete project integration test.
     */
    @Test
    public void testCompleteVisualFlow()
    {
        machine.addWheel(1);
        machine.addWheel(2);


        machine.addSymbol(1,"red");
        machine.addSymbol(1,"blue");


        machine.addSymbol(2,"green");
        machine.addSymbol(2,"yellow");


        machine.makeVisible();


        machine.spin();


        String[] configuration = machine.configuration();


        assertEquals(2, configuration.length);


        assertEquals("blue", configuration[0]);

        assertEquals("yellow", configuration[1]);


        machine.makeInvisible();


        assertTrue(machine.ok());
    }
}