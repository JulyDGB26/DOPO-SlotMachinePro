import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Unit tests for Cycle 4.
 *
 * MC1 validates the structural support for typed wheels
 * and symbols while preserving the normal behavior
 * developed in previous cycles.
 *
 * @author Julian Gomez Boada
 */
public class SlotMachineC4Test
{

    // ==========================================================
    // MC1 - TYPED CREATION AND COMPATIBILITY
    // ==========================================================

    // ----------------------------------------------------------
    // Typed wheel creation
    // ----------------------------------------------------------

    /**
     * Verifies that a normal wheel can be created
     * through the new typed operation.
     */
    @Test
    public void shouldAddNormalWheelByType()
    {
        SlotMachine machine = new SlotMachine();

        machine.addWheel("normal", 1);

        assertTrue(machine.ok());
        assertEquals(1, machine.wheels());
    }

    /**
     * Verifies that an unavailable wheel type is rejected
     * without modifying the machine.
     */
    @Test
    public void shouldNotAddUnknownWheelType()
    {
        SlotMachine machine = new SlotMachine();

        machine.addWheel("unknown", 1);

        assertFalse(machine.ok());
        assertEquals(0, machine.wheels());
    }

    // ----------------------------------------------------------
    // Typed symbol creation
    // ----------------------------------------------------------

    /**
     * Verifies that a normal symbol can be created
     * through the new typed operation.
     */
    @Test
    public void shouldAddNormalSymbolByType()
    {
        SlotMachine machine = new SlotMachine();

        machine.addWheel("normal", 1);
        machine.addSymbol("normal", "red", 1);

        assertTrue(machine.ok());
        assertArrayEquals(new String[]{"red"}, machine.symbols());
    }

    /**
     * Verifies that an unavailable symbol type is rejected
     * without adding a symbol to the wheel.
     */
    @Test
    public void shouldNotAddUnknownSymbolType()
    {
        SlotMachine machine = new SlotMachine();

        machine.addWheel("normal", 1);
        machine.addSymbol("unknown", "red", 1);

        assertFalse(machine.ok());
        assertEquals(0, machine.symbols().length);
    }

    // ----------------------------------------------------------
    // Previous-cycle compatibility
    // ----------------------------------------------------------

    /**
     * Verifies that the operations from previous cycles
     * continue creating normal wheels and symbols.
     */
    @Test
    public void shouldPreservePreviousNormalOperations()
    {
        SlotMachine machine = new SlotMachine();

        machine.addWheel(1);
        machine.addSymbol(1, "red");
        machine.addSymbol(1, "blue");

        assertTrue(machine.ok());
        assertEquals(1, machine.wheels());
        assertArrayEquals(new String[]{"red", "blue"}, machine.symbols());
    }

    // ==========================================================
    // MC2 - SPECIALIZED WHEELS
    // ==========================================================

    // ----------------------------------------------------------
    // LeftyWheel - creation and spinning
    // ----------------------------------------------------------

    /**
     * Verifies that a lefty wheel can be added through the typed operation.
     */
    @Test
    public void shouldAddLeftyWheel()
    {
        SlotMachine machine = new SlotMachine();
    
        machine.addWheel("lefty", 1);
    
        assertTrue(machine.ok());
        assertEquals(1, machine.wheels());
    }

    /**
     * Verifies that a lefty wheel copies the visible state
     * of the wheel immediately to its left.
     */
    @Test
    public void shouldCopyLeftWheelWhenLeftySpins()
    {
        SlotMachine machine = new SlotMachine();
    
        machine.addWheel("normal", 1);
        machine.addWheel("lefty", 2);
    
        machine.addSymbol(1, "red");
        machine.addSymbol(1, "blue");
    
        machine.addSymbol(2, "red");
        machine.addSymbol(2, "blue");
    
        machine.placeSymbol(1, "blue");
        machine.placeSymbol(2, "red");
    
        machine.spin(2);
    
        assertTrue(machine.ok());
        assertEquals("blue", machine.configuration()[1]);
    }

    /**
     * Verifies that a lefty wheel in the first position
     * behaves as a normal wheel because no left neighbor exists.
     */
    @Test
    public void shouldSpinNormallyWhenLeftyHasNoLeftWheel()
    {
        SlotMachine machine = new SlotMachine();
    
        machine.addWheel("lefty", 1);
        machine.addSymbol(1, "red");
        machine.addSymbol(1, "blue");
    
        machine.spin(1);
    
        assertTrue(machine.ok());
        assertEquals("blue", machine.configuration()[0]);
    }

    /**
     * Verifies that a lefty wheel does not create or copy
     * a symbol that is not part of its own symbols.
     */
    @Test
    public void shouldNotCopyUnavailableSymbolToLeftyWheel()
    {
        SlotMachine machine = new SlotMachine();
    
        machine.addWheel("normal", 1);
        machine.addWheel("lefty", 2);
    
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
    
        machine.spin(2);
    
        assertFalse(machine.ok());
        assertEquals("blue", machine.configuration()[1]);
    
        assertArrayEquals(
            new String[]{"red", "blue"},
            machine.symbols()
        );
    }

    /**
     * Verifies that locking a lefty wheel preserves
     * the historical locked-wheel behavior.
     */
    @Test
    public void shouldNotChangeLockedLeftyWheel()
    {
        SlotMachine machine = new SlotMachine();
    
        machine.addWheel("normal", 1);
        machine.addWheel("lefty", 2);
    
        machine.addSymbol(1, "red");
        machine.addSymbol(1, "blue");
    
        machine.addSymbol(2, "red");
        machine.addSymbol(2, "blue");
    
        machine.placeSymbol(1, "blue");
        machine.placeSymbol(2, "red");
    
        machine.lock(2);
        machine.spin(2);
    
        assertFalse(machine.ok());
        assertEquals("red", machine.configuration()[1]);
    }

    /**
     * Verifies that lefty behavior is also respected
     * when a wheel is rotated using several steps.
     */
    @Test
    public void shouldPreserveLeftyBehaviorWhenSpinningSeveralSteps()
    {
        SlotMachine machine = new SlotMachine();
    
        machine.addWheel("normal", 1);
        machine.addWheel("lefty", 2);
    
        machine.addSymbol(1, "red");
        machine.addSymbol(1, "blue");
    
        machine.addSymbol(2, "red");
        machine.addSymbol(2, "blue");
    
        machine.placeSymbol(1, "blue");
        machine.placeSymbol(2, "red");
    
        machine.spin(2, 3);
    
        assertTrue(machine.ok());
        assertEquals("blue", machine.configuration()[1]);
    }

    /**
     * Verifies that lefty behavior participates correctly
     * in a global spin.
     */
    @Test
    public void shouldCopyLeftStateDuringGlobalSpin()
    {
        SlotMachine machine = new SlotMachine();
    
        machine.addWheel("normal", 1);
        machine.addWheel("lefty", 2);
    
        machine.addSymbol(1, "red");
        machine.addSymbol(1, "blue");
    
        machine.addSymbol(2, "red");
        machine.addSymbol(2, "blue");
    
        machine.spin();
    
        assertTrue(machine.ok());
    
        // Wheel 1 moves red -> blue.
        // Then the lefty wheel copies that current blue state.
        assertEquals("blue", machine.configuration()[0]);
        assertEquals("blue", machine.configuration()[1]);
    }

    // ----------------------------------------------------------
    // RebelWheel - spinning and restrictions
    // ----------------------------------------------------------

    /**
     * Verifies that a rebel wheel can be created.
     */
    @Test
    public void shouldAddRebelWheel()
    {
        SlotMachine machine = new SlotMachine();
    
        machine.addWheel("rebel", 1);
    
        assertTrue(machine.ok());
        assertEquals(1, machine.wheels());
    }

    /**
     * Verifies that a rebel wheel refuses to be locked.
     */
    @Test
    public void shouldNotLockRebelWheel()
    {
        SlotMachine machine = new SlotMachine();
    
        machine.addWheel("rebel", 1);
        machine.addSymbol(1, "red");
        machine.addSymbol(1, "blue");
    
        machine.lock(1);
    
        assertFalse(machine.ok());
    
        // It must still be able to spin because it was not locked.
        machine.spin(1);
    
        assertTrue(machine.ok());
        assertEquals("blue", machine.configuration()[0]);
    }

    /**
     * Verifies that a rebel wheel can spin normally.
     */
    @Test
    public void shouldAllowRebelWheelToSpin()
    {
        SlotMachine machine = new SlotMachine();
    
        machine.addWheel("rebel", 1);
        machine.addSymbol(1, "red");
        machine.addSymbol(1, "blue");
    
        machine.spin(1);
    
        assertTrue(machine.ok());
        assertEquals("blue", machine.configuration()[0]);
    }

    /**
     * Verifies that a rebel wheel cannot be deleted.
     */
    @Test
    public void shouldNotDeleteRebelWheel()
    {
        SlotMachine machine = new SlotMachine();
    
        machine.addWheel("rebel", 1);
        machine.addSymbol(1, "red");
    
        machine.delWheel(1);
    
        assertFalse(machine.ok());
        assertEquals(1, machine.wheels());
        assertEquals("red", machine.configuration()[0]);
    }

    /**
     * Verifies that a rebel wheel cannot participate
     * as the first wheel in a swap.
     */
    @Test
    public void shouldNotSwapRebelAsFirstWheel()
    {
        SlotMachine machine = new SlotMachine();
    
        machine.addWheel("rebel", 1);
        machine.addWheel("normal", 2);
    
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
    
        machine.swap(1, 2);
    
        assertFalse(machine.ok());
        assertEquals("red", machine.configuration()[0]);
        assertEquals("blue", machine.configuration()[1]);
    }

    /**
     * Verifies that a rebel wheel cannot participate
     * as the second wheel in a swap.
     */
    @Test
    public void shouldNotSwapRebelAsSecondWheel()
    {
        SlotMachine machine = new SlotMachine();
    
        machine.addWheel("normal", 1);
        machine.addWheel("rebel", 2);
    
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
    
        machine.swap(1, 2);
    
        assertFalse(machine.ok());
        assertEquals("red", machine.configuration()[0]);
        assertEquals("blue", machine.configuration()[1]);
    }

    // ----------------------------------------------------------
    // Regression - normal wheels
    // ----------------------------------------------------------

    /**
     * Verifies that normal wheels preserve the historical
     * lock, swap and delete behavior after MC2.
     */
    @Test
    public void shouldPreserveNormalWheelBehaviorAfterMC2()
    {
        SlotMachine machine = new SlotMachine();
    
        machine.addWheel("normal", 1);
        machine.addWheel("normal", 2);
    
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
    
        machine.swap(1, 2);
    
        assertTrue(machine.ok());
        assertEquals("blue", machine.configuration()[0]);
        assertEquals("red", machine.configuration()[1]);
    
        machine.lock(1);
    
        assertTrue(machine.ok());
    
        machine.delWheel(2);
    
        assertTrue(machine.ok());
        assertEquals(1, machine.wheels());
    }

    // ==========================================================
    // MC3 - EPHEMERAL AND SHY SYMBOLS
    // ==========================================================

    // ----------------------------------------------------------
    // EphemeralSymbol - creation and shrinking
    // ----------------------------------------------------------

    /**
     * Verifies that an ephemeral symbol can be created
     * through the typed symbol operation.
     */
    @Test
    public void shouldAddEphemeralSymbolByType()
    {
        SlotMachine machine = new SlotMachine();
    
        machine.addWheel("normal", 1);
        machine.addSymbol("ephemeral", "red", 1);
    
        assertTrue(machine.ok());
        assertArrayEquals(new String[]{"red"}, machine.symbols());
    }

    /**
     * Verifies that an ephemeral symbol progressively reduces its size.
     */
    @Test
    public void shouldReduceEphemeralSymbolSizeAfterSpin()
    {
        EphemeralSymbol symbol = new EphemeralSymbol("red");
    
        assertEquals(70, symbol.getSize());
    
        symbol.spin();
    
        assertEquals(60, symbol.getSize());
    
        symbol.spin();
    
        assertEquals(50, symbol.getSize());
    }

    /**
     * Verifies that an ephemeral symbol never becomes smaller
     * than the minimum point size.
     */
    @Test
    public void shouldStopEphemeralSymbolAtPointSize()
    {
        EphemeralSymbol symbol = new EphemeralSymbol("red");
    
        for(int i = 0; i < 20; i++)
        {
            symbol.spin();
        }
    
        assertEquals(2, symbol.getSize());
    
        symbol.spin();
        symbol.spin();
    
        assertEquals(2, symbol.getSize());
    }

    // ----------------------------------------------------------
    // ShySymbol - creation, selection and logical identity
    // ----------------------------------------------------------

    /**
     * Verifies that a shy symbol can be created
     * through the typed symbol operation.
     */
    @Test
    public void shouldAddShySymbolByType()
    {
        SlotMachine machine = new SlotMachine();
    
        machine.addWheel("normal", 1);
        machine.addSymbol("shy", "blue", 1);
    
        assertTrue(machine.ok());
        assertArrayEquals(new String[]{"blue"}, machine.symbols());
    }

    /**
     * Verifies that a shy symbol alternates its visual state
     * every time it is selected.
     */
    @Test
    public void shouldAlternateShySymbolVisibilityWhenSelected()
    {
        ShySymbol symbol = new ShySymbol("red");
    
        assertTrue(symbol.isVisible());
    
        symbol.select();
        assertFalse(symbol.isVisible());
    
        symbol.select();
        assertTrue(symbol.isVisible());
    
        symbol.select();
        assertFalse(symbol.isVisible());
    }

    /**
     * Verifies that spinning alone is not the shy-specific event.
     */
    @Test
    public void shouldNotChangeShyVisibilityThroughSpinEventAlone()
    {
        ShySymbol symbol = new ShySymbol("red");
    
        symbol.spin();
    
        assertTrue(symbol.isVisible());
    }

    /**
     * Verifies that selecting a shy symbol preserves
     * its logical configuration.
     */
    @Test
    public void shouldPreserveShyLogicalConfigurationWhenSelected()
    {
        SlotMachine machine = new SlotMachine();
    
        machine.addWheel("normal", 1);
        machine.addSymbol("normal", "red", 1);
        machine.addSymbol("shy", "blue", 1);
    
        machine.placeSymbol(1, "blue");
    
        assertTrue(machine.ok());
        assertEquals("blue", machine.configuration()[0]);
    }

    /**
     * Verifies that a shy symbol preserves its logical identity
     * for jackpot evaluation even when its visual state changes.
     */
    @Test
    public void shouldPreserveJackpotWithShySymbol()
    {
        SlotMachine machine = new SlotMachine();
    
        machine.addWheel("normal", 1);
        machine.addWheel("normal", 2);
    
        machine.addSymbol("shy", "red", 1);
        machine.addSymbol("normal", "red", 2);
    
        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "red");
    
        assertTrue(machine.isJackpot());
        assertEquals("red", machine.configuration()[0]);
        assertEquals("red", machine.configuration()[1]);
    }

    // ----------------------------------------------------------
    // Integration - mixed symbol types and specialized wheels
    // ----------------------------------------------------------

    /**
     * Verifies that normal, ephemeral and shy symbols
     * can coexist in the same wheel.
     */
    @Test
    public void shouldAllowDifferentSymbolTypesInSameWheel()
    {
        SlotMachine machine = new SlotMachine();
    
        machine.addWheel("normal", 1);
    
        machine.addSymbol("normal", "red", 1);
        machine.addSymbol("ephemeral", "blue", 1);
        machine.addSymbol("shy", "green", 1);
    
        assertTrue(machine.ok());
    
        assertArrayEquals(new String[]{"red", "blue", "green"}, machine.symbols());
    }

    /**
     * Verifies that a lefty wheel can select a specialized symbol
     * without losing its MC2 behavior.
     */
    @Test
    public void shouldAllowLeftyWheelToUseSpecializedSymbols()
    {
        SlotMachine machine = new SlotMachine();
    
        machine.addWheel("normal", 1);
        machine.addWheel("lefty", 2);
    
        machine.addSymbol("normal", "red", 1);
        machine.addSymbol("normal", "blue", 1);
    
        machine.addSymbol("shy", "red", 2);
        machine.addSymbol("ephemeral", "blue", 2);
    
        machine.placeSymbol(1, "blue");
        machine.spin(2);
    
        assertTrue(machine.ok());
        assertEquals("blue", machine.configuration()[1]);
    }

    /**
     * Verifies that a rebel wheel can contain specialized symbols
     * while preserving its rebel restrictions.
     */
    @Test
    public void shouldAllowSpecializedSymbolsInRebelWheel()
    {
        SlotMachine machine = new SlotMachine();
    
        machine.addWheel("rebel", 1);
    
        machine.addSymbol("ephemeral", "red", 1);
        machine.addSymbol("shy", "blue", 1);
    
        assertTrue(machine.ok());
    
        machine.spin(1);
    
        assertTrue(machine.ok());
        assertEquals("blue", machine.configuration()[0]);
    
        machine.lock(1);
    
        assertFalse(machine.ok());
    }

    // ----------------------------------------------------------
    // Regression - normal symbols
    // ----------------------------------------------------------

    /**
     * Verifies that normal symbols preserve their historical behavior
     * after introducing specialized symbol types.
     */
    @Test
    public void shouldPreserveNormalSymbolBehaviorAfterMC3()
    {
        SlotMachine machine = new SlotMachine();
    
        machine.addWheel("normal", 1);
    
        machine.addSymbol("normal", "red", 1);
        machine.addSymbol("normal", "blue", 1);
    
        machine.spin(1);
    
        assertTrue(machine.ok());
        assertEquals("blue", machine.configuration()[0]);
    
        machine.spin(1);
    
        assertTrue(machine.ok());
        assertEquals("red", machine.configuration()[0]);
    
        assertArrayEquals(new String[]{"red", "blue"}, machine.symbols());
    }

    // ==========================================================
    // MC4 - PULSING SYMBOL AND VISUAL IDENTIFICATION
    // ==========================================================

    // ----------------------------------------------------------
    // PulsingSymbol - creation, selection and spinning
    // ----------------------------------------------------------

    /**
     * Verifies that a pulsing symbol can be created
     * through the typed symbol operation.
     */
    @Test
    public void shouldAddPulsingSymbolByType()
    {
        SlotMachine machine = new SlotMachine();
    
        machine.addWheel("normal", 1);
        machine.addSymbol("pulsing", "red", 1);
    
        assertTrue(machine.ok());
    
        assertArrayEquals(new String[]{"red"}, machine.symbols());
    }

    /**
     * Verifies that a pulsing symbol starts expanded.
     */
    @Test
    public void shouldStartPulsingSymbolExpanded()
    {
        PulsingSymbol symbol = new PulsingSymbol("red");
    
        assertEquals(70, symbol.getSize());
    }

    /**
     * Verifies that the first selection contracts
     * a pulsing symbol.
     */
    @Test
    public void shouldContractPulsingSymbolWhenSelected()
    {
        PulsingSymbol symbol = new PulsingSymbol("blue");
    
        symbol.select();
    
        assertEquals(45, symbol.getSize());
    }

    /**
     * Verifies that selecting a pulsing symbol again
     * restores its expanded size.
     */
    @Test
    public void shouldExpandPulsingSymbolWhenSelectedAgain()
    {
        PulsingSymbol symbol = new PulsingSymbol("green");
    
        symbol.select();
        assertEquals(45, symbol.getSize());
    
        symbol.select();
        assertEquals(70, symbol.getSize());
    }

    /**
     * Verifies that a pulsing symbol alternates
     * consistently across several selections.
     */
    @Test
    public void shouldAlternatePulsingSymbolRepeatedly()
    {
        PulsingSymbol symbol = new PulsingSymbol("yellow");
    
        for(int i = 0; i < 10; i++){
            symbol.select();
    
            if(i % 2 == 0){
                assertEquals(45, symbol.getSize());
            }
            else{
                assertEquals(70, symbol.getSize());
            }
        }
    }

    /**
     * Verifies that pulsing changes size
     * without hiding the symbol.
     */
    @Test
    public void shouldKeepPulsingSymbolVisible()
    {
        PulsingSymbol symbol = new PulsingSymbol("red");
    
        assertTrue(symbol.isVisible());
    
        symbol.select();
    
        assertTrue(symbol.isVisible());
    
        symbol.select();
    
        assertTrue(symbol.isVisible());
    }

    /**
     * Verifies that pulsing is associated with
     * selection rather than the spin event alone.
     */
    @Test
    public void shouldNotChangePulsingSizeWithoutSelection()
    {
        PulsingSymbol symbol = new PulsingSymbol("blue");
    
        symbol.spin();
    
        assertEquals(70, symbol.getSize());
    }

    /**
     * Verifies that pulsing does not modify
     * the logical color of the symbol.
     */
    @Test
    public void shouldPreservePulsingSymbolColor()
    {
        PulsingSymbol symbol = new PulsingSymbol("magenta");
    
        symbol.select();
        symbol.select();
        symbol.select();
    
        assertEquals("magenta", symbol.getColor());
    }

    /**
     * Verifies that a pulsing symbol can be selected
     * as a consequence of spinning its wheel.
     */
    @Test
    public void shouldPreservePulsingBehaviorDuringWheelSpin()
    {
        SlotMachine machine = new SlotMachine();
    
        machine.addWheel("normal", 1);
    
        machine.addSymbol("normal", "red", 1);
        machine.addSymbol("pulsing", "blue", 1);
    
        machine.spin(1);
    
        assertTrue(machine.ok());
        assertEquals("blue", machine.configuration()[0]);
    
        machine.spin(1);
    
        assertTrue(machine.ok());
        assertEquals("red", machine.configuration()[0]);
    }

    // ----------------------------------------------------------
    // Visual markers - identification and behavioral compatibility
    // ----------------------------------------------------------

    /**
     * Verifies that normal symbols do not require
     * an additional visual marker.
     */
    @Test
    public void shouldNotMarkNormalSymbol()
    {
        Symbol symbol = new Symbol("red");
    
        assertFalse(symbol.hasMarker());
        assertNull(symbol.markerColor());
    }

    /**
     * Verifies the visual identification
     * of an ephemeral symbol.
     */
    @Test
    public void shouldIdentifyEphemeralSymbolVisually()
    {
        Symbol symbol = new EphemeralSymbol("red");
    
        assertTrue(symbol.hasMarker());
        assertEquals("magenta", symbol.markerColor());
    }

    /**
     * Verifies the visual identification
     * of a shy symbol.
     */
    @Test
    public void shouldIdentifyShySymbolVisually()
    {
        Symbol symbol = new ShySymbol("red");
    
        assertTrue(symbol.hasMarker());
        assertEquals("blue", symbol.markerColor());
    }

    /**
     * Verifies the visual identification
     * of a pulsing symbol.
     */
    @Test
    public void shouldIdentifyPulsingSymbolVisually()
    {
        Symbol symbol = new PulsingSymbol("red");
    
        assertTrue(symbol.hasMarker());
        assertEquals("green", symbol.markerColor());
    }

    /**
     * Verifies that visual identification does not
     * interfere with shy selection behavior.
     */
    @Test
    public void shouldPreserveShyBehaviorWithMarker()
    {
        ShySymbol symbol = new ShySymbol("blue");
    
        assertTrue(symbol.isVisible());
        assertTrue(symbol.hasMarker());
    
        symbol.select();
    
        assertFalse(symbol.isVisible());
        assertTrue(symbol.hasMarker());
    
        symbol.select();
    
        assertTrue(symbol.isVisible());
        assertTrue(symbol.hasMarker());
    }

    /**
     * Verifies that visual identification does not
     * interfere with ephemeral shrinking behavior.
     */
    @Test
    public void shouldPreserveEphemeralBehaviorWithMarker()
    {
        EphemeralSymbol symbol = new EphemeralSymbol("green");
    
        assertTrue(symbol.hasMarker());
        assertEquals(70, symbol.getSize());
    
        symbol.spin();
    
        assertEquals(60, symbol.getSize());
        assertTrue(symbol.hasMarker());
    
        for(int i = 0; i < 20; i++){
            symbol.spin();
        }
    
        assertEquals(2, symbol.getSize());
        assertTrue(symbol.hasMarker());
    }

    // ----------------------------------------------------------
    // Integration - wheel types, symbol combinations and uniqueness
    // ----------------------------------------------------------

    /**
     * Verifies that all four symbol types
     * can coexist in the same wheel.
     */
    @Test
    public void accordingGbShouldShouldAllowFourSymbolTypesInSameWheel()
    {
        SlotMachine machine = new SlotMachine();
    
        machine.addWheel("normal", 1);
    
        machine.addSymbol("normal", "red", 1);
        machine.addSymbol("ephemeral", "blue", 1);
        machine.addSymbol("shy", "green", 1);
        machine.addSymbol("pulsing", "yellow", 1);
    
        assertTrue(machine.ok());
    
        assertArrayEquals(new String[]{"red", "blue", "green", "yellow"}, machine.symbols()
        );
    }

    /**
     * Verifies that a lefty wheel can select
     * a pulsing symbol while copying its left neighbor.
     */
    @Test
    public void shouldAllowPulsingSymbolInLeftyWheel()
    {
        SlotMachine machine = new SlotMachine();
    
        machine.addWheel("normal", 1);
        machine.addWheel("lefty", 2);
    
        machine.addSymbol("normal", "red", 1);
        machine.addSymbol("normal", "blue", 1);
    
        machine.addSymbol("pulsing", "red", 2);
        machine.addSymbol("normal", "blue", 2);
    
        machine.placeSymbol(1, "red");
        machine.spin(2);
    
        assertTrue(machine.ok());
        assertEquals("red", machine.configuration()[1]);
    
        machine.placeSymbol(1, "blue");
        machine.spin(2);
    
        assertTrue(machine.ok());
        assertEquals("blue", machine.configuration()[1]);
    }

    /**
     * Verifies that a rebel wheel accepts pulsing symbols
     * without losing its restrictions.
     */
    @Test
    public void shouldAllowPulsingSymbolInRebelWheel()
    {
        SlotMachine machine = new SlotMachine();
    
        machine.addWheel("rebel", 1);
    
        machine.addSymbol("normal", "red", 1);
        machine.addSymbol("pulsing", "blue", 1);
    
        machine.spin(1);
    
        assertTrue(machine.ok());
        assertEquals("blue", machine.configuration()[0]);
    
        machine.lock(1);
    
        assertFalse(machine.ok());
    
        machine.delWheel(1);
    
        assertFalse(machine.ok());
        assertEquals(1, machine.wheels());
    }

    /**
     * Verifica que diferentes tipos de simbolos no puedan
     * introducir colores duplicados en la misma rueda.
     */
    @Test
    public void accordingGbShouldNotAllowDuplicateColorAcrossSymbolTypes() {
        SlotMachine machine = new SlotMachine();
        
        machine.addWheel("normal", 1);
        machine.addSymbol("normal", "red", 1);
        assertTrue(machine.ok());
        
        machine.addSymbol("pulsing", "red", 1);
        assertFalse(machine.ok());
        
        assertArrayEquals(new String[]{"red"}, machine.symbols());
    }

    // ----------------------------------------------------------
    // Jackpot - logical identity despite visual changes
    // ----------------------------------------------------------

    /**
     * Verifies that the size of a pulsing symbol
     * does not affect jackpot evaluation.
     */
    @Test
    public void shouldPreserveJackpotWithPulsingSymbol()
    {
        SlotMachine machine = new SlotMachine();
    
        machine.addWheel("normal", 1);
        machine.addWheel("normal", 2);
    
        machine.addSymbol("pulsing", "red", 1);
        machine.addSymbol("normal", "red", 2);
    
        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "red");
    
        assertTrue(machine.isJackpot());
    
        machine.placeSymbol(1, "red");
    
        assertTrue(machine.isJackpot());
    }

    
    
    
    

   // WIKI MOODLE SlotMachineCC4test
    /**
     * Verifies repeated global spins with all three wheel
     * types and all four symbol types, including lefty
     * synchronization.
     */
    @Test
    public void accordingRojasHShouldSpinMachineWithEveryWheelAndSymbolType()
    {
        SlotMachine machine = new SlotMachine();

        String[] types = {"normal", "lefty", "rebel"};

        for(int wheel = 1; wheel <= 3; wheel++){
            machine.addWheel(types[wheel - 1], wheel);

            machine.addSymbol("normal", "red", wheel);
            machine.addSymbol("ephemeral", "blue", wheel);
            machine.addSymbol("shy", "green", wheel);
            machine.addSymbol("pulsing", "yellow", wheel);
        }

        assertEquals(3, machine.wheels());
        assertTrue(machine.ok());

        for(int i = 0; i < 10; i++){
            machine.spin();

            assertTrue(machine.ok());

            String[] configuration = machine.configuration();

            assertEquals(configuration[0], configuration[1]);
            assertEquals(configuration[0], configuration[2]);
        }
    }

    /**
     * Verifies that a rebel cannot be deleted but its normal
     * neighbor can be removed, leaving the surviving rebel
     * restrictions intact.
     */
    @Test
    public void accordingCaPpShouldNotDeleteRebelWheelButShouldDeleteNormalWheel()
    {
        SlotMachine machine = new SlotMachine();

        machine.addWheel("normal", 1);
        machine.addWheel("rebel", 2);

        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");

        machine.delWheel(2);

        assertFalse(machine.ok());
        assertEquals(2, machine.wheels());

        assertArrayEquals(
            new String[]{"red", "blue"},
            machine.configuration()
        );

        machine.delWheel(1);

        assertTrue(machine.ok());
        assertEquals(1, machine.wheels());

        assertArrayEquals(
            new String[]{"blue"},
            machine.configuration()
        );

        // The remaining wheel is still rebel,
        // now at position one.

        machine.lock(1);

        assertFalse(machine.ok());
    }

    /**
     * Verifies that a fourth-position lefty follows
     * the immediate third wheel during a global spin,
     * not the first wheel.
     */
    @Test
    public void accordingToDoOLleftyWheelCopiesItsLeftNeighbour()
    {
        SlotMachine machine = new SlotMachine(3);

        machine.addWheel("lefty", 4);

        machine.addSymbol(4, "red");
        machine.addSymbol(4, "blue");
        machine.addSymbol(4, "green");

        machine.spin(
            new String[]{"red", "green", "blue", "red"}
        );

        assertTrue(machine.ok());

        machine.spin();

        assertTrue(machine.ok());

        String[] configuration = machine.configuration();

        assertEquals(4, configuration.length);

        assertEquals(
            configuration[2],
            configuration[3]
        );

        assertNotEquals(
            configuration[0],
            configuration[3]
        );
    }

    /**
     * Adapted from AnzolaA-CastanedaC's shared case.
     *
     * Verifies that a lefty reads the updated state
     * after its neighbor is spun independently by
     * one step.
     */
    @Test
    public void accordingAaCcShouldMakeLeftyWheelCopyImmediateLeftWheel()
    {
        SlotMachine machine = new SlotMachine();

        machine.addWheel("normal", 1);
        machine.addWheel("lefty", 2);

        machine.addSymbol(1, "red");
        machine.addSymbol(1, "blue");

        machine.addSymbol(2, "red");
        machine.addSymbol(2, "blue");

        String before = machine.configuration()[0];

        machine.spin(1, 1);

        assertTrue(machine.ok());

        assertNotEquals(
            before,
            machine.configuration()[0]
        );

        machine.spin(2);

        assertTrue(machine.ok());

        assertEquals(
            machine.configuration()[0],
            machine.configuration()[1]
        );
    }

}
