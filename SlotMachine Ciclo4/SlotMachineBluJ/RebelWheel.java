/**
 * Represents a rebel wheel in the slot machine.
 *
 * A rebel wheel preserves the normal spinning and symbol behavior,
 * but it cannot be locked, swapped, or deleted.
 *
 * @author Julian Gomez Boada
 * @version Ciclo4-MC2
 */
public class RebelWheel extends Wheel
{
    /**
     * Creates a rebel wheel at the given 0-based slot index.
     *
     * @param slotIndex 0-based position of this wheel in the machine
     */
    public RebelWheel(int slotIndex)
    {
        super(slotIndex);
    }

    /**
     * Indicates that a rebel wheel cannot be locked.
     *
     * @return false
     */
    @Override
    public boolean canBeLocked()
    {
        return false;
    }

    /**
     * Indicates that a rebel wheel cannot be swapped.
     *
     * @return false
     */
    @Override
    public boolean canBeSwapped()
    {
        return false;
    }

    /**
     * Indicates that a rebel wheel cannot be deleted.
     *
     * @return false
     */
    @Override
    public boolean canBeDeleted()
    {
        return false;
    }
    
    /**
     * Returns the visual identification color
     * of a rebel wheel.
     *
     * @return orange frame color
     */
    @Override
    protected String frameColor()
    {
        return "orange";
    }
}